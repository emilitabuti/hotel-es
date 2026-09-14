package hotel.apresentacao;

import hotel.modelo.*;
import hotel.negocio.Hotel;
public class HotelTest {
    private static int passou = 0;
    private static int total = 0;

    public static void main(String[] args) {
        // Exemplo:
        // testarReservarAptoLivre();
        // testarReservarAptoOcupadoFalha();
        // ...

        System.out.println(passou + "/" + total + " testes passaram");
    }

    // Exemplo de metodo de teste:
    // static void testarReservarAptoLivre() {
    //     total++;
    //     Hotel hotel = new Hotel();
    //     Hospede h = new Hospede("123", "Joao", "Rua X", "9999", "joao@x");
    //     if (hotel.reservarApartamento(0, 0, h) && hotel.getApartamento(0, 0).estaReservado()) {
    //         passou++;
    //     } else {
    //         System.out.println("FALHOU: testarReservarAptoLivre");
    //     }
    // }
}
