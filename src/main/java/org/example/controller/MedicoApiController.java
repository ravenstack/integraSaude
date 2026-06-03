package org.example.controller;

import org.example.model.*;
import org.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medico")
public class MedicoApiController {

    @Autowired
    private ProfissionalRepository profissionalRepo;

    @Autowired
    private PacienteRepository pacienteRepo;

    @Autowired
    private ConsultaRepository consultaRepo;

    @Autowired
    private DocumentoRepository documentoRepo;

    // ====== MÉDICO INFO ======
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getMedicoInfo(@PathVariable Integer id) {
        Optional<Profissional> medico = profissionalRepo.findById(id);
        if (medico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Profissional prof = medico.get();
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", prof.getId());
        response.put("nome", prof.getNome());
        response.put("tipo", prof.getTipo());
        response.put("especialidade", prof.getEspecialidade());
        response.put("email", prof.getEmail());
        response.put("registroProfissional", prof.getRegistroProfissional());
        response.put("valorSessao", prof.getValorSessao());
        response.put("foto", getFotoMedico(prof.getNome()));

        return ResponseEntity.ok(response);
    }

    // ====== DASHBOARD ======
    @GetMapping("/dashboard/{medicoId}")
    public ResponseEntity<?> getDashboard(@PathVariable Integer medicoId) {
        Optional<Profissional> medico = profissionalRepo.findById(medicoId);
        if (medico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Profissional prof = medico.get();
        Map<String, Object> dashboard = new LinkedHashMap<>();

        // Consultas do médico (filtra consultas onde profissional_id = medicoId)
        List<Consulta> todasConsultas = consultaRepo.findAll().stream()
                .filter(c -> c.getProfissional().getId().equals(medicoId))
                .collect(Collectors.toList());

        // Hoje
        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fim = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        long consultasHoje = todasConsultas.stream()
                .filter(c -> !c.getDataHora().isBefore(inicio) && !c.getDataHora().isAfter(fim))
                .count();

        // Próxima consulta
        Optional<Consulta> proximaConsulta = todasConsultas.stream()
                .filter(c -> c.getDataHora().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Consulta::getDataHora));

        // Pacientes atendidos (únicos)
        long pacientesAtendidos = todasConsultas.stream()
                .map(c -> c.getPaciente().getId())
                .distinct()
                .count();

        // Faturamento do mês
        BigDecimal faturamento = todasConsultas.stream()
                .filter(c -> {
                    YearMonth mes = YearMonth.from(c.getDataHora());
                    YearMonth mesAtual = YearMonth.from(LocalDateTime.now());
                    return mes.equals(mesAtual);
                })
                .map(Consulta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dashboard.put("consultasHoje", consultasHoje);
        dashboard.put("proximaConsulta", proximaConsulta.map(c -> {
            Map<String, Object> consulta = new LinkedHashMap<>();
            consulta.put("id", c.getId());
            consulta.put("paciente", c.getPaciente().getNome());
            consulta.put("dataHora", c.getDataHora());
            consulta.put("tipo", c.getTipo());
            return consulta;
        }).orElse(null));
        dashboard.put("pacientesAtendidos", pacientesAtendidos);
        dashboard.put("faturamentoMes", faturamento);

        // Dica do dia
        String[] dicas = {
                "Lembre-se de fazer notas após cada sessão para melhor acompanhamento.",
                "Mantenha a ética e confidencialidade em todos os atendimentos.",
                "Ofereça um ambiente confortável e seguro para seus pacientes.",
                "Acompanhe o progresso dos pacientes regularmente.",
                "Cultive empatia e escuta ativa em cada consulta."
        };
        dashboard.put("dicaDoDia", dicas[new Random().nextInt(dicas.length)]);

        return ResponseEntity.ok(dashboard);
    }

    // ====== AGENDA ======
    @GetMapping("/agenda/{medicoId}")
    public ResponseEntity<?> getAgenda(@PathVariable Integer medicoId,
                                       @RequestParam(required = false) String data,
                                       @RequestParam(required = false) String status) {
        Optional<Profissional> medico = profissionalRepo.findById(medicoId);
        if (medico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Consulta> todasConsultas = consultaRepo.findAll().stream()
                .filter(c -> c.getProfissional().getId().equals(medicoId))
                .sorted(Comparator.comparing(Consulta::getDataHora).reversed())
                .collect(Collectors.toList());

        // Filtrar por status se fornecido
        if (status != null && !status.isEmpty()) {
            todasConsultas = todasConsultas.stream()
                    .filter(c -> c.getStatus().toString().equals(status.toUpperCase()))
                    .collect(Collectors.toList());
        }

        List<Map<String, Object>> agenda = todasConsultas.stream()
                .map(c -> {
                    Map<String, Object> consulta = new LinkedHashMap<>();
                    consulta.put("id", c.getId());
                    consulta.put("paciente", c.getPaciente().getNome());
                    consulta.put("dataHora", c.getDataHora());
                    consulta.put("tipo", c.getTipo());
                    consulta.put("status", c.getStatus());
                    consulta.put("valor", c.getValor());
                    consulta.put("observacoes", c.getObservacoes());
                    return consulta;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(agenda);
    }

    // ====== PACIENTES ======
    @GetMapping("/pacientes/{medicoId}")
    public ResponseEntity<?> getPacientes(@PathVariable Integer medicoId) {
        Optional<Profissional> medico = profissionalRepo.findById(medicoId);
        if (medico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Pega todos os pacientes únicos que já fizeram consulta com este médico
        List<Consulta> consultasDoMedico = consultaRepo.findAll().stream()
                .filter(c -> c.getProfissional().getId().equals(medicoId))
                .collect(Collectors.toList());

        Set<Integer> pacienteIds = new HashSet<>();
        for (Consulta c : consultasDoMedico) {
            pacienteIds.add(c.getPaciente().getId());
        }

        List<Map<String, Object>> pacientes = new ArrayList<>();
        for (Integer pacienteId : pacienteIds) {
            Optional<Paciente> paciente = pacienteRepo.findById(pacienteId);
            if (paciente.isPresent()) {
                Paciente p = paciente.get();

                // Última consulta
                Optional<Consulta> ultimaConsulta = consultasDoMedico.stream()
                        .filter(c -> c.getPaciente().getId().equals(pacienteId))
                        .max(Comparator.comparing(Consulta::getDataHora));

                // Próxima consulta
                Optional<Consulta> proximaConsulta = consultasDoMedico.stream()
                        .filter(c -> c.getPaciente().getId().equals(pacienteId) &&
                                c.getDataHora().isAfter(LocalDateTime.now()))
                        .min(Comparator.comparing(Consulta::getDataHora));

                Map<String, Object> pacInfo = new LinkedHashMap<>();
                pacInfo.put("id", p.getId());
                pacInfo.put("nome", p.getNome());
                pacInfo.put("email", p.getEmail());
                pacInfo.put("telefone", p.getTelefone());
                pacInfo.put("ultimaConsulta", ultimaConsulta.map(Consulta::getDataHora).orElse(null));
                pacInfo.put("proximaConsulta", proximaConsulta.map(Consulta::getDataHora).orElse(null));
                pacInfo.put("status", proximaConsulta.isPresent() ? "ATIVO" : "INATIVO");
                pacientes.add(pacInfo);
            }
        }

        return ResponseEntity.ok(pacientes);
    }

    // ====== DETALHES PACIENTE ======
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<?> getDetalhePaciente(@PathVariable Integer pacienteId) {
        Optional<Paciente> paciente = pacienteRepo.findById(pacienteId);
        if (paciente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Paciente p = paciente.get();
        Map<String, Object> detalhe = new LinkedHashMap<>();
        detalhe.put("id", p.getId());
        detalhe.put("nome", p.getNome());
        detalhe.put("cpf", p.getCpf());
        detalhe.put("email", p.getEmail());
        detalhe.put("telefone", p.getTelefone());
        detalhe.put("dataNascimento", p.getDataNascimento());

        // Consultas
        List<Consulta> consultas = p.getConsultas().stream()
                .sorted(Comparator.comparing(Consulta::getDataHora).reversed())
                .collect(Collectors.toList());

        detalhe.put("consultas", consultas.stream()
                .map(c -> {
                    Map<String, Object> cons = new LinkedHashMap<>();
                    cons.put("id", c.getId());
                    cons.put("data", c.getDataHora());
                    cons.put("medico", c.getProfissional().getNome());
                    cons.put("tipo", c.getTipo());
                    cons.put("status", c.getStatus());
                    cons.put("valor", c.getValor());
                    cons.put("observacoes", c.getObservacoes());
                    return cons;
                })
                .collect(Collectors.toList()));

        // Documentos
        List<Documento> documentos = documentoRepo.findAll().stream()
                .filter(d -> d.getPaciente().getId().equals(pacienteId))
                .collect(Collectors.toList());

        detalhe.put("documentos", documentos.stream()
                .map(d -> {
                    Map<String, Object> doc = new LinkedHashMap<>();
                    doc.put("id", d.getId());
                    doc.put("nome", d.getNome());
                    doc.put("tamanho", d.getTamanho());
                    doc.put("dataEnvio", d.getDataEnvio());
                    doc.put("url", d.getUrl());
                    return doc;
                })
                .collect(Collectors.toList()));

        return ResponseEntity.ok(detalhe);
    }

    // ====== FINANCEIRO ======
    @GetMapping("/financeiro/{medicoId}")
    public ResponseEntity<?> getFinanceiro(@PathVariable Integer medicoId,
                                            @RequestParam(required = false) String mes) {
        Optional<Profissional> medico = profissionalRepo.findById(medicoId);
        if (medico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Consulta> consultasDoMedico = consultaRepo.findAll().stream()
                .filter(c -> c.getProfissional().getId().equals(medicoId))
                .collect(Collectors.toList());

        Map<String, Object> financeiro = new LinkedHashMap<>();

        // Totalizadores
        BigDecimal recebido = consultasDoMedico.stream()
                .filter(c -> c.getStatus().toString().equals("REALIZADA"))
                .map(Consulta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal aReceber = consultasDoMedico.stream()
                .filter(c -> c.getStatus().toString().equals("AGENDADA"))
                .map(Consulta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        financeiro.put("recebido", recebido);
        financeiro.put("aReceber", aReceber);
        financeiro.put("total", recebido.add(aReceber));

        // Histórico
        List<Map<String, Object>> historico = consultasDoMedico.stream()
                .map(c -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("id", c.getId());
                    item.put("paciente", c.getPaciente().getNome());
                    item.put("data", c.getDataHora());
                    item.put("valor", c.getValor());
                    item.put("status", c.getStatus());
                    return item;
                })
                .sorted(Comparator.comparing((Map<String, Object> m) ->
                        (LocalDateTime) m.get("data")).reversed())
                .collect(Collectors.toList());

        financeiro.put("historico", historico);

        return ResponseEntity.ok(financeiro);
    }

    // ====== DOCUMENTAÇÃO ======
    @GetMapping("/documentacao/{medicoId}")
    public ResponseEntity<?> getDocumentacao(@PathVariable Integer medicoId) {
        Optional<Profissional> medico = profissionalRepo.findById(medicoId);
        if (medico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Pega documentos de todos os pacientes deste médico
        List<Consulta> consultasDoMedico = consultaRepo.findAll().stream()
                .filter(c -> c.getProfissional().getId().equals(medicoId))
                .collect(Collectors.toList());

        Set<Integer> pacienteIds = new HashSet<>();
        for (Consulta c : consultasDoMedico) {
            pacienteIds.add(c.getPaciente().getId());
        }

        List<Documento> documentos = documentoRepo.findAll().stream()
                .filter(d -> pacienteIds.contains(d.getPaciente().getId()))
                .collect(Collectors.toList());

        Map<String, Object> documentacao = new LinkedHashMap<>();

        // Categorizar documentos
        List<Map<String, Object>> exames = documentos.stream()
                .filter(d -> d.getNome().contains("Exame"))
                .map(this::mapDocumento)
                .collect(Collectors.toList());

        List<Map<String, Object>> prescricoes = documentos.stream()
                .filter(d -> d.getNome().contains("Prescricao") || d.getNome().contains("Prescrição"))
                .map(this::mapDocumento)
                .collect(Collectors.toList());

        List<Map<String, Object>> procedimentos = documentos.stream()
                .filter(d -> d.getNome().contains("Avaliacao") || d.getNome().contains("Avaliação"))
                .map(this::mapDocumento)
                .collect(Collectors.toList());

        List<Map<String, Object>> anexos = documentos.stream()
                .filter(d -> !d.getNome().contains("Exame") &&
                        !d.getNome().contains("Prescricao") &&
                        !d.getNome().contains("Prescrição") &&
                        !d.getNome().contains("Avaliacao") &&
                        !d.getNome().contains("Avaliação"))
                .map(this::mapDocumento)
                .collect(Collectors.toList());

        documentacao.put("exames", exames);
        documentacao.put("prescricoes", prescricoes);
        documentacao.put("procedimentos", procedimentos);
        documentacao.put("anexos", anexos);

        return ResponseEntity.ok(documentacao);
    }

    // ====== RELATÓRIOS ======
    @GetMapping("/relatorios/{medicoId}")
    public ResponseEntity<?> getRelatorios(@PathVariable Integer medicoId) {
        Optional<Profissional> medico = profissionalRepo.findById(medicoId);
        if (medico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Profissional prof = medico.get();
        List<Consulta> consultasDoMedico = consultaRepo.findAll().stream()
                .filter(c -> c.getProfissional().getId().equals(medicoId))
                .collect(Collectors.toList());

        Map<String, Object> relatorios = new LinkedHashMap<>();

        // Taxa de realização
        long totalConsultas = consultasDoMedico.size();
        long consultasRealizadas = consultasDoMedico.stream()
                .filter(c -> c.getStatus().toString().equals("REALIZADA"))
                .count();
        double taxaRealizacao = totalConsultas > 0 ? (consultasRealizadas / (double) totalConsultas) * 100 : 0;

        // Pacientes únicos
        Set<Integer> pacientesUnicos = consultasDoMedico.stream()
                .map(c -> c.getPaciente().getId())
                .collect(Collectors.toSet());

        // Tipos de sessão
        long presencial = consultasDoMedico.stream()
                .filter(c -> c.getTipo().toString().equals("PRESENCIAL"))
                .count();
        long online = consultasDoMedico.stream()
                .filter(c -> c.getTipo().toString().equals("ONLINE"))
                .count();

        // Receita média
        BigDecimal receitaTotal = consultasDoMedico.stream()
                .filter(c -> c.getStatus().toString().equals("REALIZADA"))
                .map(Consulta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal receitaMedia = totalConsultas > 0 ? receitaTotal.divide(new BigDecimal(consultasRealizadas), 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;

        relatorios.put("totalConsultas", totalConsultas);
        relatorios.put("consultasRealizadas", consultasRealizadas);
        relatorios.put("taxaRealizacao", String.format("%.1f%%", taxaRealizacao));
        relatorios.put("pacientesUnicos", pacientesUnicos.size());
        relatorios.put("sessoesPrelenciais", presencial);
        relatorios.put("sessoesOnline", online);
        relatorios.put("receitaTotal", receitaTotal);
        relatorios.put("receitaMedia", receitaMedia);
        relatorios.put("valorSessao", prof.getValorSessao());

        return ResponseEntity.ok(relatorios);
    }

    // ====== HELPER ======
    private String getFotoMedico(String nome) {
        if (nome.contains("Mariana")) {
            return "https://blog.ipog.edu.br/wp-content/uploads/2017/10/m%C3%A9dico.jpg";
        } else if (nome.contains("Ricardo")) {
            return "https://wordpress-cms-revista-prod-assets.quero.space/legacy_posts/post_images/41938/7d8429c45731acf5a27316ce946ece4f005b8947.jpg?1666045869";
        } else if (nome.contains("Gabriel")) {
            return "https://www.jlramos.com.br/wp-content/uploads/elementor/thumbs/portrait-of-handsome-smiling-african-american-doct-2022-01-05-01-36-43-utc-scaled-qc94d8jh82xk8coeadjjvzu1x6kzij2s3k2iguy83s.jpg";
        }
        return "https://via.placeholder.com/150";
    }

    private Map<String, Object> mapDocumento(Documento d) {
        Map<String, Object> doc = new LinkedHashMap<>();
        doc.put("id", d.getId());
        doc.put("nome", d.getNome());
        doc.put("tamanho", d.getTamanho());
        doc.put("dataEnvio", d.getDataEnvio());
        doc.put("url", d.getUrl());
        doc.put("paciente", d.getPaciente().getNome());
        return doc;
    }
}
