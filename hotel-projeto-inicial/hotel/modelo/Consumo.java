package hotel.modelo;

public class Consumo {
    private final Apartamento apartamento;
    private final Servico servico;
    private final int quantidade;

    /**
     * Cria um consumo associado a um apartamento e a um servico.
     *
     * @param apto apartamento que realizou o consumo
     * @param servico servico consumido
     * @param quantidade quantidade consumida, maior que zero
     * @throws IllegalArgumentException se apartamento, servico ou quantidade forem invalidos
     */
    public Consumo(Apartamento apto, Servico servico, int quantidade) {
        if (apto == null) {
            throw new IllegalArgumentException("Apartamento nao pode ser nulo");
        }
        if (servico == null) {
            throw new IllegalArgumentException("Servico nao pode ser nulo");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        this.apartamento = apto;
        this.servico = servico;
        this.quantidade = quantidade;
    }

    /**
     * Retorna o apartamento associado ao consumo.
     *
     * @return apartamento do consumo
     */
    public Apartamento getApartamento() { return apartamento; }

    /**
     * Retorna o servico consumido.
     *
     * @return servico do consumo
     */
    public Servico getServico() { return servico; }

    /**
     * Retorna a quantidade consumida.
     *
     * @return quantidade do consumo
     */
    public int getQuantidade() { return quantidade; }

    /**
     * Calcula o valor total deste consumo.
     *
     * @return preco do servico multiplicado pela quantidade
     */
    public float getTotal() {
        return servico.getPreco() * quantidade;
    }

    /**
     * Retorna uma representacao textual do consumo.
     *
     * @return nome, quantidade e total do consumo
     */
    @Override
    public String toString() {
        return servico.getNome() + " x" + quantidade + " = R$ " + getTotal();
    }
}
