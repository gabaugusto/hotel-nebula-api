(function () {
    const STORAGE_KEY = 'hotel-nebula-base-url';

    function getBaseUrl() {
        return localStorage.getItem(STORAGE_KEY) || 'http://localhost:8083';
    }

    function setBaseUrl(url) {
        localStorage.setItem(STORAGE_KEY, url);
    }

    function setStatus(message, isError = false) {
        const status = document.getElementById('status');
        if (!status) return;
        status.textContent = message;
        status.classList.toggle('text-danger', isError);
        status.classList.toggle('text-success', !isError);
    }

    async function request(path, options = {}) {
        const response = await fetch(`${getBaseUrl()}${path}`, {
            headers: { 'Content-Type': 'application/json' },
            ...options
        });

        if (!response.ok) {
            let detail = '';
            try {
                detail = await response.text();
            } catch {
                detail = '';
            }
            throw new Error(`Erro ${response.status}${detail ? `: ${detail}` : ''}`);
        }

        if (response.status === 204) return null;
        return response.json();
    }

    function initHome() {
        const form = document.getElementById('form-config');
        const input = document.getElementById('baseUrl');
        if (!form || !input) return;

        input.value = getBaseUrl();
        form.addEventListener('submit', (event) => {
            event.preventDefault();
            setBaseUrl(input.value.trim());
            setStatus('Configuração salva com sucesso.');
        });
    }

    async function carregarHospedes() {
        const tbody = document.getElementById('tabela-hospedes');
        if (!tbody) return;
        tbody.innerHTML = '<tr><td colspan="6">Carregando...</td></tr>';

        try {
            const data = await request('/hospedes');
            tbody.innerHTML = '';

            if (!data.length) {
                tbody.innerHTML = '<tr><td colspan="6">Nenhum hóspede encontrado.</td></tr>';
                return;
            }

            data.forEach((item) => {
                const row = document.createElement('tr');
                row.innerHTML = `
          <td>${item.idHospede}</td>
          <td>${item.nome}</td>
          <td>${item.email}</td>
          <td>${item.cpf}</td>
          <td>${item.ativo ? 'Sim' : 'Não'}</td>
          <td>
            <button class="btn btn-sm btn-outline-danger" data-id="${item.idHospede}" type="button">Excluir</button>
          </td>
        `;
                tbody.appendChild(row);
            });

            tbody.querySelectorAll('button[data-id]').forEach((button) => {
                button.addEventListener('click', async () => {
                    try {
                        await request(`/hospedes/${button.dataset.id}`, { method: 'DELETE' });
                        setStatus('Hóspede removido com sucesso.');
                        carregarHospedes();
                    } catch (error) {
                        setStatus(error.message, true);
                    }
                });
            });
        } catch (error) {
            tbody.innerHTML = '<tr><td colspan="6">Falha ao carregar hóspedes.</td></tr>';
            setStatus(error.message, true);
        }
    }

    function initBuscarHospedes() {
        const btn = document.getElementById('btn-carregar-hospedes');
        const form = document.getElementById('form-busca-id');
        const input = document.getElementById('idHospedeBusca');

        if (btn) btn.addEventListener('click', carregarHospedes);
        if (form && input) {
            form.addEventListener('submit', async (event) => {
                event.preventDefault();
                try {
                    const hospede = await request(`/hospedes/${input.value}`);
                    setStatus(`Encontrado: ${hospede.nome} (${hospede.email})`);
                } catch (error) {
                    setStatus(error.message, true);
                }
            });
        }

        carregarHospedes();
    }

    function getHospedePayload() {
        return {
            idHospede: Number(document.getElementById('idHospede').value),
            nome: document.getElementById('nome').value,
            email: document.getElementById('email').value,
            cpf: document.getElementById('cpf').value,
            telefone: document.getElementById('telefone').value || null,
            dataNascimento: document.getElementById('dataNascimento').value || null,
            ativo: document.getElementById('ativo').value === 'true'
        };
    }

    function initCadastrarHospedes() {
        const form = document.getElementById('form-hospede');
        const btnAtualizar = document.getElementById('btn-atualizar');

        if (form) {
            form.addEventListener('submit', async (event) => {
                event.preventDefault();
                try {
                    const payload = getHospedePayload();
                    await request('/hospedes', { method: 'POST', body: JSON.stringify(payload) });
                    setStatus('Hóspede criado com sucesso.');
                } catch (error) {
                    setStatus(error.message, true);
                }
            });
        }

        if (btnAtualizar) {
            btnAtualizar.addEventListener('click', async () => {
                try {
                    const payload = getHospedePayload();
                    await request(`/hospedes/${payload.idHospede}`, {
                        method: 'PUT',
                        body: JSON.stringify(payload)
                    });
                    setStatus('Hóspede atualizado com sucesso.');
                } catch (error) {
                    setStatus(error.message, true);
                }
            });
        }
    }

    async function carregarQuartosDisponiveis() {
        const list = document.getElementById('lista-quartos');
        if (!list) return;
        list.innerHTML = '<li class="list-group-item">Carregando...</li>';

        try {
            const quartos = await request('/quartos/disponiveis');
            list.innerHTML = '';
            if (!quartos.length) {
                list.innerHTML = '<li class="list-group-item">Nenhum quarto disponível.</li>';
                return;
            }

            quartos.forEach((q) => {
                const li = document.createElement('li');
                li.className = 'list-group-item';
                li.textContent = `ID ${q.idQuarto} • Quarto ${q.numero} • ${q.tipo} • R$ ${q.precoDiaria}`;
                list.appendChild(li);
            });
        } catch (error) {
            list.innerHTML = '<li class="list-group-item">Falha ao carregar quartos.</li>';
            setStatus(error.message, true);
        }
    }

    function initReservarQuartos() {
        const btn = document.getElementById('btn-carregar-quartos');
        const form = document.getElementById('form-reserva');

        if (btn) btn.addEventListener('click', carregarQuartosDisponiveis);
        if (form) {
            form.addEventListener('submit', async (event) => {
                event.preventDefault();

                const payload = {
                    idReserva: Number(document.getElementById('idReserva').value),
                    idHospede: Number(document.getElementById('idHospedeReserva').value),
                    idQuarto: Number(document.getElementById('idQuartoReserva').value),
                    dataReserva: new Date().toISOString(),
                    dataCheckin: document.getElementById('dataCheckin').value,
                    dataCheckout: document.getElementById('dataCheckout').value,
                    status: document.getElementById('statusReserva').value,
                    canal: document.getElementById('canal').value,
                    valorPrevisto: Number(document.getElementById('valorPrevisto').value)
                };

                try {
                    await request('/reservas', { method: 'POST', body: JSON.stringify(payload) });
                    setStatus('Reserva criada com sucesso.');
                } catch (error) {
                    setStatus(error.message, true);
                }
            });
        }

        carregarQuartosDisponiveis();
    }

    async function carregarDashboard() {
        try {
            const [fat, av] = await Promise.all([
                request('/dashboard/faturamento'),
                request('/avaliacoes/resumo')
            ]);

            document.getElementById('totalHospedagens').textContent = fat.totalHospedagens ?? '-';
            document.getElementById('faturamentoTotal').textContent = fat.faturamentoTotal ?? '-';
            document.getElementById('totalAvaliacoes').textContent = av.totalAvaliacoes ?? '-';
            document.getElementById('mediaGeral').textContent = av.mediaGeral ?? '-';
            setStatus('Dashboard atualizado com sucesso.');
        } catch (error) {
            setStatus(error.message, true);
        }
    }

    function initDashboard() {
        const btn = document.getElementById('btn-atualizar-dashboard');
        if (btn) btn.addEventListener('click', carregarDashboard);
        carregarDashboard();
    }

    const page = document.body.dataset.page;
    if (page === 'home') initHome();
    if (page === 'buscar-hospedes') initBuscarHospedes();
    if (page === 'cadastrar-hospedes') initCadastrarHospedes();
    if (page === 'reservar-quartos') initReservarQuartos();
    if (page === 'dashboard') initDashboard();
})();
