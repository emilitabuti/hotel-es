package hotel.modelo;

public class Servico {
    private String nome;
    private float preco;

    public Servico(String nome, float preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() { return nome; }
    public float getPreco() { return preco; }

    @Override
    public String toString() {
        return nome + " (R$ " + preco + ")";
    }
}
