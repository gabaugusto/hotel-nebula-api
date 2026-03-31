import { useState } from 'react'
import StatusAlert from '../components/StatusAlert'
import { api } from '../services/api'

const initialState = {
  idHospede: '',
  nome: '',
  email: '',
  cpf: '',
  telefone: '',
  dataNascimento: '',
  ativo: 'true'
}

function CadastrarHospedePage() {
  const [form, setForm] = useState(initialState)
  const [statusMessage, setStatusMessage] = useState('')
  const [isError, setIsError] = useState(false)

  const setField = (field, value) => setForm((current) => ({ ...current, [field]: value }))

  const payload = {
    idHospede: Number(form.idHospede),
    nome: form.nome,
    email: form.email,
    cpf: form.cpf,
    telefone: form.telefone || null,
    dataNascimento: form.dataNascimento || null,
    ativo: form.ativo === 'true'
  }

  const criar = async (event) => {
    event.preventDefault()
    try {
      await api.createHospede(payload)
      setStatusMessage('Hóspede criado com sucesso.')
      setIsError(false)
    } catch (error) {
      setStatusMessage(error.message)
      setIsError(true)
    }
  }

  const atualizar = async () => {
    try {
      await api.updateHospede(payload.idHospede, payload)
      setStatusMessage('Hóspede atualizado com sucesso.')
      setIsError(false)
    } catch (error) {
      setStatusMessage(error.message)
      setIsError(true)
    }
  }

  return (
    <>
      <h1 className="h3 mb-3">Cadastrar ou atualizar hóspede</h1>

      <section className="card shadow-sm" aria-labelledby="cadastro-titulo">
        <div className="card-body">
          <h2 id="cadastro-titulo" className="h5">Dados do hóspede</h2>
          <form className="row g-3" onSubmit={criar} noValidate>
            <div className="col-md-3"><label htmlFor="idHospede" className="form-label">ID</label><input id="idHospede" type="number" min="1" className="form-control" required value={form.idHospede} onChange={(event) => setField('idHospede', event.target.value)} /></div>
            <div className="col-md-9"><label htmlFor="nome" className="form-label">Nome</label><input id="nome" className="form-control" required value={form.nome} onChange={(event) => setField('nome', event.target.value)} /></div>
            <div className="col-md-6"><label htmlFor="email" className="form-label">E-mail</label><input id="email" type="email" className="form-control" required value={form.email} onChange={(event) => setField('email', event.target.value)} /></div>
            <div className="col-md-6"><label htmlFor="cpf" className="form-label">CPF</label><input id="cpf" className="form-control" required value={form.cpf} onChange={(event) => setField('cpf', event.target.value)} /></div>
            <div className="col-md-6"><label htmlFor="telefone" className="form-label">Telefone</label><input id="telefone" className="form-control" value={form.telefone} onChange={(event) => setField('telefone', event.target.value)} /></div>
            <div className="col-md-3"><label htmlFor="dataNascimento" className="form-label">Data de nascimento</label><input id="dataNascimento" type="date" className="form-control" value={form.dataNascimento} onChange={(event) => setField('dataNascimento', event.target.value)} /></div>
            <div className="col-md-3"><label htmlFor="ativo" className="form-label">Ativo</label><select id="ativo" className="form-select" value={form.ativo} onChange={(event) => setField('ativo', event.target.value)}><option value="true">Sim</option><option value="false">Não</option></select></div>

            <div className="col-12 d-flex gap-2">
              <button type="submit" className="btn btn-success">Criar</button>
              <button type="button" className="btn btn-primary" onClick={atualizar}>Atualizar por ID</button>
            </div>
          </form>
        </div>
      </section>

      <StatusAlert message={statusMessage} isError={isError} />
    </>
  )
}

export default CadastrarHospedePage
