const defaultBaseUrls = [
  process.env.JAVA_BASE_URL,
  process.env.KOTLIN_BASE_URL,
  process.env.BASE_URL
].filter(Boolean);

const cliUrls = process.argv.slice(2).filter((arg) => arg.startsWith('http'));
const targetBaseUrls = cliUrls.length > 0 ? cliUrls : (defaultBaseUrls.length > 0 ? defaultBaseUrls : ['http://localhost:8083']);

const endpoints = [
  { method: 'GET', path: '/hospedes', accepted: [200] },
  { method: 'GET', path: '/hospedes/1', accepted: [200, 404] },
  { method: 'GET', path: '/hospedes/email/carlos.andrade@email.com', accepted: [200, 404] },
  { method: 'GET', path: '/hospedes/historico/1', accepted: [200, 404] },
  { method: 'GET', path: '/quartos', accepted: [200] },
  { method: 'GET', path: '/quartos/disponiveis', accepted: [200] },
  { method: 'GET', path: '/reservas', accepted: [200] },
  { method: 'GET', path: '/reservas/ativas', accepted: [200] },
  { method: 'GET', path: '/hospedagens', accepted: [200] },
  { method: 'GET', path: '/servicos', accepted: [200] },
  { method: 'GET', path: '/servicos/disponiveis', accepted: [200] },
  { method: 'GET', path: '/avaliacoes', accepted: [200] },
  { method: 'GET', path: '/feedbacks', accepted: [200] },
  { method: 'GET', path: '/avaliacoes/resumo', accepted: [200] },
  { method: 'GET', path: '/pagamentos', accepted: [200] },
  { method: 'GET', path: '/dashboard/faturamento', accepted: [200] }
];

const corsEndpoints = ['/feedbacks', '/pagamentos'];

async function hit(baseUrl, endpoint) {
  const response = await fetch(`${baseUrl}${endpoint.path}`, { method: endpoint.method });
  const ok = endpoint.accepted.includes(response.status);
  return { ok, status: response.status, endpoint };
}

async function checkCors(baseUrl, path) {
  const response = await fetch(`${baseUrl}${path}`, {
    method: 'OPTIONS',
    headers: {
      Origin: 'http://localhost:5500',
      'Access-Control-Request-Method': 'GET'
    }
  });

  const allowOrigin = response.headers.get('access-control-allow-origin');
  const ok = response.status < 500 && !!allowOrigin;

  return {
    ok,
    status: response.status,
    path,
    allowOrigin: allowOrigin || '(missing)'
  };
}

async function runForBaseUrl(baseUrl) {
  console.log(`\n### Smoke testing ${baseUrl}`);
  const failures = [];

  for (const endpoint of endpoints) {
    try {
      const result = await hit(baseUrl, endpoint);
      const marker = result.ok ? '✅' : '❌';
      console.log(`${marker} ${endpoint.method} ${endpoint.path} -> ${result.status}`);
      if (!result.ok) {
        failures.push(`${endpoint.method} ${endpoint.path} returned ${result.status}`);
      }
    } catch (error) {
      console.log(`❌ ${endpoint.method} ${endpoint.path} -> connection error`);
      failures.push(`${endpoint.method} ${endpoint.path} connection error: ${error.message}`);
    }
  }

  for (const path of corsEndpoints) {
    try {
      const cors = await checkCors(baseUrl, path);
      const marker = cors.ok ? '✅' : '❌';
      console.log(`${marker} OPTIONS ${path} -> ${cors.status} | ACAO=${cors.allowOrigin}`);
      if (!cors.ok) {
        failures.push(`CORS preflight failed on ${path} (status ${cors.status}, ACAO=${cors.allowOrigin})`);
      }
    } catch (error) {
      console.log(`❌ OPTIONS ${path} -> connection error`);
      failures.push(`CORS preflight connection error on ${path}: ${error.message}`);
    }
  }

  return failures;
}

(async function main() {
  let allFailures = [];

  for (const baseUrl of targetBaseUrls) {
    const failures = await runForBaseUrl(baseUrl);
    if (failures.length > 0) {
      allFailures = allFailures.concat(failures.map((msg) => `[${baseUrl}] ${msg}`));
    }
  }

  if (allFailures.length === 0) {
    console.log('\n✅ Smoke test passed for all target URLs.');
    process.exit(0);
  }

  console.error('\n❌ Smoke test found issues:');
  allFailures.forEach((failure) => console.error(`- ${failure}`));
  process.exit(1);
})();
