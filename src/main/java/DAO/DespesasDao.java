package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Categoria;
import Model.Despesas;

public class DespesasDao {


	public static List<Despesas> getAll() {
	    List<Despesas> gastos = new ArrayList<>();
	    
	    // Altere esta linha para fazer o JOIN
	    String sql = "SELECT d.*, c.nome_categoria FROM tb_despesas d JOIN tb_categorias c ON d.id_categoria = c.id_categoria";
	    
	    try (Connection con = Conexao.getConexao();
	         PreparedStatement stm = con.prepareStatement(sql);
	         ResultSet rs = stm.executeQuery()) {

	        while (rs.next()) {
	            Despesas d = new Despesas();
	            d.setId(rs.getInt("id_despesa"));
	            d.setValor(rs.getBigDecimal("valor"));
	            d.setDescricao(rs.getString("descricao"));
	            d.setData(rs.getDate("data_despesa"));
	            d.setPagamento(rs.getString("pagamento"));
	            
	            // Passo 2: Crie o objeto Categoria e preencha-o
	            Categoria categoria = new Categoria();
	            categoria.setIdCategoria(rs.getInt("id_categoria"));
	            categoria.setNomeCategoria(rs.getString("nome_categoria"));
	            
	            // Passo 3: Associe a categoria à despesa
	            d.setCategoria(categoria);
	            
	            gastos.add(d);
	        }

	    } catch (SQLException e) {
	        System.err.println("Erro ao listar despesas: " + e.getMessage());
	    }

	    return gastos;
	}


    public static Despesas getById(int id) {
        String sql = "SELECT * FROM tb_despesas WHERE id_despesa = ?";

        try (PreparedStatement stm = Conexao.getConexao().prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                Despesas d = new Despesas();
                d.setId(rs.getInt("id_despesa"));
                d.setValor(rs.getBigDecimal("valor"));
                d.setDescricao(rs.getString("descricao"));
                d.setData(rs.getDate("data_despesa"));
                d.setPagamento(rs.getString("pagamento"));

                // Setar categoria manualmente
                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("id_categoria"));
                d.setCategoria(cat);

                return d;
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao buscar despesa por ID: " + e.getMessage());
            return null;
        }
    }



    public static boolean alterar(Despesas despesas) {
        String sqlAtualizarDespesa = "UPDATE tb_despesas SET valor = ?, descricao = ?, data_despesa = ?, pagamento = ?, id_categoria = ? WHERE id_despesa = ?";
        String sqlAtualizarSaldo = "UPDATE tb_saldo SET saldo_final = saldo_final + ? WHERE id = 1";

        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            // 1. Buscar valor antigo
            Despesas despesaAntiga = getById(despesas.getId());
            if (despesaAntiga == null) {
                System.err.println("❌ Despesa com ID " + despesas.getId() + " não encontrada.");
                return false;
            }

            // 2. Calcular diferença (novo - antigo)
            java.math.BigDecimal diferenca = despesas.getValor().subtract(despesaAntiga.getValor());

            // 3. Atualizar a despesa
            try (PreparedStatement stmDespesa = con.prepareStatement(sqlAtualizarDespesa);
                 PreparedStatement stmSaldo = con.prepareStatement(sqlAtualizarSaldo)) {

                stmDespesa.setBigDecimal(1, despesas.getValor());
                stmDespesa.setString(2, despesas.getDescricao());
                stmDespesa.setDate(3, despesas.getData());
                stmDespesa.setString(4, despesas.getPagamento());
                stmDespesa.setInt(5, despesas.getCategoria().getIdCategoria());
                stmDespesa.setInt(6, despesas.getId());
                stmDespesa.executeUpdate();

                // 4. Atualizar saldo com a diferença
                stmSaldo.setBigDecimal(1, diferenca);
                stmSaldo.executeUpdate();

                con.commit();
                return true;
            }

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            System.err.println("Erro ao alterar despesa ou atualizar saldo: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }


    public static boolean excluir(int id) {
        String sqlExcluirDespesa = "DELETE FROM tb_despesas WHERE id_despesa = ?";
        String sqlAtualizarSaldo = "UPDATE tb_saldo SET saldo_final = saldo_final - ? WHERE id = 1";

        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            Despesas despesa = getById(id);
            if (despesa == null) {
                System.err.println("❌ Despesa com ID " + id + " não encontrada.");
                return false;
            }

            try (PreparedStatement stmSaldo = con.prepareStatement(sqlAtualizarSaldo);
                 PreparedStatement stmExcluir = con.prepareStatement(sqlExcluirDespesa)) {

                stmSaldo.setBigDecimal(1, despesa.getValor());
                stmSaldo.executeUpdate();

                stmExcluir.setInt(1, id);
                stmExcluir.executeUpdate();

                con.commit();
                return true;
            }
        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            System.err.println("Erro ao excluir despesa ou atualizar saldo: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }


    public static Despesas inserir(Despesas despesas) {
        String sqlInserirDespesa = "INSERT INTO tb_despesas (valor, descricao, data_despesa, pagamento, id_categoria) VALUES (?, ?, ?, ?, ?)";
        String sqlAtualizarSaldo = "UPDATE tb_saldo SET saldo_final = saldo_final + ? WHERE id = 1";

        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            try (PreparedStatement stmDespesa = con.prepareStatement(sqlInserirDespesa);
                 PreparedStatement stmSaldo = con.prepareStatement(sqlAtualizarSaldo)) {

                stmDespesa.setBigDecimal(1, despesas.getValor());
                stmDespesa.setString(2, despesas.getDescricao());
                stmDespesa.setDate(3, despesas.getData());
                stmDespesa.setString(4, despesas.getPagamento());
                stmDespesa.setInt(5, despesas.getCategoria().getIdCategoria());

                stmDespesa.executeUpdate();

                stmSaldo.setBigDecimal(1, despesas.getValor());
                stmSaldo.executeUpdate();

                con.commit();
                return despesas;
            }
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) { /* ignore */ }
            System.err.println("Erro ao inserir despesa ou atualizar saldo: " + e.getMessage());
            return null;
        } finally {
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
    
    public static Despesas inserirTest(Despesas despesas) {
        String sqlInserirDespesa = "INSERT INTO tb_despesas (id_despesa, valor, descricao, data_despesa, pagamento, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlAtualizarSaldo = "UPDATE tb_saldo SET saldo_final = saldo_final + ? WHERE id = 1";

        Connection con = null;
        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            try (PreparedStatement stmDespesa = con.prepareStatement(sqlInserirDespesa);
                 PreparedStatement stmSaldo = con.prepareStatement(sqlAtualizarSaldo)) {

            	stmDespesa.setInt(1, despesas.getId());
                stmDespesa.setBigDecimal(2, despesas.getValor());
                stmDespesa.setString(3, despesas.getDescricao());
                stmDespesa.setDate(4, despesas.getData());
                stmDespesa.setString(5, despesas.getPagamento());
                stmDespesa.setInt(6, despesas.getCategoria().getIdCategoria());

                stmDespesa.executeUpdate();

                stmSaldo.setBigDecimal(1, despesas.getValor());
                stmSaldo.executeUpdate();

                con.commit();
                return despesas;
            }
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) { /* ignore */ }
            System.err.println("Erro ao inserir despesa ou atualizar saldo: " + e.getMessage());
            return null;
        } finally {
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
    



}
