package com.materialsystem.controller;

import com.materialsystem.dao.VendedorDAO;
import com.materialsystem.entity.Vendedor;

import java.util.List;

public class VendedorController {

    private final VendedorDAO dao = new VendedorDAO();

    public void inserir(Vendedor v) {
        dao.inserir(v);
    }

    public List<Vendedor> listarTodos() {
        return dao.buscarTodos();
    }

    public Vendedor buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Vendedor v) {
        if (dao.buscarPorId(v.getIdVendedor()) != null) {
            dao.atualizar(v);
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
