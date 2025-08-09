package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import DAO.ReceitaDao;
import Model.Categoria;
import Model.Receita;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class ReceitaTest {

    private Receita receitaInserida;

    @BeforeEach
    void setup() {
        Categoria categoria = new Categoria(7, "Salário");
        receitaInserida = new Receita(99, new BigDecimal("500.00")
                ,"Salário de Teste",Date.valueOf("2025-08-01"),"PIX",categoria);
    }
    @AfterEach
    public void Limpeza() {
        if (receitaInserida != null && receitaInserida.getId() != 0) {
            ReceitaDao.excluir(receitaInserida.getId());
        }
    }
    //OK
    @Test
    void testInserir() {
        receitaInserida = ReceitaDao.inserirTest(receitaInserida);

        assertNotNull(receitaInserida, "A receita inserida não deve ser nula.");
        assertNotEquals(receitaInserida.getId(), "o id deve ser automatico");

        Receita receitaBuscada = ReceitaDao.getById(receitaInserida.getId());
        assertNotNull(receitaBuscada, "A receita inserida não deve ser nula.");
        assertEquals(receitaInserida.getId(), receitaBuscada.getId(), "Os ids devem ser iguais");
    }
    //OK
    @Test
    void testGetByIdExistente() {
        receitaInserida = ReceitaDao.inserirTest(receitaInserida);
        int receitaId = receitaInserida.getId();
        receitaId = 99;

        Receita r = ReceitaDao.getById(receitaId);

        assertNotNull(r, "A receita não deve ser nula ao buscar por ID.");
        assertEquals(r.getId(), receitaId,"O ID da categoria deve ser o mesmo.");
        assertEquals("Salário de Teste", r.getDescricaoReceita(),"a dercricao deve ser a mesma.");
    }
    @Test
    void testGetByIdInexistente() {
        Receita r = ReceitaDao.getById(999999);

        assertNull(r, "A receita deve ser nula ao buscar por ID inexistente.");
    }
    //OK
    @Test
    void testGetAll() {
        List<Receita> receitas = ReceitaDao.getAll();
        assertFalse(receitas.isEmpty(), "A lista de receitas não pode estar vazia.");
        assertTrue(receitas.size() >=1 , "a lista deve conter pelo menos uma receita de teste");
    }

    @Test
    void testAlterarExistente() {

        receitaInserida = ReceitaDao.inserirTest(receitaInserida);


        BigDecimal novoValor = (new BigDecimal("600.00"));
        String NovaDescricaoReceita = ("Salário Alterado");

        Receita receitaParaAtualizar = new Receita(receitaInserida.getId(),
                novoValor,
                NovaDescricaoReceita,
                receitaInserida.getDataReceita(),
                receitaInserida.getPagamento(),
                receitaInserida.getCategoria()
        );
        ReceitaDao.alterar(receitaParaAtualizar);

        Receita receitaAlterada = ReceitaDao.getById(receitaInserida.getId());
        assertNotNull(receitaAlterada,"A despesa atualizada não deve ser nula.");
        assertEquals(new BigDecimal("600.00"), receitaAlterada.getValorRecebido(),
                "O valor deve ser 600.00 após a alteração.");
        assertEquals("Salário Alterado", receitaAlterada.getDescricaoReceita(),
                "A descrição deve ser 'Salário Alterado'.");
    }
    //OK
    @Test
    void testExcluir() {
        receitaInserida = ReceitaDao.inserirTest(receitaInserida);
        int receitaExcluir = receitaInserida.getId();

        Receita receitaExistente = ReceitaDao.getById(receitaExcluir);
        assertNotNull(receitaExistente,"deve existir antes de Excluir");

        boolean sucesso = ReceitaDao.excluir(receitaExcluir);
        assertTrue(sucesso, "A exclusão da receita deveria retornar true.");

        Receita receitaBuscada = ReceitaDao.getById(receitaExcluir);
        assertNull(receitaBuscada, "A receita deve ser nula após a exclusão.");

        receitaInserida = null;
    }

    @Test
    void testAlterarInexistente() {


        Receita receitaParaAtualizar = new Receita(
                999999,
                new BigDecimal("5.00"),
                "descricão",
                Date.valueOf("2025-08-08"),
                "Pix",
                new Categoria(7,"Salário")
        );

        boolean alterado = ReceitaDao.alterar(receitaParaAtualizar);
        assertFalse(alterado, "a alteração de inexistente deve ser retorna false");

    }
}

