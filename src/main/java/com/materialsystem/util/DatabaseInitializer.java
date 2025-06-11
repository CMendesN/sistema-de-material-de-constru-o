package com.materialsystem.util;

import com.materialsystem.controller.UsuarioController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void inicializarBanco() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Criação automática das tabelas caso não existam
            stmt.execute("""
                CREATE TABLE Usuario (
                    id_usuario SERIAL PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    senha VARCHAR(255) NOT NULL,
                    papel VARCHAR(20) NOT NULL CHECK (papel IN ('gerente', 'vendedor', 'comprador')),

                );
                
                CREATE TABLE IF NOT EXISTS Fabricante (
                    id_fabricante SERIAL PRIMARY KEY,
                    nome_fabricante VARCHAR(100) UNIQUE NOT NULL,
                    contato VARCHAR(100),
                    endereco VARCHAR(255)
                );

                CREATE TABLE IF NOT EXISTS Produto (
                    id_produto SERIAL PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    descricao TEXT,
                    preco_unitario NUMERIC(10, 2) NOT NULL,
                    quantidade_em_estoque INTEGER NOT NULL,
                    id_fabricante INTEGER NOT NULL REFERENCES Fabricante(id_fabricante),
                    categoria VARCHAR(50)
                );

                CREATE TABLE IF NOT EXISTS Estoque (
                    id_estoque SERIAL PRIMARY KEY,
                    localizacao VARCHAR(255) UNIQUE NOT NULL,
                    capacidade NUMERIC(10, 2)
                );

                CREATE TABLE ProdutoEstoque (
                    id_produto INTEGER NOT NULL REFERENCES Produto(id_produto),
                    id_estoque INTEGER NOT NULL REFERENCES Estoque(id_estoque),
                    quantidade INTEGER NOT NULL CHECK (quantidade >= 0),
                    PRIMARY KEY (id_produto, id_estoque),
                    FOREIGN KEY (id_produto) REFERENCES Produto(id_produto)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE,
                    FOREIGN KEY (id_estoque) REFERENCES Estoque(id_estoque)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE
                );

                CREATE TABLE Vendedor (
                    id_vendedor SERIAL PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    cpf VARCHAR(14) UNIQUE NOT NULL,
                    contato VARCHAR(100),
                    salario NUMERIC(10, 2),
                    data_contratacao DATE,
                );

                CREATE TABLE Comprador (
                    id_comprador SERIAL PRIMARY KEY,
                    nome VARCHAR(100) NOT NULL,
                    cpf VARCHAR(14) UNIQUE NOT NULL,
                    contato VARCHAR(100),
                    endereco VARCHAR(255)
                );

                CREATE TABLE IF NOT EXISTS Venda (
                    id_venda SERIAL PRIMARY KEY,
                    data_venda TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    id_vendedor INTEGER REFERENCES Vendedor(id_vendedor),
                    id_comprador INTEGER REFERENCES Comprador(id_comprador),
                    valor_total NUMERIC(10, 2) NOT NULL
                );

                CREATE TABLE IF NOT EXISTS ItemVenda (
                    id_item_venda SERIAL PRIMARY KEY,
                    id_venda INTEGER NOT NULL REFERENCES Venda(id_venda),
                    id_produto INTEGER NOT NULL REFERENCES Produto(id_produto),
                    quantidade INTEGER NOT NULL,
                    preco_unitario_venda NUMERIC(10, 2) NOT NULL
                );
            """);

            System.out.println("Tabelas verificadas/criadas com sucesso.");

            // Verifica se existe pelo menos um usuário
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Usuario");
            rs.next();
            int totalUsuarios = rs.getInt(1);

            if (totalUsuarios == 0) {
                System.out.println("\nNenhum usuário cadastrado ainda.");
                System.out.println("Iniciando fluxo de criação do primeiro usuário administrador (Gerente fixo)...");
                UsuarioController uc = new UsuarioController();
                uc.cadastrarUsuarioInicial();
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro durante a inicialização do banco de dados.");
            System.exit(1);
        }
    }
}
