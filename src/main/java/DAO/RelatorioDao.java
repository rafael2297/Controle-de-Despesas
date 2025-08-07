package DAO;

import Model.Relatorio;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioDao {

    public List<Relatorio> listarTodosRelatorios() {
        List<Relatorio> relatorios = new ArrayList<>();
        String sql = """
                    SELECT 
                        d.id_despesa AS id,
                        d.valor AS valor,
                        d.data_despesa AS data,
                        d.descricao AS descricao,
                        d.pagamento AS pagamento,
                        c.id_categoria,
                        c.nome_categoria
                    FROM tb_despesas d
                    JOIN tb_categorias c ON d.id_categoria = c.id_categoria
                
                    UNION ALL
                
                    SELECT
                        r.id AS id,
                        r.valor_recebido AS valor,
                        r.data_receita AS data,
                        r.descricao_receita AS descricao,
                        r.pagamento AS pagamento,
                        c.id_categoria,
                        c.nome_categoria
                    FROM tb_receita r
                    LEFT JOIN tb_categorias c ON r.id_categoria = c.id_categoria
                
                    ORDER BY data ASC
                """;


        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Relatorio r = new Relatorio();
                r.setId(rs.getInt("id"));
                r.setValor(rs.getBigDecimal("valor")); // Pode ser negativo (despesa) ou positivo (receita)
                r.setData(rs.getDate("data"));
                r.setDescricao(rs.getString("descricao"));
                r.setPagamento(rs.getString("pagamento"));
                r.setIdCategoria(rs.getInt("id_categoria"));
                r.setNomeCategoria(rs.getString("nome_categoria"));
                relatorios.add(r);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os relatórios: " + e.getMessage());
        }

        return relatorios;
    }


    public List<Relatorio> listarRelatoriosPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        List<Relatorio> relatorios = new ArrayList<>();

        String sql = """
                    SELECT d.id_despesa, d.valor, d.data_despesa, d.Descricao, d.pagamento,
                           c.id_categoria, c.nome_categoria
                    FROM tb_despesas d
                    JOIN tb_categorias c ON d.id_categoria = c.id_categoria
                    WHERE d.data_despesa BETWEEN ? AND ?
                    ORDER BY d.data_despesa ASC
                """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(dataFim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Relatorio r = new Relatorio();
                    r.setId(rs.getInt("id_despesa"));
                    r.setValor(rs.getBigDecimal("valor"));
                    r.setData(rs.getDate("data_despesa"));
                    r.setDescricao(rs.getString("Descricao"));
                    r.setPagamento(rs.getString("pagamento"));
                    r.setIdCategoria(rs.getInt("id_categoria"));
                    r.setNomeCategoria(rs.getString("nome_categoria"));

                    relatorios.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar relatório por período: " + e.getMessage());
        }

        return relatorios;
    }

    public List<Relatorio> listarRelatoriosMensal(int mes) {
        List<Relatorio> relatorios = new ArrayList<>();
        String sql = """
                    SELECT d.id_despesa, d.valor, d.data_despesa, d.Descricao, d.pagamento,
                           c.id_categoria, c.nome_categoria
                    FROM tb_despesas d
                    JOIN tb_categorias c ON d.id_categoria = c.id_categoria
                    WHERE MONTH(d.data_despesa) = ?
                    ORDER BY d.data_despesa ASC
                """;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mes);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Relatorio r = new Relatorio();
                    r.setId(rs.getInt("id_despesa"));
                    r.setValor(rs.getBigDecimal("valor"));
                    r.setData(rs.getDate("data_despesa"));
                    r.setDescricao(rs.getString("Descricao"));
                    r.setPagamento(rs.getString("pagamento"));
                    r.setIdCategoria(rs.getInt("id_categoria"));
                    r.setNomeCategoria(rs.getString("nome_categoria"));
                    relatorios.add(r);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar relatório mensal: " + e.getMessage());
        }

        return relatorios;
    }

    public List<Relatorio> listarRelatoriosPorCategorias(List<String> categorias) {
        List<Relatorio> relatorios = new ArrayList<>();

        if (categorias == null || categorias.isEmpty()) {
            System.out.println("Nenhuma categoria fornecida para busca.");
            return relatorios;
        }

        List<String> categoriasParaBusca = categorias.stream()
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toList());

        String placeholders = categoriasParaBusca.stream()
                .map(c -> "?")
                .collect(Collectors.joining(", "));

        String sql = """
                    SELECT d.id_despesa, d.valor, d.data_despesa, d.Descricao, d.pagamento,
                           c.id_categoria, c.nome_categoria
                    FROM tb_despesas d
                    JOIN tb_categorias c ON d.id_categoria = c.id_categoria
                    WHERE c.nome_categoria IN (%s)
                    ORDER BY d.data_despesa ASC
                """.formatted(placeholders);

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < categorias.size(); i++) {
                stmt.setString(i + 1, categorias.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Relatorio r = new Relatorio();
                    r.setId(rs.getInt("id_despesa"));
                    r.setValor(rs.getBigDecimal("valor"));
                    r.setData(rs.getDate("data_despesa"));
                    r.setDescricao(rs.getString("Descricao"));
                    r.setPagamento(rs.getString("pagamento"));
                    r.setIdCategoria(rs.getInt("id_categoria"));
                    r.setNomeCategoria(rs.getString("nome_categoria"));

                    relatorios.add(r);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar relatórios por categorias: " + e.getMessage());
        }

        return relatorios;
    }
}



