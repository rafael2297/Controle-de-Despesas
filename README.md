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

## 🤝 Contribuições

