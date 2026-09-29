package hotel.modelo;

public class ApartamentoPremium extends Apartamento {
    private static final float PRECO_DIARIA = 350.0f;

    /**
     * Cria um apartamento premium inicialmente livre.
     *
     * @pre Nenhuma
     * @post Apartamento premium criado com status LIVRE
     */
    public ApartamentoPremium() {
        super();
    }

    /**
     * Retorna o preco da diaria premium.
     *
     * @return preco de R$ 350,00
     * @pre Apartamento criado
     * @post Estado do apartamento nao e alterado
     */
    @Override
    public float getPrecoDiaria() {
        return PRECO_DIARIA;
    }
}
