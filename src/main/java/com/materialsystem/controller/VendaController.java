package com.materialsystem.controller;

import com.materialsystem.dao.VendaDAO;
import com.materialsystem.entity.ItemVenda;
import com.materialsystem.entity.Venda;
import com.materialsystem.util.VendaComItens;

import java.util.List;

public class VendaController {

    private final VendaDAO dao = new VendaDAO();

    public boolean registrarVenda(VendaComItens vendaComItens) {
        return dao.registrarVendaComItens(vendaComItens.venda(), vendaComItens.itens());
    }

    public List<Venda> listarTodas() {
        return dao.buscarTodas();
    }

    public List<ItemVenda> buscarItensPorVenda(int idVenda) {
        return dao.buscarItensPorVenda(idVenda);
    }

    public boolean cancelarVenda(int idVenda) {
        return dao.deletarVenda(idVenda);
    }
    public List<Venda> listarPorComprador(int idComprador) {
        return dao.buscarPorComprador(idComprador);
    }

}
