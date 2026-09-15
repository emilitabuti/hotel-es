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
     * @pre Todos os parametros devem ser nao nulos e nao vazios.
     * @post O hospede e criado com os dados informados e nao pode ser alterado por setters.
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

    /**
     * Retorna o CPF do hospede.
     *
     * @return CPF informado no construtor.
     * @pre Hospede criado.
     * @post O estado do hospede permanece inalterado.
     */
    public String getCpf() { return cpf; }

    /**
     * Retorna o nome do hospede.
     *
     * @return nome informado no construtor.
     * @pre Hospede criado.
     * @post O estado do hospede permanece inalterado.
     */
    public String getNome() { return nome; }

    /**
     * Retorna o endereco do hospede.
     *
     * @return endereco informado no construtor.
     * @pre Hospede criado.
     * @post O estado do hospede permanece inalterado.
     */
    public String getEndereco() { return endereco; }

    /**
     * Retorna o celular do hospede.
     *
     * @return celular informado no construtor.
     * @pre Hospede criado.
     * @post O estado do hospede permanece inalterado.
     */
    public String getCelular() { return celular; }

    /**
     * Retorna o email do hospede.
     *
     * @return email informado no construtor.
     * @pre Hospede criado.
     * @post O estado do hospede permanece inalterado.
     */
    public String getEmail() { return email; }

    /**
     * Retorna uma representacao textual do hospede.
     *
     * @return texto com nome e CPF do hospede.
     * @pre Hospede criado.
     * @post O estado do hospede permanece inalterado.
     */
    @Override
    public String toString() {
        return nome + " (CPF: " + cpf + ")";
    }

    /**
     * Compara hospedes pelo CPF.
     *
     * @param o objeto a ser comparado.
     * @return true se o objeto for um hospede com o mesmo CPF; false caso contrario.
     * @pre Hospede criado.
     * @post O estado do hospede permanece inalterado.
     */
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

    /**
     * Calcula o codigo hash baseado no CPF.
     *
     * @return codigo hash do CPF.
     * @pre Hospede criado.
     * @post O estado do hospede permanece inalterado.
     */
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
