const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8083'

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options
  })

  if (!response.ok) {
    let detail = ''
    try {
      detail = await response.text()
    } catch {
      detail = ''
    }
    throw new Error(`Erro ${response.status}${detail ? `: ${detail}` : ''}`)
  }

  if (response.status === 204) return null
  return response.json()
}

export const api = {
  baseUrl: API_BASE_URL,
  listHospedes: () => request('/hospedes'),
  getHospedeById: (id) => request(`/hospedes/${id}`),
  createHospede: (payload) => request('/hospedes', { method: 'POST', body: JSON.stringify(payload) }),
  updateHospede: (id, payload) => request(`/hospedes/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
  deleteHospede: (id) => request(`/hospedes/${id}`, { method: 'DELETE' }),

  listQuartosDisponiveis: () => request('/quartos/disponiveis'),
  createReserva: (payload) => request('/reservas', { method: 'POST', body: JSON.stringify(payload) }),

  getDashboardFaturamento: () => request('/dashboard/faturamento'),
  getAvaliacoesResumo: () => request('/avaliacoes/resumo')
}
