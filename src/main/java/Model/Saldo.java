package Model;

import java.math.BigDecimal;

public class Saldo {

    private int id;
    private BigDecimal saldoInicial;
    private BigDecimal  saldoFinal;

    public Saldo() {
    }

    public Saldo(int id, BigDecimal  saldoInicial, BigDecimal  saldoFinal) {
        this.id = id;
        this.saldoInicial = saldoInicial;
        this.saldoFinal = saldoFinal;
    }

    public Saldo(int id, BigDecimal  saldoInicial) {
        this.id = id;
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal  getSaldoFinal() {
        return saldoFinal;
    }

    public void setSaldoFinal(BigDecimal  saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public BigDecimal  getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal  saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
