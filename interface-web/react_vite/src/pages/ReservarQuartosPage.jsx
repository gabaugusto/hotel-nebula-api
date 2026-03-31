import { useEffect, useState } from 'react'
import StatusAlert from '../components/StatusAlert'
import { api } from '../services/api'

const initialReserva = {
  idReserva: '',
  idHospede: '',
  idQuarto: '',
  canal: 'site_proprio',
  dataCheckin: '',
  dataCheckout: '',
  status: 'confirmada',
  valorPrevisto: ''
}

function ReservarQuartosPage() {
  const [quartos, setQuartos] = useState([])
  const [reserva, setReserva] = useState(initialReserva)
  const [statusMessage, setStatusMessage] = useState('')
  const [isError, setIsError] = useState(false)

  const setField = (field, value) => setReserva((current) => ({ ...current, [field]: value }))

  const carregarQuartos = async () => {
    try {
      const data = await api.listQuartosDisponiveis()
      setQuartos(data)
      setStatusMessage('Quartos disponíveis carregados.')
      setIsError(false)
    } catch (error) {
      setStatusMessage(error.message)
      setIsError(true)
    }
  }

  const criarReserva = async (event) => {
    event.preventDefault()
    const payload = {
      idReserva: Number(reserva.idReserva),
      idHospede: Number(reserva.idHospede),
      idQuarto: Number(reserva.idQuarto),
      dataReserva: new Date().toISOString(),
      dataCheckin: reserva.dataCheckin,
      dataCheckout: reserva.dataCheckout,
      status: reserva.status,
      canal: reserva.canal,
      valorPrevisto: Number(reserva.valorPrevisto)
    }

    try {
      await api.createReserva(payload)
      setStatusMessage('Reserva criada com sucesso.')
      setIsError(false)
    } catch (error) {
      setStatusMessage(error.message)
      setIsError(true)
    }
  }

  useEffect(() => {
    carregarQuartos()
  }, [])

  return (
    <>
      <h1 className="h3 mb-3">Reservar quartos</h1>

      <section className="card shadow-sm mb-3" aria-labelledby="quartos-titulo">
        <div className="card-body">
          <div className="d-flex justify-content-between align-items-center mb-2">
            <h2 id="quartos-titulo" className="h5 mb-0">Quartos disponíveis</h2>
            <button className="btn btn-outline-primary btn-sm" onClick={carregarQuartos} type="button">Atualizar</button>
          </div>
          <ul className="list-group">
            {quartos.length === 0 ? (
              <li className="list-group-item">Nenhum quarto disponível.</li>
            ) : (
              quartos.map((q) => (
                <li key={q.idQuarto} className="list-group-item">
                  ID {q.idQuarto} • Quarto {q.numero} • {q.tipo} • R$ {q.precoDiaria}
                </li>
              ))
            )}
          </ul>
        </div>
      </section>

      <section className="card shadow-sm" aria-labelledby="reserva-titulo">
        <div className="card-body">
          <h2 id="reserva-titulo" className="h5">Criar reserva</h2>
          <form className="row g-3" onSubmit={criarReserva} noValidate>
            <div className="col-md-3"><label className="form-label" htmlFor="idReserva">ID reserva</label><input id="idReserva" type="number" className="form-control" required value={reserva.idReserva} onChange={(event) => setField('idReserva', event.target.value)} /></div>
            <div className="col-md-3"><label className="form-label" htmlFor="idHospedeReserva">ID hóspede</label><input id="idHospedeReserva" type="number" className="form-control" required value={reserva.idHospede} onChange={(event) => setField('idHospede', event.target.value)} /></div>
            <div className="col-md-3"><label className="form-label" htmlFor="idQuartoReserva">ID quarto</label><input id="idQuartoReserva" type="number" className="form-control" required value={reserva.idQuarto} onChange={(event) => setField('idQuarto', event.target.value)} /></div>
            <div className="col-md-3"><label className="form-label" htmlFor="canal">Canal</label><input id="canal" className="form-control" required value={reserva.canal} onChange={(event) => setField('canal', event.target.value)} /></div>
            <div className="col-md-3"><label className="form-label" htmlFor="dataCheckin">Check-in</label><input id="dataCheckin" type="date" className="form-control" required value={reserva.dataCheckin} onChange={(event) => setField('dataCheckin', event.target.value)} /></div>
            <div className="col-md-3"><label className="form-label" htmlFor="dataCheckout">Check-out</label><input id="dataCheckout" type="date" className="form-control" required value={reserva.dataCheckout} onChange={(event) => setField('dataCheckout', event.target.value)} /></div>
            <div className="col-md-3"><label className="form-label" htmlFor="statusReserva">Status</label><input id="statusReserva" className="form-control" required value={reserva.status} onChange={(event) => setField('status', event.target.value)} /></div>
            <div className="col-md-3"><label className="form-label" htmlFor="valorPrevisto">Valor previsto</label><input id="valorPrevisto" type="number" step="0.01" className="form-control" required value={reserva.valorPrevisto} onChange={(event) => setField('valorPrevisto', event.target.value)} /></div>

            <div className="col-12"><button className="btn btn-success" type="submit">Salvar reserva</button></div>
          </form>
        </div>
      </section>

      <StatusAlert message={statusMessage} isError={isError} />
    </>
  )
}

export default ReservarQuartosPage
