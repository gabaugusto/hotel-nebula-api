import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import AppLayout from './components/AppLayout'
import BuscarHospedesPage from './pages/BuscarHospedesPage'
import CadastrarHospedePage from './pages/CadastrarHospedePage'
import DashboardPage from './pages/DashboardPage'
import HomePage from './pages/HomePage'
import ReservarQuartosPage from './pages/ReservarQuartosPage'
import './App.css'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AppLayout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/buscar-hospedes" element={<BuscarHospedesPage />} />
          <Route path="/cadastrar-hospedes" element={<CadastrarHospedePage />} />
          <Route path="/reservar-quartos" element={<ReservarQuartosPage />} />
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
