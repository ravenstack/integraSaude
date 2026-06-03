package org.example.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String nome;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String email;
    private String telefone;
    private LocalDate dataNascimento;

    // 🚀 Coluna com tamanho protegido para o Hash
    @Column(name = "senha_hash", length = 255, nullable = false)
    private String senhaHash;

    // 🚀 Campo que você tinha criado antes para segurança extra
    private Boolean doisFatoresAtivo = false;

    // 🚀 Relação com as consultas recuperada
    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas;

    public Paciente() {}

    // --- GETTERS E SETTERS ---

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public Boolean getDoisFatoresAtivo() { return doisFatoresAtivo; }
    public void setDoisFatoresAtivo(Boolean doisFatoresAtivo) { this.doisFatoresAtivo = doisFatoresAtivo; }

    public List<Consulta> getConsultas() { return consultas; }
    public void setConsultas(List<Consulta> consultas) { this.consultas = consultas; }

    // --- O SETTER MÁGICO DO HASH ---
    public String getSenhaHash() { return senhaHash; }

    public void setSenhaHash(String senha) {
        if (senha == null) return;

        // Se já for um hash (começa com $2a$), apenas guarda
        if (senha.startsWith("$2a$")) {
            this.senhaHash = senha;
        } else {
            // Se for senha limpa, criptografa agora
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            this.senhaHash = encoder.encode(senha);
        }
    }
}