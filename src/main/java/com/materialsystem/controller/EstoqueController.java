package com.materialsystem.controller;

import com.materialsystem.dao.EstoqueDAO;
import com.materialsystem.entity.Estoque;

import java.util.List;

public class EstoqueController {

    private final EstoqueDAO dao = new EstoqueDAO();

    public void inserir(Estoque e) {
        dao.inserir(e);
    }

    public List<Estoque> listarTodos() {
        return dao.buscarTodos();
    }

    public Estoque buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Estoque e) {
        if (dao.buscarPorId(e.getIdEstoque()) != null) {
            dao.atualizar(e);
            return true;
        }
        return false;
    }

    public boolean deletar(int id) {
        if (dao.buscarPorId(id) != null) {
            dao.deletar(id);
            return true;
        }
        return false;
    }
}
