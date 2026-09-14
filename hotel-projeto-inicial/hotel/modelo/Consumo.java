package hotel.modelo;

public class Consumo {
    private Apartamento apartamento;
    private Servico servico;
    private int quantidade;

    public Consumo(Apartamento apto, Servico servico, int quantidade) {
        this.apartamento = apto;
        this.servico = servico;
        this.quantidade = quantidade;
    }

    public Apartamento getApartamento() { return apartamento; }
    public Servico getServico() { return servico; }
    public int getQuantidade() { return quantidade; }

    public float getTotal() {
        return servico.getPreco() * quantidade;
    }

    @Override
    public String toString() {
        return servico.getNome() + " x" + quantidade + " = R$ " + getTotal();
    }
}
