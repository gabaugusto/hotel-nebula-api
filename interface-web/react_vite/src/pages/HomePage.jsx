import { useState } from 'react'
import { api } from '../services/api'
import StatusAlert from '../components/StatusAlert'

function HomePage() {
  const [baseUrl, setBaseUrl] = useState(api.baseUrl)
  const [statusMessage, setStatusMessage] = useState('')

  const handleSubmit = (event) => {
    event.preventDefault()
    setStatusMessage(
      `URL base atual detectada no .env: ${baseUrl}. Para alterar, edite VITE_API_BASE_URL.`
    )
  }

  return (
    <>
      <h1 className="h3 mb-3">Bem-vindo ao painel de estudos</h1>
      <p className="text-secondary">Interface React para praticar integração com a API do Hotel Nebula.</p>

      <section className="card shadow-sm" aria-labelledby="configuracao-api-titulo">
        <div className="card-body">
          <h2 id="configuracao-api-titulo" className="h5">Configuração da API</h2>
          <p className="mb-3">A URL base vem do arquivo <code>.env</code> via variável <code>VITE_API_BASE_URL</code>.</p>

          <form className="row g-3" onSubmit={handleSubmit} noValidate>
            <div className="col-12 col-md-8">
              <label htmlFor="baseUrl" className="form-label">URL base da API</label>
              <input
                id="baseUrl"
                name="baseUrl"
                type="url"
                className="form-control"
                value={baseUrl}
                onChange={(event) => setBaseUrl(event.target.value)}
                aria-describedby="apiHelp"
                placeholder="http://localhost:8083"
                readOnly
              />
              <div id="apiHelp" className="form-text">Altere o arquivo <code>.env</code> para outro ambiente.</div>
            </div>
            <div className="col-12 col-md-4 d-flex align-items-end">
              <button className="btn btn-primary w-100" type="submit">Validar configuração</button>
            </div>
          </form>

          <StatusAlert message={statusMessage} />
        </div>
      </section>
    </>
  )
}

export default HomePage
