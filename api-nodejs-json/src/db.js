import fs from 'node:fs'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)

const runtimeDbPath = path.resolve(__dirname, '../data/database.json')
const seedDirPath = path.resolve(__dirname, '../../data/colecoes')

function readJson(filePath) {
  return JSON.parse(fs.readFileSync(filePath, 'utf-8'))
}

function mapHospede(item) {
  return {
    idHospede: Number(item._id),
    nome: item.nome,
    email: item.email,
    cpf: item.cpf,
    telefone: item.telefone ?? null,
    dataNascimento: item.data_nascimento ?? null,
    dataCadastro: item.data_cadastro ?? null,
    ativo: Boolean(item.ativo)
  }
}

function mapQuarto(item) {
  return {
    idQuarto: Number(item._id),
    numero: String(item.numero),
    tipo: item.tipo,
    capacidade: Number(item.capacidade),
    precoDiaria: Number(item.preco_diaria),
    status: item.status,
    andar: Number(item.andar),
    vista: item.vista ?? null
  }
}

function mapReserva(item) {
  return {
    idReserva: Number(item._id),
    idHospede: Number(item.hospede_id),
    idQuarto: Number(item.quarto_id),
    dataReserva: item.data_reserva ?? null,
    dataCheckin: item.data_checkin ?? null,
    dataCheckout: item.data_checkout ?? null,
    status: item.status,
    canal: item.canal ?? null,
    valorPrevisto: Number(item.valor_previsto ?? 0)
  }
}

function mapHospedagem(item) {
  return {
    idHospedagem: Number(item._id),
    idReserva: Number(item.reserva_id),
    idFuncionarioCheckin: Number(item.funcionario_checkin_id),
    idFuncionarioCheckout: item.funcionario_checkout_id == null ? null : Number(item.funcionario_checkout_id),
    checkinReal: item.checkin_real ?? null,
    checkoutReal: item.checkout_real ?? null,
    status: item.status,
    totalDiarias: Number(item.total_diarias ?? 0),
    totalServicos: Number(item.total_servicos ?? 0),
    totalGeral: Number(item.total_geral ?? 0)
  }
}

function mapServico(item) {
  return {
    idServico: Number(item._id),
    nome: item.nome,
    categoria: item.categoria,
    preco: Number(item.preco),
    disponivel: Boolean(item.disponivel)
  }
}

function mapAvaliacao(item) {
  return {
    idFeedback: Number(item._id),
    idHospede: Number(item.hospede_id),
    idQuarto: Number(item.quarto_id),
    idHospedagem: Number(item.hospedagem_id),
    notaGeral: Number(item.nota_geral),
    comentario: item.comentario ?? '',
    dataAvaliacao: item.data_avaliacao ?? null
  }
}

function initializeDatabase() {
  const initialData = {
    hospedes: readJson(path.resolve(seedDirPath, 'hospedes.json')).map(mapHospede),
    quartos: readJson(path.resolve(seedDirPath, 'quartos.json')).map(mapQuarto),
    reservas: readJson(path.resolve(seedDirPath, 'reservas.json')).map(mapReserva),
    hospedagens: readJson(path.resolve(seedDirPath, 'hospedagens.json')).map(mapHospedagem),
    servicos: readJson(path.resolve(seedDirPath, 'servicos.json')).map(mapServico),
    avaliacoes: readJson(path.resolve(seedDirPath, 'feedbacks.json')).map(mapAvaliacao)
  }

  fs.writeFileSync(runtimeDbPath, JSON.stringify(initialData, null, 2), 'utf-8')
  return initialData
}

function loadDatabase() {
  if (!fs.existsSync(runtimeDbPath)) {
    return initializeDatabase()
  }

  return readJson(runtimeDbPath)
}

function saveDatabase(db) {
  fs.writeFileSync(runtimeDbPath, JSON.stringify(db, null, 2), 'utf-8')
}

export const db = loadDatabase()

export function persistDb() {
  saveDatabase(db)
}
