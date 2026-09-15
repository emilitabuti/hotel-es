package hotel.modelo;

import java.io.Serializable;
import java.util.Objects;

public class Hospede implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String cpf;
    private final String nome;
    private final String endereco;
    private final String celular;
    private final String email;

    /**
     * Cria um hospede imutavel com todos os campos obrigatorios.
     *
     * @param cpf CPF do hospede.
     * @param nome nome do hospede.
     * @param endereco endereco do hospede.
     * @param celular celular do hospede.
     * @param email email do hospede.
     * @throws IllegalArgumentException se algum campo for nulo ou vazio.
     */
    public Hospede(String cpf, String nome, String endereco, String celular, String email) {
        validarObrigatorio(cpf, "cpf");
        validarObrigatorio(nome, "nome");
        validarObrigatorio(endereco, "endereco");
        validarObrigatorio(celular, "celular");
        validarObrigatorio(email, "email");

        this.cpf = cpf;
        this.nome = nome;
        this.endereco = endereco;
        this.celular = celular;
        this.email = email;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getEndereco() { return endereco; }
    public String getCelular() { return celular; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hospede)) {
            return false;
        }
        Hospede hospede = (Hospede) o;
        return cpf.equals(hospede.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    private static void validarObrigatorio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Campo obrigatorio invalido: " + campo);
        }
    }

}
