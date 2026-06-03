package org.example.config;

import org.example.enums.statusConsulta;
import org.example.enums.tipoSessao;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    ApplicationRunner initDatabase(
            ProfissionalRepository profissionalRepo,
            PacienteRepository pacienteRepo,
            ConsultaRepository consultaRepo,
            DocumentoRepository documentoRepo) {
        return args -> {
            // Verifica se já existem dados
            if (profissionalRepo.count() > 0) {
                return;
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            // ====== CRIAR MÉDICOS ======
            Profissional medica = new Profissional();
            medica.setNome("Dra. Mariana Albuquerque");
            medica.setTipo("Psicólogo");
            medica.setEspecialidade("Psicologia Clínica / TCC");
            medica.setRegistroProfissional("CRP 06/123456");
            medica.setEmail("mariana.albuquerque@clinic.com");
            medica.setSenhaHash("senha123");
            medica.setValorSessao(new BigDecimal("150.00"));
            medica.setDoisFatoresAtivo(false);
            profissionalRepo.save(medica);

            Profissional medico1 = new Profissional();
            medico1.setNome("Dr. Ricardo Santos");
            medico1.setTipo("Psicólogo");
            medico1.setEspecialidade("Neuropsicologia");
            medico1.setRegistroProfissional("CRP 06/789012");
            medico1.setEmail("ricardo.santos@clinic.com");
            medico1.setSenhaHash("senha123");
            medico1.setValorSessao(new BigDecimal("180.00"));
            medico1.setDoisFatoresAtivo(false);
            profissionalRepo.save(medico1);

            Profissional medico2 = new Profissional();
            medico2.setNome("Dr. Gabriel Souza");
            medico2.setTipo("Psicólogo");
            medico2.setEspecialidade("Psicologia Organizacional e Comportamental");
            medico2.setRegistroProfissional("CRP 06/345678");
            medico2.setEmail("gabriel.souza@clinic.com");
            medico2.setSenhaHash("senha123");
            medico2.setValorSessao(new BigDecimal("160.00"));
            medico2.setDoisFatoresAtivo(false);
            profissionalRepo.save(medico2);

            // ====== CRIAR PACIENTES ======
            Paciente[] pacientes = new Paciente[6];
            String[] nomesPacientes = {
                    "João Silva", "Maria Santos", "Carlos Oliveira",
                    "Ana Costa", "Pedro Almeida", "Fernanda Lima"
            };
            String[] cpfsPacientes = {
                    "123.456.789-00", "234.567.890-11", "345.678.901-22",
                    "456.789.012-33", "567.890.123-44", "678.901.234-55"
            };
            String[] emailsPacientes = {
                    "joao.silva@email.com", "maria.santos@email.com", "carlos.oliveira@email.com",
                    "ana.costa@email.com", "pedro.almeida@email.com", "fernanda.lima@email.com"
            };

            for (int i = 0; i < 6; i++) {
                Paciente paciente = new Paciente();
                paciente.setNome(nomesPacientes[i]);
                paciente.setCpf(cpfsPacientes[i]);
                paciente.setEmail(emailsPacientes[i]);
                paciente.setTelefone("(11) 9" + (1000000 + i * 111111));
                paciente.setDataNascimento(LocalDate.of(1990 + (i % 5), (i % 12) + 1, (i % 28) + 1));
                paciente.setSenhaHash("senha123");
                paciente.setDoisFatoresAtivo(false);
                pacientes[i] = pacienteRepo.save(paciente);
            }

            // ====== CRIAR CONSULTAS ======
            Profissional[] medicos = { medica, medico1, medico2 };

            // Consultas para João Silva
            Consulta consulta1 = new Consulta();
            consulta1.setPaciente(pacientes[0]);
            consulta1.setProfissional(medica);
            consulta1.setDataHora(LocalDateTime.now().minusDays(7));
            consulta1.setValor(new BigDecimal("150.00"));
            consulta1.setTipo(tipoSessao.PRESENCIAL);
            consulta1.setStatus(statusConsulta.REALIZADA);
            consulta1.setObservacoes("Primeira sessão de avaliação completa");
            consultaRepo.save(consulta1);

            Consulta consulta2 = new Consulta();
            consulta2.setPaciente(pacientes[0]);
            consulta2.setProfissional(medica);
            consulta2.setDataHora(LocalDateTime.now().plusDays(2));
            consulta2.setValor(new BigDecimal("150.00"));
            consulta2.setTipo(tipoSessao.PRESENCIAL);
            consulta2.setStatus(statusConsulta.AGENDADA);
            consulta2.setObservacoes("Segunda sessão TCC");
            consultaRepo.save(consulta2);

            // Consultas para Maria Santos
            Consulta consulta3 = new Consulta();
            consulta3.setPaciente(pacientes[1]);
            consulta3.setProfissional(medico1);
            consulta3.setDataHora(LocalDateTime.now().minusDays(3));
            consulta3.setValor(new BigDecimal("180.00"));
            consulta3.setTipo(tipoSessao.ONLINE);
            consulta3.setStatus(statusConsulta.REALIZADA);
            consulta3.setObservacoes("Avaliação neuropsicológica");
            consultaRepo.save(consulta3);

            Consulta consulta4 = new Consulta();
            consulta4.setPaciente(pacientes[1]);
            consulta4.setProfissional(medico1);
            consulta4.setDataHora(LocalDateTime.now().plusDays(5));
            consulta4.setValor(new BigDecimal("180.00"));
            consulta4.setTipo(tipoSessao.ONLINE);
            consulta4.setStatus(statusConsulta.AGENDADA);
            consulta4.setObservacoes("Acompanhamento");
            consultaRepo.save(consulta4);

            // Consultas para Carlos Oliveira
            Consulta consulta5 = new Consulta();
            consulta5.setPaciente(pacientes[2]);
            consulta5.setProfissional(medico2);
            consulta5.setDataHora(LocalDateTime.now().minusDays(10));
            consulta5.setValor(new BigDecimal("160.00"));
            consulta5.setTipo(tipoSessao.PRESENCIAL);
            consulta5.setStatus(statusConsulta.REALIZADA);
            consulta5.setObservacoes("Sessão inicial de coaching organizacional");
            consultaRepo.save(consulta5);

            Consulta consulta6 = new Consulta();
            consulta6.setPaciente(pacientes[2]);
            consulta6.setProfissional(medico2);
            consulta6.setDataHora(LocalDateTime.now().plusDays(7));
            consulta6.setValor(new BigDecimal("160.00"));
            consulta6.setTipo(tipoSessao.PRESENCIAL);
            consulta6.setStatus(statusConsulta.AGENDADA);
            consulta6.setObservacoes("Continuação do programa");
            consultaRepo.save(consulta6);

            // Mais consultas para os outros pacientes
            for (int i = 3; i < 6; i++) {
                Consulta consulta = new Consulta();
                consulta.setPaciente(pacientes[i]);
                consulta.setProfissional(medicos[i % 3]);
                consulta.setDataHora(LocalDateTime.now().minusDays(5 + i));
                consulta.setValor(new BigDecimal("150.00"));
                consulta.setTipo(i % 2 == 0 ? tipoSessao.PRESENCIAL : tipoSessao.ONLINE);
                consulta.setStatus(statusConsulta.REALIZADA);
                consulta.setObservacoes("Sessão de acompanhamento");
                consultaRepo.save(consulta);

                Consulta consultaFutura = new Consulta();
                consultaFutura.setPaciente(pacientes[i]);
                consultaFutura.setProfissional(medicos[i % 3]);
                consultaFutura.setDataHora(LocalDateTime.now().plusDays(10 + i));
                consultaFutura.setValor(new BigDecimal("150.00"));
                consultaFutura.setTipo(i % 2 == 0 ? tipoSessao.PRESENCIAL : tipoSessao.ONLINE);
                consultaFutura.setStatus(statusConsulta.AGENDADA);
                consultaFutura.setObservacoes("Próxima sessão agendada");
                consultaRepo.save(consultaFutura);
            }

            // ====== CRIAR DOCUMENTOS ======
            String[] tiposDocumentos = {
                    "Recibo_Sessao_1.pdf",
                    "Avaliacao_Psicologica.pdf",
                    "Prescricao_Medica.pdf",
                    "Exame_Laboratorio.pdf",
                    "Parecer_Tecnico.pdf",
                    "Prontuario_Atualizado.pdf"
            };

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 3; j++) {
                    Documento doc = new Documento();
                    doc.setPaciente(pacientes[i]);
                    doc.setNome(tiposDocumentos[j]);
                    doc.setTamanho((j + 1) * 0.5 + " MB");
                    doc.setUrl("/documentos/" + pacientes[i].getId() + "/" + tiposDocumentos[j]);
                    doc.setDataEnvio(LocalDateTime.now().minusDays(j + 1));
                    documentoRepo.save(doc);
                }
            }

            System.out.println("✅ Dados iniciais carregados com sucesso!");
        };
    }
}
