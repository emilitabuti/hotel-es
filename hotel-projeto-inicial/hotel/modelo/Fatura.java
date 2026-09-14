package hotel.modelo;

import java.util.List;

public class Fatura {
    private Apartamento apartamento;
    private int dias;
    private List<Consumo> consumos;

    public Fatura(Apartamento apto, int dias, List<Consumo> consumos) {
        this.apartamento = apto;
        this.dias = dias;
        this.consumos = consumos;
    }

    public float getTotalGeral() {
        throw new UnsupportedOperationException("Implementar calculo da fatura");
    }

    public Apartamento getApartamento() { return apartamento; }
    public int getDias() { return dias; }
    public List<Consumo> getConsumos() { return consumos; }

    public float getTotalDiarias() {
        throw new UnsupportedOperationException("Implementar calculo total de diarias");
    }

    public float getTotalConsumos() {
        throw new UnsupportedOperationException("Implementar calculo total de consumos");
    }

    @Override
    public String toString() {
        return "Fatura: R$ " + getTotalGeral();
    }
}
