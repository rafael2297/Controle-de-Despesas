package DAO;

import Model.Categoria;
import Model.Receita;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceitaDao {

    public static List<Receita> getAll() {
        List<Receita> receitas = new ArrayList<>();
        String sql = "SELECT * FROM tb_receita";

        try (Connection con = Conexao.getConexao();
             PreparedStatement stm = con.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                Receita r = new Receita();
                r.setId(rs.getInt("id"));
                r.setValorRecebido(rs.getBigDecimal("valor_recebido"));
                r.setDescricaoReceita(rs.getString("descricao_receita"));
                r.setDataReceita(rs.getDate("data_receita"));
                r.setPagamento(rs.getString("pagamento"));

                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                r.setCategoria(categoria);

                receitas.add(r);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar receitas: " + e.getMessage());
        }

        return receitas;
    }

    public static Receita getById(int id) {
        String sql = "SELECT * FROM tb_receita WHERE id = ?";

        try (PreparedStatement stm = Conexao.getConexao().prepareStatement(sql)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                Receita r = new Receita();
                r.setId(rs.getInt("id"));
                r.setValorRecebido(rs.getBigDecimal("valor_recebido"));
                r.setDescricaoReceita(rs.getString("descricao_receita"));
                r.setDataReceita(rs.getDate("data_receita"));
                r.setPagamento(rs.getString("pagamento"));

                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                r.setCategoria(categoria);

                return r;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar receita por ID: " + e.getMessage());
        }

        return null;
    }

    public static boolean alterar(Receita receita) {
        String sqlUpdate = "UPDATE tb_receita SET valor_recebido = ?, descricao_receita = ?, data_receita = ?, pagamento = ?, id_categoria = ? WHERE id = ?";
        String sqlUpdateSaldo = "UPDATE tb_saldo SET saldo_final = saldo_final + ? WHERE id = 1";

        Connection con = null;

        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            Receita antiga = getById(receita.getId());
            if (antiga == null) {
                System.err.println("❌ Receita com ID " + receita.getId() + " não encontrada.");
                return false;
            }

            java.math.BigDecimal diferenca = receita.getValorRecebido().subtract(antiga.getValorRecebido());

            try (PreparedStatement stmReceita = con.prepareStatement(sqlUpdate);
                 PreparedStatement stmSaldo = con.prepareStatement(sqlUpdateSaldo)) {

                stmReceita.setBigDecimal(1, receita.getValorRecebido());
                stmReceita.setString(2, receita.getDescricaoReceita());
                stmReceita.setDate(3, receita.getDataReceita());
                stmReceita.setString(4, receita.getPagamento());
                stmReceita.setInt(5, receita.getCategoria().getIdCategoria());
                stmReceita.setInt(6, receita.getId());
                stmReceita.executeUpdate();

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
            System.err.println("Erro ao alterar receita: " + e.getMessage());
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
        String sqlDelete = "DELETE FROM tb_receita WHERE id = ?";
        String sqlUpdateSaldo = "UPDATE tb_saldo SET saldo_final = saldo_final - ? WHERE id = 1";

        Connection con = null;

        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            Receita receita = getById(id);
            if (receita == null) {
                System.err.println("❌ Receita com ID " + id + " não encontrada.");
                return false;
            }

            try (PreparedStatement stmSaldo = con.prepareStatement(sqlUpdateSaldo);
                 PreparedStatement stmDelete = con.prepareStatement(sqlDelete)) {

                stmSaldo.setBigDecimal(1, receita.getValorRecebido());
                stmSaldo.executeUpdate();

                stmDelete.setInt(1, id);
                stmDelete.executeUpdate();

                con.commit();
                return true;
            }

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            System.err.println("Erro ao excluir receita: " + e.getMessage());
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

    public static Receita inserir(Receita receita) {
        String sqlInsert = "INSERT INTO tb_receita (valor_recebido, descricao_receita, data_receita, pagamento, id_categoria) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateSaldo = "UPDATE tb_saldo SET saldo_final = saldo_final + ? WHERE id = 1";

        Connection con = null;

        try {
            con = Conexao.getConexao();
            con.setAutoCommit(false);

            try (PreparedStatement stmReceita = con.prepareStatement(sqlInsert);
                 PreparedStatement stmSaldo = con.prepareStatement(sqlUpdateSaldo)) {

                stmReceita.setBigDecimal(1, receita.getValorRecebido());
                stmReceita.setString(2, receita.getDescricaoReceita());
                stmReceita.setDate(3, receita.getDataReceita());
                stmReceita.setString(4, receita.getPagamento());
                stmReceita.setInt(5, receita.getCategoria().getIdCategoria());

                stmReceita.executeUpdate();

                stmSaldo.setBigDecimal(1, receita.getValorRecebido());
                stmSaldo.executeUpdate();

                con.commit();
                return receita;
            }

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro ao fazer rollback: " + ex.getMessage());
            }
            System.err.println("Erro ao inserir receita: " + e.getMessage());
            return null;
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
}
