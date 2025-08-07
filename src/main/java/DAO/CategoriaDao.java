package DAO;

import Model.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDao{

    // Usar a conexão da classe Conexao
    private static Connection getConnection() throws SQLException {
        return Conexao.getConexao();
    }

    // Inserir nova categoria
    public static boolean inserir(Categoria categoria) {
        String sql = "INSERT INTO tb_categorias (nome_categoria) VALUES (?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNomeCategoria());
            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    categoria.setIdCategoria(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir categoria: " + e.getMessage());
        }
        return false;
    }

    // Buscar categoria por ID
    public Categoria getById(int id) {
        String sql = "SELECT * FROM tb_categorias WHERE id_categoria = ?";
        Categoria categoria = null;

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNomeCategoria(rs.getString("nome_categoria"));
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar categoria: " + e.getMessage());
        }

        return categoria;
    }

    // Listar todas as categorias
    public static List<Categoria> getAll() {
        String sql = "SELECT * FROM tb_categorias ORDER BY nome_categoria";
        List<Categoria> categorias = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNomeCategoria(rs.getString("nome_categoria"));
                categorias.add(categoria);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar categorias: " + e.getMessage());
        }

        return categorias;
    }

    // Alterar categoria
    public boolean alterar(Categoria categoria) {
        String sql = "UPDATE tb_categorias SET nome_categoria = ? WHERE id_categoria = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, categoria.getNomeCategoria());
            ps.setInt(2, categoria.getIdCategoria());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao alterar categoria: " + e.getMessage());
        }

        return false;
    }

    // Deletar categoria
    public boolean excluir(int id) {
        String sql = "DELETE FROM tb_categorias WHERE id_categoria = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar categoria: " + e.getMessage());
        }

        return false;
    }
}

