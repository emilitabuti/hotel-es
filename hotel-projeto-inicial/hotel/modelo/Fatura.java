package hotel.modelo;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Fatura {
    private final Apartamento apartamento;
    private final int dias;
    private final List<Consumo> consumos;
    private final float precoDiaria;
    private final float totalDiarias;
    private final float totalConsumos;

    /**
     * Cria uma fatura para um apartamento e um periodo de hospedagem.
     *
     * @param apto apartamento faturado
     * @param dias quantidade de diarias, maior que zero
     * @param consumos consumos associados a fatura
     * @throws IllegalArgumentException se apartamento, periodo ou consumos forem invalidos
     */
    public Fatura(Apartamento apto, int dias, List<Consumo> consumos) {
        if (apto == null) {
            throw new IllegalArgumentException("Apartamento nao pode ser nulo");
        }
        if (dias <= 0) {
            throw new IllegalArgumentException("Quantidade de dias deve ser maior que zero");
        }
        if (consumos == null) {
            throw new IllegalArgumentException("Lista de consumos nao pode ser nula");
        }
        if (consumos.contains(null)) {
            throw new IllegalArgumentException("Consumo nao pode ser nulo");
        }
        this.apartamento = apto;
        this.dias = dias;
        this.consumos = new ArrayList<>(consumos);
        this.precoDiaria = apto.getPrecoDiaria();
        this.totalDiarias = calcularTotalDiarias();
        this.totalConsumos = calcularTotalConsumos();
    }

    /**
     * Calcula o valor total da fatura.
     *
     * @return soma das diarias e dos consumos
     */
    public float getTotalGeral() {
        return getTotalDiarias() + getTotalConsumos();
    }

    /**
     * Retorna o apartamento faturado.
     *
     * @return apartamento associado a fatura
     */
    public Apartamento getApartamento() { return apartamento; }

    /**
     * Retorna a quantidade de diarias faturadas.
     *
     * @return numero de dias da hospedagem
     */
    public int getDias() { return dias; }

    /**
     * Retorna os consumos incluidos na fatura.
     *
     * @return lista nao modificavel dos consumos faturados
     */
    public List<Consumo> getConsumos() {
        return Collections.unmodifiableList(consumos);
    }

    /**
     * Calcula o total das diarias.
     *
     * @return preco da diaria multiplicado pela quantidade de dias
     */
    public float getTotalDiarias() {
        return totalDiarias;
    }

    private float calcularTotalDiarias() {
        return dias * precoDiaria;
    }

    /**
     * Calcula o total dos consumos incluidos na fatura.
     *
     * @return soma dos valores dos consumos
     */
    public float getTotalConsumos() {
        return totalConsumos;
    }

    private float calcularTotalConsumos() {
        float total = 0.0f;
        for (Consumo consumo : consumos) {
            total += consumo.getTotal();
        }
        return total;
    }

    /**
     * Retorna uma representacao textual da fatura.
     *
     * @return total geral da fatura
     */
    @Override
    public String toString() {
        String separador = System.lineSeparator();
        StringBuilder fatura = new StringBuilder(separador)
                .append("========== FATURA ==========").append(separador);
        fatura.append(String.format(java.util.Locale.forLanguageTag("pt-BR"),
                "Diarias: %d x R$ %.2f = R$ %.2f%n",
                dias, precoDiaria, totalDiarias));
        fatura.append("Servicos consumidos:").append(separador);
        if (consumos.isEmpty()) {
            fatura.append("  Nenhum consumo registrado.").append(separador);
        } else {
            for (Consumo consumo : consumos) {
                fatura.append("  ").append(consumo).append(separador);
            }
        }
        fatura.append(String.format(java.util.Locale.forLanguageTag("pt-BR"),
                "Subtotal de servicos: R$ %.2f%n", totalConsumos));
        fatura.append(String.format(java.util.Locale.forLanguageTag("pt-BR"),
                "TOTAL GERAL: R$ %.2f%n", getTotalGeral()));
        fatura.append("============================");
        return fatura.toString();
    }
}
