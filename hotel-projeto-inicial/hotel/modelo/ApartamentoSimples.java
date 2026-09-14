package hotel.modelo;

public class ApartamentoSimples extends Apartamento {
    private static final float PRECO_DIARIA = 150.0f;

    public ApartamentoSimples() {
        super();
    }

    @Override
    public float getPrecoDiaria() {
        return PRECO_DIARIA;
    }
}
