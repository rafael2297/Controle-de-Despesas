package DAO;

import Model.Saldo;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaldoDAO {

    private Connection conn;

    public SaldoDAO() {
        try {
            this.conn = Conexao.getConexao();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // INSERIR saldo_inicial (e cria linha com saldo_final como NULL)
    public boolean inserirSaldoInicial(float saldoInicial) {
        String sql = "INSERT INTO tb_saldo (saldo_inicial, saldo_final) VALUES (?, NULL)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setFloat(1, saldoInicial);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir saldo inicial: " + e.getMessage());
            return false;
        }
    }

    // INSERIR saldo_final (e cria linha com saldo_inicial como NULL)
    public boolean inserirSaldoFinal(float saldoFinal) {
        String sql = "INSERT INTO tb_saldo (saldo_inicial, saldo_final) VALUES (NULL, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setFloat(1, saldoFinal);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir saldo final: " + e.getMessage());
            return false;
        }
    }

    // BUSCAR saldo_inicial por ID
    public BigDecimal buscarSaldoInicial(int id) {
        String sql = "SELECT saldo_inicial FROM tb_saldo WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getBigDecimal("saldo_inicial");
        } catch (SQLException e) {
            System.out.println("Erro ao buscar saldo inicial: " + e.getMessage());
        }
        return null;
    }

    // BUSCAR saldo_final por ID
    public BigDecimal buscarSaldoFinal(int id) {
        String sql = "SELECT saldo_final FROM tb_saldo WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getBigDecimal("saldo_final");
        } catch (SQLException e) {
            System.out.println("Erro ao buscar saldo final: " + e.getMessage());
        }
        return null;
    }

    // ATUALIZAR saldo_inicial
    public boolean atualizarSaldoInicial(int id, float novoSaldoInicial) {
        String sql = "UPDATE tb_saldo SET saldo_inicial = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setFloat(1, novoSaldoInicial);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar saldo inicial: " + e.getMessage());
            return false;
        }
    }

    // ATUALIZAR saldo_final
    public boolean atualizarSaldoFinal(int id, float novoSaldoFinal) {
        String sql = "UPDATE tb_saldo SET saldo_final = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setFloat(1, novoSaldoFinal);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar saldo final: " + e.getMessage());
            return false;
        }
    }

    // "DELETAR" saldo_inicial (seta como zero)
    public boolean deletarSaldoInicial(int id) {
        String sql = "UPDATE tb_saldo SET saldo_inicial = 0 WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            System.out.println("Linhas afetadas no deletarSaldoInicial: " + linhasAfetadas);
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao zerar saldo inicial: " + e.getMessage());
            return false;
        }
    }



    // "DELETAR" saldo_final (seta como NULL)
    public boolean deletarSaldoFinal(int id) {
        String sql = "UPDATE tb_saldo SET saldo_final = NULL WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar saldo final: " + e.getMessage());
            return false;
        }
    }

    // DELETAR LINHA COMPLETA
    public boolean deletarSaldoPorId(int id) {
        String sql = "DELETE FROM tb_saldo WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao deletar saldo: " + e.getMessage());
            return false;
        }
    }

    // LISTAR TODOS
    public List<Saldo> listarTodos() {
        List<Saldo> saldos = new ArrayList<>();
        String sql = "SELECT * FROM tb_saldo";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Saldo saldo = new Saldo(rs.getInt("id"), rs.getBigDecimal("saldo_inicial"), rs.getBigDecimal("saldo_final"));
                saldos.add(saldo);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar saldos: " + e.getMessage());
        }

        return saldos;
    }

    public boolean definirOuAtualizarSaldoInicial(BigDecimal saldoInicial) {
        String selectSql = "SELECT id FROM tb_saldo WHERE id = 1";
        try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                // Já existe linha: atualizar ambos os campos
                String updateSql = "UPDATE tb_saldo SET saldo_inicial = ?, saldo_final = ? WHERE id = 1";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setBigDecimal(1, saldoInicial);
                    updateStmt.setBigDecimal(2, saldoInicial);
                    return updateStmt.executeUpdate() > 0;
                }
            } else {
                // Não existe linha: inserir nova
                String insertSql = "INSERT INTO tb_saldo (id, saldo_inicial, saldo_final) VALUES (1, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setBigDecimal(1, saldoInicial);
                    insertStmt.setBigDecimal(2, saldoInicial);
                    return insertStmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao definir ou atualizar saldo inicial: " + e.getMessage());
            return false;
        }
    }


}
