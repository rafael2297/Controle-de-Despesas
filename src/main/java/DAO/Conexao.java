package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static String URL = "jdbc:mysql://localhost:3306/controle_de_despesas"; // Substitua pelo seu banco
    private static final String USUARIO = "root";
    private static final String SENHA = "1997"; // Substitua pela sua senha

    public static Connection getConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

}
