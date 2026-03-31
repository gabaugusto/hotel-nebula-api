import { NavLink } from 'react-router-dom'

const links = [
  { to: '/', label: 'Início' },
  { to: '/buscar-hospedes', label: 'Buscar hóspedes' },
  { to: '/cadastrar-hospedes', label: 'Cadastrar hóspede' },
  { to: '/reservar-quartos', label: 'Reservar quartos' },
  { to: '/dashboard', label: 'Dashboard' }
]

function AppNavbar() {
  return (
    <header className="navbar navbar-expand-lg bg-white border-bottom" role="banner">
      <div className="container">
        <span className="navbar-brand fw-semibold">Hotel Nebula • React + Vite</span>
        <nav aria-label="Navegação principal">
          <ul className="navbar-nav flex-row gap-3">
            {links.map((link) => (
              <li key={link.to} className="nav-item">
                <NavLink
                  to={link.to}
                  className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`}
                  end={link.to === '/'}
                >
                  {link.label}
                </NavLink>
              </li>
            ))}
          </ul>
        </nav>
      </div>
    </header>
  )
}

export default AppNavbar
