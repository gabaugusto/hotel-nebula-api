(function () {
    const STORAGE_KEY = 'hotel-nebula-base-url';
    let allData = {};

    const HIGHCHARTS_SCRIPT_FALLBACKS = [
        [
            'https://code.highcharts.com/highcharts.js',
            'https://cdn.jsdelivr.net/npm/highcharts/highcharts.js'
        ],
        [
            'https://code.highcharts.com/modules/exporting.js',
            'https://cdn.jsdelivr.net/npm/highcharts/modules/exporting.js'
        ],
        [
            'https://code.highcharts.com/modules/export-data.js',
            'https://cdn.jsdelivr.net/npm/highcharts/modules/export-data.js'
        ],
        [
            'https://code.highcharts.com/modules/heatmap.js',
            'https://cdn.jsdelivr.net/npm/highcharts/modules/heatmap.js'
        ],
        [
            'https://code.highcharts.com/modules/accessibility.js',
            'https://cdn.jsdelivr.net/npm/highcharts/modules/accessibility.js'
        ]
    ];

    function getBaseUrl() {
        return localStorage.getItem(STORAGE_KEY) || 'http://localhost:8083';
    }

    function setStatus(message, isError = false) {
        const status = document.getElementById('status');
        if (!status) return;
        status.textContent = message;
        status.classList.toggle('text-danger', isError);
        status.classList.toggle('text-success', !isError);
    }

    function carregarScript(url) {
        return new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = url;
            script.async = false;
            script.onload = () => {
                script.dataset.loaded = 'true';
                resolve();
            };
            script.onerror = () => reject(new Error(`Falha ao carregar script: ${url}`));
            document.head.appendChild(script);
        });
    }

    async function carregarPrimeiroScriptDisponivel(urls) {
        let ultimoErro = null;

        for (const url of urls) {
            try {
                await carregarScript(url);
                return;
            } catch (error) {
                ultimoErro = error;
            }
        }

        throw ultimoErro || new Error(`Falha ao carregar scripts: ${urls.join(', ')}`);
    }

    async function garantirHighcharts() {
        if (window.Highcharts) {
            return;
        }

        for (const fallbackList of HIGHCHARTS_SCRIPT_FALLBACKS) {
            await carregarPrimeiroScriptDisponivel(fallbackList);
        }

        if (!window.Highcharts) {
            throw new Error('Highcharts não foi carregado. Verifique internet/proxy/firewall e tente novamente.');
        }
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

    async function carregarDados() {
        try {
            setStatus('Carregando dados...');
            
            const [hospedagens, quartos, reservas, feedbacks, pagamentos, hospedes] = await Promise.all([
                request('/hospedagens'),
                request('/quartos'),
                request('/reservas'),
                request('/feedbacks'),
                request('/pagamentos'),
                request('/hospedes')
            ]);

            allData = {
                hospedagens: hospedagens || [],
                quartos: quartos || [],
                reservas: reservas || [],
                feedbacks: feedbacks || [],
                pagamentos: pagamentos || [],
                hospedes: hospedes || []
            };

            renderizarDashboard();
            setStatus('Dashboard atualizado com sucesso!');
        } catch (error) {
            setStatus(error.message, true);
            console.error('Erro ao carregar dados:', error);
        }
    }

    function renderizarDashboard() {
        if (!window.Highcharts) {
            throw new Error('Highcharts indisponível no momento.');
        }

        atualizarEstatisticas();
        renderizarGraficoLucroQuartos();
        renderizarGraficoTempoHospedagem();
        renderizarGraficoHospedagensPorMes();
        renderizarGraficoCanalReservas();
        renderizarGraficoPrecoAvaliacao();
        renderizarGraficoTopHospedes();
        renderizarGraficoHeatmapMesTipoQuarto();
    }

    function obterValor(obj, chaves, fallback = null) {
        for (const chave of chaves) {
            if (obj && obj[chave] !== undefined && obj[chave] !== null) {
                return obj[chave];
            }
        }
        return fallback;
    }

    function paraNumero(valor) {
        const numero = Number(valor);
        return Number.isFinite(numero) ? numero : 0;
    }

    function atualizarEstatisticas() {
        const { hospedagens, feedbacks, pagamentos } = allData;

        // Total hospedagens
        const totalHosp = hospedagens.filter(h => h.status === 'encerrada').length;
        document.getElementById('totalHospedagens').textContent = totalHosp;

        // Faturamento total
        const faturamento = hospedagens.reduce((sum, h) => sum + (h.total_geral || 0), 0);
        document.getElementById('faturamentoTotal').textContent = `R$ ${faturamento.toLocaleString('pt-BR')}`;

        // Média avaliações
        const mediaAval = feedbacks.length > 0
            ? (feedbacks.reduce((sum, f) => sum + f.nota_geral, 0) / feedbacks.length).toFixed(2)
            : '-';
        document.getElementById('mediaGeral').textContent = mediaAval;

        // Total avaliações
        document.getElementById('totalAvaliacoes').textContent = feedbacks.length;
    }

    function renderizarGraficoLucroQuartos() {
        const { hospedagens, quartos, reservas } = allData;

        // Mapear hospedagens por quarto
        const lucroQuartos = {};
        
        hospedagens.forEach(hosp => {
            const reserva = reservas.find(r => r._id === hosp.reserva_id);
            if (!reserva) return;

            const quarto = quartos.find(q => q._id === reserva.quarto_id);
            if (!quarto) return;

            const key = `${quarto.numero} (${quarto.tipo})`;
            if (!lucroQuartos[key]) {
                lucroQuartos[key] = 0;
            }
            lucroQuartos[key] += hosp.total_geral || 0;
        });

        const categorias = Object.keys(lucroQuartos).sort((a, b) => lucroQuartos[b] - lucroQuartos[a]);
        const dados = categorias.map(cat => lucroQuartos[cat]);

        Highcharts.chart('chart-lucro-quartos', {
            chart: { type: 'bar' },
            title: { text: '' },
            xAxis: { categories: categorias, title: { text: 'Quarto' } },
            yAxis: { title: { text: 'Faturamento (R$)' } },
            series: [{
                name: 'Lucro',
                data: dados,
                color: '#667eea',
                dataLabels: { enabled: true, format: 'R$ {point.y:,.0f}' }
            }],
            credits: { enabled: false },
            exporting: { enabled: true }
        });
    }

    function renderizarGraficoTempoHospedagem() {
        const { hospedagens, reservas } = allData;

        // Calcular dias hospedados
        const diasHospedagem = [];
        
        hospedagens.forEach(hosp => {
            const checkin = new Date(hosp.checkin_real);
            const checkout = new Date(hosp.checkout_real);
            const dias = Math.ceil((checkout - checkin) / (1000 * 60 * 60 * 24));
            if (dias > 0) {
                diasHospedagem.push(dias);
            }
        });

        // Criar histograma (bins de 1-3, 4-6, 7-9, 10+)
        const bins = {
            '1-3 dias': 0,
            '4-6 dias': 0,
            '7-9 dias': 0,
            '10+ dias': 0
        };

        diasHospedagem.forEach(dias => {
            if (dias <= 3) bins['1-3 dias']++;
            else if (dias <= 6) bins['4-6 dias']++;
            else if (dias <= 9) bins['7-9 dias']++;
            else bins['10+ dias']++;
        });

        Highcharts.chart('chart-tempo-hospedagem', {
            chart: { type: 'column' },
            title: { text: '' },
            xAxis: { 
                categories: Object.keys(bins),
                title: { text: 'Duração da hospedagem' }
            },
            yAxis: { title: { text: 'Quantidade' } },
            series: [{
                name: 'Hospedagens',
                data: Object.values(bins),
                color: '#38ef7d',
                dataLabels: { enabled: true, format: '{point.y}' }
            }],
            credits: { enabled: false },
            exporting: { enabled: true }
        });
    }

    function renderizarGraficoHospedagensPorMes() {
        const { hospedagens } = allData;

        // Agrupar por mês
        const hospedagensPorMes = {};
        
        hospedagens.forEach(hosp => {
            const data = new Date(hosp.checkin_real);
            const mes = data.toLocaleDateString('pt-BR', { month: 'long', year: 'numeric' });
            hospedagensPorMes[mes] = (hospedagensPorMes[mes] || 0) + 1;
        });

        const meses = Object.keys(hospedagensPorMes).sort();
        const dados = meses.map(m => hospedagensPorMes[m]);

        Highcharts.chart('chart-hospedagens-mes', {
            chart: { type: 'line' },
            title: { text: '' },
            xAxis: { categories: meses, title: { text: 'Mês' } },
            yAxis: { title: { text: 'Hospedagens' } },
            series: [{
                name: 'Hospedagens',
                data: dados,
                color: '#ff6b6b',
                marker: { enabled: true, radius: 5 },
                lineWidth: 3
            }],
            credits: { enabled: false },
            exporting: { enabled: true }
        });
    }

    function renderizarGraficoCanalReservas() {
        const { reservas } = allData;

        // Agrupar por canal
        const porCanal = {};
        
        reservas.forEach(res => {
            porCanal[res.canal] = (porCanal[res.canal] || 0) + 1;
        });

        const dados = Object.entries(porCanal).map(([canal, qtd]) => ({
            name: canal.replace(/_/g, ' ').toUpperCase(),
            y: qtd
        }));

        Highcharts.chart('chart-canal-reservas', {
            chart: { type: 'pie' },
            title: { text: '' },
            tooltip: { pointFormat: '<b>{point.name}</b>: {point.y} ({point.percentage:.1f}%)' },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: { enabled: true, format: '{point.name}: {point.percentage:.0f}%' },
                    showInLegend: true
                }
            },
            series: [{
                name: 'Reservas',
                colorByPoint: true,
                data: dados
            }],
            credits: { enabled: false },
            exporting: { enabled: true }
        });
    }

    function renderizarGraficoPrecoAvaliacao() {
        const { hospedagens, feedbacks, quartos, reservas } = allData;

        // Criar mapa de quarto para avaliação média
        const avaliacaoPorQuarto = {};
        feedbacks.forEach(fb => {
            const quarto = fb.quarto_id;
            if (!avaliacaoPorQuarto[quarto]) {
                avaliacaoPorQuarto[quarto] = { notas: [], count: 0, soma: 0 };
            }
            avaliacaoPorQuarto[quarto].notas.push(fb.nota_geral);
            avaliacaoPorQuarto[quarto].soma += fb.nota_geral;
            avaliacaoPorQuarto[quarto].count++;
        });

        // Preparar dados de dispersão
        const dadosDispersa = hospedagens.map(hosp => {
            const reserva = reservas.find(r => r._id === hosp.reserva_id);
            const quarto = quartos.find(q => q._id === reserva?.quarto_id);
            
            if (!quarto || !avaliacaoPorQuarto[quarto._id]) {
                return null;
            }

            const mediaAval = avaliacaoPorQuarto[quarto._id].soma / avaliacaoPorQuarto[quarto._id].count;
            
            return {
                x: hosp.total_geral || 0,
                y: mediaAval,
                name: `Quarto ${quarto.numero}`
            };
        }).filter(d => d !== null);

        Highcharts.chart('chart-preco-avaliacao', {
            chart: { type: 'scatter', zoomType: 'xy' },
            title: { text: '' },
            xAxis: { title: { text: 'Valor da Hospedagem (R$)' }, type: 'linear' },
            yAxis: { title: { text: 'Avaliação Média' } },
            plotOptions: {
                scatter: {
                    marker: { radius: 8, states: { hover: { radiusPlus: 2 } } },
                    dataLabels: { enabled: false }
                }
            },
            series: [{
                name: 'Hospedagens',
                colorByPoint: true,
                data: dadosDispersa,
                color: '#4facfe'
            }],
            credits: { enabled: false },
            exporting: { enabled: true }
        });
    }

    function renderizarGraficoTopHospedes() {
        const { hospedagens, reservas, hospedes } = allData;

        const reservaParaHospede = new Map(
            reservas.map((reserva) => [
                obterValor(reserva, ['_id', 'idReserva']),
                obterValor(reserva, ['hospede_id', 'idHospede'])
            ])
        );

        const nomeHospede = new Map(
            hospedes.map((hospede) => [
                obterValor(hospede, ['_id', 'idHospede']),
                obterValor(hospede, ['nome'], 'Hóspede sem nome')
            ])
        );

        const gastoPorHospede = new Map();

        hospedagens.forEach((hospedagem) => {
            const idReserva = obterValor(hospedagem, ['reserva_id', 'idReserva']);
            const idHospede = reservaParaHospede.get(idReserva);
            if (!idHospede) return;

            const valor = paraNumero(obterValor(hospedagem, ['total_geral', 'totalGeral'], 0));
            gastoPorHospede.set(idHospede, (gastoPorHospede.get(idHospede) || 0) + valor);
        });

        const top = Array.from(gastoPorHospede.entries())
            .map(([idHospede, gasto]) => ({
                nome: nomeHospede.get(idHospede) || `Hóspede #${idHospede}`,
                gasto
            }))
            .sort((a, b) => b.gasto - a.gasto)
            .slice(0, 10)
            .reverse();

        Highcharts.chart('chart-top-hospedes', {
            chart: { type: 'bar' },
            title: { text: '' },
            xAxis: {
                categories: top.map((item) => item.nome),
                title: { text: 'Hóspede' }
            },
            yAxis: {
                title: { text: 'Gasto total (R$)' }
            },
            series: [{
                name: 'Gasto',
                data: top.map((item) => item.gasto),
                color: '#8e44ad',
                dataLabels: {
                    enabled: true,
                    formatter: function () {
                        return `R$ ${Highcharts.numberFormat(this.y, 0, ',', '.')}`;
                    }
                }
            }],
            tooltip: {
                pointFormatter: function () {
                    return `<b>R$ ${Highcharts.numberFormat(this.y, 2, ',', '.')}</b>`;
                }
            },
            credits: { enabled: false },
            exporting: { enabled: true }
        });
    }

    function renderizarGraficoHeatmapMesTipoQuarto() {
        const { hospedagens, reservas, quartos } = allData;

        const tipoPorQuarto = new Map(
            quartos.map((quarto) => [
                obterValor(quarto, ['_id', 'idQuarto']),
                obterValor(quarto, ['tipo'], 'Não informado')
            ])
        );

        const quartoPorReserva = new Map(
            reservas.map((reserva) => [
                obterValor(reserva, ['_id', 'idReserva']),
                obterValor(reserva, ['quarto_id', 'idQuarto'])
            ])
        );

        const mesesSet = new Set();
        const tiposSet = new Set();
        const matriz = new Map();

        hospedagens.forEach((hospedagem) => {
            const checkin = obterValor(hospedagem, ['checkin_real', 'checkinReal']);
            const idReserva = obterValor(hospedagem, ['reserva_id', 'idReserva']);
            const idQuarto = quartoPorReserva.get(idReserva);
            const tipo = tipoPorQuarto.get(idQuarto) || 'Não informado';
            if (!checkin) return;

            const data = new Date(checkin);
            if (Number.isNaN(data.getTime())) return;

            const mes = data.toLocaleDateString('pt-BR', { month: 'short', year: '2-digit' });

            mesesSet.add(mes);
            tiposSet.add(tipo);

            const chave = `${mes}|||${tipo}`;
            matriz.set(chave, (matriz.get(chave) || 0) + 1);
        });

        const meses = Array.from(mesesSet);
        const tipos = Array.from(tiposSet);

        const pontos = [];
        meses.forEach((mes, x) => {
            tipos.forEach((tipo, y) => {
                const chave = `${mes}|||${tipo}`;
                pontos.push([x, y, matriz.get(chave) || 0]);
            });
        });

        Highcharts.chart('chart-heatmap-mes-tipo', {
            chart: { type: 'heatmap' },
            title: { text: '' },
            xAxis: {
                categories: meses,
                title: { text: 'Mês' }
            },
            yAxis: {
                categories: tipos,
                title: { text: 'Tipo de quarto' },
                reversed: true
            },
            colorAxis: {
                min: 0,
                minColor: '#edf8ff',
                maxColor: '#08306b'
            },
            legend: {
                align: 'right',
                layout: 'vertical',
                verticalAlign: 'top',
                y: 25,
                symbolHeight: 220
            },
            tooltip: {
                formatter: function () {
                    return `<b>${tipos[this.point.y]}</b><br/><b>${meses[this.point.x]}</b>: ${this.point.value} hospedagem(ns)`;
                }
            },
            series: [{
                name: 'Hospedagens',
                borderWidth: 1,
                data: pontos,
                dataLabels: {
                    enabled: true,
                    color: '#000000'
                }
            }],
            credits: { enabled: false },
            exporting: { enabled: true }
        });
    }

    async function init() {
        const btn = document.getElementById('btn-atualizar-dashboard');
        if (btn) {
            btn.addEventListener('click', carregarDados);
        }

        try {
            await garantirHighcharts();
            carregarDados();
        } catch (error) {
            setStatus(error.message, true);
            console.error('Erro ao inicializar dashboard:', error);
        }
    }

    // Inicializar quando o DOM estiver pronto
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();
