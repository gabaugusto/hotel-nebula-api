import { Outlet } from 'react-router-dom'
import AppNavbar from './AppNavbar'

function AppLayout() {
  return (
    <>
      <a className="skip-link" href="#conteudo">Pular para o conteúdo principal</a>
      <AppNavbar />
      <main id="conteudo" className="container py-4" role="main">
        <Outlet />
      </main>
    </>
  )
}

export default AppLayout
