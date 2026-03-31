import { useEffect, useState } from 'react'
import StatusAlert from '../components/StatusAlert'
import { api } from '../services/api'

function BuscarHospedesPage() {
  const [hospedes, setHospedes] = useState([])
  const [idBusca, setIdBusca] = useState('')
  const [statusMessage, setStatusMessage] = useState('')
  const [isError, setIsError] = useState(false)

  const carregarHospedes = async () => {
    try {
      const data = await api.listHospedes()
      setHospedes(data)
      setStatusMessage('Lista carregada com sucesso.')
      setIsError(false)
    } catch (error) {
      setStatusMessage(error.message)
      setIsError(true)
    }
  }

  const buscarPorId = async (event) => {
    event.preventDefault()
    try {
      const hospede = await api.getHospedeById(idBusca)
      setStatusMessage(`Encontrado: ${hospede.nome} (${hospede.email})`)
      setIsError(false)
    } catch (error) {
      setStatusMessage(error.message)
      setIsError(true)
    }
  }

  const excluirHospede = async (id) => {
    try {
      await api.deleteHospede(id)
      setStatusMessage('Hóspede removido com sucesso.')
      setIsError(false)
      carregarHospedes()
    } catch (error) {
      setStatusMessage(error.message)
      setIsError(true)
    }
  }

  useEffect(() => {
    carregarHospedes()
  }, [])

  return (
    <>
      <h1 className="h3 mb-3">Buscar hóspedes</h1>

      <section className="card shadow-sm mb-3" aria-labelledby="busca-titulo">
        <div className="card-body">
          <h2 id="busca-titulo" className="h5">Busca por ID</h2>
          <form className="row g-3" onSubmit={buscarPorId} noValidate>
            <div className="col-md-4">
              <label htmlFor="idHospedeBusca" className="form-label">ID do hóspede</label>
              <input
                id="idHospedeBusca"
                type="number"
                min="1"
                className="form-control"
                required
                value={idBusca}
                onChange={(event) => setIdBusca(event.target.value)}
              />
            </div>
            <div className="col-md-3 d-flex align-items-end">
              <button className="btn btn-primary w-100" type="submit">Buscar por ID</button>
            </div>
          </form>
        </div>
      </section>

      <section className="card shadow-sm mb-3" aria-labelledby="lista-titulo">
        <div className="card-body">
          <div className="d-flex justify-content-between align-items-center mb-2">
            <h2 id="lista-titulo" className="h5 mb-0">Lista de hóspedes</h2>
            <button className="btn btn-outline-primary btn-sm" onClick={carregarHospedes} type="button">Recarregar lista</button>
          </div>

          <div className="table-responsive">
            <table className="table table-striped align-middle">
              <caption className="visually-hidden">Tabela com hóspedes cadastrados</caption>
              <thead>
                <tr>
                  <th>ID</th><th>Nome</th><th>E-mail</th><th>CPF</th><th>Ativo</th><th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {hospedes.length === 0 ? (
                  <tr><td colSpan="6">Nenhum hóspede encontrado.</td></tr>
                ) : (
                  hospedes.map((item) => (
                    <tr key={item.idHospede}>
                      <td>{item.idHospede}</td>
                      <td>{item.nome}</td>
                      <td>{item.email}</td>
                      <td>{item.cpf}</td>
                      <td>{item.ativo ? 'Sim' : 'Não'}</td>
                      <td>
                        <button
                          className="btn btn-sm btn-outline-danger"
                          type="button"
                          onClick={() => excluirHospede(item.idHospede)}
                        >
                          Excluir
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </section>

      <StatusAlert message={statusMessage} isError={isError} />
    </>
  )
}

export default BuscarHospedesPage
