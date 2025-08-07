package Test;

import DAO.RelatorioDao;
import Model.Relatorio;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de testes para verificar o funcionamento da classe RelatorioDao,
 * utilizando um banco de dados real.
 *
 * Pré-requisitos:
 * - Banco de dados acessível via Conexao.getConexao()
 * - Tabelas: tb_despesas, tb_receita, tb_categorias
 * - Dados de teste previamente inseridos
 */

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RelatorioDaoTest {

    private static RelatorioDao relatorioDao;

    /**
     * Inicializa o objeto RelatorioDao antes de qualquer teste ser executado.
     * Esse metodo é executado uma única vez, antes de todos os testes (@BeforeAll).
     */

    @BeforeAll
    static void setup() {
        relatorioDao = new RelatorioDao();

    }

    /**
     * Testa o metodo listarTodosRelatorios.
     *
     * Verifica se a lista retornada não é nula e contém pelo menos um relatório.
     * Esse teste garante que os dados estão sendo recuperados corretamente da base
     * unindo despesas e receitas.
     */

    @Test
    @Order(1)
    void testListarTodosRelatorios() {
        List<Relatorio> relatorios = relatorioDao.listarTodosRelatorios();
        assertNotNull(relatorios);
        assertTrue(relatorios.size() > 0, "Deveria haver pelo menos um relatório na base");
    }

    /**
     * Testa o metodo listarRelatoriosPorPeriodo.
     *
     * Verifica se os relatórios retornados estão dentro do intervalo de datas especificado.
     * As datas devem ser ajustadas conforme os dados existentes no banco real.
     */


    @Test
    @Order(2)
    void testListarRelatoriosPorPeriodo() {
        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fim = LocalDate.of(2024, 12, 31);

        List<Relatorio> relatorios = relatorioDao.listarRelatoriosPorPeriodo(inicio, fim);
        assertNotNull(relatorios);
        relatorios.forEach(r -> {
            assertTrue(!r.getData().toLocalDate().isBefore(inicio));
            assertTrue(!r.getData().toLocalDate().isAfter(fim));
        });
    }

    /**
     * Testa o metodo listarRelatoriosMensal.
     *
     * Garante que todas as despesas retornadas estão dentro do mês especificado.
     * O mês deve corresponder a dados existentes para que o teste passe.
     */


    @Test
    @Order(3)
    void testListarRelatoriosMensal() {
        int mes = 8; // Agosto
        List<Relatorio> relatorios = relatorioDao.listarRelatoriosMensal(mes);
        assertNotNull(relatorios);
        relatorios.forEach(r -> assertEquals(mes, r.getData().toLocalDate().getMonthValue()));
    }

    /**
     * Testa o metodo listarRelatoriosPorCategorias.
     *
     * Usa uma lista de categorias para filtrar os dados no banco.
     * Verifica se todos os relatórios retornados pertencem a uma das categorias fornecidas.
     */


    @Test
    @Order(4)
    void testListarRelatoriosPorCategorias() {
        List<String> categorias = Arrays.asList("Transporte", "Alimentação");

        List<Relatorio> relatorios = relatorioDao.listarRelatoriosPorCategorias(categorias);
        assertNotNull(relatorios);
        relatorios.forEach(r -> assertTrue(categorias.contains(r.getNomeCategoria())));
    }
}
