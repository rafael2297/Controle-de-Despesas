package Test;


import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import DAO.DespesasDao;
import Model.Categoria;
import Model.Despesas;

class TestDespesas {
	
	// 
    private Despesas despesaDeTeste;

    @BeforeEach
    public void setup() {
        Categoria categoria = new Categoria(1, "Alimentação"); 
        despesaDeTeste = new Despesas(99, new BigDecimal("50.00"), "Almoço de teste", categoria, Date.valueOf("2025-08-05"), "Débito");
    }
    
    // O AfterEach GARANTE QUE OS DADOS CRIADOS NO TESTE SÃO LIMPOS
    @AfterEach
    public void cleanup() {
        if (despesaDeTeste != null && despesaDeTeste.getId() != 0) {
            DespesasDao.excluir(despesaDeTeste.getId());
        }
    }


    @Test
    public void testCriarDespesa() {
        despesaDeTeste = DespesasDao.inserirTest(despesaDeTeste);
    
        assertNotNull(despesaDeTeste, "A despesa inserida não deve ser nula.");
        assertNotEquals( despesaDeTeste.getId(), "O ID da despesa deve ser gerado pelo banco.");
        
  
        Despesas despesaBuscada = DespesasDao.getById(despesaDeTeste.getId());
        assertNotNull(despesaBuscada, "A despesa deveria ser encontrada após a inserção.");
        assertEquals(despesaDeTeste.getId(), despesaBuscada.getId(), "Os IDs devem ser iguais.");
    }
    @Test
    public void testListarDespesa() {
        
        List<Despesas> despesas = DespesasDao.getAll();
        
       
        assertFalse(despesas.isEmpty(), "A lista de despesas não deve estar vazia.");
        assertTrue(despesas.size() >= 2, "A lista deve conter pelo menos as duas despesas de teste.");
        
        for (Despesas d : despesas) {
            if (d.getDescricao().startsWith("Item ")) {
                DespesasDao.excluir(d.getId());
            }
        }
    }
    
    @Test
    public void testGetByIDExistenteDespesa() {

        despesaDeTeste = DespesasDao.inserirTest(despesaDeTeste);
        int idBuscando = despesaDeTeste.getId();
        idBuscando = 99;
     
        Despesas d = DespesasDao.getById(idBuscando);
        
       
        assertNotNull(d, "A despesa com ID existente deve ser encontrada.");
        assertEquals(idBuscando, d.getId(), "O ID da despesa encontrada deve ser o mesmo que o buscado.");
        assertEquals("Almoço de teste", d.getDescricao(), "A descrição deve ser a correta.");
    }

    @Test
    public void testGetByIdInexistenteDespesa() {
        
        Despesas d = DespesasDao.getById(99999);
       
        assertNull(d, "O método deve retornar null para um ID inexistente.");
    }
    
    @Test
    public void testAtualizarDespesa() {

        despesaDeTeste = DespesasDao.inserirTest(despesaDeTeste);
        
        String novaDescricao = "Lanche da tarde";
        BigDecimal novoValor = new BigDecimal("15.50");
        
        Despesas despesaParaAtualizar = new Despesas(
            despesaDeTeste.getId(),
            novoValor,
            novaDescricao,
            despesaDeTeste.getCategoria(),
            despesaDeTeste.getData(),
            despesaDeTeste.getPagamento()
        );
        
        DespesasDao.alterar(despesaParaAtualizar);
        
        Despesas despesaAtualizada = DespesasDao.getById(despesaDeTeste.getId());
        assertNotNull(despesaAtualizada, "A despesa atualizada não deve ser nula.");
        assertEquals(novaDescricao, despesaAtualizada.getDescricao(), "A descrição deveria ter sido atualizada.");
        assertEquals(novoValor, despesaAtualizada.getValor(), "O valor deveria ter sido atualizado.");
    }
    
    @Test
    public void testAtualizarDespesaInexistente() {
      
        Despesas despesaInexistente = new Despesas(
            999999, // ID inexistente
            new BigDecimal("10.00"),
            "Descrição",
            new Categoria(1, "Alimentação"),
            Date.valueOf("2025-08-05"),
            "Dinheiro"
        );
        
        boolean alterado = DespesasDao.alterar(despesaInexistente);
        assertFalse(alterado, "A alteração de uma despesa inexistente deve retornar false.");
    }
    
    @Test
    public void testExcluirDespesa() {
        
        despesaDeTeste = DespesasDao.inserirTest(despesaDeTeste);
        int idExcluir = despesaDeTeste.getId();
        
        Despesas despesaExistente = DespesasDao.getById(idExcluir);
        assertNotNull(despesaExistente, "A despesa deve existir antes de ser excluída.");
        
        boolean excluido = DespesasDao.excluir(idExcluir);
        assertTrue(excluido, "A exclusão de uma despesa existente deve retornar true.");
        
        Despesas despesaBuscada = DespesasDao.getById(idExcluir);
        assertNull(despesaBuscada, "A despesa não deve ser encontrada após a exclusão.");
        
        despesaDeTeste = null;
    }
}
