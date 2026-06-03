package org.example.model;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tipo; // Ex: psicologo, nutricionista, psiquiatra...

    private String nome;

    @Column(unique = true)
    private String registroProfissional;

    private String especialidade;

    private BigDecimal valorSessao;

    @Column(unique = true)
    private String email;

    @Column(length = 255)
    private String senhaHash;

    // Atualizado: o mappedBy agora aponta para o campo 'profissional' em Consulta
    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL)
    private List<Consulta> consultas;

    // Atualizado: o mappedBy agora aponta para o campo 'profissional' em Agenda
    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL)
    private List<Agenda> horariosDisponiveis;

    private Boolean doisFatoresAtivo = false;

    public Profissional() {
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRegistroProfissional() { return registroProfissional; }
    public void setRegistroProfissional(String registroProfissional) {
        this.registroProfissional = registroProfissional;
    }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public BigDecimal getValorSessao() { return valorSessao; }
    public void setValorSessao(BigDecimal valorSessao) { this.valorSessao = valorSessao; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senha) {
        if (senha == null) return;

        if (senha.startsWith("$2a$")) {
            this.senhaHash = senha;
        } else {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            this.senhaHash = encoder.encode(senha);
        }
    }

    public List<Consulta> getConsultas() { return consultas; }
    public void setConsultas(List<Consulta> consultas) { this.consultas = consultas; }

    public List<Agenda> getHorariosDisponiveis() { return horariosDisponiveis; }
    public void setHorariosDisponiveis(List<Agenda> horariosDisponiveis) { this.horariosDisponiveis = horariosDisponiveis; }

    public Boolean getDoisFatoresAtivo() { return doisFatoresAtivo; }
    public void setDoisFatoresAtivo(Boolean doisFatoresAtivo) { this.doisFatoresAtivo = doisFatoresAtivo; }
}