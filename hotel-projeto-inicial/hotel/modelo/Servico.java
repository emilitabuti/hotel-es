package hotel.modelo;

public class Servico {
    private final String nome;
    private final float preco;

    /**
     * Cria um servico do cardapio.
     *
     * @param nome nome nao vazio do servico
     * @param preco preco positivo e finito
     * @throws IllegalArgumentException se o nome ou o preco forem invalidos
     */
    public Servico(String nome, float preco) {
        if (nome == null) {
            throw new IllegalArgumentException("Nome do servico nao pode ser vazio");
        }
        String nomeNormalizado = nome.trim();
        if (nomeNormalizado.isEmpty()) {
            throw new IllegalArgumentException("Nome do servico nao pode ser vazio");
        }
        if (!Float.isFinite(preco) || preco <= 0.0f) {
            throw new IllegalArgumentException("Preco do servico deve ser positivo e finito");
        }
        this.nome = nomeNormalizado;
        this.preco = preco;
    }

    /**
     * Retorna o nome do servico.
     *
     * @return nome do servico
     */
    public String getNome() { return nome; }

    /**
     * Retorna o preco do servico.
     *
     * @return preco do servico
     */
    public float getPreco() { return preco; }

    /**
     * Retorna uma representacao textual do servico.
     *
     * @return nome e preco do servico
     */
    @Override
    public String toString() {
        return nome + " (R$ " + preco + ")";
    }
}
