package Model;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import java.math.BigDecimal;
import java.sql.Date;

public class Despesas {

	private BigDecimal  valor;
	private String descricao;
	private Date data;
	private int id;
	private Categoria categoria;
	private String pagamento;

	public Despesas(){

	}
	public Despesas(int id, BigDecimal valor, String descricao, Categoria categoria, Date data, String pagamento) {
		this.id = id;
		this.valor = valor;
		this.descricao = descricao;
		this.categoria = categoria;
		this.data = data;
		this.pagamento = pagamento;
	}

	public Categoria getCategoria() {
		return categoria;
	}
	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	public BigDecimal  getValor() {
		return valor;
	}
	public void setValor(BigDecimal  valor) {
		this.valor = valor;
	}
	public String getDescricao(){
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getPagamento(){
		return pagamento;
	}
	public void setPagamento(String pagamento) {
		this.pagamento = pagamento;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "==== Gasto ====" +
				"\nID: " + id +
				"\nValor: " + valor +
				"\nDescricao: " + descricao +
				"\nCategoria: " + categoria +
				"\nData do Gasto: " + data +
				"\nForma de pagamento: " + pagamento;
	}
}
