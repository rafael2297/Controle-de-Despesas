Com certeza\! Adicionar uma lista de funcionalidades esperadas para a próxima fase do projeto torna o README ainda mais útil e claro sobre o futuro do sistema.

Aqui está o seu README atualizado com a nova seção **"Próximos Passos (Parte 2)"**, detalhando as funcionalidades planejadas.

-----

# 💰 Controle de Despesas

Este é o projeto **Controle de Despesas**, desenvolvido como parte do curso **PI Jovem Programador**. O objetivo principal é criar um sistema robusto para gerenciar e monitorar receitas e despesas pessoais, oferecendo uma visão clara da saúde financeira do usuário.

-----

## 🛠️ Tecnologias Utilizadas

  * **Linguagem:** Java 20
  * **Gerenciador de Dependências:** Maven
  * **Banco de Dados:** MySQL
  * **Linguagem de Definição de Dados (DDL):** SQL

-----

## 📦 Dependências do Projeto

Este projeto utiliza o **Maven** para gerenciar suas dependências. As bibliotecas e ferramentas abaixo são necessárias para o seu funcionamento:

  * **`mysql-connector-java`**: Conector oficial do MySQL para Java, permitindo a comunicação com o banco de dados.
  * **`log4j-core` e `log4j-api`**: Bibliotecas para registro de logs, facilitando o rastreamento de eventos e depuração do sistema.
  * **`junit-jupiter-api` e `junit-jupiter-engine`**: Framework de testes unitários, essencial para garantir a qualidade e o bom funcionamento do código.
  * **`poi-ooxml`**: Biblioteca do Apache POI para manipulação de arquivos do Microsoft Office (como Excel), o que pode ser útil para exportar relatórios financeiros.
  * **`jansi`**: Biblioteca que permite adicionar cores e formatação ao console, melhorando a experiência do usuário.

-----

## 🚀 Como Executar o Projeto

Para começar a usar o sistema, você precisará configurar o banco de dados. Siga os passos abaixo para criar as tabelas e a estrutura necessária.

### 1\. Crie o Banco de Dados

Primeiro, crie o banco de dados com o nome `controle_de_despesas`.

```sql
CREATE DATABASE controle_de_despesas;
```

### 2\. Selecione o Banco de Dados

Em seguida, selecione o banco de dados recém-criado para as próximas operações.

```sql
USE controle_de_despesas;
```

### 3\. Crie as Tabelas

Agora, execute os seguintes comandos SQL para criar todas as tabelas necessárias: `tb_categorias`, `tb_despesas`, `tb_receita` e `tb_saldo`.

```sql
-- Tabela para gerenciar as categorias (Ex: Alimentação, Transporte, Salário)
CREATE TABLE tb_categorias (
    id_categoria INT PRIMARY KEY AUTO_INCREMENT,
    nome_categoria VARCHAR(100) NOT NULL UNIQUE
);

-- Tabela para registrar as despesas
CREATE TABLE tb_despesas (
    id_despesa INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(300),
    valor DECIMAL(10,2) NOT NULL,
    data_despesa DATE NOT NULL,
    id_categoria INT NOT NULL,
    pagamento VARCHAR(50),
    FOREIGN KEY (id_categoria) REFERENCES tb_categorias(id_categoria)
);

-- Tabela para registrar as receitas
CREATE TABLE tb_receita (
    id INT PRIMARY KEY AUTO_INCREMENT,
    valor_recebido DECIMAL(10,2) NOT NULL CHECK (valor_recebido > 0),
    descricao_receita VARCHAR(255),
    data_receita DATE NOT NULL,
    id_categoria INT NOT NULL,
    pagamento VARCHAR(50),
    FOREIGN KEY (id_categoria) REFERENCES tb_categorias(id_categoria)
);

-- Tabela para controlar o saldo financeiro
CREATE TABLE tb_saldo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    saldo_inicial DECIMAL(10,2) NOT NULL,
    saldo_final DECIMAL(10,2)
);
```

-----

## 💡 Próximos Passos (Parte 2 do Projeto)

A estrutura do banco de dados e as dependências já estão configuradas. A próxima etapa de desenvolvimento incluirá a criação da lógica de aplicação em Java para implementar as seguintes funcionalidades:

  * **Instruções de Uso (Manual):** Adicionar um guia detalhado sobre como usar o sistema, com explicações sobre cada funcionalidade.
  * **Lista de Desejos:** Criar uma nova tabela no banco de dados (`tb_lista_desejos`) e a lógica em Java para gerenciar itens com: `descrição`, `data de início`, `valor`, `status de checagem` e `data da compra`.
  * **Página de Parcelados:** Desenvolver uma interface para visualizar as parcelas pendentes que ainda serão debitadas nas próximas faturas do cartão de crédito.
  * **Calendário Mensal:** Implementar um calendário que exiba de forma objetiva as datas previstas de receitas e despesas (como salários e vencimentos de contas), com a possibilidade de visualizar os valores gastos em cada data.
  * **Separação de Gastos por Categorias:** Adicionar a funcionalidade de somar e visualizar o valor total gasto em cada categoria específica (por exemplo, "quanto gastei em Lanche" ou "quanto gastei em Gasolina").

-----

## 🤝 Contribuições

Sinta-se à vontade para enviar sugestões ou reportar problemas. Este projeto é uma base para aprendizado e evolução\!

-----

## ✨ Agradecimentos

Gostaríamos de agradecer às seguintes pessoas por sua colaboração e apoio neste projeto:

  * **[Hayanne](https://www.google.com/search?q=https://github.com/usuario1)** - Criar e modificar parte de Despesas e Receita, responsável pelo gerenciamento e organização em geral.
  * **[Julia](https://www.google.com/search?q=https://github.com/usuario1)** - Criação e modificação pesada de Banco de Dados.
  * **[Gisely](https://www.google.com/search?q=https://github.com/usuario1)** - Criar e modificar parte de Categoria.
  * **[Rafael](https://github.com/rafael2297)** - Criar e modificar Relatório, integrar as diferentes partes e responsável pela conversão em site.
