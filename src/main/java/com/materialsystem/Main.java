package com.materialsystem;

import com.materialsystem.controller.ProdutoController;

public class Main {
    public static void main(String[] args) {
        ProdutoController controller = new ProdutoController();
        controller.gerenciarProdutos();
    }
}
