import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const currentFilePath = fileURLToPath(import.meta.url);
const repoRoot = path.resolve(path.dirname(currentFilePath), '..', '..');
const javaControllersDir = path.join(repoRoot, 'api-java-sql', 'src', 'main', 'java', 'com', 'nebulahotel', 'controller');
const kotlinControllersDir = path.join(repoRoot, 'api-kotlin-nosql', 'src', 'main', 'kotlin', 'com', 'nebulahotel', 'controller');

const methodMap = [
  ['Get', 'GET'],
  ['Post', 'POST'],
  ['Put', 'PUT'],
  ['Delete', 'DELETE']
];

function readControllerFiles(dirPath) {
  return fs
    .readdirSync(dirPath)
    .filter((fileName) => fileName.endsWith('.java') || fileName.endsWith('.kt'))
    .map((fileName) => path.join(dirPath, fileName));
}

function extractQuotedValues(text) {
  return [...text.matchAll(/"([^"]+)"/g)].map((match) => match[1]);
}

function joinPath(basePath, subPath = '') {
  const normalizedBase = basePath.startsWith('/') ? basePath : `/${basePath}`;
  if (!subPath) return normalizedBase;
  const normalizedSub = subPath.startsWith('/') ? subPath : `/${subPath}`;
  return `${normalizedBase}${normalizedSub}`.replace(/\/+/g, '/');
}

function parseEndpointsFromFile(filePath) {
  const content = fs.readFileSync(filePath, 'utf-8');
  const lines = content.split(/\r?\n/);

  let basePaths = [];
  const endpoints = [];

  for (const line of lines) {
    if (line.includes('@RequestMapping')) {
      const mappings = extractQuotedValues(line);
      if (mappings.length > 0) {
        basePaths = mappings;
      }
      continue;
    }

    for (const [annotationName, httpMethod] of methodMap) {
      if (!line.includes(`@${annotationName}Mapping`)) continue;

      const mappingValues = extractQuotedValues(line);
      const subPaths = mappingValues.length > 0 ? mappingValues : [''];
      const effectiveBases = basePaths.length > 0 ? basePaths : ['/'];

      for (const basePath of effectiveBases) {
        for (const subPath of subPaths) {
          endpoints.push(`${httpMethod} ${joinPath(basePath, subPath)}`);
        }
      }
    }
  }

  return endpoints;
}

function parseEndpointsFromDir(dirPath) {
  const files = readControllerFiles(dirPath);
  const endpointSet = new Set();

  for (const filePath of files) {
    for (const endpoint of parseEndpointsFromFile(filePath)) {
      endpointSet.add(endpoint);
    }
  }

  return endpointSet;
}

function printSet(title, setValues) {
  console.log(`\n${title} (${setValues.size})`);
  [...setValues].sort().forEach((value) => console.log(`- ${value}`));
}

const javaEndpoints = parseEndpointsFromDir(javaControllersDir);
const kotlinEndpoints = parseEndpointsFromDir(kotlinControllersDir);

const missingInKotlin = [...javaEndpoints].filter((endpoint) => !kotlinEndpoints.has(endpoint)).sort();
const extraInKotlin = [...kotlinEndpoints].filter((endpoint) => !javaEndpoints.has(endpoint)).sort();

printSet('Java SQL endpoints', javaEndpoints);
printSet('Kotlin NoSQL endpoints', kotlinEndpoints);

if (missingInKotlin.length === 0 && extraInKotlin.length === 0) {
  console.log('\n✅ Endpoint parity OK: Kotlin NoSQL and Java SQL expose the same routes.');
  process.exit(0);
}

if (missingInKotlin.length > 0) {
  console.error('\n❌ Missing in Kotlin NoSQL:');
  missingInKotlin.forEach((endpoint) => console.error(`- ${endpoint}`));
}

if (extraInKotlin.length > 0) {
  console.error('\n❌ Extra in Kotlin NoSQL (not in Java SQL):');
  extraInKotlin.forEach((endpoint) => console.error(`- ${endpoint}`));
}

process.exit(1);
