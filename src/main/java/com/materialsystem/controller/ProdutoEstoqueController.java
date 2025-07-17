package com.materialsystem.controller;

import com.materialsystem.dao.ProdutoEstoqueDAO;
import com.materialsystem.entity.ProdutoEstoque;

import java.util.List;

public class ProdutoEstoqueController {

    private final ProdutoEstoqueDAO dao = new ProdutoEstoqueDAO();

    public boolean inserir(ProdutoEstoque pe) {
        if (dao.existeAssociacao(pe.getIdProduto(), pe.getIdEstoque())) {
            return false;
        }
        dao.inserir(pe);
        return true;
    }

    public List<ProdutoEstoque> buscarPorProduto(int idProduto) {
        return dao.buscarPorProduto(idProduto);
    }
}
