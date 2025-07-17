package com.materialsystem.controller;

import com.materialsystem.dao.ProdutoDAO;
import com.materialsystem.entity.Produto;

import java.util.List;

public class ProdutoController {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    public void inserirProduto(Produto produto) {
        produtoDAO.inserir(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoDAO.buscarTodos();
    }

    public Produto buscarPorId(int id) {
        return produtoDAO.buscarPorId(id);
    }

    public boolean atualizarProduto(Produto produto) {
        if (produtoDAO.buscarPorId(produto.getIdProduto()) != null) {
            produtoDAO.atualizar(produto);
            return true;
        }
        return false;
    }

    public boolean deletarProduto(int id) {
        if (produtoDAO.buscarPorId(id) != null) {
            produtoDAO.deletar(id);
            return true;
        }
        return false;
    }
}
