package hotel.modelo;

public class ApartamentoSimples extends Apartamento {
    private static final float PRECO_DIARIA = 150.0f;

    /**
     * Cria um apartamento simples inicialmente livre.
     *
     * @pre Nenhuma
     * @post Apartamento simples criado com status LIVRE
     */
    public ApartamentoSimples() {
        super();
    }

    /**
     * Retorna o preco da diaria.
     *
     * @return preco de R$ 150,00
     * @pre Apartamento criado
     * @post Estado do apartamento nao e alterado
     */
    @Override
    public float getPrecoDiaria() {
        return PRECO_DIARIA;
    }
}
