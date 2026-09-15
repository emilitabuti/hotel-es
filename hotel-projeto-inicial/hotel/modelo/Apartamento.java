package hotel.modelo;

import java.io.Serializable;

public class Apartamento implements Serializable {
    private static final long serialVersionUID = 1L;

    private Status status;
    private Hospede hospede;

    /**
     * Cria um apartamento livre e sem hospede associado.
     *
     * @pre Nenhuma.
     * @post O apartamento fica com status LIVRE e hospede nulo.
     */
    public Apartamento() {
        this.status = Status.LIVRE;
        this.hospede = null;
    }

    /**
     * Retorna o status atual do apartamento.
     *
     * @return status atual do apartamento.
     * @pre Apartamento inicializado.
     * @post O estado do apartamento permanece inalterado.
     */
    public Status getStatus() { return status; }

    /**
     * Retorna o hospede associado ao apartamento.
     *
     * @return hospede atual, ou null se o apartamento estiver livre.
     * @pre Apartamento inicializado.
     * @post O estado do apartamento permanece inalterado.
     */
    public Hospede getHospede() { return hospede; }

    /**
     * Reserva um apartamento livre.
     *
     * @param h hospede da reserva.
     * @throws IllegalArgumentException se o hospede for nulo.
     * @throws IllegalStateException se o apartamento nao estiver livre.
     * @pre O apartamento deve estar LIVRE e o hospede deve ser nao nulo.
     * @post O apartamento fica RESERVADO e passa a referenciar o hospede informado.
     */
    public void reservar(Hospede h) {
        validarHospede(h);
        if (status != Status.LIVRE) {
            throw new IllegalStateException("Apartamento precisa estar livre para reservar");
        }
        status = Status.RESERVADO;
        hospede = h;
    }

    /**
     * Realiza check-in em apartamento livre ou reservado.
     *
     * @param h hospede do check-in.
     * @throws IllegalArgumentException se o hospede for nulo.
     * @throws IllegalStateException se o apartamento ja estiver ocupado.
     * @pre O apartamento deve estar LIVRE ou RESERVADO e o hospede deve ser nao nulo.
     * @post O apartamento fica OCUPADO e passa a referenciar o hospede informado.
     */
    public void checkin(Hospede h) {
        validarHospede(h);
        if (status == Status.OCUPADO) {
            throw new IllegalStateException("Apartamento ja esta ocupado");
        }
        status = Status.OCUPADO;
        hospede = h;
    }

    /**
     * Realiza checkout de um apartamento ocupado.
     *
     * @throws IllegalStateException se o apartamento nao estiver ocupado.
     * @pre O apartamento deve estar OCUPADO.
     * @post O apartamento fica LIVRE e sem hospede associado.
     */
    public void checkout() {
        if (status != Status.OCUPADO) {
            throw new IllegalStateException("Apartamento precisa estar ocupado para checkout");
        }
        status = Status.LIVRE;
        hospede = null;
    }

    /**
     * Cancela uma reserva existente.
     *
     * @throws IllegalStateException se o apartamento nao estiver reservado.
     * @pre O apartamento deve estar RESERVADO.
     * @post O apartamento fica LIVRE e sem hospede associado.
     */
    public void cancelarReserva() {
        if (status != Status.RESERVADO) {
            throw new IllegalStateException("Apartamento precisa estar reservado para cancelar reserva");
        }
        status = Status.LIVRE;
        hospede = null;
    }

    /**
     * Indica se o apartamento esta livre.
     *
     * @return true se o status atual for LIVRE; false caso contrario.
     * @pre Apartamento inicializado.
     * @post O estado do apartamento permanece inalterado.
     */
    public boolean estaLivre() { return status == Status.LIVRE; }

    /**
     * Indica se o apartamento esta reservado.
     *
     * @return true se o status atual for RESERVADO; false caso contrario.
     * @pre Apartamento inicializado.
     * @post O estado do apartamento permanece inalterado.
     */
    public boolean estaReservado() { return status == Status.RESERVADO; }

    /**
     * Indica se o apartamento esta ocupado.
     *
     * @return true se o status atual for OCUPADO; false caso contrario.
     * @pre Apartamento inicializado.
     * @post O estado do apartamento permanece inalterado.
     */
    public boolean estaOcupado() { return status == Status.OCUPADO; }

    /**
     * Retorna o preco da diaria da classe base.
     *
     * @return 0f para a classe base Apartamento.
     * @pre Apartamento inicializado.
     * @post O estado do apartamento permanece inalterado.
     */
    public float getPrecoDiaria() { return 0f; }

    /**
     * Retorna o simbolo usado no mapa de ocupacao.
     *
     * @return '.' para LIVRE, 'R' para RESERVADO e 'O' para OCUPADO.
     * @pre Apartamento inicializado.
     * @post O estado do apartamento permanece inalterado.
     */
    public char getSymbol() {
        switch (status) {
            case LIVRE: return '.';
            case RESERVADO: return 'R';
            case OCUPADO: return 'O';
            default: return '?';
        }
    }

    /**
     * Retorna uma representacao textual do apartamento.
     *
     * @return texto com status e, quando existir, hospede associado.
     * @pre Apartamento inicializado.
     * @post O estado do apartamento permanece inalterado.
     */
    @Override
    public String toString() {
        if (hospede == null) {
            return "Apartamento " + status;
        }
        return "Apartamento " + status + " - " + hospede;
    }

    private static void validarHospede(Hospede h) {
        if (h == null) {
            throw new IllegalArgumentException("Hospede nao pode ser nulo");
        }
    }
}
