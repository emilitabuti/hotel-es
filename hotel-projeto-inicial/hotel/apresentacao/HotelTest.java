package hotel.apresentacao;

import hotel.modelo.*;
import hotel.negocio.Hotel;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
     * Executa todos os testes do CJ11 e CJ12.
     *
     * @param args argumentos de linha de comando nao utilizados.
     * @pre Projeto compilado.
     * @post Todos os testes sao contabilizados e o placar final e impresso.
     */
    public static void main(String[] args) {
        //hospede
        testarCriarHospedeValido();
        testarCriarHospedeComCampoNuloFalha();
        testarHospedesComMesmoCpf();
        testarHospedesComCpfDiferente();
        testarToStringHospede();
        testarHospedeSemReserva();
        testarHospedeComIdentificadorReserva();
        testarAlteracaoIdentificadorReserva();

        //apartamento
        testarApartamentoNovoNasceLivre();
        testarReservarApartamentoLivre();
        testarReservarApartamentoReservadoFalha();
        testarReservarComHospedeNuloFalha();
        testarCheckinApartamentoLivre();
        testarCheckinApartamentoReservado();
        testarCheckinApartamentoOcupadoFalha();
        testarCheckinComHospedeNuloFalha();
        testarCheckoutApartamentoOcupado();
        testarCheckoutApartamentoLivreFalha();
        testarCancelarReservaApartamentoReservado();
        testarCancelarReservaApartamentoLivreFalha();
        testarPrecoDiariaApartamentoBase();
        testarSymbolApartamento();
        testarToStringApartamento();

        //hierarquia
        testarPrecoApartamentoSimples();
        testarPrecoApartamentoPremium();
        testarApartamentoSimplesNasceLivre();
        testarApartamentoPremiumNasceLivre();

        //hotel - inicializacao
        testarHotelNasceVazio();
        testarHotelCriaApartamentoSimples();
        testarHotelCriaApartamentoPremium();
        testarQuantidadeSimplesPorAndar();
        testarQuantidadePremiumPorAndar();

        //hotel - reserva
        testarReservaPeloHotel();
        testarReservaEmApartamentoNaoLivreFalha();
        testarReservaCoordenadaInvalida();
        testarReservaComHospedeNuloFalha();

        //identificador
        testarIdentificadorReserva();
        testarIdentificadoresDiferentes();
        testarIdentificadorContemQuarto();

        //hotel - check-in
        testarCheckinDiretoPeloHotel();
        testarCheckinDeReservaPeloHotel();
        testarCheckinEmApartamentoOcupadoPeloHotelFalha();
        testarCheckinCoordenadaInvalida();
        testarCheckinComHospedeNuloPeloHotelFalha();

        //hotel - check-out
        testarCheckoutPeloHotel();
        testarCheckoutApartamentoLivrePeloHotelFalha();
        testarCheckoutCoordenadaInvalida();

        //hotel - cancelamento
        testarCancelarReservaPeloHotel();
        testarCancelarApartamentoLivrePeloHotelFalha();
        testarCancelarReservaCoordenadaInvalida();

        //taxas
        testarTaxaOcupacaoHotelVazio();
        testarTaxaOcupacao();
        testarTaxaReservasHotelVazio();
        testarTaxaDeReservas();

        //consultas
        testarGetApartamentoValido();
        testarCoordenadaInvalidaHotel();
        testarMostrarMapa();
        testarConsultarApartamentoLivre();
        testarConsultarApartamentoReservado();

        //transferencia
        testarTransferenciaDeQuarto();
        testarTransferenciaDeHospedeOcupado();
        testarTransferenciaMantemNumeroReserva();
        testarTransferenciaAtualizaQuarto();
        testarTransferenciaOrigemLivreFalha();
        testarTransferenciaDestinoNaoLivreFalha();
        testarTransferenciaOrigemInvalida();
        testarTransferenciaDestinoInvalido();

        //hotel vazio
        testarHotelDeixaDeEstarVazioAposReserva();
        testarHotelVoltaAFicarVazioAposCancelamento();

        System.out.println("\n" + passou + "/" + total + " testes passaram");
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
                System.out.println("PASSOU: testarCriarHospedeValido");
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
            System.out.println("PASSOU: testarCriarHospedeComCampoNuloFalha");
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
                System.out.println("PASSOU: testarHospedesComMesmoCpf");
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
                System.out.println("PASSOU: testarHospedesComCpfDiferente");
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
                System.out.println("PASSOU: testarToStringHospede");
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
                System.out.println("PASSOU: testarApartamentoNovoNasceLivre");
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
                System.out.println("PASSOU: testarReservarApartamentoLivre");
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
            System.out.println("PASSOU: testarReservarApartamentoReservadoFalha");
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
                System.out.println("PASSOU: testarCheckinApartamentoLivre");
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
                System.out.println("PASSOU: testarCheckinApartamentoReservado");
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
            System.out.println("PASSOU: testarCheckinApartamentoOcupadoFalha");
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
                System.out.println("PASSOU: testarPrecoDiariaApartamentoBase");
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
                System.out.println("PASSOU: testarCheckoutApartamentoOcupado");
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
                System.out.println("PASSOU: testarToStringApartamento");
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
            System.out.println("PASSOU: testarCheckoutApartamentoLivreFalha");
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
                System.out.println("PASSOU: testarCancelarReservaApartamentoReservado");
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
            System.out.println("PASSOU: testarCancelarReservaApartamentoLivreFalha");
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
                System.out.println("PASSOU: testarSymbolApartamento");
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

    /**
     * Verifica se o hotel cria apartamentos simples corretamente.
     *
     * @pre Classe Hotel e ApartamentoSimples disponiveis.
     * @post Incrementa o total e, se o apartamento criado for simples, incrementa passou.
     */
    static void testarHotelCriaApartamentoSimples() {
        total++;
        Hotel hotel = new Hotel();
        
        if (hotel.getApartamento(0, 0) instanceof ApartamentoSimples) {
            passou++;
            System.out.println("PASSOU: testarHotelCriaApartamentoSimples");
        } else {
            System.out.println("FALHOU: testarHotelCriaApartamentoSimples");
        }
    }

    /**
     * Verifica se o hotel cria apartamentos premium corretamente.
     *
     * @pre Classe Hotel e ApartamentoPremium disponiveis.
     * @post Incrementa o total e, se o apartamento criado for premium, incrementa passou.
     */
    static void testarHotelCriaApartamentoPremium() {
        total++;
        Hotel hotel = new Hotel();

        if (hotel.getApartamento(0, 8) instanceof ApartamentoPremium) {
            passou++;
            System.out.println("PASSOU: testarHotelCriaApartamentoPremium");
        } else {
            System.out.println("FALHOU: testarHotelCriaApartamentoPremium");
        }
    }

    /**
     * Verifica a reserva de um apartamento por meio da classe Hotel.
     *
     * @pre Hotel inicializado e apartamento livre.
     * @post Incrementa o total e, se a reserva for realizada, incrementa passou.
     */
    static void testarReservaPeloHotel() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h = hospedePadrao();
        boolean resultado = hotel.reservarApartamento(0, 0, h);

        if (resultado && hotel.getApartamento(0, 0).estaReservado()) {
            passou++;
            System.out.println("PASSOU: testarReservaPeloHotel");
        } else {
            System.out.println("FALHOU: testarReservaPeloHotel");
        }
    }

    /**
     * Verifica a criacao do identificador da reserva.
     *
     * @pre Hotel inicializado, apartamento livre e hospede valido.
     * @post Incrementa o total e, se o identificador esperado for criado, incrementa passou.
     */
    static void testarIdentificadorReserva() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h = hospedePadrao();
        hotel.reservarApartamento(0, 0, h);

        if ("RES-0001-Q0101".equals(h.getIdentificadorReserva())) {
            passou++;
            System.out.println("PASSOU: testarIdentificadorReserva");
        } else {
            System.out.println("FALHOU: testarIdentificadorReserva");
        }   
    }

    /**
     * Verifica a transferencia de um hospede para outro apartamento.
     *
     * @pre Hospede deve possuir uma reserva e o apartamento de destino deve estar livre.
     * @post Incrementa o total e, se o hospede for transferido e o identificador atualizado, incrementa passou.
    */
    static void testarTransferenciaDeQuarto() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h = hospedePadrao();
        hotel.reservarApartamento(0, 0, h);
        boolean transferiu = hotel.transferirHospede(0, 0, 0, 4);

        if (transferiu && hotel.getApartamento(0, 0).estaLivre() && hotel.getApartamento(0, 4).estaReservado() && "RES-0001-Q0105".equals(h.getIdentificadorReserva())) {
            passou++;
            System.out.println("PASSOU: testarTransferenciaDeQuarto");
        } else {
            System.out.println("FALHOU: testarTransferenciaDeQuarto");
        }
    }

    /**
     * Verifica o calculo da taxa de reservas do hotel.
     *
     * @pre Hotel inicializado e uma reserva realizada.
     * @post Incrementa o total e, se a taxa calculada estiver correta, incrementa passou.
     */
    static void testarTaxaDeReservas() {
        total++;
        Hotel hotel = new Hotel();
        hotel.reservarApartamento(0, 0, hospedePadrao());
        float esperada = 1.0f / 280.0f; 
        float obtida = hotel.calcularTaxaReservas();

        if (Math.abs(esperada - obtida) < 0.0001) {
            passou++;
            System.out.println("PASSOU: testarTaxaDeReservas");
        } else {
            System.out.println("FALHOU: testarTaxaDeReservas");
        }
    }

    /**
     * Verifica o tratamento de coordenadas invalidas no Hotel.
     *
     * @pre Classe Hotel disponivel.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarCoordenadaInvalidaHotel() {
        total++;
        try {
            Hotel hotel = new Hotel();
            hotel.getApartamento(-1, 0);
            System.out.println("FALHOU: testarCoordenadaInvalidaHotel");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarCoordenadaInvalidaHotel");
        }
    }

    /**
     * Verifica se um hospede novo ainda nao possui identificador de reserva.
     *
     * @pre Classe Hospede disponivel.
     * @post Incrementa o total e, se o identificador for nulo, incrementa passou.
     */
    static void testarHospedeSemReserva() {
        total++;
        Hospede h = hospedePadrao();
        if (h.getIdentificadorReserva() == null) {
            passou++;
            System.out.println("PASSOU: testarHospedeSemReserva");
        } else {
            System.out.println("FALHOU: testarHospedeSemReserva");
        }
    }

    /**
     * Verifica a atribuicao de um identificador de reserva.
     *
     * @pre Hospede valido criado.
     * @post Incrementa o total e, se o identificador for armazenado, incrementa passou.
     */
    static void testarHospedeComIdentificadorReserva() {
        total++;
        Hospede h = hospedePadrao();
        h.setIdentificadorReserva("RES-0001-Q0101");

        if ("RES-0001-Q0101".equals(h.getIdentificadorReserva())) {
            passou++;
            System.out.println("PASSOU: testarHospedeComIdentificadorReserva");
        } else {
            System.out.println("FALHOU: testarHospedeComIdentificadorReserva");
        }
    }

    /**
     * Verifica se o identificador pode ser atualizado em uma troca de quarto.
     *
     * @pre Hospede com identificador de reserva.
     * @post Incrementa o total e, se o novo identificador for armazenado, incrementa passou.
     */
    static void testarAlteracaoIdentificadorReserva() {
        total++;
        Hospede h = hospedePadrao();
        h.setIdentificadorReserva("RES-0001-Q0101");
        h.setIdentificadorReserva("RES-0001-Q0105");

        if ("RES-0001-Q0105".equals(h.getIdentificadorReserva())) {
            passou++;
            System.out.println("PASSOU: testarAlteracaoIdentificadorReserva");
        } else {
            System.out.println("FALHOU: testarAlteracaoIdentificadorReserva");
        }
    }

    /**
     * Verifica que nao e permitido reservar com hospede nulo.
     *
     * @pre Apartamento livre.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarReservarComHospedeNuloFalha() {
        total++;

        try {
            Apartamento apto = new Apartamento();
            apto.reservar(null);
            System.out.println("FALHOU: testarReservarComHospedeNuloFalha");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarReservarComHospedeNuloFalha");
        }
    }


    /**
     * Verifica que nao e permitido realizar check-in com hospede nulo.
     *
     * @pre Apartamento livre.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarCheckinComHospedeNuloFalha() {
        total++;
        try {
            Apartamento apto = new Apartamento();
            apto.checkin(null);
            System.out.println("FALHOU: testarCheckinComHospedeNuloFalha");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarCheckinComHospedeNuloFalha");
        }
    }

    /**
     * Verifica o preco da diaria do apartamento simples.
     *
     * @pre Classe ApartamentoSimples disponivel.
     * @post Incrementa o total e, se o preco for 150, incrementa passou.
     */
    static void testarPrecoApartamentoSimples() {
        total++;
        ApartamentoSimples apto = new ApartamentoSimples();

        if (apto.getPrecoDiaria() == 150.0f) {
            passou++;
            System.out.println("PASSOU: testarPrecoApartamentoSimples");
        } else {
            System.out.println("FALHOU: testarPrecoApartamentoSimples");
        }
    }


    /**
     * Verifica o preco da diaria do apartamento premium.
     *
     * @pre Classe ApartamentoPremium disponivel.
     * @post Incrementa o total e, se o preco for 350, incrementa passou.
     */
    static void testarPrecoApartamentoPremium() {
        total++;
        ApartamentoPremium apto = new ApartamentoPremium();

        if (apto.getPrecoDiaria() == 350.0f) {
            passou++;
            System.out.println("PASSOU: testarPrecoApartamentoPremium");
        } else {
            System.out.println("FALHOU: testarPrecoApartamentoPremium");
        }
    }


    /**
     * Verifica se um apartamento simples nasce livre.
     *
     * @pre Classe ApartamentoSimples disponivel.
     * @post Incrementa o total e, se estiver livre, incrementa passou.
     */
    static void testarApartamentoSimplesNasceLivre() {
        total++;
        ApartamentoSimples apto = new ApartamentoSimples();
        if (apto.estaLivre()) {
            passou++;
            System.out.println("PASSOU: testarApartamentoSimplesNasceLivre");
        } else {
            System.out.println("FALHOU: testarApartamentoSimplesNasceLivre");
        }
    }


    /**
     * Verifica se um apartamento premium nasce livre.
     *
     * @pre Classe ApartamentoPremium disponivel.
     * @post Incrementa o total e, se estiver livre, incrementa passou.
     */
    static void testarApartamentoPremiumNasceLivre() {
        total++;
        ApartamentoPremium apto = new ApartamentoPremium();
        if (apto.estaLivre()) {
            passou++;
            System.out.println("PASSOU: testarApartamentoPremiumNasceLivre");
        } else {
            System.out.println("FALHOU: testarApartamentoPremiumNasceLivre");
        }
    }

    /**
     * Verifica se um hotel novo possui todos os apartamentos livres.
     *
     * @pre Classe Hotel disponivel.
     * @post Incrementa o total e, se todos estiverem livres, incrementa passou.
     */
    static void testarHotelNasceVazio() {
        total++;
        Hotel hotel = new Hotel();

        if (hotel.estaCompletoVazio()) {
            passou++;
            System.out.println("PASSOU: testarHotelNasceVazio");
        } else {
            System.out.println("FALHOU: testarHotelNasceVazio");
        }
    }


    /**
     * Verifica se cada andar possui oito apartamentos simples.
     *
     * @pre Hotel inicializado.
     * @post Incrementa o total e, se houver oito simples, incrementa passou.
     */
    static void testarQuantidadeSimplesPorAndar() {
        total++;
        Hotel hotel = new Hotel();
        boolean correto = true;

        for (int andar = 0; andar < Hotel.NUM_ANDARES; andar++) {
            int quantidade = 0;

            for (int numero = 0; numero < Hotel.APTOS_POR_ANDAR; numero++) {
                if (hotel.getApartamento(andar, numero) instanceof ApartamentoSimples) {
                    quantidade++;
                }
            }

            if (quantidade != 8) { correto = false; }
        }

        if (correto) {
            passou++;
            System.out.println("PASSOU: testarQuantidadeSimplesPorAndar");
        } else {
            System.out.println("FALHOU: testarQuantidadeSimplesPorAndar");
        }
    }


    /**
     * Verifica se cada andar possui seis apartamentos premium.
     *
     * @pre Hotel inicializado.
     * @post Incrementa o total e, se houver seis premium, incrementa passou.
     */
    static void testarQuantidadePremiumPorAndar() {
        total++;
        Hotel hotel = new Hotel();
        boolean correto = true;

        for (int andar = 0; andar < Hotel.NUM_ANDARES; andar++) {
            int quantidade = 0;

            for (int numero = 0; numero < Hotel.APTOS_POR_ANDAR; numero++) {
                if (hotel.getApartamento(andar, numero) instanceof ApartamentoPremium) {
                    quantidade++;
                }
            }
            if (quantidade != 6) { correto = false; }
        }

        if (correto) {
            passou++;
            System.out.println("PASSOU: testarQuantidadePremiumPorAndar");
        } else {
            System.out.println("FALHOU: testarQuantidadePremiumPorAndar");
        }
    }

    /**
     * Verifica que nao e possivel reservar um apartamento que ja possui reserva.
     *
     * @pre Apartamento previamente reservado.
     * @post Incrementa o total e, se a segunda reserva retornar false, incrementa passou.
     */
    static void testarReservaEmApartamentoNaoLivreFalha() {
        total++;
        Hotel hotel = new Hotel();
        hotel.reservarApartamento(0, 0, hospedePadrao());
        Hospede outro = new Hospede("456", "Maria", "Rua Y", "8888", "maria@y");
        boolean resultado = hotel.reservarApartamento(0, 0, outro);

        if (!resultado) {
            passou++;
            System.out.println("PASSOU: testarReservaEmApartamentoNaoLivreFalha");
        } else {
            System.out.println("FALHOU: testarReservaEmApartamentoNaoLivreFalha");
        }
    }

    /**
     * Verifica coordenada invalida durante uma reserva.
     *
     * @pre Hotel inicializado.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarReservaCoordenadaInvalida() {
        total++;
        try {
            Hotel hotel = new Hotel();
            hotel.reservarApartamento(-1, 0, hospedePadrao());
            System.out.println("FALHOU: testarReservaCoordenadaInvalida");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarReservaCoordenadaInvalida");
        }
    }


    /**
     * Verifica que nao e permitido reservar com hospede nulo.
     *
     * @pre Hotel inicializado e apartamento livre.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarReservaComHospedeNuloFalha() {
        total++;
        try {
            Hotel hotel = new Hotel();
            hotel.reservarApartamento(0, 0, null);
            System.out.println("FALHOU: testarReservaComHospedeNuloFalha");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarReservaComHospedeNuloFalha");
        }
    }

    /**
     * Verifica se duas reservas recebem identificadores diferentes.
     *
     * @pre Hotel inicializado e dois apartamentos livres.
     * @post Incrementa o total e, se os identificadores forem diferentes, incrementa passou.
     */
    static void testarIdentificadoresDiferentes() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h1 = hospedePadrao();
        Hospede h2 = new Hospede("456", "Maria", "Rua Y", "8888", "maria@y");
        hotel.reservarApartamento(0, 0, h1);
        hotel.reservarApartamento(0, 1, h2);
        if (!h1.getIdentificadorReserva().equals(h2.getIdentificadorReserva())) {
            passou++;
            System.out.println("PASSOU: testarIdentificadoresDiferentes");
        } else {
            System.out.println("FALHOU: testarIdentificadoresDiferentes");
        }
    }

    /**
     * Verifica se o identificador possui corretamente o quarto reservado.
     *
     * @pre Hotel inicializado e apartamento livre.
     * @post Incrementa o total e, se o identificador possuir o quarto correto, incrementa passou.
     */
    static void testarIdentificadorContemQuarto() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h = hospedePadrao();
        hotel.reservarApartamento(2, 4, h);
    
        if ("RES-0001-Q0305".equals(h.getIdentificadorReserva())) {
            passou++;
            System.out.println("PASSOU: testarIdentificadorContemQuarto");
        } else {
            System.out.println("FALHOU: testarIdentificadorContemQuarto");
        }
    }

    /**
     * Verifica check-in direto em apartamento livre.
     *
     * @pre Hotel inicializado, apartamento livre e hospede valido.
     * @post Incrementa o total e, se o apartamento ficar ocupado, incrementa passou.
     */
    static void testarCheckinDiretoPeloHotel() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h = hospedePadrao();
        boolean resultado = hotel.realizarCheckin(0, 0, h);

        if (resultado && hotel.getApartamento(0, 0).estaOcupado() && hotel.getApartamento(0, 0).getHospede() == h) {
            passou++;
            System.out.println("PASSOU: testarCheckinDiretoPeloHotel");
        } else {
            System.out.println("FALHOU: testarCheckinDiretoPeloHotel");
        }
    }


    /**
     * Verifica check-in realizado a partir de uma reserva existente.
     *
     * @pre Apartamento previamente reservado.
     * @post Incrementa o total e, se o apartamento ficar ocupado, incrementa passou.
     */
    static void testarCheckinDeReservaPeloHotel() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h = hospedePadrao();
        hotel.reservarApartamento(0, 0, h);
        boolean resultado = hotel.realizarCheckin(0, 0, h);

        if (resultado && hotel.getApartamento(0, 0).estaOcupado() && hotel.getApartamento(0, 0).getHospede() == h) {
            passou++;
            System.out.println("PASSOU: testarCheckinDeReservaPeloHotel");
        } else {
            System.out.println("FALHOU: testarCheckinDeReservaPeloHotel");
        }
    }


    /**
     * Verifica que nao e permitido check-in em apartamento ocupado.
     *
     * @pre Apartamento previamente ocupado.
     * @post Incrementa o total e, se o segundo check-in falhar, incrementa passou.
     */
    static void testarCheckinEmApartamentoOcupadoPeloHotelFalha() {
        total++;
        Hotel hotel = new Hotel();
        hotel.realizarCheckin(0, 0, hospedePadrao());
        Hospede outro = new Hospede("456", "Maria", "Rua Y", "8888", "maria@y");
        boolean resultado = hotel.realizarCheckin(0, 0, outro);
        if (!resultado) {
            passou++;
            System.out.println("PASSOU: testarCheckinEmApartamentoOcupadoPeloHotelFalha");
        } else {
            System.out.println("FALHOU: testarCheckinEmApartamentoOcupadoPeloHotelFalha");
        }
        }


    /**
     * Verifica coordenada invalida durante o check-in.
     *
     * @pre Hotel inicializado.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarCheckinCoordenadaInvalida() {
        total++;
        try {
            Hotel hotel = new Hotel();
            hotel.realizarCheckin(20, 0, hospedePadrao());
            System.out.println("FALHOU: testarCheckinCoordenadaInvalida");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarCheckinCoordenadaInvalida");
        }
    }


    /**
     * Verifica que nao e permitido check-in com hospede nulo.
     *
     * @pre Hotel inicializado e apartamento livre.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarCheckinComHospedeNuloPeloHotelFalha() {
        total++;
        try {
            Hotel hotel = new Hotel();
            hotel.realizarCheckin(0, 0, null);
            System.out.println("FALHOU: testarCheckinComHospedeNuloPeloHotelFalha");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarCheckinComHospedeNuloPeloHotelFalha");
        }
    }

    /**
     * Verifica checkout realizado pela classe Hotel.   
     *
     * @pre Apartamento ocupado.
     * @post Incrementa o total e, se o apartamento ficar livre e sem hospede, incrementa passou.
     */
    static void testarCheckoutPeloHotel() {
        total++;
        Hotel hotel = new Hotel();
        hotel.realizarCheckin(0, 0, hospedePadrao());
        boolean resultado = hotel.realizarCheckout(0, 0);

        if (resultado && hotel.getApartamento(0, 0).estaLivre() && hotel.getApartamento(0, 0).getHospede() == null) {
            passou++;
            System.out.println("PASSOU: testarCheckoutPeloHotel");
        } else {
            System.out.println("FALHOU: testarCheckoutPeloHotel");
        }
    }

    /**
     * Verifica que checkout de apartamento livre retorna false.
     *
     * @pre Apartamento livre.
     * @post Incrementa o total e, se o checkout falhar, incrementa passou.
     */
    static void testarCheckoutApartamentoLivrePeloHotelFalha() {
        total++;
        Hotel hotel = new Hotel();
        boolean resultado = hotel.realizarCheckout(0, 0);
        if (!resultado) {
            passou++;
            System.out.println("PASSOU: testarCheckoutApartamentoLivrePeloHotelFalha");
        } else {
            System.out.println("FALHOU: testarCheckoutApartamentoLivrePeloHotelFalha");
        }
    }

    /**
     * Verifica coordenada invalida durante o checkout.
     *
     * @pre Hotel inicializado.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarCheckoutCoordenadaInvalida() {
        total++;
        try {
            Hotel hotel = new Hotel();
            hotel.realizarCheckout(0, 14);
            System.out.println("FALHOU: testarCheckoutCoordenadaInvalida");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarCheckoutCoordenadaInvalida");
        }
    }

    /**
     * Verifica o cancelamento de uma reserva pela classe Hotel.
     *
     * @pre Apartamento previamente reservado.
     * @post Incrementa o total e, se o apartamento ficar livre, incrementa passou.
     */
    static void testarCancelarReservaPeloHotel() {
        total++;
        Hotel hotel = new Hotel();
        hotel.reservarApartamento(0, 0, hospedePadrao());
        boolean resultado = hotel.cancelarReserva(0, 0);
    
        if (resultado && hotel.getApartamento(0, 0).estaLivre() && hotel.getApartamento(0, 0).getHospede() == null) {
            passou++;
            System.out.println("PASSOU: testarCancelarReservaPeloHotel");
        } else {
            System.out.println("FALHOU: testarCancelarReservaPeloHotel");
        }
    }


    /**
     * Verifica que nao e possivel cancelar uma reserva em apartamento livre.
     *
     * @pre Apartamento livre.
     * @post Incrementa o total e, se o cancelamento falhar, incrementa passou.
     */
    static void testarCancelarApartamentoLivrePeloHotelFalha() {
        total++;
        Hotel hotel = new Hotel();
        boolean resultado = hotel.cancelarReserva(0, 0);
        if (!resultado) {
            passou++;
            System.out.println("PASSOU: testarCancelarApartamentoLivrePeloHotelFalha");
        } else {
            System.out.println("FALHOU: testarCancelarApartamentoLivrePeloHotelFalha");
        }
    }

    /**
     * Verifica coordenada invalida durante o cancelamento. 
     *
     * @pre Hotel inicializado.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarCancelarReservaCoordenadaInvalida() {
        total++;
        try {
            Hotel hotel = new Hotel();
            hotel.cancelarReserva(-1, 0);
            System.out.println("FALHOU: testarCancelarReservaCoordenadaInvalida");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarCancelarReservaCoordenadaInvalida");
        }
    }
    
    /**
     * Verifica se a taxa de ocupacao de um hotel novo e zero.
     *
     * @pre Hotel novo.
     * @post Incrementa o total e, se a taxa for zero, incrementa passou.
     */
    static void testarTaxaOcupacaoHotelVazio() {
        total++;
        Hotel hotel = new Hotel();

        if (hotel.calcularTaxaOcupacao() == 0.0f) {
            passou++;
            System.out.println("PASSOU: testarTaxaOcupacaoHotelVazio");
        } else {
            System.out.println("FALHOU: testarTaxaOcupacaoHotelVazio");
        }
    }


    /**
     * Verifica o calculo da taxa de ocupacao.  
     *
     * @pre Hotel contendo um apartamento ocupado.
     * @post Incrementa o total e, se a taxa estiver correta, incrementa passou.
     */
    static void testarTaxaOcupacao() {
        total++;
        Hotel hotel = new Hotel();
        hotel.realizarCheckin(0, 0, hospedePadrao());
        float esperada = 1.0f / 280.0f;
        float obtida = hotel.calcularTaxaOcupacao();
        if (Math.abs(esperada - obtida) < 0.0001f) {
            passou++;
            System.out.println("PASSOU: testarTaxaOcupacao");
        } else {
            System.out.println("FALHOU: testarTaxaOcupacao");
        }
    }


    /**
     * Verifica se a taxa de reservas de um hotel novo e zero.  
     *
     * @pre Hotel novo.
     * @post Incrementa o total e, se a taxa for zero, incrementa passou.
     */
    static void testarTaxaReservasHotelVazio() {
        total++;
        Hotel hotel = new Hotel();
        if (hotel.calcularTaxaReservas() == 0.0f) {
            passou++;
            System.out.println("PASSOU: testarTaxaReservasHotelVazio");
        } else {
            System.out.println("FALHOU: testarTaxaReservasHotelVazio");
        }
    }

    /**
     * Verifica a consulta de um apartamento com coordenadas validas.
     *
     * @pre Hotel inicializado.
     * @post Incrementa o total e, se um apartamento for retornado, incrementa passou.
     */
    static void testarGetApartamentoValido() {
        total++;
        Hotel hotel = new Hotel();
        if (hotel.getApartamento(0, 0) != null) {
            passou++;
            System.out.println("PASSOU: testarGetApartamentoValido");
        } else {
            System.out.println("FALHOU: testarGetApartamentoValido");
        }
    }

    /**
     * Verifica se o mapa de ocupacao e exibido com os simbolos corretos.
     *
     * @pre Hotel inicializado com uma reserva e um apartamento ocupado.
     * @post Incrementa o total e, se a saida possuir os simbolos esperados, incrementa passou.
     */
    static void testarMostrarMapa() {
        total++;
        PrintStream saidaOriginal = System.out;
        ByteArrayOutputStream saidaTeste = new ByteArrayOutputStream();
        try {
            Hotel hotel = new Hotel();
            hotel.reservarApartamento(0, 0, hospedePadrao());
            Hospede h2 = new Hospede("456", "Maria", "Rua Y", "8888", "maria@y");
            hotel.realizarCheckin(0, 1, h2);
            System.setOut(new PrintStream(saidaTeste));
            hotel.mostrarMapa();

            String resultado = saidaTeste.toString();

            if (resultado.contains("R") && resultado.contains("O") && resultado.contains(".")) {
                passou++;
                System.setOut(saidaOriginal);
                System.out.println("PASSOU: testarMostrarMapa");
            } else {
                System.setOut(saidaOriginal);
                System.out.println("FALHOU: testarMostrarMapa");
            }
        } catch (Throwable e) {
            System.setOut(saidaOriginal);
            System.out.println("FALHOU: testarMostrarMapa (" + e.getClass().getSimpleName() + ")");
        } finally {
            System.setOut(saidaOriginal);
        }
    }

    /**
     * Verifica a consulta de um apartamento livre.
     *
     * @pre Hotel inicializado.
     * @post Incrementa o total e, se a consulta indicar status LIVRE, incrementa passou.
     */
    static void testarConsultarApartamentoLivre() {
        total++;
        PrintStream saidaOriginal = System.out;
        ByteArrayOutputStream saidaTeste = new ByteArrayOutputStream();
        try {
            Hotel hotel = new Hotel();
            System.setOut(new PrintStream(saidaTeste));
            hotel.consultarApartamento(0, 0);
            String resultado = saidaTeste.toString();
            if (resultado.contains("LIVRE")) {
                passou++;
                System.setOut(saidaOriginal);
                System.out.println("PASSOU: testarConsultarApartamentoLivre");
            } else {
                System.setOut(saidaOriginal);
                System.out.println("FALHOU: testarConsultarApartamentoLivre");
            }
        } catch (Throwable e) {
            System.setOut(saidaOriginal);
            System.out.println("FALHOU: testarConsultarApartamentoLivre (" + e.getClass().getSimpleName() + ")");
        } finally {
            System.setOut(saidaOriginal);
        }
    }

    /**
     * Verifica a consulta de um apartamento reservado.
     *
     * @pre Apartamento previamente reservado.
     * @post Incrementa o total e, se a consulta exibir status e hospede, incrementa passou.
     */
    static void testarConsultarApartamentoReservado() {
        total++;
        PrintStream saidaOriginal = System.out;
        ByteArrayOutputStream saidaTeste = new ByteArrayOutputStream();
        try {
            Hotel hotel = new Hotel();
            Hospede h = hospedePadrao();
            hotel.reservarApartamento(0, 0, h);
            System.setOut(new PrintStream(saidaTeste));
            hotel.consultarApartamento(0, 0);
            String resultado = saidaTeste.toString();

            if (resultado.contains("RESERVADO") && resultado.contains("Joao") && resultado.contains("123") && resultado.contains("RES-0001-Q0101")) {
                passou++;
                System.setOut(saidaOriginal);
                System.out.println("PASSOU: testarConsultarApartamentoReservado");
            } else {
                System.setOut(saidaOriginal);
                System.out.println("FALHOU: testarConsultarApartamentoReservado");
            }
        } catch (Throwable e) {
            System.setOut(saidaOriginal);
            System.out.println("FALHOU: testarConsultarApartamentoReservado (" + e.getClass().getSimpleName() + ")");
        } finally {
            System.setOut(saidaOriginal);
        }
    }

    /**
     * Verifica a transferencia de um hospede que ja realizou check-in.
     *
     * @pre Apartamento de origem ocupado e destino livre.
     * @post Incrementa o total e, se o novo apartamento ficar ocupado, incrementa passou.
     */
    static void testarTransferenciaDeHospedeOcupado() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h = hospedePadrao();
        hotel.realizarCheckin(0, 0, h);
        boolean resultado = hotel.transferirHospede(0, 0, 0, 4);

        if (resultado && hotel.getApartamento(0, 0).estaLivre() && hotel.getApartamento(0, 4).estaOcupado() && hotel.getApartamento(0, 4).getHospede() == h) {
            passou++;
            System.out.println("PASSOU: testarTransferenciaDeHospedeOcupado");
        } else {
            System.out.println("FALHOU: testarTransferenciaDeHospedeOcupado");
        }
    }


    /**
     * Verifica se a transferencia mantem o numero original da reserva.
     *
     * @pre Hospede com reserva existente.
     * @post Incrementa o total e, se RES-0001 permanecer, incrementa passou.
     */
    static void testarTransferenciaMantemNumeroReserva() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h = hospedePadrao();
        hotel.reservarApartamento(0, 0, h);
        hotel.transferirHospede(0, 0, 0, 4);
        if (h.getIdentificadorReserva().startsWith("RES-0001-")) {
            passou++;
            System.out.println("PASSOU: testarTransferenciaMantemNumeroReserva");
        } else {
            System.out.println("FALHOU: testarTransferenciaMantemNumeroReserva");
        }
    }

    /**
     * Verifica se a transferencia atualiza o quarto no identificador.
     *
     * @pre Hospede com reserva e apartamento de destino livre.
     * @post Incrementa o total e, se o identificador possuir o novo quarto, incrementa passou.
     */
    static void testarTransferenciaAtualizaQuarto() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h = hospedePadrao();
        hotel.reservarApartamento(0, 0, h);
        hotel.transferirHospede(0, 0, 0, 4);
        if ("RES-0001-Q0105".equals(h.getIdentificadorReserva())) {
            passou++;
            System.out.println("PASSOU: testarTransferenciaAtualizaQuarto");
        } else {
            System.out.println("FALHOU: testarTransferenciaAtualizaQuarto");
        }
    }   

    /**
     * Verifica que nao e possivel transferir de um apartamento livre.
     *
     * @pre Apartamento de origem livre.
     * @post Incrementa o total e, se a transferencia retornar false, incrementa passou.
     */
    static void testarTransferenciaOrigemLivreFalha() {
        total++;
        Hotel hotel = new Hotel();
        boolean resultado = hotel.transferirHospede(0, 0, 0, 4);
        if (!resultado) {
            passou++;
            System.out.println("PASSOU: testarTransferenciaOrigemLivreFalha");
        } else {
            System.out.println("FALHOU: testarTransferenciaOrigemLivreFalha");
        }
    }

    /**
     * Verifica que nao e possivel transferir para apartamento que nao esta livre.
     *
     * @pre Origem reservada e destino tambem reservado.
     * @post Incrementa o total e, se a transferencia retornar false, incrementa passou.
     */
    static void testarTransferenciaDestinoNaoLivreFalha() {
        total++;
        Hotel hotel = new Hotel();
        Hospede h1 = hospedePadrao();
        Hospede h2 = new Hospede("456", "Maria", "Rua Y", "8888", "maria@y");
        hotel.reservarApartamento(0, 0, h1);
        hotel.reservarApartamento(0, 4, h2);
        boolean resultado = hotel.transferirHospede(0, 0, 0, 4);
        if (!resultado) {
            passou++;
            System.out.println("PASSOU: testarTransferenciaDestinoNaoLivreFalha");
        } else {
            System.out.println("FALHOU: testarTransferenciaDestinoNaoLivreFalha");
        }
    }


    /**
     * Verifica coordenada de origem invalida durante uma transferencia.    
     *
     * @pre Hotel inicializado.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarTransferenciaOrigemInvalida() {
        total++;
        try {
            Hotel hotel = new Hotel();
            hotel.transferirHospede(-1, 0, 0, 1);
            System.out.println("FALHOU: testarTransferenciaOrigemInvalida");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarTransferenciaOrigemInvalida");
        }
    }

    /**
     * Verifica coordenada de destino invalida durante uma transferencia.
     *
     * @pre Hotel inicializado com uma reserva.
     * @post Incrementa o total e, se IllegalArgumentException for lancada, incrementa passou.
     */
    static void testarTransferenciaDestinoInvalido() {
        total++;
        try {
            Hotel hotel = new Hotel();
            hotel.reservarApartamento(0, 0, hospedePadrao());
            hotel.transferirHospede(0, 0, 30, 1);
            System.out.println("FALHOU: testarTransferenciaDestinoInvalido");
        } catch (IllegalArgumentException e) {
            passou++;
            System.out.println("PASSOU: testarTransferenciaDestinoInvalido");
        }
    }

    /**
     * Verifica se o hotel deixa de estar vazio depois de uma reserva.
     *
     * @pre Hotel novo.
     * @post Incrementa o total e, se estaCompletoVazio retornar false, incrementa passou.
     */
    static void testarHotelDeixaDeEstarVazioAposReserva() {
        total++;
        Hotel hotel = new Hotel();
        hotel.reservarApartamento(0, 0, hospedePadrao());
        if (!hotel.estaCompletoVazio()) {
            passou++;
            System.out.println("PASSOU: testarHotelDeixaDeEstarVazioAposReserva");
        } else {
            System.out.println("FALHOU: testarHotelDeixaDeEstarVazioAposReserva");
        }
    }

    /**
     * Verifica se o hotel volta a ficar vazio apos o cancelamento da unica reserva.
     *
     * @pre Hotel contendo uma unica reserva.
     * @post Incrementa o total e, se todos os apartamentos ficarem livres, incrementa passou.
     */
    static void testarHotelVoltaAFicarVazioAposCancelamento() {
        total++;
        Hotel hotel = new Hotel();
        hotel.reservarApartamento(0, 0, hospedePadrao());
        hotel.cancelarReserva(0, 0);
        if (hotel.estaCompletoVazio()) {
            passou++;
            System.out.println("PASSOU: testarHotelVoltaAFicarVazioAposCancelamento");
        } else {
            System.out.println("FALHOU: testarHotelVoltaAFicarVazioAposCancelamento");
        }
    }
}