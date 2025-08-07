package Model;

import java.math.BigDecimal;
import java.sql.Date;

public class Receita {
    private int id;
    private BigDecimal valorRecebido;
    private String descricaoReceita;
    private Date dataReceita;
    private String pagamento;
    private Categoria categoria;

    public Receita() {
    }

    public Receita(int id, BigDecimal valorRecebido, String descricaoReceita, Date dataReceita, String pagamento, Categoria categoria) {
        this.id = id;
        this.valorRecebido = valorRecebido;
        this.descricaoReceita = descricaoReceita;
        this.dataReceita = dataReceita;
        this.pagamento = pagamento;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getValorRecebido() {
        return valorRecebido;
    }

    public void setValorRecebido(BigDecimal valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public String getDescricaoReceita() {
        return descricaoReceita;
    }

    public void setDescricaoReceita(String descricaoReceita) {
        this.descricaoReceita = descricaoReceita;
    }

    public Date getDataReceita() {
        return dataReceita;
    }

    public void setDataReceita(Date dataReceita) {
        this.dataReceita = dataReceita;
    }

    public String getPagamento() {
        return pagamento;
    }

    public void setPagamento(String pagamento) {
        this.pagamento = pagamento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
