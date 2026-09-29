package hotel.negocio;

import hotel.modelo.*;
import java.util.ArrayList;

public class Hotel {
    public static final int NUM_ANDARES = 20;
    public static final int APTOS_POR_ANDAR = 14;
    public static final int SIMPLES_POR_ANDAR = 8;

    private Apartamento[][] matriz;
    private ArrayList<Servico> servicos;
    private ArrayList<Consumo> consumos;

    private int proximaReserva = 1;

    /**
     * Cria o hotel e inicializa os apartamentos.
     *
     * @pre Nenhuma
     * @post Hotel criado com todos os apartamentos livres
     */
    public Hotel() {
        this.matriz = new Apartamento[NUM_ANDARES][APTOS_POR_ANDAR];
        this.servicos = new ArrayList<>();
        this.consumos = new ArrayList<>();
        inicializar();
    }

    /*
     * Cada andar possui:
     * 8 apartamentos simples
     * 6 apartamentos premium
     */
    private void inicializar() {
        for (int andar = 0; andar < NUM_ANDARES; andar++) {

            for (int numero = 0; numero < APTOS_POR_ANDAR; numero++) {

                if (numero < SIMPLES_POR_ANDAR) {
                    matriz[andar][numero] =
                            new ApartamentoSimples();
                } else {
                    matriz[andar][numero] =
                            new ApartamentoPremium();
                }
            }
        }
    }

    private boolean aptoValido(int andar, int numero) {
        return andar >= 0 && andar < NUM_ANDARES && numero >= 0 && numero < APTOS_POR_ANDAR;
    }

    private void validarCoordenadas(int andar, int numero) {
        if (!aptoValido(andar, numero)) {
            throw new IllegalArgumentException("Andar ou numero de apartamento invalido");
        }
    }

    /*
     * Exemplo: RES-0001-Q0101
     *
     * RES-0001 = numero da reserva
     * Q0101    = quarto
     */
    private String criarIdentificadorReserva(int andar, int numero) {
        String identificador = String.format("RES-%04d-Q%02d%02d", proximaReserva, andar + 1, numero + 1);
        proximaReserva++;
        return identificador;
    }

    //Mantem o numero da reserva, mudando apenas o quarto.
    private String atualizarQuartoIdentificador(String identificador, int novoAndar, int novoNumero) {
        if (identificador == null) {
            return criarIdentificadorReserva(novoAndar, novoNumero);
        }

        int posicaoQuarto = identificador.indexOf("-Q");
        if (posicaoQuarto == -1) {
            return criarIdentificadorReserva(novoAndar,novoNumero);
        }

        String reserva = identificador.substring(0, posicaoQuarto);

        return reserva + String.format("-Q%02d%02d", novoAndar + 1, novoNumero + 1);
    }

    /**
     * Reserva um apartamento.
     *
     * @param andar andar desejado
     * @param numero numero do apartamento
     * @param hospede hospede da reserva
     * @return true se conseguiu reservar
     * @throws IllegalArgumentException se a coordenada for invalida
     * @pre Apartamento deve estar livre
     * @post Apartamento fica reservado
     */
    public boolean reservarApartamento(int andar, int numero, Hospede hospede) {
        validarCoordenadas(andar, numero);
        
        if (hospede == null) {
            throw new IllegalArgumentException("Hospede nao pode ser nulo");
        }
        
        Apartamento apto = matriz[andar][numero];
        
        if (!apto.estaLivre()) { return false; }
        
        String identificador = criarIdentificadorReserva(andar, numero);
        hospede.setIdentificadorReserva(identificador);
        apto.reservar(hospede);

        return true;
    }

    /**
     * Realiza check-in.
     *
     * @param andar andar do apartamento
     * @param numero numero do apartamento
     * @param hospede hospede
     * @return true se o check-in foi realizado
     * @throws IllegalArgumentException para coordenada invalida
     * @pre Apartamento livre ou reservado
     * @post Apartamento fica ocupado
     */
    public boolean realizarCheckin(int andar, int numero, Hospede hospede) {
        validarCoordenadas(andar, numero);

        if (hospede == null) {
            throw new IllegalArgumentException("Hospede nao pode ser nulo");
        }

        Apartamento apto = matriz[andar][numero];

        if (apto.estaOcupado()) { return false; }

        //Se ja existia uma reserva, aproveita o identificador da reserva.
        if (apto.estaReservado() && apto.getHospede() != null) {
            String identificadorAnterior = apto.getHospede().getIdentificadorReserva();
            hospede.setIdentificadorReserva(identificadorAnterior);
        } else {
            //Check-in direto, sem reserva anterior.
            if (hospede.getIdentificadorReserva() == null) {
                hospede.setIdentificadorReserva(criarIdentificadorReserva(andar, numero));
            }
        }
        apto.checkin(hospede);
        return true;
    }

    /**
     * Realiza checkout.
     *
     * @param andar andar
     * @param numero apartamento
     * @return true se realizou checkout
     * @throws IllegalArgumentException para coordenada invalida
     * @pre Apartamento ocupado
     * @post Apartamento fica livre
     */
    public boolean realizarCheckout(int andar, int numero) {
        validarCoordenadas(andar, numero);
        Apartamento apto = matriz[andar][numero];

        if (!apto.estaOcupado()) { return false; }

        apto.checkout();
        return true;
    }

    /**
     * Cancela uma reserva.
     *
     * @param andar andar
     * @param numero apartamento
     * @return true se cancelou
     * @throws IllegalArgumentException para coordenada invalida
     * @pre Apartamento reservado
     * @post Apartamento fica livre
     */
    public boolean cancelarReserva(int andar, int numero) {
        validarCoordenadas(andar, numero);
        Apartamento apto = matriz[andar][numero];

        if (!apto.estaReservado()) { return false; }

        apto.cancelarReserva();
        return true;
    }

    /**
     * Transfere um hospede para outro apartamento.
     *
     * @param andarOrigem andar atual
     * @param numeroOrigem apartamento atual
     * @param andarDestino novo andar
     * @param numeroDestino novo apartamento
     * @return true se a transferencia ocorreu
     * @throws IllegalArgumentException para coordenada invalida
     * @pre Origem deve estar reservada ou ocupada e destino livre
     * @post Hospede passa para o novo apartamento e identificador e atualizado
     */
    public boolean transferirHospede(int andarOrigem, int numeroOrigem, int andarDestino, int numeroDestino) {
        validarCoordenadas(andarOrigem, numeroOrigem);
        validarCoordenadas(andarDestino, numeroDestino);

        Apartamento origem = matriz[andarOrigem][numeroOrigem];
        Apartamento destino = matriz[andarDestino][numeroDestino];

        if (origem.estaLivre()) { return false; }
        if (!destino.estaLivre()) { return false; }

        Hospede hospede = origem.getHospede();
        boolean estavaOcupado = origem.estaOcupado();

        String novoIdentificador = atualizarQuartoIdentificador(hospede.getIdentificadorReserva(), andarDestino, numeroDestino);
        hospede.setIdentificadorReserva(novoIdentificador);

        if (estavaOcupado) {
            destino.checkin(hospede);
            origem.checkout();
        } else {
            destino.reservar(hospede);
            origem.cancelarReserva();
        }

        return true;
    }

    /**
     * Mostra o mapa de ocupacao.
     *
     * @pre Hotel inicializado
     * @post Mapa exibido no console
     */
    public void mostrarMapa() {
        System.out.println("\n=== MAPA DO HOTEL ===");

        for (int andar = 0; andar < NUM_ANDARES; andar++) {
            System.out.printf("Andar %02d: ", andar + 1);
            for (int numero = 0; numero < APTOS_POR_ANDAR; numero++) {
                System.out.print(matriz[andar][numero].getSymbol() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Consulta um apartamento.
     *
     * @param andar andar
     * @param numero apartamento
     * @throws IllegalArgumentException para coordenada invalida
     * @pre Coordenadas validas
     * @post Dados exibidos no console
     */
    public void consultarApartamento(int andar, int numero) {
        validarCoordenadas(andar, numero);
        Apartamento apto = matriz[andar][numero];

        System.out.println("Status: " + apto.getStatus());
        System.out.println("Tipo: " + apto.getClass().getSimpleName());
        System.out.printf("Preco da diaria: R$ %.2f%n", apto.getPrecoDiaria());

        if (apto.getHospede() != null) {
            Hospede hospede = apto.getHospede();

            System.out.println("Hospede: " + hospede.getNome());
            System.out.println("CPF: " + hospede.getCpf());
            System.out.println("Identificador: " + hospede.getIdentificadorReserva());
        }
    }

    /**
     * Calcula a taxa de ocupacao.
     *
     * @return valor entre 0 e 1
     * @pre Hotel inicializado
     * @post Hotel nao e alterado
     */
    public float calcularTaxaOcupacao() {
        int ocupados = 0;
        for (int andar = 0; andar < NUM_ANDARES; andar++) {
            for (int numero = 0; numero < APTOS_POR_ANDAR; numero++) {
                if (matriz[andar][numero].estaOcupado()) {
                    ocupados++;
                }
            }
        }
        int total = NUM_ANDARES * APTOS_POR_ANDAR;
        return (float) ocupados / total;
    }

    /**
     * Calcula a taxa de reservas.
     *
     * @return valor entre 0 e 1
     * @pre Hotel inicializado
     * @post Hotel nao e alterado
     */
    public float calcularTaxaReservas() {
        int reservados = 0;
        for (int andar = 0; andar < NUM_ANDARES; andar++) {
            for (int numero = 0; numero < APTOS_POR_ANDAR; numero++) {
                if (matriz[andar][numero].estaReservado()) {
                    reservados++;
                }
            }
        }
        int total = NUM_ANDARES * APTOS_POR_ANDAR;
        return (float) reservados / total;
    }

    /**
     * Verifica se todos os apartamentos estao livres.
     *
     * @return true se todos os apartamentos estiverem livres
     * @pre Hotel inicializado
     * @post Estado do hotel nao e alterado
     */
    public boolean estaCompletoVazio() {
        for (int andar = 0; andar < NUM_ANDARES; andar++) {
            for (int numero = 0; numero < APTOS_POR_ANDAR; numero++) {
            if (!matriz[andar][numero].estaLivre()) { return false; }
            }
        }
        return true;
    }

    public void cadastrarServico(String nome, float preco) {
        throw new UnsupportedOperationException("Implementar cadastrarServico");
    }

    public boolean registrarConsumo(int andar, int numero, int indiceServico, int quantidade) {
        if (!aptoValido(andar, numero)) {
            throw new IllegalArgumentException("Andar ou numero invalido");
        }
        throw new UnsupportedOperationException("Implementar registrarConsumo");
    }

    public ArrayList<Consumo> getConsumosDoApartamento(int andar, int numero) {
        if (!aptoValido(andar, numero)) {
            throw new IllegalArgumentException("Andar ou numero invalido");
        }
        throw new UnsupportedOperationException("Implementar getConsumosDoApartamento");
    }

    public Fatura emitirFatura(int andar, int numero, int dias) {
        if (!aptoValido(andar, numero)) {
            throw new IllegalArgumentException("Andar ou numero invalido");
        }
        throw new UnsupportedOperationException("Implementar emitirFatura");
    }

    /**
     * Retorna um apartamento.
     *
     * @param andar andar
     * @param numero numero
     * @return apartamento encontrado
     * @throws IllegalArgumentException para coordenada invalida
     * @pre Coordenadas validas
     * @post Hotel nao e alterado
     */
    public Apartamento getApartamento(int andar, int numero) {
        validarCoordenadas(andar, numero);
        return matriz[andar][numero];
    }

    public ArrayList<Servico> getServicos() { return servicos; }
    public ArrayList<Consumo> getConsumos() { return consumos; }
}