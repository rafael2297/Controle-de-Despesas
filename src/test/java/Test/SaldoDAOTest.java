package Test;

import DAO.SaldoDAO;
import Model.Saldo;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Define que os testes serão executados em uma ordem específica
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SaldoDAOTest {

    private static SaldoDAO saldoDAO;

    // Inicializa o DAO uma única vez antes de todos os testes
    @BeforeAll
    static void setup() {
        saldoDAO = new SaldoDAO();
    }

    @Test
    @Order(1)
    void testDefinirOuAtualizarSaldoInicial() {
        // Testa a criação ou atualização do saldo inicial com id = 1
        // O método insere saldo_inicial e saldo_final com o mesmo valor
        boolean sucesso = saldoDAO.definirOuAtualizarSaldoInicial(new BigDecimal("1000.00"));
        assertTrue(sucesso, "Saldo inicial deve ser definido com sucesso");

        // Verifica se o saldo inicial foi inserido corretamente
        BigDecimal saldoInicial = saldoDAO.buscarSaldoInicial(1);
        assertEquals(new BigDecimal("1000.00"), saldoInicial.setScale(2));

        // Verifica se o saldo final também foi definido corretamente
        BigDecimal saldoFinal = saldoDAO.buscarSaldoFinal(1);
        assertEquals(new BigDecimal("1000.00"), saldoFinal.setScale(2));
    }

    @Test
    @Order(2)
    void testAtualizarSaldoFinal() {
        // Testa a atualização do saldo_final do registro com id = 1
        boolean atualizado = saldoDAO.atualizarSaldoFinal(1, 1500.00f);
        assertTrue(atualizado, "Saldo final deve ser atualizado com sucesso");

        // Confirma se o valor foi realmente atualizado no banco
        BigDecimal saldoFinal = saldoDAO.buscarSaldoFinal(1);
        assertEquals(new BigDecimal("1500.00"), saldoFinal.setScale(2));
    }

    @Test
    @Order(3)
    void testAtualizarSaldoInicial() {
        // Testa a atualização do saldo_inicial do registro com id = 1
        boolean atualizado = saldoDAO.atualizarSaldoInicial(1, 800.00f);
        assertTrue(atualizado, "Saldo inicial deve ser atualizado com sucesso");

        // Verifica se o saldo inicial foi realmente atualizado
        BigDecimal saldoInicial = saldoDAO.buscarSaldoInicial(1);
        assertEquals(new BigDecimal("800.00"), saldoInicial.setScale(2));
    }

    @Test
    @Order(4)
    void testDeletarSaldoInicial() {
        // Testa a deleção lógica do saldo_inicial (define como 0)
        boolean deletado = saldoDAO.deletarSaldoInicial(1);
        assertTrue(deletado, "Saldo inicial deve ser zerado");

        // Verifica se o saldo foi alterado para 0 após a operação
        BigDecimal saldoInicial = saldoDAO.buscarSaldoInicial(1);
        assertEquals(new BigDecimal("0.00"), saldoInicial.setScale(2), "Saldo inicial deve ser 0 após deleção lógica");
    }

    @Test
    @Order(5)
    void testDeletarSaldoFinal() {
        // Testa a deleção lógica do saldo_final (define como NULL)
        boolean deletado = saldoDAO.deletarSaldoFinal(1);
        assertTrue(deletado, "Saldo final deve ser removido (setado como NULL)");

        // Confirma que o campo ficou NULL no banco
        BigDecimal saldoFinal = saldoDAO.buscarSaldoFinal(1);
        assertNull(saldoFinal, "Saldo final deve ser NULL após deleção lógica");
    }

    @Test
    @Order(6)
    void testListarTodos() {
        // Testa a listagem de todos os registros de saldo existentes
        List<Saldo> lista = saldoDAO.listarTodos();

        // Garante que a lista não está nula e contém ao menos um item
        assertNotNull(lista, "A lista de saldos não pode ser nula");
        assertTrue(lista.size() >= 1, "Deve haver pelo menos um saldo na tabela (id = 1)");
    }

    @Test
    @Order(7)
    void testDeletarSaldoPorId() {
        // Testa a exclusão total (física) do registro com id = 1
        boolean excluido = saldoDAO.deletarSaldoPorId(1);
        assertTrue(excluido, "Registro de saldo deve ser removido permanentemente");

        // Verifica se o campo saldo_inicial ficou NULL após remoção
        BigDecimal saldo = saldoDAO.buscarSaldoInicial(1);
        assertNull(saldo, "Saldo deve ser nulo após remoção total");
    }
}
