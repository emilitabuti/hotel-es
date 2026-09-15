package hotel.apresentacao;

import hotel.modelo.*;
import hotel.negocio.Hotel;

/**
 * Suite simples de testes do nucleo CJ11.
 *
 * @pre Classes de modelo compiladas no classpath.
 * @post Exibe no console o total de testes aprovados.
 */
public class HotelTest {
    private static int passou = 0;
    private static int total = 0;

    /**
     * Executa todos os testes do CJ11.
     *
     * @param args argumentos de linha de comando nao utilizados.
     * @pre Projeto compilado.
     * @post Todos os testes sao contabilizados e o placar final e impresso.
     */
    public static void main(String[] args) {
        testarCriarHospedeValido();
        testarCriarHospedeComCampoNuloFalha();
        testarHospedesComMesmoCpf();
        testarHospedesComCpfDiferente();
        testarToStringHospede();
        testarApartamentoNovoNasceLivre();
        testarReservarApartamentoLivre();
        testarReservarApartamentoReservadoFalha();
        testarCheckinApartamentoLivre();
        testarCheckinApartamentoReservado();
        testarCheckinApartamentoOcupadoFalha();
        testarCheckoutApartamentoOcupado();
        testarCheckoutApartamentoLivreFalha();
        testarCancelarReservaApartamentoReservado();
        testarCancelarReservaApartamentoLivreFalha();
        testarPrecoDiariaApartamentoBase();
        testarSymbolApartamento();
        testarToStringApartamento();

        System.out.println(passou + "/" + total + " testes passaram");
    }

    /**
     * Verifica o caminho feliz de criacao de hospede.
     *
     * @pre Classe Hospede disponivel.
     * @post Incrementa o total e, se os getters retornarem os dados esperados, incrementa passou.
     */
    static void testarCriarHospedeValido() {
        total++;
        try {
            Hospede h = hospedePadrao();
            if ("123".equals(h.getCpf())
                    && "Joao".equals(h.getNome())
                    && "Rua X".equals(h.getEndereco())
                    && "9999".equals(h.getCelular())
                    && "joao@x".equals(h.getEmail())) {
                passou++;
            } else {
                System.out.println("FALHOU: testarCriarHospedeValido");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarCriarHospedeValido (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica o caso triste de criacao de hospede com campo nulo.
     *
     * @pre Classe Hospede disponivel.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarCriarHospedeComCampoNuloFalha() {
        total++;
        try {
            new Hospede(null, "Joao", "Rua X", "9999", "joao@x");
            System.out.println("FALHOU: testarCriarHospedeComCampoNuloFalha (nao lancou excecao)");
        } catch (IllegalArgumentException e) {
            passou++;
        } catch (Throwable e) {
            System.out.println("FALHOU: testarCriarHospedeComCampoNuloFalha (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica igualdade e hashCode de hospedes com o mesmo CPF.
     *
     * @pre Classe Hospede disponivel.
     * @post Incrementa o total e, se os hospedes forem iguais e tiverem mesmo hashCode, incrementa passou.
     */
    static void testarHospedesComMesmoCpf() {
        total++;
        try {
            Hospede h1 = hospedePadrao();
            Hospede h2 = new Hospede("123", "Maria", "Rua Y", "8888", "maria@y");
            if (h1.equals(h2) && h1.hashCode() == h2.hashCode()) {
                passou++;
            } else {
                System.out.println("FALHOU: testarHospedesComMesmoCpf");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarHospedesComMesmoCpf (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica que hospedes com CPF diferente nao sao iguais.
     *
     * @pre Classe Hospede disponivel.
     * @post Incrementa o total e, se os hospedes forem diferentes, incrementa passou.
     */
    static void testarHospedesComCpfDiferente() {
        total++;
        try {
            Hospede h1 = hospedePadrao();
            Hospede h2 = new Hospede("456", "Joao", "Rua X", "9999", "joao@x");
            if (!h1.equals(h2)) {
                passou++;
            } else {
                System.out.println("FALHOU: testarHospedesComCpfDiferente");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarHospedesComCpfDiferente (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica a representacao textual de um hospede.
     *
     * @pre Classe Hospede disponivel.
     * @post Incrementa o total e, se o texto tiver nome e CPF, incrementa passou.
     */
    static void testarToStringHospede() {
        total++;
        try {
            if ("Joao (CPF: 123)".equals(hospedePadrao().toString())) {
                passou++;
            } else {
                System.out.println("FALHOU: testarToStringHospede");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarToStringHospede (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica o estado inicial de um apartamento novo.
     *
     * @pre Classe Apartamento disponivel.
     * @post Incrementa o total e, se o apartamento nascer LIVRE e sem hospede, incrementa passou.
     */
    static void testarApartamentoNovoNasceLivre() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            if (apto.getStatus() == Status.LIVRE
                    && apto.getHospede() == null
                    && apto.estaLivre()
                    && !apto.estaReservado()
                    && !apto.estaOcupado()) {
                passou++;
            } else {
                System.out.println("FALHOU: testarApartamentoNovoNasceLivre");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarApartamentoNovoNasceLivre (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica a reserva de um apartamento livre.
     *
     * @pre Apartamento novo deve estar LIVRE.
     * @post Incrementa o total e, se o apartamento ficar RESERVADO com o hospede informado, incrementa passou.
     */
    static void testarReservarApartamentoLivre() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            Hospede h = hospedePadrao();
            apto.reservar(h);
            if (apto.getStatus() == Status.RESERVADO
                    && apto.getHospede() == h
                    && apto.estaReservado()) {
                passou++;
            } else {
                System.out.println("FALHOU: testarReservarApartamentoLivre");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarReservarApartamentoLivre (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica que reservar apartamento ja reservado falha.
     *
     * @pre Apartamento ja reservado.
     * @post Incrementa o total e, se IllegalStateException for lancada, incrementa passou.
     */
    static void testarReservarApartamentoReservadoFalha() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            apto.reservar(hospedePadrao());
            apto.reservar(new Hospede("456", "Maria", "Rua Y", "8888", "maria@y"));
            System.out.println("FALHOU: testarReservarApartamentoReservadoFalha (nao lancou excecao)");
        } catch (IllegalStateException e) {
            passou++;
        } catch (Throwable e) {
            System.out.println("FALHOU: testarReservarApartamentoReservadoFalha (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica check-in direto em apartamento livre.
     *
     * @pre Apartamento novo deve estar LIVRE.
     * @post Incrementa o total e, se o apartamento ficar OCUPADO com o hospede informado, incrementa passou.
     */
    static void testarCheckinApartamentoLivre() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            Hospede h = hospedePadrao();
            apto.checkin(h);
            if (apto.getStatus() == Status.OCUPADO
                    && apto.getHospede() == h
                    && apto.estaOcupado()) {
                passou++;
            } else {
                System.out.println("FALHOU: testarCheckinApartamentoLivre");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarCheckinApartamentoLivre (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica check-in em apartamento reservado.
     *
     * @pre Apartamento reservado para um hospede.
     * @post Incrementa o total e, se o apartamento ficar OCUPADO, incrementa passou.
     */
    static void testarCheckinApartamentoReservado() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            Hospede h = hospedePadrao();
            apto.reservar(h);
            apto.checkin(h);
            if (apto.getStatus() == Status.OCUPADO
                    && apto.getHospede() == h
                    && apto.estaOcupado()) {
                passou++;
            } else {
                System.out.println("FALHOU: testarCheckinApartamentoReservado");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarCheckinApartamentoReservado (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica que check-in em apartamento ocupado falha.
     *
     * @pre Apartamento ja ocupado.
     * @post Incrementa o total e, se IllegalStateException for lancada, incrementa passou.
     */
    static void testarCheckinApartamentoOcupadoFalha() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            apto.checkin(hospedePadrao());
            apto.checkin(new Hospede("456", "Maria", "Rua Y", "8888", "maria@y"));
            System.out.println("FALHOU: testarCheckinApartamentoOcupadoFalha (nao lancou excecao)");
        } catch (IllegalStateException e) {
            passou++;
        } catch (Throwable e) {
            System.out.println("FALHOU: testarCheckinApartamentoOcupadoFalha (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica o preco da diaria da classe base Apartamento.
     *
     * @pre Classe Apartamento disponivel.
     * @post Incrementa o total e, se o preco base for 0f, incrementa passou.
     */
    static void testarPrecoDiariaApartamentoBase() {
        total++;
        try {
            if (new Apartamento().getPrecoDiaria() == 0f) {
                passou++;
            } else {
                System.out.println("FALHOU: testarPrecoDiariaApartamentoBase");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarPrecoDiariaApartamentoBase (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica checkout de apartamento ocupado.
     *
     * @pre Apartamento ocupado.
     * @post Incrementa o total e, se o apartamento ficar LIVRE e sem hospede, incrementa passou.
     */
    static void testarCheckoutApartamentoOcupado() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            apto.checkin(hospedePadrao());
            apto.checkout();
            if (apto.getStatus() == Status.LIVRE
                    && apto.getHospede() == null
                    && apto.estaLivre()) {
                passou++;
            } else {
                System.out.println("FALHOU: testarCheckoutApartamentoOcupado");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarCheckoutApartamentoOcupado (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica a representacao textual de um apartamento.
     *
     * @pre Classe Apartamento disponivel.
     * @post Incrementa o total e, se o texto refletir status e hospede, incrementa passou.
     */
    static void testarToStringApartamento() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            if (!"Apartamento LIVRE".equals(apto.toString())) {
                System.out.println("FALHOU: testarToStringApartamento");
                return;
            }
            apto.reservar(hospedePadrao());
            if ("Apartamento RESERVADO - Joao (CPF: 123)".equals(apto.toString())) {
                passou++;
            } else {
                System.out.println("FALHOU: testarToStringApartamento");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarToStringApartamento (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica que checkout de apartamento livre falha.
     *
     * @pre Apartamento livre.
     * @post Incrementa o total e, se IllegalStateException for lancada, incrementa passou.
     */
    static void testarCheckoutApartamentoLivreFalha() {
        total++;
        try {
            new Apartamento().checkout();
            System.out.println("FALHOU: testarCheckoutApartamentoLivreFalha (nao lancou excecao)");
        } catch (IllegalStateException e) {
            passou++;
        } catch (Throwable e) {
            System.out.println("FALHOU: testarCheckoutApartamentoLivreFalha (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica cancelamento de reserva em apartamento reservado.
     *
     * @pre Apartamento reservado.
     * @post Incrementa o total e, se o apartamento ficar LIVRE e sem hospede, incrementa passou.
     */
    static void testarCancelarReservaApartamentoReservado() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            apto.reservar(hospedePadrao());
            apto.cancelarReserva();
            if (apto.getStatus() == Status.LIVRE
                    && apto.getHospede() == null
                    && apto.estaLivre()) {
                passou++;
            } else {
                System.out.println("FALHOU: testarCancelarReservaApartamentoReservado");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarCancelarReservaApartamentoReservado (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica que cancelar reserva em apartamento livre falha.
     *
     * @pre Apartamento livre.
     * @post Incrementa o total e, se IllegalStateException for lancada, incrementa passou.
     */
    static void testarCancelarReservaApartamentoLivreFalha() {
        total++;
        try {
            new Apartamento().cancelarReserva();
            System.out.println("FALHOU: testarCancelarReservaApartamentoLivreFalha (nao lancou excecao)");
        } catch (IllegalStateException e) {
            passou++;
        } catch (Throwable e) {
            System.out.println("FALHOU: testarCancelarReservaApartamentoLivreFalha (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Verifica os simbolos do mapa para estados livre, reservado e ocupado.
     *
     * @pre Classe Apartamento disponivel.
     * @post Incrementa o total e, se os simbolos forem '.', 'R' e 'O', incrementa passou.
     */
    static void testarSymbolApartamento() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            if (apto.getSymbol() != '.') {
                System.out.println("FALHOU: testarSymbolApartamento");
                return;
            }
            apto.reservar(hospedePadrao());
            if (apto.getSymbol() != 'R') {
                System.out.println("FALHOU: testarSymbolApartamento");
                return;
            }
            apto.checkin(hospedePadrao());
            if (apto.getSymbol() == 'O') {
                passou++;
            } else {
                System.out.println("FALHOU: testarSymbolApartamento");
            }
        } catch (Throwable e) {
            System.out.println("FALHOU: testarSymbolApartamento (" + e.getClass().getSimpleName() + ")");
        }
    }

    /**
     * Cria um hospede valido usado pelos testes.
     *
     * @return hospede com dados padrao validos.
     * @pre Classe Hospede disponivel.
     * @post Retorna uma nova instancia sem alterar os contadores de teste.
     */
    private static Hospede hospedePadrao() {
        return new Hospede("123", "Joao", "Rua X", "9999", "joao@x");
    }
}
