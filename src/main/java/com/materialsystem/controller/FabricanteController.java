package com.materialsystem.controller;

import com.materialsystem.dao.FabricanteDAO;
import com.materialsystem.entity.Fabricante;

import java.util.List;

public class FabricanteController {

    private final FabricanteDAO dao = new FabricanteDAO();

    public void inserir(Fabricante f) {
        dao.inserir(f);
    }

    public List<Fabricante> listarTodos() {
        return dao.buscarTodos();
    }

    public Fabricante buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Fabricante f) {
        if (dao.buscarPorId(f.getIdFabricante()) != null) {
            dao.atualizar(f);
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
