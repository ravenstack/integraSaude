// ============================================
// SISTEMA DE PÁGINAS REATIVO - ÁREA MÉDICA
// ============================================

let medicoId = null;
let medicoDados = null;
let paginaAtual = 'dashboard';

// Inicializar a aplicação
async function inicializarMedico() {
    // Por enquanto, usamos medico ID 1 como padrão (em produção seria do localStorage)
    const sessionStr = localStorage.getItem('user_session');
    if (sessionStr) {
        try {
            const usuario = JSON.parse(sessionStr);
            medicoId = usuario.id || 1;
        } catch (e) {
            medicoId = 1;
        }
    } else {
        medicoId = 1;
    }

    await carregarDadosMedico();
}

// Carregar dados do médico
async function carregarDadosMedico() {
    try {
        const response = await fetch(`/api/medico/info/${medicoId}`);
        if (!response.ok) throw new Error('Erro ao carregar dados');
        medicoDados = await response.json();

        // Atualizar header
        const primeiroNome = medicoDados.nome.split(' ')[0];
        const iniciais = primeiroNome.substring(0, 2).toUpperCase();

        document.getElementById('header-nome').textContent = primeiroNome;
        document.getElementById('header-nome').classList.remove('skeleton');
        document.getElementById('header-avatar').textContent = iniciais;
        document.getElementById('dropdown-nome').textContent = primeiroNome;
        document.getElementById('dropdown-email').textContent = medicoDados.email;
        document.getElementById('dropdown-avatar').textContent = iniciais;

        // Tentar carregar foto
        if (medicoDados.foto) {
            document.getElementById('header-avatar-img').src = medicoDados.foto;
            document.getElementById('header-avatar-img').style.display = 'block';
            document.getElementById('header-avatar').style.display = 'none';

            document.getElementById('dropdown-avatar-img').src = medicoDados.foto;
            document.getElementById('dropdown-avatar-img').style.display = 'block';
            document.getElementById('dropdown-avatar').style.display = 'none';
        }
    } catch (error) {
        console.error('Erro:', error);
    }
}

// Função principal para carregar páginas
function carregarPagina(pagina, event = null) {
    if (event) {
        event.preventDefault();
    }

    paginaAtual = pagina;

    // Atualizar sidebar
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('bg-primary-fixed', 'text-primary', 'font-bold');
        link.classList.add('text-secondary');
    });

    const linkAtivo = document.querySelector(`[data-page="${pagina}"]`);
    if (linkAtivo) {
        linkAtivo.classList.remove('text-secondary');
        linkAtivo.classList.add('bg-primary-fixed', 'text-primary', 'font-bold');
    }

    // Renderizar página
    const conteudo = document.getElementById('conteudo-principal');
    conteudo.innerHTML = '';

    switch (pagina) {
        case 'dashboard':
            renderizarDashboard();
            break;
        case 'agenda':
            renderizarAgenda();
            break;
        case 'pacientes':
            renderizarPacientes();
            break;
        case 'financeiro':
            renderizarFinanceiro();
            break;
        case 'documentacao':
            renderizarDocumentacao();
            break;
        case 'relatorios':
            renderizarRelatorios();
            break;
        case 'configuracoes':
            renderizarConfiguracoes();
            break;
        default:
            renderizarDashboard();
    }
}

// ============================================
// DASHBOARD
// ============================================
async function renderizarDashboard() {
    atualizarHeader('Dashboard', 'Resumo do seu dia');

    try {
        const response = await fetch(`/api/medico/dashboard/${medicoId}`);
        const dados = await response.json();

        const html = `
            <div class="space-y-lg">
                <div class="grid grid-cols-1 md:grid-cols-4 gap-md">
                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <div class="flex justify-between items-start">
                            <div>
                                <p class="text-on-surface-variant text-sm font-bold uppercase">Consultas Hoje</p>
                                <h2 class="text-h2 font-bold text-primary mt-sm">${dados.consultasHoje}</h2>
                            </div>
                            <div class="bg-primary-fixed rounded-full p-md">
                                <span class="material-symbols-outlined text-primary">today</span>
                            </div>
                        </div>
                    </div>

                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <div class="flex justify-between items-start">
                            <div>
                                <p class="text-on-surface-variant text-sm font-bold uppercase">Pacientes Atendidos</p>
                                <h2 class="text-h2 font-bold text-primary mt-sm">${dados.pacientesAtendidos}</h2>
                            </div>
                            <div class="bg-primary-fixed rounded-full p-md">
                                <span class="material-symbols-outlined text-primary">people</span>
                            </div>
                        </div>
                    </div>

                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <div class="flex justify-between items-start">
                            <div>
                                <p class="text-on-surface-variant text-sm font-bold uppercase">Faturamento (Mês)</p>
                                <h2 class="text-h2 font-bold text-primary mt-sm">R$ ${parseFloat(dados.faturamentoMes).toLocaleString('pt-BR', {minimumFractionDigits: 2, maximumFractionDigits: 2})}</h2>
                            </div>
                            <div class="bg-primary-fixed rounded-full p-md">
                                <span class="material-symbols-outlined text-primary">attach_money</span>
                            </div>
                        </div>
                    </div>

                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <div class="flex justify-between items-start">
                            <div>
                                <p class="text-on-surface-variant text-sm font-bold uppercase">Taxa Conclusão</p>
                                <h2 class="text-h2 font-bold text-primary mt-sm">95%</h2>
                            </div>
                            <div class="bg-primary-fixed rounded-full p-md">
                                <span class="material-symbols-outlined text-primary">check_circle</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-lg">
                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <h3 class="font-h3 text-h3 text-primary mb-md flex items-center gap-xs">
                            <span class="material-symbols-outlined">schedule</span>
                            Próxima Consulta
                        </h3>
                        ${dados.proximaConsulta ? `
                            <div class="space-y-sm">
                                <div class="flex justify-between items-center">
                                    <p class="text-on-surface-variant">Paciente:</p>
                                    <p class="font-bold text-on-surface">${dados.proximaConsulta.paciente}</p>
                                </div>
                                <div class="flex justify-between items-center">
                                    <p class="text-on-surface-variant">Data/Hora:</p>
                                    <p class="font-bold text-on-surface">${new Date(dados.proximaConsulta.dataHora).toLocaleString('pt-BR')}</p>
                                </div>
                                <div class="flex justify-between items-center">
                                    <p class="text-on-surface-variant">Tipo:</p>
                                    <p class="font-bold text-on-surface">${dados.proximaConsulta.tipo === 'PRESENCIAL' ? '🏢 Presencial' : '💻 Online'}</p>
                                </div>
                                <button onclick="carregarPagina('agenda')" class="w-full mt-md bg-primary text-on-primary font-bold py-sm rounded-full hover:opacity-90 transition-all">
                                    Ver Agenda
                                </button>
                            </div>
                        ` : `
                            <p class="text-on-surface-variant">Nenhuma consulta agendada</p>
                        `}
                    </div>

                    <div class="bg-tertiary-container/10 border border-tertiary-container/20 rounded-2xl p-lg">
                        <h3 class="font-h3 text-h3 text-tertiary mb-md flex items-center gap-xs">
                            <span class="material-symbols-outlined">lightbulb</span>
                            Dica do Dia
                        </h3>
                        <p class="text-on-surface italic">"${dados.dicaDoDia}"</p>
                    </div>
                </div>
            </div>
        `;

        document.getElementById('conteudo-principal').innerHTML = html;
    } catch (error) {
        console.error('Erro ao carregar dashboard:', error);
        document.getElementById('conteudo-principal').innerHTML = '<p class="text-error">Erro ao carregar dashboard</p>';
    }
}

// ============================================
// AGENDA
// ============================================
async function renderizarAgenda() {
    atualizarHeader('Agenda', 'Gerenciamento de consultas agendadas');

    try {
        const response = await fetch(`/api/medico/agenda/${medicoId}`);
        const consultas = await response.json();

        let html = `
            <div class="space-y-lg">
                <div class="flex gap-md mb-lg">
                    <button class="px-md py-sm bg-primary text-on-primary rounded-full font-bold hover:opacity-90" onclick="filtrarAgenda('AGENDADA')">
                        Agendadas
                    </button>
                    <button class="px-md py-sm bg-surface-variant text-on-surface rounded-full font-bold hover:bg-surface-container" onclick="filtrarAgenda('REALIZADA')">
                        Realizadas
                    </button>
                    <button class="px-md py-sm bg-surface-variant text-on-surface rounded-full font-bold hover:bg-surface-container" onclick="filtrarAgenda('CANCELADA')">
                        Canceladas
                    </button>
                </div>

                <div class="space-y-md">
        `;

        if (consultas.length === 0) {
            html += '<p class="text-on-surface-variant">Nenhuma consulta encontrada</p>';
        } else {
            consultas.forEach(c => {
                const data = new Date(c.dataHora);
                const statusColor = c.status === 'AGENDADA' ? 'bg-primary-fixed text-primary' :
                                   c.status === 'REALIZADA' ? 'bg-green-100 text-green-700' : 'bg-error-container text-error';

                html += `
                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm hover:shadow-md transition-shadow">
                        <div class="flex justify-between items-start mb-md">
                            <div>
                                <h3 class="font-h3 text-h3 text-primary">${c.paciente}</h3>
                                <p class="text-on-surface-variant text-sm">${data.toLocaleString('pt-BR')}</p>
                            </div>
                            <span class="px-md py-xs rounded-full text-sm font-bold ${statusColor}">${c.status}</span>
                        </div>
                        <div class="flex justify-between items-center text-sm">
                            <span class="text-on-surface-variant">${c.tipo === 'PRESENCIAL' ? '🏢 Presencial' : '💻 Online'}</span>
                            <span class="font-bold text-on-surface">R$ ${parseFloat(c.valor).toLocaleString('pt-BR', {minimumFractionDigits: 2})}</span>
                        </div>
                        ${c.observacoes ? `<p class="text-on-surface-variant text-sm mt-md italic">"${c.observacoes}"</p>` : ''}
                    </div>
                `;
            });
        }

        html += '</div></div>';
        document.getElementById('conteudo-principal').innerHTML = html;
    } catch (error) {
        console.error('Erro ao carregar agenda:', error);
        document.getElementById('conteudo-principal').innerHTML = '<p class="text-error">Erro ao carregar agenda</p>';
    }
}

// Filtrar agenda
async function filtrarAgenda(status) {
    try {
        const response = await fetch(`/api/medico/agenda/${medicoId}?status=${status}`);
        const consultas = await response.json();

        let html = `
            <div class="space-y-lg">
                <div class="space-y-md">
        `;

        if (consultas.length === 0) {
            html += '<p class="text-on-surface-variant">Nenhuma consulta encontrada</p>';
        } else {
            consultas.forEach(c => {
                const data = new Date(c.dataHora);
                const statusColor = c.status === 'AGENDADA' ? 'bg-primary-fixed text-primary' :
                                   c.status === 'REALIZADA' ? 'bg-green-100 text-green-700' : 'bg-error-container text-error';

                html += `
                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm hover:shadow-md transition-shadow">
                        <div class="flex justify-between items-start mb-md">
                            <div>
                                <h3 class="font-h3 text-h3 text-primary">${c.paciente}</h3>
                                <p class="text-on-surface-variant text-sm">${data.toLocaleString('pt-BR')}</p>
                            </div>
                            <span class="px-md py-xs rounded-full text-sm font-bold ${statusColor}">${c.status}</span>
                        </div>
                        <div class="flex justify-between items-center text-sm">
                            <span class="text-on-surface-variant">${c.tipo === 'PRESENCIAL' ? '🏢 Presencial' : '💻 Online'}</span>
                            <span class="font-bold text-on-surface">R$ ${parseFloat(c.valor).toLocaleString('pt-BR', {minimumFractionDigits: 2})}</span>
                        </div>
                    </div>
                `;
            });
        }

        html += '</div></div>';
        document.getElementById('conteudo-principal').innerHTML = html;
    } catch (error) {
        console.error('Erro ao filtrar agenda:', error);
    }
}

// ============================================
// PACIENTES
// ============================================
async function renderizarPacientes() {
    atualizarHeader('Pacientes', 'Gerenciar seus pacientes');

    try {
        const response = await fetch(`/api/medico/pacientes/${medicoId}`);
        const pacientes = await response.json();

        let html = `
            <div class="space-y-lg">
                <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl overflow-hidden shadow-sm">
                    <div class="overflow-x-auto">
                        <table class="w-full text-sm">
                            <thead class="bg-surface-container border-b border-outline-variant">
                                <tr>
                                    <th class="px-lg py-md text-left font-bold text-on-surface">Nome</th>
                                    <th class="px-lg py-md text-left font-bold text-on-surface">Última Consulta</th>
                                    <th class="px-lg py-md text-left font-bold text-on-surface">Próxima Consulta</th>
                                    <th class="px-lg py-md text-left font-bold text-on-surface">Status</th>
                                    <th class="px-lg py-md text-left font-bold text-on-surface">Ações</th>
                                </tr>
                            </thead>
                            <tbody>
        `;

        pacientes.forEach(p => {
            const ultimaData = p.ultimaConsulta ? new Date(p.ultimaConsulta).toLocaleDateString('pt-BR') : '-';
            const proximaData = p.proximaConsulta ? new Date(p.proximaConsulta).toLocaleDateString('pt-BR') : '-';
            const statusColor = p.status === 'ATIVO' ? 'bg-green-100 text-green-700' : 'bg-surface-variant text-on-surface-variant';

            html += `
                <tr class="border-b border-outline-variant/30 hover:bg-surface-variant transition-colors">
                    <td class="px-lg py-md text-on-surface font-bold">${p.nome}</td>
                    <td class="px-lg py-md text-on-surface-variant">${ultimaData}</td>
                    <td class="px-lg py-md text-on-surface-variant">${proximaData}</td>
                    <td class="px-lg py-md">
                        <span class="px-md py-xs rounded-full text-xs font-bold ${statusColor}">${p.status}</span>
                    </td>
                    <td class="px-lg py-md">
                        <button onclick="verDetalhePaciente(${p.id})" class="text-primary hover:text-primary-container font-bold text-sm">
                            Ver Detalhes
                        </button>
                    </td>
                </tr>
            `;
        });

        html += `
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        `;

        document.getElementById('conteudo-principal').innerHTML = html;
    } catch (error) {
        console.error('Erro ao carregar pacientes:', error);
        document.getElementById('conteudo-principal').innerHTML = '<p class="text-error">Erro ao carregar pacientes</p>';
    }
}

// Ver detalhes do paciente
async function verDetalhePaciente(pacienteId) {
    try {
        const response = await fetch(`/api/medico/paciente/${pacienteId}`);
        const paciente = await response.json();

        atualizarHeader(`${paciente.nome}`, 'Prontuário do paciente');

        let html = `
            <div class="space-y-lg">
                <div class="grid grid-cols-1 md:grid-cols-3 gap-lg">
                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <p class="text-on-surface-variant text-sm font-bold uppercase mb-sm">Email</p>
                        <p class="text-on-surface font-bold">${paciente.email}</p>

                        <p class="text-on-surface-variant text-sm font-bold uppercase mt-lg mb-sm">Telefone</p>
                        <p class="text-on-surface font-bold">${paciente.telefone}</p>

                        <p class="text-on-surface-variant text-sm font-bold uppercase mt-lg mb-sm">Data de Nascimento</p>
                        <p class="text-on-surface font-bold">${new Date(paciente.dataNascimento).toLocaleDateString('pt-BR')}</p>

                        <p class="text-on-surface-variant text-sm font-bold uppercase mt-lg mb-sm">CPF</p>
                        <p class="text-on-surface font-bold">${paciente.cpf}</p>
                    </div>

                    <div class="md:col-span-2">
                        <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                            <h3 class="font-h3 text-h3 text-primary mb-md">Histórico de Consultas</h3>
                            <div class="space-y-md max-h-96 overflow-y-auto">
        `;

        paciente.consultas.forEach(c => {
            const data = new Date(c.data);
            const statusColor = c.status === 'REALIZADA' ? 'bg-green-100 text-green-700' :
                               c.status === 'AGENDADA' ? 'bg-primary-fixed text-primary' : 'bg-error-container text-error';

            html += `
                <div class="border-l-4 border-primary pl-md py-sm">
                    <div class="flex justify-between items-start">
                        <div>
                            <p class="text-on-surface font-bold">${data.toLocaleString('pt-BR')}</p>
                            <p class="text-on-surface-variant text-sm">${c.tipo === 'PRESENCIAL' ? '🏢 Presencial' : '💻 Online'} - R$ ${parseFloat(c.valor).toLocaleString('pt-BR', {minimumFractionDigits: 2})}</p>
                        </div>
                        <span class="px-md py-xs rounded-full text-xs font-bold ${statusColor}">${c.status}</span>
                    </div>
                    ${c.observacoes ? `<p class="text-on-surface-variant text-sm mt-sm italic">"${c.observacoes}"</p>` : ''}
                </div>
            `;
        });

        html += `
                            </div>
                        </div>

                        <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm mt-lg">
                            <h3 class="font-h3 text-h3 text-primary mb-md">Documentos</h3>
                            <div class="space-y-md">
        `;

        if (paciente.documentos.length === 0) {
            html += '<p class="text-on-surface-variant">Nenhum documento</p>';
        } else {
            paciente.documentos.forEach(d => {
                const data = new Date(d.dataEnvio);
                html += `
                    <div class="flex justify-between items-center p-md border border-outline-variant rounded-lg hover:bg-surface-variant transition-colors">
                        <div>
                            <p class="font-bold text-on-surface">${d.nome}</p>
                            <p class="text-on-surface-variant text-sm">${d.tamanho} • ${data.toLocaleDateString('pt-BR')}</p>
                        </div>
                        <a href="${d.url}" target="_blank" class="text-primary hover:text-primary-container">
                            <span class="material-symbols-outlined">download</span>
                        </a>
                    </div>
                `;
            });
        }

        html += `
                            </div>
                        </div>

                        <button onclick="carregarPagina('pacientes')" class="mt-lg w-full bg-surface-variant text-on-surface py-sm rounded-full font-bold hover:bg-surface-container transition-colors">
                            Voltar aos Pacientes
                        </button>
                    </div>
                </div>
            </div>
        `;

        document.getElementById('conteudo-principal').innerHTML = html;
    } catch (error) {
        console.error('Erro ao carregar detalhes:', error);
        document.getElementById('conteudo-principal').innerHTML = '<p class="text-error">Erro ao carregar detalhes</p>';
    }
}

// ============================================
// FINANCEIRO
// ============================================
async function renderizarFinanceiro() {
    atualizarHeader('Financeiro', 'Relatório de ganhos e despesas');

    try {
        const response = await fetch(`/api/medico/financeiro/${medicoId}`);
        const dados = await response.json();

        let html = `
            <div class="space-y-lg">
                <div class="grid grid-cols-1 md:grid-cols-3 gap-md">
                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <p class="text-on-surface-variant text-sm font-bold uppercase">Recebido</p>
                        <h2 class="text-h2 font-bold text-primary mt-sm">R$ ${parseFloat(dados.recebido).toLocaleString('pt-BR', {minimumFractionDigits: 2})}</h2>
                    </div>

                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <p class="text-on-surface-variant text-sm font-bold uppercase">A Receber</p>
                        <h2 class="text-h2 font-bold text-primary mt-sm">R$ ${parseFloat(dados.aReceber).toLocaleString('pt-BR', {minimumFractionDigits: 2})}</h2>
                    </div>

                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <p class="text-on-surface-variant text-sm font-bold uppercase">Total</p>
                        <h2 class="text-h2 font-bold text-primary mt-sm">R$ ${parseFloat(dados.total).toLocaleString('pt-BR', {minimumFractionDigits: 2})}</h2>
                    </div>
                </div>

                <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                    <h3 class="font-h3 text-h3 text-primary mb-md">Histórico de Transações</h3>
                    <div class="overflow-x-auto">
                        <table class="w-full text-sm">
                            <thead class="bg-surface-container border-b border-outline-variant">
                                <tr>
                                    <th class="px-lg py-md text-left font-bold text-on-surface">Paciente</th>
                                    <th class="px-lg py-md text-left font-bold text-on-surface">Data</th>
                                    <th class="px-lg py-md text-left font-bold text-on-surface">Valor</th>
                                    <th class="px-lg py-md text-left font-bold text-on-surface">Status</th>
                                </tr>
                            </thead>
                            <tbody>
        `;

        dados.historico.forEach(item => {
            const data = new Date(item.data);
            const statusColor = item.status === 'REALIZADA' ? 'bg-green-100 text-green-700' : 'bg-primary-fixed text-primary';

            html += `
                <tr class="border-b border-outline-variant/30 hover:bg-surface-variant transition-colors">
                    <td class="px-lg py-md text-on-surface font-bold">${item.paciente}</td>
                    <td class="px-lg py-md text-on-surface-variant">${data.toLocaleDateString('pt-BR')}</td>
                    <td class="px-lg py-md text-on-surface font-bold">R$ ${parseFloat(item.valor).toLocaleString('pt-BR', {minimumFractionDigits: 2})}</td>
                    <td class="px-lg py-md">
                        <span class="px-md py-xs rounded-full text-xs font-bold ${statusColor}">${item.status}</span>
                    </td>
                </tr>
            `;
        });

        html += `
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        `;

        document.getElementById('conteudo-principal').innerHTML = html;
    } catch (error) {
        console.error('Erro ao carregar financeiro:', error);
        document.getElementById('conteudo-principal').innerHTML = '<p class="text-error">Erro ao carregar financeiro</p>';
    }
}

// ============================================
// DOCUMENTAÇÃO
// ============================================
async function renderizarDocumentacao() {
    atualizarHeader('Documentação', 'Arquivos e documentos dos pacientes');

    try {
        const response = await fetch(`/api/medico/documentacao/${medicoId}`);
        const dados = await response.json();

        const renderizarSecao = (titulo, icon, documentos) => {
            if (documentos.length === 0) return '';

            let html = `
                <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                    <h3 class="font-h3 text-h3 text-primary mb-md flex items-center gap-xs">
                        <span class="material-symbols-outlined">${icon}</span>
                        ${titulo}
                    </h3>
                    <div class="space-y-md">
            `;

            documentos.forEach(d => {
                const data = new Date(d.dataEnvio);
                html += `
                    <div class="flex justify-between items-center p-md border border-outline-variant rounded-lg hover:bg-surface-variant transition-colors">
                        <div>
                            <p class="font-bold text-on-surface">${d.nome}</p>
                            <p class="text-on-surface-variant text-sm">${d.paciente} • ${d.tamanho} • ${data.toLocaleDateString('pt-BR')}</p>
                        </div>
                        <a href="${d.url}" target="_blank" class="text-primary hover:text-primary-container">
                            <span class="material-symbols-outlined">download</span>
                        </a>
                    </div>
                `;
            });

            html += '</div></div>';
            return html;
        };

        let html = '<div class="space-y-lg">';
        html += renderizarSecao('Exames', 'lab_panel', dados.exames);
        html += renderizarSecao('Procedimentos', 'article', dados.procedimentos);
        html += renderizarSecao('Prescrições', 'prescription', dados.prescricoes);
        html += renderizarSecao('Anexos', 'attach_file', dados.anexos);
        html += '</div>';

        document.getElementById('conteudo-principal').innerHTML = html;
    } catch (error) {
        console.error('Erro ao carregar documentação:', error);
        document.getElementById('conteudo-principal').innerHTML = '<p class="text-error">Erro ao carregar documentação</p>';
    }
}

// ============================================
// RELATÓRIOS
// ============================================
async function renderizarRelatorios() {
    atualizarHeader('Relatórios', 'Análise de desempenho');

    try {
        const response = await fetch(`/api/medico/relatorios/${medicoId}`);
        const dados = await response.json();

        let html = `
            <div class="space-y-lg">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-lg">
                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <p class="text-on-surface-variant text-sm font-bold uppercase mb-md">Consultas Realizadas</p>
                        <div class="space-y-md">
                            <div class="flex justify-between items-center">
                                <span class="text-on-surface">Total</span>
                                <span class="text-h2 font-bold text-primary">${dados.totalConsultas}</span>
                            </div>
                            <div class="flex justify-between items-center">
                                <span class="text-on-surface">Realizadas</span>
                                <span class="font-bold text-on-surface">${dados.consultasRealizadas}</span>
                            </div>
                            <div class="flex justify-between items-center">
                                <span class="text-on-surface">Taxa de Realização</span>
                                <span class="font-bold text-primary">${dados.taxaRealizacao}</span>
                            </div>
                        </div>
                    </div>

                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <p class="text-on-surface-variant text-sm font-bold uppercase mb-md">Receita</p>
                        <div class="space-y-md">
                            <div class="flex justify-between items-center">
                                <span class="text-on-surface">Total Recebido</span>
                                <span class="text-h2 font-bold text-primary">R$ ${parseFloat(dados.receitaTotal).toLocaleString('pt-BR', {minimumFractionDigits: 2})}</span>
                            </div>
                            <div class="flex justify-between items-center">
                                <span class="text-on-surface">Valor Médio</span>
                                <span class="font-bold text-on-surface">R$ ${parseFloat(dados.receitaMedia).toLocaleString('pt-BR', {minimumFractionDigits: 2})}</span>
                            </div>
                            <div class="flex justify-between items-center">
                                <span class="text-on-surface">Valor por Sessão</span>
                                <span class="font-bold text-on-surface">R$ ${parseFloat(dados.valorSessao).toLocaleString('pt-BR', {minimumFractionDigits: 2})}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-lg">
                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <p class="text-on-surface-variant text-sm font-bold uppercase mb-md">Pacientes</p>
                        <p class="text-h2 font-bold text-primary">${dados.pacientesUnicos}</p>
                    </div>

                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <p class="text-on-surface-variant text-sm font-bold uppercase mb-md">Modalidade de Atendimento</p>
                        <div class="space-y-sm">
                            <div class="flex justify-between items-center">
                                <span class="text-on-surface">🏢 Presenciais</span>
                                <span class="font-bold text-on-surface">${dados.sessoesPrelenciais}</span>
                            </div>
                            <div class="flex justify-between items-center">
                                <span class="text-on-surface">💻 Online</span>
                                <span class="font-bold text-on-surface">${dados.sessoesOnline}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;

        document.getElementById('conteudo-principal').innerHTML = html;
    } catch (error) {
        console.error('Erro ao carregar relatórios:', error);
        document.getElementById('conteudo-principal').innerHTML = '<p class="text-error">Erro ao carregar relatórios</p>';
    }
}

// ============================================
// CONFIGURAÇÕES
// ============================================
async function renderizarConfiguracoes() {
    atualizarHeader('Configurações', 'Gerencie sua conta e preferências');

    if (!medicoDados) return;

    let html = `
        <div class="space-y-lg">
            <div class="flex gap-md mb-lg border-b border-outline-variant pb-md">
                <a href="#" onclick="abrirAbaConfiguracoes('perfil', event)" class="pb-md px-sm font-body-md text-primary border-b-2 border-primary aba-config" data-aba="perfil">Perfil</a>
                <a href="#" onclick="abrirAbaConfiguracoes('profissional', event)" class="pb-md px-sm font-body-md text-on-surface-variant hover:text-on-surface border-b-2 border-transparent hover:border-outline-variant aba-config" data-aba="profissional">Dados Profissionais</a>
                <a href="#" onclick="abrirAbaConfiguracoes('notificacoes', event)" class="pb-md px-sm font-body-md text-on-surface-variant hover:text-on-surface border-b-2 border-transparent hover:border-outline-variant aba-config" data-aba="notificacoes">Notificações</a>
                <a href="#" onclick="abrirAbaConfiguracoes('seguranca', event)" class="pb-md px-sm font-body-md text-on-surface-variant hover:text-on-surface border-b-2 border-transparent hover:border-outline-variant aba-config" data-aba="seguranca">Segurança</a>
            </div>

            <div id="aba-conteudo">
                <!-- Será renderizada pela função abrirAbaConfiguracoes -->
            </div>
        </div>
    `;

    document.getElementById('conteudo-principal').innerHTML = html;

    // Carregar primeira aba
    abrirAbaConfiguracoes('perfil');
}

async function abrirAbaConfiguracoes(aba, event = null) {
    if (event) event.preventDefault();

    // Atualizar abas ativas
    document.querySelectorAll('.aba-config').forEach(el => {
        el.classList.remove('text-primary', 'border-primary');
        el.classList.add('text-on-surface-variant', 'border-transparent');
    });

    document.querySelector(`[data-aba="${aba}"]`).classList.add('text-primary', 'border-primary');
    document.querySelector(`[data-aba="${aba}"]`).classList.remove('text-on-surface-variant', 'border-transparent');

    let html = '';

    switch (aba) {
        case 'perfil':
            html = `
                <div class="grid grid-cols-1 md:grid-cols-2 gap-lg">
                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <h3 class="font-h3 text-h3 text-primary mb-md">Informações Pessoais</h3>
                        <div class="space-y-md">
                            <div>
                                <label class="block text-on-surface-variant text-sm font-bold mb-xs">Nome Completo</label>
                                <input type="text" value="${medicoDados.nome}" class="w-full px-md py-sm border border-outline-variant rounded-lg focus:ring-2 focus:ring-primary outline-none" />
                            </div>
                            <div>
                                <label class="block text-on-surface-variant text-sm font-bold mb-xs">Email</label>
                                <input type="email" value="${medicoDados.email}" class="w-full px-md py-sm border border-outline-variant rounded-lg focus:ring-2 focus:ring-primary outline-none" />
                            </div>
                            <button class="w-full bg-primary text-on-primary py-sm rounded-full font-bold hover:opacity-90 transition-all">
                                Salvar Alterações
                            </button>
                        </div>
                    </div>

                    <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                        <h3 class="font-h3 text-h3 text-primary mb-md">Fotografia de Perfil</h3>
                        <div class="space-y-md">
                            ${medicoDados.foto ? `<img src="${medicoDados.foto}" alt="Foto" class="w-32 h-32 rounded-full object-cover mx-auto border-4 border-primary" />` : ''}
                            <input type="file" class="w-full" accept="image/*" />
                            <button class="w-full bg-primary text-on-primary py-sm rounded-full font-bold hover:opacity-90 transition-all">
                                Atualizar Foto
                            </button>
                        </div>
                    </div>
                </div>
            `;
            break;

        case 'profissional':
            html = `
                <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm">
                    <h3 class="font-h3 text-h3 text-primary mb-md">Informações Profissionais</h3>
                    <div class="space-y-md max-w-2xl">
                        <div>
                            <label class="block text-on-surface-variant text-sm font-bold mb-xs">Especialidade</label>
                            <input type="text" value="${medicoDados.especialidade}" class="w-full px-md py-sm border border-outline-variant rounded-lg focus:ring-2 focus:ring-primary outline-none" />
                        </div>
                        <div>
                            <label class="block text-on-surface-variant text-sm font-bold mb-xs">Registro Profissional (CRP/CFP/CRM)</label>
                            <input type="text" value="${medicoDados.registroProfissional}" class="w-full px-md py-sm border border-outline-variant rounded-lg focus:ring-2 focus:ring-primary outline-none" />
                        </div>
                        <div>
                            <label class="block text-on-surface-variant text-sm font-bold mb-xs">Valor da Sessão (R$)</label>
                            <input type="number" value="${medicoDados.valorSessao}" step="0.01" class="w-full px-md py-sm border border-outline-variant rounded-lg focus:ring-2 focus:ring-primary outline-none" />
                        </div>
                        <button class="w-full bg-primary text-on-primary py-sm rounded-full font-bold hover:opacity-90 transition-all">
                            Salvar Alterações
                        </button>
                    </div>
                </div>
            `;
            break;

        case 'notificacoes':
            html = `
                <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm max-w-2xl">
                    <h3 class="font-h3 text-h3 text-primary mb-md">Preferências de Notificações</h3>
                    <div class="space-y-md">
                        <div class="flex items-center justify-between p-md border border-outline-variant rounded-lg">
                            <label class="text-on-surface font-bold">Lembrete de Consultas</label>
                            <input type="checkbox" checked class="w-5 h-5" />
                        </div>
                        <div class="flex items-center justify-between p-md border border-outline-variant rounded-lg">
                            <label class="text-on-surface font-bold">Novo Paciente Agendado</label>
                            <input type="checkbox" checked class="w-5 h-5" />
                        </div>
                        <div class="flex items-center justify-between p-md border border-outline-variant rounded-lg">
                            <label class="text-on-surface font-bold">Mensagens do Suporte</label>
                            <input type="checkbox" checked class="w-5 h-5" />
                        </div>
                        <div class="flex items-center justify-between p-md border border-outline-variant rounded-lg">
                            <label class="text-on-surface font-bold">Atualizações do Sistema</label>
                            <input type="checkbox" class="w-5 h-5" />
                        </div>
                        <button class="w-full bg-primary text-on-primary py-sm rounded-full font-bold hover:opacity-90 transition-all mt-lg">
                            Salvar Preferências
                        </button>
                    </div>
                </div>
            `;
            break;

        case 'seguranca':
            html = `
                <div class="bg-surface-container-lowest border border-outline-variant rounded-2xl p-lg shadow-sm max-w-2xl">
                    <h3 class="font-h3 text-h3 text-primary mb-md">Alterar Senha</h3>
                    <div class="space-y-md mb-lg">
                        <div>
                            <label class="block text-on-surface-variant text-sm font-bold mb-xs">Senha Atual</label>
                            <input type="password" class="w-full px-md py-sm border border-outline-variant rounded-lg focus:ring-2 focus:ring-primary outline-none" placeholder="••••••••" />
                        </div>
                        <div>
                            <label class="block text-on-surface-variant text-sm font-bold mb-xs">Nova Senha</label>
                            <input type="password" class="w-full px-md py-sm border border-outline-variant rounded-lg focus:ring-2 focus:ring-primary outline-none" placeholder="••••••••" />
                        </div>
                        <div>
                            <label class="block text-on-surface-variant text-sm font-bold mb-xs">Confirmar Nova Senha</label>
                            <input type="password" class="w-full px-md py-sm border border-outline-variant rounded-lg focus:ring-2 focus:ring-primary outline-none" placeholder="••••••••" />
                        </div>
                        <button class="w-full bg-primary text-on-primary py-sm rounded-full font-bold hover:opacity-90 transition-all">
                            Atualizar Senha
                        </button>
                    </div>

                    <div class="border-t border-outline-variant pt-lg">
                        <h3 class="font-h3 text-h3 text-primary mb-md">Autenticação em Dois Fatores</h3>
                        <div class="flex items-center justify-between p-md border border-outline-variant rounded-lg">
                            <label class="text-on-surface font-bold">Ativar 2FA</label>
                            <input type="checkbox" class="w-5 h-5" />
                        </div>
                        <p class="text-on-surface-variant text-sm mt-md">A autenticação em dois fatores aumenta a segurança da sua conta.</p>
                    </div>
                </div>
            `;
            break;
    }

    document.getElementById('aba-conteudo').innerHTML = html;
}

// ============================================
// HELPERS
// ============================================
function atualizarHeader(titulo, subtitulo) {
    document.getElementById('header-titulo').textContent = titulo;
    document.getElementById('header-subtitulo').textContent = subtitulo;
}
