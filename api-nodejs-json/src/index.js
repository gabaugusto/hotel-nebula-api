import express from 'express'
import cors from 'cors'
import { db, persistDb } from './db.js'

const app = express()
const port = Number(process.env.PORT || 8083)

app.use(cors())
app.use(express.json())

function findById(collection, idField, id) {
  return collection.find((item) => Number(item[idField]) === Number(id))
}

function removeById(collection, idField, id) {
  const index = collection.findIndex((item) => Number(item[idField]) === Number(id))
  if (index === -1) return false
  collection.splice(index, 1)
  persistDb()
  return true
}

app.get('/health', (_req, res) => {
  res.json({ status: 'ok', service: 'hotel-nebula-api-nodejs' })
})

app.get('/hospedes', (_req, res) => {
  res.json(db.hospedes)
})

app.get('/hospedes/:id', (req, res) => {
  const hospede = findById(db.hospedes, 'idHospede', req.params.id)
  if (!hospede) return res.sendStatus(404)
  return res.json(hospede)
})

app.get('/hospedes/email/:email', (req, res) => {
  const hospede = db.hospedes.find((item) => item.email === req.params.email)
  if (!hospede) return res.sendStatus(404)
  return res.json(hospede)
})

app.get('/hospedes/historico/:id', (req, res) => {
  const idHospede = Number(req.params.id)
  const hospede = findById(db.hospedes, 'idHospede', idHospede)
  if (!hospede) return res.sendStatus(404)

  const reservas = db.reservas
    .filter((item) => Number(item.idHospede) === idHospede)
    .sort((a, b) => String(b.dataReserva).localeCompare(String(a.dataReserva)))

  const avaliacoes = db.avaliacoes
    .filter((item) => Number(item.idHospede) === idHospede)
    .sort((a, b) => String(b.dataAvaliacao).localeCompare(String(a.dataAvaliacao)))

  return res.json({ hospede, reservas, avaliacoes })
})

app.post('/hospedes', (req, res) => {
  const hospede = req.body

  if (hospede.idHospede == null) {
    return res.status(400).json({ erro: 'O campo idHospede e obrigatorio.' })
  }
  if (findById(db.hospedes, 'idHospede', hospede.idHospede)) {
    return res.status(409).json({ erro: 'Ja existe hospede com este idHospede.' })
  }
  if (hospede.email && db.hospedes.some((item) => item.email === hospede.email)) {
    return res.status(409).json({ erro: 'Ja existe hospede com este email.' })
  }
  if (hospede.cpf && db.hospedes.some((item) => item.cpf === hospede.cpf)) {
    return res.status(409).json({ erro: 'Ja existe hospede com este CPF.' })
  }

  const novoHospede = {
    ...hospede,
    idHospede: Number(hospede.idHospede),
    dataCadastro: hospede.dataCadastro || new Date().toISOString(),
    ativo: hospede.ativo == null ? true : Boolean(hospede.ativo)
  }

  db.hospedes.push(novoHospede)
  persistDb()
  return res.status(201).json(novoHospede)
})

app.put('/hospedes/:id', (req, res) => {
  const idHospede = Number(req.params.id)
  const index = db.hospedes.findIndex((item) => Number(item.idHospede) === idHospede)
  if (index === -1) return res.sendStatus(404)

  const hospedeExistente = db.hospedes[index]
  const hospedeAtualizado = req.body

  if (
    hospedeAtualizado.email &&
    hospedeAtualizado.email !== hospedeExistente.email &&
    db.hospedes.some((item) => item.email === hospedeAtualizado.email)
  ) {
    return res.status(409).json({ erro: 'Ja existe hospede com este email.' })
  }

  if (
    hospedeAtualizado.cpf &&
    hospedeAtualizado.cpf !== hospedeExistente.cpf &&
    db.hospedes.some((item) => item.cpf === hospedeAtualizado.cpf)
  ) {
    return res.status(409).json({ erro: 'Ja existe hospede com este CPF.' })
  }

  db.hospedes[index] = {
    ...hospedeExistente,
    ...hospedeAtualizado,
    idHospede,
    dataCadastro: hospedeAtualizado.dataCadastro || hospedeExistente.dataCadastro,
    ativo: hospedeAtualizado.ativo == null ? hospedeExistente.ativo : Boolean(hospedeAtualizado.ativo)
  }

  persistDb()
  return res.json(db.hospedes[index])
})

app.delete('/hospedes/:id', (req, res) => {
  const removed = removeById(db.hospedes, 'idHospede', req.params.id)
  if (!removed) return res.sendStatus(404)
  return res.sendStatus(204)
})

app.get('/quartos', (_req, res) => {
  res.json(db.quartos)
})

app.get('/quartos/disponiveis', (_req, res) => {
  res.json(db.quartos.filter((item) => String(item.status).toLowerCase() === 'disponivel'))
})

app.get('/quartos/:id', (req, res) => {
  const quarto = findById(db.quartos, 'idQuarto', req.params.id)
  if (!quarto) return res.sendStatus(404)
  return res.json(quarto)
})

app.post('/quartos', (req, res) => {
  const quarto = req.body
  if (findById(db.quartos, 'idQuarto', quarto.idQuarto)) {
    return res.status(409).json({ erro: 'Ja existe quarto com este idQuarto.' })
  }
  if (db.quartos.some((item) => item.numero === quarto.numero)) {
    return res.status(409).json({ erro: 'Ja existe quarto com este numero.' })
  }

  const novoQuarto = {
    ...quarto,
    idQuarto: Number(quarto.idQuarto),
    precoDiaria: Number(quarto.precoDiaria)
  }
  db.quartos.push(novoQuarto)
  persistDb()
  return res.status(201).json(novoQuarto)
})

app.put('/quartos/:id', (req, res) => {
  const idQuarto = Number(req.params.id)
  const index = db.quartos.findIndex((item) => Number(item.idQuarto) === idQuarto)
  if (index === -1) return res.sendStatus(404)

  const atual = req.body
  const conflitoNumero = db.quartos.find(
    (item) => item.numero === atual.numero && Number(item.idQuarto) !== idQuarto
  )
  if (conflitoNumero) {
    return res.status(409).json({ erro: 'Ja existe quarto com este numero.' })
  }

  db.quartos[index] = { ...db.quartos[index], ...atual, idQuarto }
  persistDb()
  return res.json(db.quartos[index])
})

app.delete('/quartos/:id', (req, res) => {
  const removed = removeById(db.quartos, 'idQuarto', req.params.id)
  if (!removed) return res.sendStatus(404)
  return res.sendStatus(204)
})

app.get('/reservas', (_req, res) => {
  res.json(db.reservas)
})

app.get('/reservas/ativas', (_req, res) => {
  const active = new Set(['confirmada', 'ativa', 'em_andamento'])
  res.json(db.reservas.filter((item) => active.has(String(item.status).toLowerCase())))
})

app.get('/reservas/:id', (req, res) => {
  const reserva = findById(db.reservas, 'idReserva', req.params.id)
  if (!reserva) return res.sendStatus(404)
  return res.json(reserva)
})

app.post('/reservas', (req, res) => {
  const reserva = req.body
  if (findById(db.reservas, 'idReserva', reserva.idReserva)) {
    return res.status(409).json({ erro: 'Ja existe reserva com este idReserva.' })
  }

  const novaReserva = {
    ...reserva,
    idReserva: Number(reserva.idReserva),
    idHospede: Number(reserva.idHospede),
    idQuarto: Number(reserva.idQuarto),
    dataReserva: reserva.dataReserva || new Date().toISOString(),
    valorPrevisto: Number(reserva.valorPrevisto ?? 0)
  }

  db.reservas.push(novaReserva)
  persistDb()
  return res.status(201).json(novaReserva)
})

app.put('/reservas/:id', (req, res) => {
  const idReserva = Number(req.params.id)
  const index = db.reservas.findIndex((item) => Number(item.idReserva) === idReserva)
  if (index === -1) return res.sendStatus(404)

  db.reservas[index] = {
    ...db.reservas[index],
    ...req.body,
    idReserva
  }

  persistDb()
  return res.json(db.reservas[index])
})

app.delete('/reservas/:id', (req, res) => {
  const removed = removeById(db.reservas, 'idReserva', req.params.id)
  if (!removed) return res.sendStatus(404)
  return res.sendStatus(204)
})

app.get('/hospedagens', (_req, res) => {
  res.json(db.hospedagens)
})

app.get('/hospedagens/:id', (req, res) => {
  const hospedagem = findById(db.hospedagens, 'idHospedagem', req.params.id)
  if (!hospedagem) return res.sendStatus(404)
  return res.json(hospedagem)
})

app.post('/hospedagens', (req, res) => {
  const hospedagem = req.body
  if (findById(db.hospedagens, 'idHospedagem', hospedagem.idHospedagem)) {
    return res.status(409).json({ erro: 'Ja existe hospedagem com este idHospedagem.' })
  }

  const novaHospedagem = {
    ...hospedagem,
    idHospedagem: Number(hospedagem.idHospedagem),
    idReserva: Number(hospedagem.idReserva),
    totalDiarias: Number(hospedagem.totalDiarias ?? 0),
    totalServicos: Number(hospedagem.totalServicos ?? 0),
    totalGeral: Number(hospedagem.totalGeral ?? 0)
  }

  db.hospedagens.push(novaHospedagem)
  persistDb()
  return res.status(201).json(novaHospedagem)
})

app.put('/hospedagens/:id', (req, res) => {
  const idHospedagem = Number(req.params.id)
  const index = db.hospedagens.findIndex((item) => Number(item.idHospedagem) === idHospedagem)
  if (index === -1) return res.sendStatus(404)

  db.hospedagens[index] = {
    ...db.hospedagens[index],
    ...req.body,
    idHospedagem
  }

  persistDb()
  return res.json(db.hospedagens[index])
})

app.delete('/hospedagens/:id', (req, res) => {
  const removed = removeById(db.hospedagens, 'idHospedagem', req.params.id)
  if (!removed) return res.sendStatus(404)
  return res.sendStatus(204)
})

app.get('/servicos', (_req, res) => {
  res.json(db.servicos)
})

app.get('/servicos/disponiveis', (_req, res) => {
  res.json(db.servicos.filter((item) => Boolean(item.disponivel)))
})

app.get('/servicos/:id', (req, res) => {
  const servico = findById(db.servicos, 'idServico', req.params.id)
  if (!servico) return res.sendStatus(404)
  return res.json(servico)
})

app.post('/servicos', (req, res) => {
  const servico = req.body
  if (findById(db.servicos, 'idServico', servico.idServico)) {
    return res.status(409).json({ erro: 'Ja existe servico com este idServico.' })
  }

  const novoServico = {
    ...servico,
    idServico: Number(servico.idServico),
    preco: Number(servico.preco),
    disponivel: servico.disponivel == null ? true : Boolean(servico.disponivel)
  }

  db.servicos.push(novoServico)
  persistDb()
  return res.status(201).json(novoServico)
})

app.put('/servicos/:id', (req, res) => {
  const idServico = Number(req.params.id)
  const index = db.servicos.findIndex((item) => Number(item.idServico) === idServico)
  if (index === -1) return res.sendStatus(404)

  db.servicos[index] = {
    ...db.servicos[index],
    ...req.body,
    idServico
  }

  persistDb()
  return res.json(db.servicos[index])
})

app.delete('/servicos/:id', (req, res) => {
  const removed = removeById(db.servicos, 'idServico', req.params.id)
  if (!removed) return res.sendStatus(404)
  return res.sendStatus(204)
})

app.get('/avaliacoes', (_req, res) => {
  res.json(db.avaliacoes)
})

app.get('/avaliacoes/resumo', (_req, res) => {
  const total = db.avaliacoes.length
  const media =
    total === 0
      ? 0
      : Number((db.avaliacoes.reduce((acc, item) => acc + Number(item.notaGeral || 0), 0) / total).toFixed(2))

  res.json({
    totalAvaliacoes: total,
    mediaGeral: media
  })
})

app.get('/avaliacoes/:id', (req, res) => {
  const avaliacao = findById(db.avaliacoes, 'idFeedback', req.params.id)
  if (!avaliacao) return res.sendStatus(404)
  return res.json(avaliacao)
})

app.post('/avaliacoes', (req, res) => {
  const avaliacao = req.body
  if (findById(db.avaliacoes, 'idFeedback', avaliacao.idFeedback)) {
    return res.status(409).json({ erro: 'Ja existe avaliacao com este idFeedback.' })
  }

  const novaAvaliacao = {
    ...avaliacao,
    idFeedback: Number(avaliacao.idFeedback),
    idHospede: Number(avaliacao.idHospede),
    idQuarto: Number(avaliacao.idQuarto),
    idHospedagem: Number(avaliacao.idHospedagem),
    notaGeral: Number(avaliacao.notaGeral),
    dataAvaliacao: avaliacao.dataAvaliacao || new Date().toISOString()
  }

  db.avaliacoes.push(novaAvaliacao)
  persistDb()
  return res.status(201).json(novaAvaliacao)
})

app.put('/avaliacoes/:id', (req, res) => {
  const idFeedback = Number(req.params.id)
  const index = db.avaliacoes.findIndex((item) => Number(item.idFeedback) === idFeedback)
  if (index === -1) return res.sendStatus(404)

  db.avaliacoes[index] = {
    ...db.avaliacoes[index],
    ...req.body,
    idFeedback
  }

  persistDb()
  return res.json(db.avaliacoes[index])
})

app.delete('/avaliacoes/:id', (req, res) => {
  const removed = removeById(db.avaliacoes, 'idFeedback', req.params.id)
  if (!removed) return res.sendStatus(404)
  return res.sendStatus(204)
})

app.get('/dashboard/faturamento', (_req, res) => {
  const totalHospedagens = db.hospedagens.length
  const faturamentoTotal = db.hospedagens.reduce((acc, item) => acc + Number(item.totalGeral || 0), 0)

  res.json({ totalHospedagens, faturamentoTotal })
})

app.listen(port, () => {
  console.log(`Hotel Nebula API Node.js running on port ${port}`)
})
