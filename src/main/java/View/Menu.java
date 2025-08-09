package View;

import DAO.*;
import Model.*;
import Util.RelatorioExcelExporter;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RelatorioDao relatorioDao = new RelatorioDao();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final SaldoDAO saldoDAO = new SaldoDAO();
    private static final DespesasDao despesaDAO = new DespesasDao();
    private static final CategoriaDao categoriaDAO = new CategoriaDao();
    private static final ReceitaDao receitaDAO = new ReceitaDao();

    public static void exibirMenu() {
        while (true) {
            System.out.println("\n=== Controle de Despesas Pessoais ===");
            System.out.println("1. Menu Despesas");
            System.out.println("2. Menu Relatório");
            System.out.println("3. Menu Categoria");
            System.out.println("4. Menu Receita");
            System.out.println("5. Menu Saldo");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> menuDespesas();
                case "2" -> menuRelatorio();
                case "3" -> menuCategoria();
                case "4" -> menuReceita();
                case "5" -> menuSaldo();
                case "0" -> {
                    System.out.println("Saindo...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuDespesas() {
        while (true) {
            System.out.println("\n=== Despesas ===");
            System.out.println("1. Adicionar nova Despesa");
            System.out.println("2. alterar Despesa");
            System.out.println("3. Deletar Despesa");
            System.out.println("4. Listar Despesas");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> criarDespesa();
                case "2" -> alterarDespesa();
                case "3" -> deletarDespesa();
                case "4" -> listarDespesas();
                case "0" -> {
                    System.out.println("Voltando...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuRelatorio() {
        while (true) {
            System.out.println("\n=== Relatório ===");
            System.out.println("1. Criar Relatório Completo");
            System.out.println("2. Exibir Relatório por Período");
            System.out.println("3. Exibir Relatório por Mês");
            System.out.println("4. Exibir Relatório de Despesas Fixas");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> criarRelatorioCompleto();
                case "2" -> exibirRelatorioPorPeriodo();
                case "3" -> criarRelatorioMensal();
                case "4" -> exibirRelatorioDespesaFixa();
                case "0" -> {
                    System.out.println("Voltando...");
                    return;
                }
                default -> System.out.println("❌ Opção inválida. Tente novamente.");
            }
        }
    }

    private static void menuCategoria() {
        while (true) {
            System.out.println("\n=== Categoria ===");
            System.out.println("1. Criar Categoria");
            System.out.println("2. alterar Categoria");
            System.out.println("3. deletar Categoria ");
            System.out.println("4. Listar Categorias");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> criarCategoria();
                case "2" -> alterarCategoria();
                case "3" -> deletarCategoria();
                case "4" -> mostrarCategorias();
                case "0" -> {
                    System.out.println("Voltando...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuReceita() {
        while (true) {
            System.out.println("\n=== Receita ===");
            System.out.println("1. Adicionar nova Receita");
            System.out.println("2. Alterar Receita");
            System.out.println("3. Deletar Receita");
            System.out.println("4. Listar Receitas");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> adicionarReceita();
                case "2" -> alterarReceita();
                case "3" -> deletarReceita();
                case "4" -> listarReceitas();
                case "0" -> {
                    System.out.println("Voltando ao menu principal...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuSaldo() {
        while (true) {
            System.out.println("\n=== Saldo ===");
            System.out.println("1. Exibir Saldo Inicial e Saldo Final");
            System.out.println("2. Atualizar Saldo Inicial e Saldo Final");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> exibirSaldoInicialESaldoFinal();
                case "2" -> atualizarSaldoInicial();
                case "0" -> {
                    System.out.println("Voltando ao menu principal...");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }


    private static void criarDespesa() {
        System.out.println("\n=== Adicionar nova Despesa ===");
        try {
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            System.out.print("Valor (use negativo para despesa): ");
            String valorStr = scanner.nextLine().replace(",", ".");
            BigDecimal valor = new BigDecimal(valorStr);

            System.out.print("Data (DD/MM/AAAA): ");
            Date data = converterData(scanner.nextLine());

            Categoria categoriaSelecionada = null;
            List<Categoria> categorias = categoriaDAO.getAll();

            while (categoriaSelecionada == null) {
                mostrarCategorias();
                System.out.print("Informe o ID da Categoria (ou digite 0 para cadastrar nova): ");
                int idCategoria = Integer.parseInt(scanner.nextLine());

                if (idCategoria == 0) {
                    System.out.print("Digite o nome da nova categoria: ");
                    String nomeNova = scanner.nextLine();

                    Categoria nova = new Categoria();
                    nova.setNomeCategoria(nomeNova);
                    boolean sucesso = categoriaDAO.inserir(nova);

                    if (sucesso) {
                        categorias = categoriaDAO.getAll(); // Atualiza lista
                        categoriaSelecionada = categorias.stream()
                                .filter(cat -> cat.getNomeCategoria().equalsIgnoreCase(nomeNova))
                                .findFirst().orElse(null);
                    } else {
                        System.out.println("❌ Falha ao cadastrar nova categoria.");
                    }
                } else {
                    categoriaSelecionada = categoriaDAO.getById(idCategoria);
                    if (categoriaSelecionada == null) {
                        System.out.println("❌ Categoria não encontrada.");
                    }
                }
            }

            System.out.print("Forma de pagamento: ");
            String pagamento = scanner.nextLine();

            Model.Despesas despesa = new Model.Despesas();
            despesa.setDescricao(descricao);
            despesa.setValor(valor);
            despesa.setData(data);
            despesa.setCategoria(categoriaSelecionada);
            despesa.setPagamento(pagamento);

            Despesas novaDespesa = DespesasDao.inserir(despesa);
            System.out.println(novaDespesa != null ? "✅ Despesa adicionada com sucesso!" : "❌ Falha ao adicionar despesa.");

        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }


    private static void alterarDespesa() {
        System.out.println("\n=== Alterar Despesa ===");

        mostrarDespesas();
        System.out.print("Digite o ID da despesa que deseja alterar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());

            Model.Despesas despesa = DespesasDao.getById(id);
            if (despesa == null) {
                System.out.println("❌ Despesa não encontrada.");
                return;
            }

            System.out.print("Nova descrição (atual: " + despesa.getDescricao() + "): ");
            String descricao = scanner.nextLine();

            System.out.print("Novo valor (atual: " + despesa.getValor() + "): ");
            BigDecimal valor = new BigDecimal(scanner.nextLine().replace(",", "."));

            System.out.print("Nova data (atual: " + despesa.getData() + ") [DD/MM/AAAA]: ");
            Date data = converterData(scanner.nextLine());

            Categoria novaCategoria = null;
            List<Categoria> categorias = categoriaDAO.getAll();

            while (novaCategoria == null) {
                mostrarCategorias();
                System.out.print("Novo ID da Categoria (atual: " + despesa.getCategoria().getIdCategoria() + ") ou 0 para cadastrar nova: ");
                int idCategoria = Integer.parseInt(scanner.nextLine());

                if (idCategoria == 0) {
                    System.out.print("Digite o nome da nova categoria: ");
                    String nomeNova = scanner.nextLine();

                    Categoria nova = new Categoria();
                    nova.setNomeCategoria(nomeNova);
                    boolean sucesso = categoriaDAO.inserir(nova);

                    if (sucesso) {
                        categorias = categoriaDAO.getAll(); // Atualiza lista
                        novaCategoria = categorias.stream()
                                .filter(cat -> cat.getNomeCategoria().equalsIgnoreCase(nomeNova))
                                .findFirst().orElse(null);
                    } else {
                        System.out.println("❌ Falha ao cadastrar nova categoria.");
                    }
                } else {
                    novaCategoria = categoriaDAO.getById(idCategoria);
                    if (novaCategoria == null) {
                        System.out.println("❌ Categoria não encontrada.");
                    }
                }
            }

            System.out.print("Nova forma de pagamento (atual: " + despesa.getPagamento() + "): ");
            String pagamento = scanner.nextLine();

            despesa.setDescricao(descricao);
            despesa.setValor(valor);
            despesa.setData(data);
            despesa.setCategoria(novaCategoria);
            despesa.setPagamento(pagamento);

            boolean sucesso = DespesasDao.alterar(despesa);
            System.out.println(sucesso ? "✅ Despesa atualizada com sucesso!" : "❌ Falha ao atualizar despesa.");

        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }


    private static void deletarDespesa() {
        System.out.println("\n=== Deletar Despesa ===");
        try {
            mostrarDespesas();
            System.out.print("Digite o ID da despesa que deseja deletar: ");
            int id = Integer.parseInt(scanner.nextLine());

            boolean sucesso = DespesasDao.excluir(id);
            System.out.println(sucesso ? "✅ Despesa deletada com sucesso!" : "❌ Despesa não encontrada ou erro ao deletar.");

        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private static void mostrarDespesas() {
        try {
            List<Model.Despesas> despesas = DespesasDao.getAll();
            if (despesas.isEmpty()) {
                System.out.println("Nenhuma despesa cadastrada.");
            } else {
                System.out.println("\n=== Despesas Cadastradas ===");
                for (Model.Despesas d : despesas) {
                    System.out.printf("ID: %d | Descrição: %s | Valor: R$ %.2f%n", d.getId(), d.getDescricao(), d.getValor());
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar despesas: " + e.getMessage());
        }
    }

    private static void listarDespesas() {
        System.out.println("\n=== Lista de Despesas ===");
        try {
            List<Model.Despesas> despesas = DespesasDao.getAll();
            if (despesas.isEmpty()) {
                System.out.println("Nenhuma despesa cadastrada.");
            } else {
                for (Model.Despesas d : despesas) {
                    
                    System.out.printf("ID: %d | Descrição: %s | Valor: R$ %.2f | Data: %s | Categoria: %s | Pagamento: %s%n",
                            d.getId(),
                            d.getDescricao(),
                            d.getValor(),
                            d.getData(),
                            d.getCategoria().getNomeCategoria(),
                            d.getPagamento());
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar despesas: " + e.getMessage());
        }
    }

    private static void criarCategoria() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite o nome da nova categoria:");
        String nome = sc.nextLine();

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(nome);

        CategoriaDao dao = new CategoriaDao();
        boolean sucesso = dao.inserir(categoria);

        if (sucesso) {
            System.out.println("✅ Categoria criada com sucesso! ID: " + categoria.getIdCategoria());
        } else {
            System.out.println("❌ Falha ao criar categoria.");
        }
    }

    private static void alterarCategoria() {
        Scanner sc = new Scanner(System.in);
        CategoriaDao dao = new CategoriaDao();

        // Mostrar categorias existentes
        List<Categoria> categorias = dao.getAll();
        if (categorias.isEmpty()) {
            System.out.println("⚠️ Nenhuma categoria encontrada.");
            return;
        }

        System.out.println("===== CATEGORIAS EXISTENTES =====");
        for (Categoria c : categorias) {
            System.out.println("ID: " + c.getIdCategoria() + " | Nome: " + c.getNomeCategoria());
        }

        System.out.print("Digite o ID da categoria que deseja alterar: ");
        int id = sc.nextInt();
        sc.nextLine(); // limpar buffer

        Categoria categoria = dao.getById(id);
        if (categoria == null) {
            System.out.println("❌ Categoria não encontrada.");
            return;
        }

        System.out.print("Digite o novo nome para a categoria: ");
        String novoNome = sc.nextLine();
        categoria.setNomeCategoria(novoNome);

        boolean sucesso = dao.alterar(categoria);
        if (sucesso) {
            System.out.println("✅ Categoria alterada com sucesso!");
        } else {
            System.out.println("❌ Falha ao alterar categoria.");
        }
    }

    private static void deletarCategoria() {
        Scanner sc = new Scanner(System.in);
        CategoriaDao dao = new CategoriaDao();

        // Mostrar categorias existentes
        List<Categoria> categorias = dao.getAll();
        if (categorias.isEmpty()) {
            System.out.println("⚠️ Nenhuma categoria encontrada.");
            return;
        }

        System.out.println("===== CATEGORIAS EXISTENTES =====");
        for (Categoria c : categorias) {
            System.out.println("ID: " + c.getIdCategoria() + " | Nome: " + c.getNomeCategoria());
        }

        System.out.print("Digite o ID da categoria que deseja deletar: ");
        int id = sc.nextInt();

        boolean sucesso = dao.excluir(id);
        if (sucesso) {
            System.out.println("✅ Categoria deletada com sucesso!");
        } else {
            System.out.println("❌ Falha ao deletar categoria. Verifique se ela está sendo usada em receitas ou despesas.");
        }
    }

    private static void mostrarCategorias() {
        List<Categoria> categorias = categoriaDAO.getAll();
        if (categorias.isEmpty()) {
            System.out.println("Nenhuma categoria cadastrada.");
        } else {
            System.out.println("\n=== Categorias Disponíveis ===");
            for (Categoria c : categorias) {
                System.out.printf("ID: %d | %s%n", c.getIdCategoria(), c.getNomeCategoria());
            }
        }
    }

    private static void criarRelatorioCompleto() {
        System.out.println("\n=== Relatório Completo ===");
        List<Relatorio> todosRelatorios = relatorioDao.listarTodosRelatorios();

        criarPlanilha(todosRelatorios);
        if (todosRelatorios.isEmpty()) {
            System.out.println("Nenhuma despesa ou receita encontrada.");
        } else {
            imprimirRelatorioConsole(todosRelatorios);
        }
    }

    private static void criarRelatorioMensal() {
        System.out.println("Selecione o mês do relatório:");
        System.out.println("1 - Janeiro");
        System.out.println("2 - Fevereiro");
        System.out.println("3 - Março");
        System.out.println("4 - Abril");
        System.out.println("5 - Maio");
        System.out.println("6 - Junho");
        System.out.println("7 - Julho");
        System.out.println("8 - Agosto");
        System.out.println("9 - Setembro");
        System.out.println("10 - Outubro");
        System.out.println("11 - Novembro");
        System.out.println("12 - Dezembro");

        System.out.print("Mês do Relatório desejado (número): ");
        try {
            int valor = Integer.parseInt(scanner.nextLine());
            List<Relatorio> relatoriosMensal = relatorioDao.listarRelatoriosMensal(valor);

            criarPlanilha(relatoriosMensal);

            if (relatoriosMensal.isEmpty()) {
                System.out.println("Nenhuma despesa ou relatorio encontrada para o mês informado.");
            } else {
                imprimirRelatorioConsole(relatoriosMensal);
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Digite um número válido para o mês.");
        }
    }

    private static void exibirRelatorioPorPeriodo() {
        try {
            System.out.print("Digite a data inicial (DD/MM/AAAA): ");
            String inicioStr = scanner.nextLine();
            System.out.print("Digite a data final (DD/MM/AAAA): ");
            String fimStr = scanner.nextLine();

            Date inicio = converterData(inicioStr);
            Date fim = converterData(fimStr);

            List<Relatorio> relatorios = relatorioDao.listarRelatoriosPorPeriodo(inicio.toLocalDate(), fim.toLocalDate());

            criarPlanilha(relatorios);

            if (relatorios.isEmpty()) {
                System.out.println("Nenhuma despesa ou receita encontrada no período.");
            } else {
                imprimirRelatorioConsole(relatorios);
            }

        } catch (Exception e) {
            System.out.println("❌ Erro: Formato de data inválido. Use DD/MM/AAAA.");
        }
    }

    private static void exibirRelatorioDespesaFixa() {
        List<String> categoriasDesejadas = Arrays.asList("Aluguel", "Agua", "Energia");
        List<Relatorio> relatoriosFiltrados = relatorioDao.listarRelatoriosPorCategorias(categoriasDesejadas);

        criarPlanilha(relatoriosFiltrados);

        if (relatoriosFiltrados.isEmpty()) {
            System.out.println("Nenhum relatório encontrado para as categorias especificadas.");
        } else {
            imprimirRelatorioConsole(relatoriosFiltrados);
        }
    }

    private static void adicionarReceita() {
        System.out.println("\n=== Adicionar Receita ===");
        try {
            System.out.print("Valor recebido: ");
            String entrada = scanner.nextLine().replace(",", ".");
            BigDecimal valor = new BigDecimal(entrada);

            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            System.out.print("Data da receita (DD/MM/AAAA): ");
            String dataStr = scanner.nextLine();
            Date data = converterData(dataStr);

            // Listar categorias disponíveis
            List<Categoria> categorias = CategoriaDao.getAll();
            Categoria categoriaSelecionada = null;

            while (categoriaSelecionada == null) {
                System.out.println("Categorias disponíveis:");
                for (Categoria cat : categorias) {
                    System.out.println("[" + cat.getIdCategoria() + "] " + cat.getNomeCategoria());
                }

                System.out.print("Escolha o ID da categoria (ou 0 para cadastrar nova): ");
                int idCategoria = Integer.parseInt(scanner.nextLine());

                if (idCategoria == 0) {
                    System.out.print("Digite o nome da nova categoria: ");
                    String nomeNova = scanner.nextLine();

                    Categoria nova = new Categoria();
                    nova.setNomeCategoria(nomeNova);
                    boolean sucesso = CategoriaDao.inserir(nova);

                    if (sucesso) {
                        categorias = CategoriaDao.getAll();
                        categoriaSelecionada = categorias.stream()
                                .filter(cat -> cat.getNomeCategoria().equalsIgnoreCase(nomeNova))
                                .findFirst().orElse(null);
                    } else {
                        System.out.println("❌ Falha ao cadastrar categoria.");
                    }
                } else {
                    categoriaSelecionada = categorias.stream()
                            .filter(cat -> cat.getIdCategoria() == idCategoria)
                            .findFirst()
                            .orElse(null);

                    if (categoriaSelecionada == null) {
                        System.out.println("❌ Categoria inválida.");
                    }
                }
            }

            System.out.print("Forma de pagamento (ex: pix, crédito, etc): ");
            String pagamento = scanner.nextLine();

            Receita r = new Receita();
            r.setValorRecebido(valor);
            r.setDescricaoReceita(descricao);
            r.setDataReceita(data);
            r.setCategoria(categoriaSelecionada);
            r.setPagamento(pagamento);

            Receita resultado = ReceitaDao.inserir(r);
            System.out.println(resultado != null ? "✅ Receita adicionada com sucesso!" : "❌ Falha ao adicionar receita.");

        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }


    private static void alterarReceita() {
        System.out.println("\n=== Alterar Receita ===");
        try {
            listarReceitas();
            if (ReceitaDao.getAll().isEmpty()) {
                System.out.println("Nenhuma receita cadastrada.");
                return;
            }

            System.out.print("ID da receita que deseja alterar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Receita r = ReceitaDao.getById(id);
            if (r == null) {
                System.out.println("❌ Receita não encontrada.");
                return;
            }

            System.out.print("Novo valor (atual: " + r.getValorRecebido() + "): ");
            String entrada = scanner.nextLine().replace(",", ".");
            BigDecimal novoValor = new BigDecimal(entrada);

            System.out.print("Nova descrição (atual: " + r.getDescricaoReceita() + "): ");
            String novaDescricao = scanner.nextLine();

            System.out.print("Nova data (atual: " + r.getDataReceita() + ") [DD/MM/AAAA]: ");
            String novaDataStr = scanner.nextLine();
            Date novaData = converterData(novaDataStr);

            // Listar e permitir cadastrar categoria
            List<Categoria> categorias = CategoriaDao.getAll();
            Categoria novaCategoria = null;

            while (novaCategoria == null) {
                System.out.println("Categorias disponíveis:");
                for (Categoria cat : categorias) {
                    System.out.println("[" + cat.getIdCategoria() + "] " + cat.getNomeCategoria());
                }

                System.out.print("Nova categoria (atual ID: " + r.getCategoria().getIdCategoria() + ") - Digite o ID ou 0 para cadastrar nova: ");
                int novoIdCategoria = Integer.parseInt(scanner.nextLine());

                if (novoIdCategoria == 0) {
                    System.out.print("Digite o nome da nova categoria: ");
                    String nomeNova = scanner.nextLine();

                    Categoria novaCat = new Categoria();
                    novaCat.setNomeCategoria(nomeNova);
                    boolean sucesso = CategoriaDao.inserir(novaCat);

                    if (sucesso) {
                        categorias = CategoriaDao.getAll(); // atualiza lista
                        novaCategoria = categorias.stream()
                                .filter(cat -> cat.getNomeCategoria().equalsIgnoreCase(nomeNova))
                                .findFirst().orElse(null);
                    } else {
                        System.out.println("❌ Falha ao cadastrar categoria.");
                    }
                } else {
                    novaCategoria = categorias.stream()
                            .filter(cat -> cat.getIdCategoria() == novoIdCategoria)
                            .findFirst()
                            .orElse(null);

                    if (novaCategoria == null) {
                        System.out.println("❌ Categoria inválida.");
                    }
                }
            }

            System.out.print("Nova forma de pagamento (atual: " + r.getPagamento() + "): ");
            String novoPagamento = scanner.nextLine();

            r.setValorRecebido(novoValor);
            r.setDescricaoReceita(novaDescricao);
            r.setDataReceita(novaData);
            r.setCategoria(novaCategoria);
            r.setPagamento(novoPagamento);

            boolean sucesso = ReceitaDao.alterar(r);
            System.out.println(sucesso ? "✅ Receita atualizada com sucesso!" : "❌ Falha ao atualizar.");

        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private static void deletarReceita() {
        System.out.println("\n=== Deletar Receita ===");
        listarReceitas();
        try {
            System.out.print("ID da receita a deletar: ");
            int id = Integer.parseInt(scanner.nextLine());

            boolean sucesso = ReceitaDao.excluir(id);
            System.out.println(sucesso ? "✅ Receita deletada com sucesso!" : "❌ Receita não encontrada ou erro ao deletar.");

        } catch (Exception e) {
            System.out.println("❌ Erro: " + e.getMessage());
        }
    }

    private static void listarReceitas() {
        System.out.println("\n=== Lista de Receitas ===");
        try {
            List<Model.Receita> receitas = ReceitaDao.getAll();
            if (receitas.isEmpty()) {
                System.out.println("Nenhuma receita cadastrada.");
            } else {
                for (Model.Receita r : receitas) {
                    System.out.printf("ID: %d | Descrição: %s | Valor: R$ %.2f | Data: %s | Categoria: %s | Pagamento: %s%n",
                            r.getId(),
                            r.getDescricaoReceita(),
                            r.getValorRecebido(),
                            r.getDataReceita(),
                            r.getCategoria().getNomeCategoria(), 
                            r.getPagamento());
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao listar receitas: " + e.getMessage());
        }
    }


    private static Date converterData(String dataStr) {
        String[] partes = dataStr.split("/");
        if (partes.length != 3) {
            throw new IllegalArgumentException("Formato de data inválido.");
        }

        int dia = Integer.parseInt(partes[0]);
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[2]);

        try {
            return Date.valueOf(LocalDate.parse(dataStr, DATE_FORMATTER));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de data inválido. Use DD/MM/AAAA.");
        }

    }

    public static void imprimirRelatorioConsole(List<Relatorio> lista) {
        System.out.println("\n📄 Relatório de Receitas e Despesas");
        System.out.println("┌────────────────────┬───────────────┬────────────┬──────────────────────────────┬────────────────────┐");
        System.out.printf("│ %-18s │ %-13s │ %-10s │ %-28s │ %-18s │%n", "Categoria", "Valor (R$)", "Data", "Descrição", "Pagamento");
        System.out.println("├────────────────────┼───────────────┼────────────┼──────────────────────────────┼────────────────────┤");

        BigDecimal totalReceitas = BigDecimal.valueOf(0);
        BigDecimal totalDespesas = BigDecimal.valueOf(0);

        for (Relatorio r : lista) {
            LocalDate localDate = r.getData().toLocalDate();
            String dataFormatada = localDate.format(DATE_FORMATTER);

            BigDecimal valor = r.getValor();
            String valorFormatado = String.format("%+.2f", valor); // exibe com sinal (+/-)

            if (valor.compareTo(BigDecimal.ZERO) < 0) {
                totalDespesas = totalDespesas.add(valor);
            } else {
                totalReceitas = totalReceitas.add(valor);
            }

            System.out.printf("│ %-18s │ %13s │ %-10s │ %-28s │ %-18s │%n",
                    r.getNomeCategoria(),
                    valorFormatado,
                    dataFormatada,
                    r.getDescricao() != null ? r.getDescricao() : "-",
                    r.getPagamento() != null ? r.getPagamento() : "-");
        }

        System.out.println("└────────────────────┴───────────────┴────────────┴──────────────────────────────┴────────────────────┘");

        System.out.printf("Total de RECEITAS: R$ %.2f%n", totalReceitas);
        System.out.printf("Total de DESPESAS: R$ %.2f%n", totalDespesas);

        try {
            BigDecimal saldoInicial = saldoDAO.buscarSaldoInicial(1);
            BigDecimal saldoFinal = saldoDAO.buscarSaldoFinal(1);
            System.out.printf("📦 Saldo INICIAL no banco: R$ %.2f%n", saldoInicial);
            System.out.printf("📦 Saldo FINAL no banco:   R$ %.2f%n", saldoFinal);
        } catch (Exception e) {
            System.err.println("Erro ao acessar saldos: " + e.getMessage());
        }
    }


    private static void criarPlanilha(List<Relatorio> relatorios) {
        while (true) {
            System.out.println("Deseja criar uma planilha?:"
                    + "\n 1-Sim."
                    + "\n 2-Não.");
            System.out.print("Escolha:");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    RelatorioExcelExporter.exportarParaExcel(relatorios, "relatorio.xlsx");
                    return;
                case "2":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }

        }
    }


    private static void exibirSaldoInicialESaldoFinal() {
        List<Saldo> saldos = saldoDAO.listarTodos();

        if (saldos.isEmpty()) {
            System.out.println("Nenhum saldo encontrado.");
            return;
        }

        System.out.println("\n=== Saldos ===");
        for (Saldo saldo : saldos) {
            System.out.println("ID: " + saldo.getId());
            System.out.println("Saldo Inicial: R$ " + saldo.getSaldoInicial());
            System.out.println("Saldo Final:   R$ " + saldo.getSaldoFinal());
            System.out.println("---------------------------");
        }
    }

    private static void atualizarSaldoInicial() {
        System.out.print("Informe o novo valor do saldo inicial: ");
        String entrada = scanner.nextLine().replace(",", ".");
        BigDecimal novoSaldo = new BigDecimal(entrada);

        scanner.nextLine();

        boolean sucesso = saldoDAO.definirOuAtualizarSaldoInicial(novoSaldo);

        if (sucesso) {
            System.out.println("✅ Saldo inicial e final atualizados com sucesso.");
        } else {
            System.out.println("❌ Erro ao atualizar o saldo.");
        }
    }


}
