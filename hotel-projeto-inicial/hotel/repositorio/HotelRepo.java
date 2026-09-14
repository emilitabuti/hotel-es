package hotel.repositorio;

import hotel.negocio.Hotel;
import java.io.*;

public class HotelRepo {
    private static final String ARQUIVO = "hotel.dat";

    public static void salvar(Hotel hotel) throws IOException {
        throw new UnsupportedOperationException("Implementar salvar");
    }

    public static Hotel carregar() throws IOException, ClassNotFoundException {
        throw new UnsupportedOperationException("Implementar carregar");
    }
}
