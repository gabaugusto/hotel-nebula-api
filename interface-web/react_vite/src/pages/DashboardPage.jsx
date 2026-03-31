import { useEffect, useState } from 'react'
import StatusAlert from '../components/StatusAlert'
import { api } from '../services/api'

function DashboardPage() {
  const [data, setData] = useState({
    totalHospedagens: '-',
    faturamentoTotal: '-',
    totalAvaliacoes: '-',
    mediaGeral: '-'
  })
  const [statusMessage, setStatusMessage] = useState('')
  const [isError, setIsError] = useState(false)

  const carregarDashboard = async () => {
    try {
      const [faturamento, avaliacoes] = await Promise.all([
        api.getDashboardFaturamento(),
        api.getAvaliacoesResumo()
      ])

      setData({
        totalHospedagens: faturamento.totalHospedagens ?? '-',
        faturamentoTotal: faturamento.faturamentoTotal ?? '-',
        totalAvaliacoes: avaliacoes.totalAvaliacoes ?? '-',
        mediaGeral: avaliacoes.mediaGeral ?? '-'
      })
      setStatusMessage('Dashboard atualizado com sucesso.')
      setIsError(false)
    } catch (error) {
      setStatusMessage(error.message)
      setIsError(true)
    }
  }

  useEffect(() => {
    carregarDashboard()
  }, [])

  return (
    <>
      <h1 className="h3 mb-3">Dashboard</h1>

      <div className="row g-3">
        <section className="col-md-6" aria-labelledby="faturamento-titulo">
          <div className="card shadow-sm h-100">
            <div className="card-body">
              <h2 id="faturamento-titulo" className="h5">Faturamento</h2>
              <p className="mb-1">Total de hospedagens: <strong>{data.totalHospedagens}</strong></p>
              <p className="mb-0">Faturamento total: <strong>{data.faturamentoTotal}</strong></p>
            </div>
          </div>
        </section>

        <section className="col-md-6" aria-labelledby="avaliacoes-titulo">
          <div className="card shadow-sm h-100">
            <div className="card-body">
              <h2 id="avaliacoes-titulo" className="h5">Avaliações</h2>
              <p className="mb-1">Total de avaliações: <strong>{data.totalAvaliacoes}</strong></p>
              <p className="mb-0">Média geral: <strong>{data.mediaGeral}</strong></p>
            </div>
          </div>
        </section>
      </div>

      <button className="btn btn-primary mt-3" type="button" onClick={carregarDashboard}>Atualizar dados</button>
      <StatusAlert message={statusMessage} isError={isError} />
    </>
  )
}

export default DashboardPage
