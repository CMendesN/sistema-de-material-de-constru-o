package com.materialsystem.controller;

import com.materialsystem.dao.CompradorDAO;
import com.materialsystem.entity.Comprador;

import java.util.List;

public class CompradorController {

    private final CompradorDAO dao = new CompradorDAO();

    public void inserir(Comprador c) {
        dao.inserir(c);
    }

    public List<Comprador> listarTodos() {
        return dao.buscarTodos();
    }

    public Comprador buscarPorId(int id) {
        return dao.buscarPorId(id);
    }

    public boolean atualizar(Comprador c) {
        if (dao.buscarPorId(c.getIdComprador()) != null) {
            dao.atualizar(c);
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
