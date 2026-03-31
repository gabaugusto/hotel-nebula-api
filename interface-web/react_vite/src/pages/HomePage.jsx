import { useState } from 'react'
import { api } from '../services/api'
import StatusAlert from '../components/StatusAlert'
import heroImage from '../assets/Hotel_Nebula.jpg'
import gallery01 from '../assets/0001_al-maha.webp'
import gallery02 from '../assets/0002_amangari.webp'
import gallery03 from '../assets/0003_under_sea.webp'
import gallery04 from '../assets/0004_incredible_sea.jpg'
import room01 from '../assets/room_0001.jpg'
import room02 from '../assets/room_0002.jpg'
import room03 from '../assets/room_0003.jpg'

const galleryItems = [
  {
    src: gallery01,
    alt: 'Lobby sofisticado com iluminação aconchegante',
  },
  {
    src: gallery02,
    alt: 'Área externa do hotel com paisagismo elegante',
  },
  {
    src: gallery03,
    alt: 'Restaurante temático com atmosfera refinada',
  },
  {
    src: gallery04,
    alt: 'Vista panorâmica para o mar em frente ao hotel',
  },
  {
    src: room01,
    alt: 'Quarto de luxo com cama king size',
  },
  {
    src: room02,
    alt: 'Suíte confortável com varanda privativa',
  },
  {
    src: room03,
    alt: 'Banheiro moderno com acabamento premium',
  },
]

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
      <section className="hero-section card overflow-hidden shadow-sm mb-4" aria-labelledby="hero-titulo">
        <div className="row g-0 align-items-stretch">
          <div className="col-12 col-lg-6 p-4 p-md-5 d-flex flex-column justify-content-center hero-content">
            <p className="hero-kicker mb-2">Experiência premium</p>
            <h1 id="hero-titulo" className="display-6 fw-bold mb-3">Bem-vindo ao Hotel Nebula</h1>
            <p className="text-secondary mb-4">
              Conforto, sofisticação e atendimento de excelência em um só lugar.
              Gerencie reservas e hóspedes com uma interface simples e elegante.
            </p>
            <div className="d-flex flex-wrap gap-2">
              <a href="/reservar-quartos" className="btn btn-primary btn-lg">Reservar agora</a>
              <a href="#galeria" className="btn btn-outline-primary btn-lg">Ver galeria</a>
            </div>
          </div>
          <div className="col-12 col-lg-6">
            <img
              src={heroImage}
              className="img-fluid hero-image"
              alt="Fachada iluminada do Hotel Nebula ao entardecer"
            />
          </div>
        </div>
      </section>

      <section id="galeria" className="card shadow-sm mb-4" aria-labelledby="galeria-titulo">
        <div className="card-body p-4">
          <div className="d-flex flex-wrap justify-content-between align-items-center gap-2 mb-3">
            <h2 id="galeria-titulo" className="h4 mb-0">Galeria de fotos</h2>
            <span className="text-secondary small">Ambientes e experiências do Hotel Nebula</span>
          </div>

          <div className="photo-gallery">
            {galleryItems.map((item) => (
              <figure key={item.src} className="gallery-item">
                <img src={item.src} alt={item.alt} />
              </figure>
            ))}
          </div>
        </div>
      </section>

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
