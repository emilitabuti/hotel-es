package hotel.modelo;

import java.io.Serializable;

public class Apartamento implements Serializable {
    private static final long serialVersionUID = 1L;

    private Status status;
    private Hospede hospede;

    public Apartamento() {
        this.status = Status.LIVRE;
        this.hospede = null;
    }

    public Status getStatus() { return status; }
    public Hospede getHospede() { return hospede; }

    /**
     * Reserva um apartamento livre.
     *
     * @param h hospede da reserva.
     * @throws IllegalArgumentException se o hospede for nulo.
     * @throws IllegalStateException se o apartamento nao estiver livre.
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
     */
    public void cancelarReserva() {
        if (status != Status.RESERVADO) {
            throw new IllegalStateException("Apartamento precisa estar reservado para cancelar reserva");
        }
        status = Status.LIVRE;
        hospede = null;
    }

    public boolean estaLivre() { return status == Status.LIVRE; }
    public boolean estaReservado() { return status == Status.RESERVADO; }
    public boolean estaOcupado() { return status == Status.OCUPADO; }

    public float getPrecoDiaria() { return 0f; }

    public char getSymbol() {
        switch (status) {
            case LIVRE: return '.';
            case RESERVADO: return 'R';
            case OCUPADO: return 'O';
            default: return '?';
        }
    }

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
