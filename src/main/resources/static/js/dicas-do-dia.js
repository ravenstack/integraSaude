// Array com as dicas do dia para pacientes
const DICAS_DO_DIA = [
    {
        emoji: "🧠",
        titulo: "Dica do Dia",
        conteudo: "Identifique uma emoção desconfortável que surgiu hoje. Nomeie-a em voz alta ou anote. Dar nome às emoções reduz a intensidade delas e aumenta seu autocontrole emocional."
    },
    {
        emoji: "🌿",
        titulo: "Dica do Dia",
        conteudo: "Pratique a técnica do \"corpo como termômetro\": pare por 2 minutos, feche os olhos e escaneie mentalmente cada parte do corpo. Onde você sente tensão? Respire suavemente nessa região por 5 respirações."
    },
    {
        emoji: "🗣️",
        titulo: "Dica do Dia",
        conteudo: "Em uma conversa difícil hoje, tente a escuta sem julgamento. Repita mentalmente: \"Não preciso concordar, apenas entender\". Isso reduz reatividade e melhora a comunicação."
    },
    {
        emoji: "📓",
        titulo: "Dica do Dia",
        conteudo: "Escreva 3 coisas que deram certo nas últimas 24 horas, por menores que sejam. Esse exercício de gratidão ativa fortalece redes neurais associadas ao bem-estar."
    },
    {
        emoji: "🚶",
        titulo: "Dica do Dia",
        conteudo: "Faça uma \"caminhada consciente\" de 10 minutos. Sinta o contato dos pés com o chão, observe cores e sons ao redor, sem rumo ou pressa. Isso reduz ruminação e ansiedade."
    },
    {
        emoji: "🛑",
        titulo: "Dica do Dia",
        conteudo: "Estabeleça um \"ponto de parada\" entre um estímulo e sua reação. Quando sentir irritação ou urgência, respire uma vez e pergunte: \"O que é mais útil agora?\""
    },
    {
        emoji: "💤",
        titulo: "Dica do Dia",
        conteudo: "Antes de dormir, faça uma \"revisão sem julgamento\" do dia: note um momento de estresse e um momento de calma. Aceite ambos como partes legítimas da sua experiência."
    },
    {
        emoji: "🎨",
        titulo: "Dica do Dia",
        conteudo: "Expresse uma emoção sem palavras: desenhe, pinte, dance ou faça colagem por 5 minutos. A expressão criativa ativa áreas cerebrais ligadas à regulação emocional."
    },
    {
        emoji: "📵",
        titulo: "Dica do Dia",
        conteudo: "Desconecte-se das telas por 30 minutos seguidos. Use esse tempo para uma atividade que envolva seus 5 sentidos: tomar um chá, tocar em uma textura, ouvir uma música inteira."
    },
    {
        emoji: "❤️",
        titulo: "Dica do Dia",
        conteudo: "Envie uma mensagem curta para alguém que você valoriza, dizendo algo genuíno sobre o que admira nessa pessoa. Conexões sociais significativas são protetoras da saúde mental."
    },
    {
        emoji: "🧘",
        titulo: "Dica do Dia",
        conteudo: "Experimente a \"respiração quadrada\": inspire (4s), segure (4s), expire (4s), segure (4s). Repita 5 ciclos. Útil antes de reuniões, provas ou momentos de alta demanda."
    },
    {
        emoji: "🌧️",
        titulo: "Dica do Dia",
        conteudo: "Permita-se sentir uma emoção considerada \"negativa\" sem tentar mudá-la imediatamente. Diga internamente: \"Estou triste/ansioso(a) e isso é humano.\" A aceitação reduz o sofrimento secundário."
    }
];

// Função para obter uma dica aleatória
function obterDicaAleatoria() {
    const indice = Math.floor(Math.random() * DICAS_DO_DIA.length);
    return DICAS_DO_DIA[indice];
}

// Função para obter dica do dia (mesma para o mesmo dia)
function obterDicaDoDia() {
    const hoje = new Date().toDateString();
    const stored = localStorage.getItem('dica_do_dia_data');
    const storedDica = localStorage.getItem('dica_do_dia');

    if (stored === hoje && storedDica) {
        return JSON.parse(storedDica);
    }

    const dica = obterDicaAleatoria();
    localStorage.setItem('dica_do_dia_data', hoje);
    localStorage.setItem('dica_do_dia', JSON.stringify(dica));
    return dica;
}
