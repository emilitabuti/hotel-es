package hotel.apresentacao;

import hotel.modelo.*;
import hotel.negocio.Hotel;
import java.util.Arrays;
import java.util.Collections;

/**
 * Testes dos modulos A, B e C do sistema Hotel.
 * Total: 72 testes.
 */
public class HotelTest {

    private static int total = 0;
    private static int passou = 0;

    private interface Caso {
        void executar();
    }

    private static void teste(String nome, Caso caso) {
        total++;
        try {
            caso.executar();
            passou++;
            System.out.println("PASSOU: " + nome);
        } catch (Throwable e) {
            System.out.println("FALHOU: " + nome + " - " + e);
        }
    }

    private static void ok(boolean condicao, String mensagem) {
        if (!condicao) {
            throw new AssertionError(mensagem);
        }
    }

    private static void valor(float esperado, float recebido) {
        ok(Math.abs(esperado - recebido) < 0.001f,
            "Esperado " + esperado + ", recebido " + recebido);
    }

    private static void erro(
        Class<? extends Throwable> tipo, Caso caso
    ) {
        try {
            caso.executar();
        } catch (Throwable e) {
            if (tipo.isInstance(e)) return;
            throw new AssertionError("Excecao inesperada: " + e);
        }
        throw new AssertionError(
            "Era esperada a excecao " + tipo.getSimpleName()
        );
    }

    private static Hospede hospede(String cpf) {
        return new Hospede(
            cpf, "Hospede " + cpf,
            "Rua A", "9999", cpf + "@exemplo.com"
        );
    }

    private static Hotel hotelComServico() {
        Hotel hotel = new Hotel();
        hotel.cadastrarServico("Cafe", 12.5f);
        return hotel;
    }

    public static void main(String[] args) {

        // MODULO A - HOSPEDES E APARTAMENTOS

        teste("Criar hospede e validar dados", () -> {
            Hospede h = hospede("123");
            ok(h.getCpf().equals("123")
                && h.getNome().equals("Hospede 123"),
                "Dados incorretos");
            ok(h.getIdentificadorReserva() == null,
                "Reserva deve iniciar vazia");
        });

        teste("Rejeitar hospede com dados invalidos", () -> {
            erro(IllegalArgumentException.class,
                () -> new Hospede(null, "Ana", "Rua", "999", "a@b"));
            erro(IllegalArgumentException.class,
                () -> new Hospede("1", " ", "Rua", "999", "a@b"));
        });

        teste("Igualdade de hospede por CPF", () -> {
            ok(hospede("123").equals(hospede("123")),
                "Mesmo CPF deve ser igual");
            ok(!hospede("123").equals(hospede("456")),
                "CPFs distintos devem ser diferentes");
            ok(hospede("123").hashCode()
                == hospede("123").hashCode(),
                "hashCode diferente");
        });

        teste("Apartamento novo esta livre", () -> {
            Apartamento a = new Apartamento();
            ok(a.estaLivre()
                && a.getHospede() == null
                && a.getSymbol() == '.',
                "Estado inicial incorreto");
        });

        teste("Estados reserva, check-in e check-out", () -> {
            Apartamento a = new Apartamento();
            Hospede h = hospede("123");
            a.reservar(h);
            ok(a.estaReservado() && a.getSymbol() == 'R',
                "Reserva incorreta");
            a.checkin(h);
            ok(a.estaOcupado() && a.getSymbol() == 'O',
                "Check-in incorreto");
            a.checkout();
            ok(a.estaLivre() && a.getHospede() == null,
                "Check-out incorreto");
        });

        teste("Cancelar reserva", () -> {
            Apartamento a = new Apartamento();
            a.reservar(hospede("123"));
            a.cancelarReserva();
            ok(a.estaLivre() && a.getHospede() == null,
                "Reserva nao cancelada");
        });

        teste("Bloquear estados invalidos do apartamento", () -> {
            Apartamento a = new Apartamento();
            erro(IllegalStateException.class, a::checkout);
            erro(IllegalStateException.class, a::cancelarReserva);
            erro(IllegalArgumentException.class,
                () -> a.reservar(null));
            a.reservar(hospede("123"));
            erro(IllegalStateException.class,
                () -> a.reservar(hospede("456")));
            a.checkin(hospede("123"));
            erro(IllegalStateException.class,
                () -> a.checkin(hospede("456")));
        });

        // MODULO B - HIERARQUIA

        teste("Tipos e precos das diarias", () -> {
            ok(new ApartamentoSimples() instanceof Apartamento,
                "Heranca simples");
            ok(new ApartamentoPremium() instanceof Apartamento,
                "Heranca premium");
            valor(150f, new ApartamentoSimples().getPrecoDiaria());
            valor(350f, new ApartamentoPremium().getPrecoDiaria());
        });

        teste("Hotel inicia com 20 andares e 14 apartamentos", () -> {
            Hotel hotel = new Hotel();
            ok(hotel.estaCompletoVazio(), "Hotel nao inicia vazio");
            for (int andar = 0; andar < Hotel.NUM_ANDARES; andar++) {
                for (int numero = 0;
                     numero < Hotel.APTOS_POR_ANDAR; numero++) {
                    Apartamento a = hotel.getApartamento(andar, numero);
                    ok(a.estaLivre(), "Apartamento nao inicia livre");
                    ok(numero < 8
                        ? a instanceof ApartamentoSimples
                        : a instanceof ApartamentoPremium,
                        "Tipo de apartamento incorreto");
                }
            }
        });

        teste("Rejeitar coordenadas invalidas", () -> {
            Hotel hotel = new Hotel();
            erro(IllegalArgumentException.class,
                () -> hotel.getApartamento(-1, 0));
            erro(IllegalArgumentException.class,
                () -> hotel.reservarApartamento(
                    20, 0, hospede("123")));
            erro(IllegalArgumentException.class,
                () -> hotel.realizarCheckin(
                    0, 14, hospede("123")));
            erro(IllegalArgumentException.class,
                () -> hotel.transferirHospede(0, 0, 30, 1));
        });

        // MODULO A - OPERACOES DO HOTEL

        teste("Reservar quarto e criar identificador unico", () -> {
            Hotel hotel = new Hotel();
            Hospede a = hospede("123");
            Hospede b = hospede("456");
            ok(hotel.reservarApartamento(0, 0, a), "Reserva 1");
            ok(hotel.reservarApartamento(0, 1, b), "Reserva 2");
            ok("RES-0001-Q0101".equals(a.getIdentificadorReserva()),
                "Codigo da primeira reserva");
            ok(!a.getIdentificadorReserva().equals(
                b.getIdentificadorReserva()), "Codigos repetidos");
            ok(!hotel.reservarApartamento(0, 0, hospede("789")),
                "Reservou quarto indisponivel");
        });

        teste("Check-in direto e bloqueio de quarto ocupado", () -> {
            Hotel hotel = new Hotel();
            ok(hotel.realizarCheckin(0, 0, hospede("123")),
                "Check-in direto");
            ok(hotel.getApartamento(0, 0).estaOcupado(),
                "Quarto nao ocupado");
            ok(!hotel.realizarCheckin(0, 0, hospede("456")),
                "Segundo check-in permitido");
        });

        teste("Check-in de reserva preserva titular", () -> {
            Hotel hotel = new Hotel();
            Hospede titular = hospede("123");
            hotel.reservarApartamento(0, 0, titular);
            ok(hotel.realizarCheckin(0, 0, hospede("123")),
                "CPF correto rejeitado");
            ok(hotel.getApartamento(0, 0).getHospede() == titular,
                "Titular substituido");
        });

        teste("Check-in rejeita CPF diferente", () -> {
            Hotel hotel = new Hotel();
            Hospede titular = hospede("123");
            hotel.reservarApartamento(0, 0, titular);
            ok(!hotel.realizarCheckin(0, 0, hospede("456")),
                "Aceitou outro CPF");
            ok(hotel.getApartamento(0, 0).estaReservado(),
                "Alterou reserva indevidamente");
            ok(hotel.getApartamento(0, 0).getHospede() == titular,
                "Perdeu titular");
        });

        teste("Cancelar reserva pela classe Hotel", () -> {
            Hotel hotel = new Hotel();
            hotel.reservarApartamento(0, 0, hospede("123"));
            ok(hotel.cancelarReserva(0, 0), "Nao cancelou");
            ok(!hotel.cancelarReserva(0, 0)
                && hotel.estaCompletoVazio(),
                "Cancelamento incorreto");
        });

        teste("Check-out rejeita quarto livre", () -> {
            ok(!new Hotel().realizarCheckout(0, 0),
                "Check-out em quarto livre aceito");
        });

        teste("Taxas de ocupacao e reservas", () -> {
            Hotel hotel = new Hotel();
            valor(0f, hotel.calcularTaxaOcupacao());
            valor(0f, hotel.calcularTaxaReservas());
            hotel.realizarCheckin(0, 0, hospede("123"));
            hotel.reservarApartamento(0, 1, hospede("456"));
            valor(1f / 280f, hotel.calcularTaxaOcupacao());
            valor(1f / 280f, hotel.calcularTaxaReservas());
        });

        // MODULO C - SERVICOS E CONSUMOS

        teste("Validar cadastro de servico", () -> {
            Hotel hotel = hotelComServico();
            ok(hotel.getServicos().size() == 1,
                "Cadastro nao incluido");
            valor(12.5f, hotel.getServicos().get(0).getPreco());
            erro(IllegalArgumentException.class,
                () -> hotel.cadastrarServico(" ", 10f));
            erro(IllegalArgumentException.class,
                () -> hotel.cadastrarServico("Cafe", -1f));
            ok(hotel.getServicos().size() == 1,
                "Servico invalido foi cadastrado");
        });

        teste("Consumo calcula preco vezes quantidade", () -> {
            Consumo c = new Consumo(
                new ApartamentoSimples(),
                new Servico("Cafe", 12.5f), 2);
            valor(25f, c.getTotal());
            ok(c.getQuantidade() == 2, "Quantidade incorreta");
        });

        teste("Rejeitar consumo invalido", () -> {
            Apartamento a = new ApartamentoSimples();
            Servico s = new Servico("Cafe", 12.5f);
            erro(IllegalArgumentException.class,
                () -> new Consumo(null, s, 1));
            erro(IllegalArgumentException.class,
                () -> new Consumo(a, null, 1));
            erro(IllegalArgumentException.class,
                () -> new Consumo(a, s, 0));
        });

        teste("Registrar consumo apenas em quarto ocupado", () -> {
            Hotel hotel = hotelComServico();
            ok(!hotel.registrarConsumo(0, 0, 0, 1),
                "Consumo em quarto livre");
            hotel.reservarApartamento(0, 0, hospede("123"));
            ok(!hotel.registrarConsumo(0, 0, 0, 1),
                "Consumo em quarto reservado");
            hotel.realizarCheckin(0, 0, hospede("123"));
            ok(hotel.registrarConsumo(0, 0, 0, 2),
                "Consumo nao registrado");
            valor(25f,
                hotel.getConsumosDoApartamento(0, 0).get(0).getTotal());
        });

        teste("Rejeitar indice e quantidade invalidos", () -> {
            Hotel hotel = hotelComServico();
            hotel.realizarCheckin(0, 0, hospede("123"));
            erro(IllegalArgumentException.class,
                () -> hotel.registrarConsumo(0, 0, 5, 1));
            erro(IllegalArgumentException.class,
                () -> hotel.registrarConsumo(0, 0, 0, 0));
            ok(hotel.getConsumos().isEmpty(),
                "Consumo invalido incluido");
        });

        teste("Isolar consumos de quartos diferentes", () -> {
            Hotel hotel = hotelComServico();
            hotel.realizarCheckin(0, 0, hospede("123"));
            hotel.realizarCheckin(0, 1, hospede("456"));
            hotel.registrarConsumo(0, 0, 0, 1);
            hotel.registrarConsumo(0, 1, 0, 2);
            valor(12.5f,
                hotel.getConsumosDoApartamento(0, 0).get(0).getTotal());
            valor(25f,
                hotel.getConsumosDoApartamento(0, 1).get(0).getTotal());
        });

        // MODULO C - FATURAMENTO

        teste("Calcular fatura simples e premium", () -> {
            Fatura f1 = new Fatura(
                new ApartamentoSimples(), 2,
                Collections.emptyList());
            Fatura f2 = new Fatura(
                new ApartamentoPremium(), 2,
                Collections.emptyList());
            valor(300f, f1.getTotalGeral());
            valor(700f, f2.getTotalGeral());
        });

        teste("Fatura soma diarias e consumos", () -> {
            Apartamento a = new ApartamentoSimples();
            Consumo c = new Consumo(
                a, new Servico("Cafe", 12.5f), 2);
            Fatura f = new Fatura(a, 2, Arrays.asList(c));
            valor(300f, f.getTotalDiarias());
            valor(25f, f.getTotalConsumos());
            valor(325f, f.getTotalGeral());
            ok(f.getConsumos().size() == 1, "Consumo ausente");
        });

        teste("Rejeitar fatura invalida", () -> {
            Apartamento a = new ApartamentoSimples();
            erro(IllegalArgumentException.class,
                () -> new Fatura(null, 1, Collections.emptyList()));
            erro(IllegalArgumentException.class,
                () -> new Fatura(a, 0, Collections.emptyList()));
            erro(IllegalArgumentException.class,
                () -> new Fatura(a, 1, null));
            erro(IllegalArgumentException.class,
                () -> new Fatura(a, 1, Arrays.asList((Consumo) null)));
        });

        teste("Fatura detalha diarias e servicos", () -> {
            Hotel hotel = hotelComServico();
            hotel.realizarCheckin(0, 0, hospede("123"));
            hotel.registrarConsumo(0, 0, 0, 2);
            String texto = hotel.emitirFatura(0, 0, 2).toString();
            ok(texto.contains("Diarias")
                && texto.contains("Cafe")
                && texto.contains("TOTAL GERAL"),
                "Faltam dados na fatura");
            ok(texto.contains("325,00"),
                "Total formatado incorretamente");
        });

        teste("Nao emitir fatura para quarto livre ou reservado", () -> {
            Hotel hotel = new Hotel();
            erro(IllegalStateException.class,
                () -> hotel.emitirFatura(0, 0, 1));
            hotel.reservarApartamento(0, 0, hospede("123"));
            erro(IllegalStateException.class,
                () -> hotel.emitirFatura(0, 0, 1));
        });

        // INTEGRACAO A + C

        teste("Transferencia de reserva atualiza quarto", () -> {
            Hotel hotel = new Hotel();
            Hospede h = hospede("123");
            hotel.reservarApartamento(0, 0, h);
            ok(hotel.transferirHospede(0, 0, 0, 4),
                "Falha na transferencia");
            ok(hotel.getApartamento(0, 0).estaLivre()
                && hotel.getApartamento(0, 4).estaReservado(),
                "Estados incorretos");
            ok("RES-0001-Q0105".equals(h.getIdentificadorReserva()),
                "Identificador nao atualizado");
        });

        teste("Transferencia de ocupado leva os consumos", () -> {
            Hotel hotel = hotelComServico();
            hotel.realizarCheckin(0, 0, hospede("123"));
            hotel.registrarConsumo(0, 0, 0, 2);
            ok(hotel.transferirHospede(0, 0, 0, 4),
                "Falha na transferencia");
            ok(hotel.getApartamento(0, 0).estaLivre()
                && hotel.getApartamento(0, 4).estaOcupado(),
                "Estados incorretos");
            ok(hotel.getConsumosDoApartamento(0, 0).isEmpty(),
                "Consumo permaneceu na origem");
            ok(hotel.getConsumosDoApartamento(0, 4).size() == 1,
                "Consumo nao acompanhou a hospedagem");
            valor(25f,
                hotel.emitirFatura(0, 4, 1).getTotalConsumos());
        });

        teste("Bloquear transferencia para destino ocupado", () -> {
            Hotel hotel = new Hotel();
            hotel.realizarCheckin(0, 0, hospede("123"));
            hotel.realizarCheckin(0, 1, hospede("456"));
            ok(!hotel.transferirHospede(0, 0, 0, 1),
                "Destino ocupado aceito");
            ok(!hotel.transferirHospede(0, 2, 0, 3),
                "Origem livre aceita");
        });

        teste("Check-out nao passa consumo ao proximo hospede", () -> {
            Hotel hotel = hotelComServico();
            hotel.realizarCheckin(0, 0, hospede("123"));
            hotel.registrarConsumo(0, 0, 0, 2);
            ok(hotel.realizarCheckout(0, 0),
                "Check-out nao realizado");
            hotel.realizarCheckin(0, 0, hospede("456"));
            ok(hotel.getConsumosDoApartamento(0, 0).isEmpty(),
                "Consumos do hospede anterior");
            valor(150f,
                hotel.emitirFatura(0, 0, 1).getTotalGeral());
        });

        teste("Finalizar gera fatura e libera apartamento", () -> {
            Hotel hotel = hotelComServico();
            hotel.realizarCheckin(0, 0, hospede("123"));
            hotel.registrarConsumo(0, 0, 0, 2);
            Fatura f = hotel.finalizarHospedagem(0, 0, 2);
            valor(325f, f.getTotalGeral());
            ok(f.getConsumos().size() == 1,
                "Fatura perdeu consumo");
            ok(hotel.getApartamento(0, 0).estaLivre(),
                "Apartamento nao liberado");
            ok(hotel.getConsumos().isEmpty(),
                "Consumos anteriores nao removidos");
        });

        teste("Erro ao finalizar nao altera hospedagem", () -> {
            Hotel hotel = hotelComServico();
            hotel.realizarCheckin(0, 0, hospede("123"));
            hotel.registrarConsumo(0, 0, 0, 1);
            erro(IllegalArgumentException.class,
                () -> hotel.finalizarHospedagem(0, 0, 0));
            erro(IllegalStateException.class,
                () -> hotel.finalizarHospedagem(0, 1, 1));
            ok(hotel.getApartamento(0, 0).estaOcupado(),
                "Quarto foi liberado");
            ok(hotel.getConsumosDoApartamento(0, 0).size() == 1,
                "Consumo perdido");
        });

        // TESTES COMPLEMENTARES

        teste("Identificador de hospede pode mudar", () -> {
            Hospede h = hospede("123");
            h.setIdentificadorReserva("RES-0001-Q0101");
            h.setIdentificadorReserva("RES-0001-Q0105");
            ok(h.getIdentificadorReserva().equals("RES-0001-Q0105"),
                "Codigo desatualizado");
        });

        teste("Identificador de hospede com reserva e valido", () -> {
            Hospede h = hospede("123");
            h.setIdentificadorReserva("RES-0002-Q0101");
            ok(h.getIdentificadorReserva().startsWith("RES-0002"),
                "Reserva nao gravada");
        });

        teste("Hospedes com CPF diferente nao sao iguais", () -> {
            ok(!hospede("123").equals(hospede("124")),
                "CPFs diferentes comparados como iguais");
        });

        teste("Apartamento reservado referencia hospede", () -> {
            Apartamento a = new Apartamento();
            Hospede h = hospede("123");
            a.reservar(h);
            ok(a.getHospede() == h
                && a.getStatus() == Status.RESERVADO,
                "Titular da reserva incorreto");
        });

        teste("Apartamento ocupado identifica status", () -> {
            Apartamento a = new Apartamento();
            a.checkin(hospede("123"));
            ok(a.getStatus() == Status.OCUPADO && !a.estaLivre(),
                "Status errado");
        });

        teste("Check-in de apartamento guarda titular", () -> {
            Apartamento a = new Apartamento();
            Hospede h = hospede("123");
            a.checkin(h);
            ok(a.getHospede() == h, "Titular nao associado");
        });

        teste("Apartamento simples inicia livre", () -> {
            ok(new ApartamentoSimples().estaLivre(),
                "Simples nao livre");
        });

        teste("Apartamento premium inicia livre", () -> {
            ok(new ApartamentoPremium().estaLivre(),
                "Premium nao livre");
        });

        teste("Quantidade de tipos por andar", () -> {
            Hotel h = new Hotel();
            for (int a = 0; a < Hotel.NUM_ANDARES; a++) {
                int simples = 0;
                int premium = 0;
                for (int q = 0; q < Hotel.APTOS_POR_ANDAR; q++) {
                    if (h.getApartamento(a, q)
                        instanceof ApartamentoSimples) simples++;
                    if (h.getApartamento(a, q)
                        instanceof ApartamentoPremium) premium++;
                }
                ok(simples == 8 && premium == 6,
                    "Distribuicao incorreta no andar " + a);
            }
        });

        teste("Apartamento nao acessivel fora do ultimo andar", () -> {
            erro(IllegalArgumentException.class,
                () -> new Hotel().getApartamento(20, 0));
        });

        teste("Apartamento nao acessivel fora do ultimo numero", () -> {
            erro(IllegalArgumentException.class,
                () -> new Hotel().getApartamento(0, 14));
        });

        teste("Reserva nao aceita hospede nulo", () -> {
            erro(IllegalArgumentException.class,
                () -> new Hotel().reservarApartamento(0, 0, null));
        });

        teste("Reserva distinta gera numero diferente", () -> {
            Hotel h = new Hotel();
            Hospede a = hospede("123");
            Hospede b = hospede("456");
            h.reservarApartamento(0, 0, a);
            h.reservarApartamento(0, 1, b);
            ok(!a.getIdentificadorReserva().equals(
                b.getIdentificadorReserva()),
                "Reservas repetidas");
        });

        teste("Reserva identifica corretamente o andar", () -> {
            Hotel h = new Hotel();
            Hospede a = hospede("123");
            h.reservarApartamento(2, 4, a);
            ok(a.getIdentificadorReserva().endsWith("Q0305"),
                "Quarto incorreto no identificador");
        });

        teste("Check-in direto cria identificador", () -> {
            Hotel h = new Hotel();
            Hospede a = hospede("123");
            h.realizarCheckin(0, 0, a);
            ok(a.getIdentificadorReserva() != null
                && a.getIdentificadorReserva().contains("Q0101"),
                "Sem identificador");
        });

        teste("Check-in no hotel rejeita hospede nulo", () -> {
            erro(IllegalArgumentException.class,
                () -> new Hotel().realizarCheckin(0, 0, null));
        });

        teste("Check-in preserva codigo de reserva", () -> {
            Hotel h = new Hotel();
            Hospede a = hospede("123");
            h.reservarApartamento(0, 0, a);
            String codigo = a.getIdentificadorReserva();
            h.realizarCheckin(0, 0, hospede("123"));
            ok(codigo.equals(
                h.getApartamento(0, 0)
                 .getHospede().getIdentificadorReserva()),
                "Codigo mudou");
        });

        teste("Cancelamento deixa apartamento livre", () -> {
            Hotel h = new Hotel();
            h.reservarApartamento(0, 0, hospede("123"));
            ok(h.cancelarReserva(0, 0)
                && h.getApartamento(0, 0).getHospede() == null,
                "Nao liberou");
        });

        teste("Cancelar apartamento ocupado nao permitido", () -> {
            Hotel h = new Hotel();
            h.realizarCheckin(0, 0, hospede("123"));
            ok(!h.cancelarReserva(0, 0)
                && h.getApartamento(0, 0).estaOcupado(),
                "Cancelou ocupacao");
        });

        teste("Check-out remove hospede do apartamento", () -> {
            Hotel h = new Hotel();
            h.realizarCheckin(0, 0, hospede("123"));
            ok(h.realizarCheckout(0, 0)
                && h.getApartamento(0, 0).getHospede() == null,
                "Hospede permaneceu");
        });

        teste("Taxa de ocupacao exclui reserva", () -> {
            Hotel h = new Hotel();
            h.reservarApartamento(0, 0, hospede("123"));
            valor(0f, h.calcularTaxaOcupacao());
        });

        teste("Taxa de reservas exclui ocupado", () -> {
            Hotel h = new Hotel();
            h.realizarCheckin(0, 0, hospede("123"));
            valor(0f, h.calcularTaxaReservas());
        });

        teste("Servico preserva nome e preco", () -> {
            Servico s = new Servico("Cafe", 12.5f);
            ok(s.getNome().equals("Cafe"), "Nome incorreto");
            valor(12.5f, s.getPreco());
        });

        teste("Servico nao aceita nome nulo", () -> {
            erro(IllegalArgumentException.class,
                () -> new Servico(null, 10f));
        });

        teste("Servico nao aceita preco zero", () -> {
            erro(IllegalArgumentException.class,
                () -> new Servico("Cafe", 0f));
        });

        teste("Servico nao aceita preco NaN", () -> {
            erro(IllegalArgumentException.class,
                () -> new Servico("Cafe", Float.NaN));
        });

        teste("Consumo registra servico e apartamento", () -> {
            Apartamento a = new ApartamentoSimples();
            Servico s = new Servico("Cafe", 12.5f);
            Consumo c = new Consumo(a, s, 2);
            ok(c.getApartamento() == a
                && c.getServico() == s
                && c.getQuantidade() == 2,
                "Referencias incorretas");
        });

        teste("Consumo extrai numero fixo da hospedagem", () -> {
            ok("RES-0012".equals(
                Consumo.extrairIdentificadorHospedagem(
                    "RES-0012-Q0305")),
                "ID nao normalizado");
        });

        teste("Consumo preserva identificador depois da mudanca", () -> {
            Hotel h = hotelComServico();
            Hospede a = hospede("123");
            h.realizarCheckin(0, 0, a);
            h.registrarConsumo(0, 0, 0, 1);
            String antes = h.getConsumos().get(0)
                .getIdentificadorHospedagem();
            h.transferirHospede(0, 0, 0, 4);
            ok(antes.equals(
                h.getConsumos().get(0).getIdentificadorHospedagem()),
                "ID do consumo mudou");
        });

        teste("Registrar dois servicos soma corretamente", () -> {
            Hotel h = hotelComServico();
            h.cadastrarServico("Agua", 5f);
            h.realizarCheckin(0, 0, hospede("123"));
            h.registrarConsumo(0, 0, 0, 2);
            h.registrarConsumo(0, 0, 1, 3);
            valor(40f,
                h.emitirFatura(0, 0, 1).getTotalConsumos());
        });

        teste("Fatura guarda dias e apartamento", () -> {
            Apartamento a = new ApartamentoSimples();
            Fatura f = new Fatura(
                a, 3, Collections.emptyList());
            ok(f.getApartamento() == a && f.getDias() == 3,
                "Dados da fatura errados");
        });

        teste("Fatura sem servicos tem total apenas diarias", () -> {
            Fatura f = new Fatura(
                new ApartamentoSimples(),
                3, Collections.emptyList());
            valor(450f, f.getTotalGeral());
            valor(0f, f.getTotalConsumos());
        });

        teste("Fatura possui copia da lista de consumo", () -> {
            Apartamento a = new ApartamentoSimples();
            java.util.ArrayList<Consumo> lista =
                new java.util.ArrayList<>();
            lista.add(new Consumo(
                a, new Servico("Cafe", 10f), 1));
            Fatura f = new Fatura(a, 1, lista);
            lista.clear();
            ok(f.getConsumos().size() == 1,
                "Fatura depende de lista externa");
        });

        teste("Fatura impede alterar lista retornada", () -> {
            Fatura f = new Fatura(
                new ApartamentoSimples(),
                1, Collections.emptyList());
            erro(UnsupportedOperationException.class,
                () -> f.getConsumos().add(
                    new Consumo(
                        new ApartamentoSimples(),
                        new Servico("Cafe", 10f), 1)));
        });

        teste("Fatura apresenta aviso sem consumo", () -> {
            Fatura f = new Fatura(
                new ApartamentoSimples(),
                1, Collections.emptyList());
            ok(f.toString().contains("Nenhum consumo"),
                "Aviso ausente");
        });

        teste("Transferencia entre andares atualiza reserva", () -> {
            Hotel h = new Hotel();
            Hospede a = hospede("123");
            h.reservarApartamento(0, 0, a);
            ok(h.transferirHospede(0, 0, 2, 4)
                && a.getIdentificadorReserva().endsWith("Q0305"),
                "Transferencia entre andares falhou");
        });

        teste("Transferencia nao duplica consumos", () -> {
            Hotel h = hotelComServico();
            h.realizarCheckin(0, 0, hospede("123"));
            h.registrarConsumo(0, 0, 0, 2);
            h.transferirHospede(0, 0, 0, 4);
            h.transferirHospede(0, 4, 1, 4);
            ok(h.getConsumosDoApartamento(1, 4).size() == 1,
                "Consumo duplicado");
            valor(25f,
                h.emitirFatura(1, 4, 1).getTotalConsumos());
        });

        teste("Finalizar fatura impede cobranca para proximo hospede", () -> {
            Hotel h = hotelComServico();
            h.realizarCheckin(0, 0, hospede("123"));
            h.registrarConsumo(0, 0, 0, 2);
            Fatura f = h.finalizarHospedagem(0, 0, 1);
            h.realizarCheckin(0, 0, hospede("456"));
            valor(175f, f.getTotalGeral());
            valor(150f,
                h.emitirFatura(0, 0, 1).getTotalGeral());
        });

        System.out.printf(
            "%nRESULTADO: %d/%d testes passaram%n",
            passou, total
        );

        if (passou != total) {
            System.exit(1);
        }
    }
}