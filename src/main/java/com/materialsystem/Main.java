package com.materialsystem;

import com.materialsystem.controller.MainController;
import com.materialsystem.util.DatabaseInitializer;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.inicializarBanco();  // Primeiro inicializa banco e cria o primeiro usuário
        MainController mainController = new MainController();
        mainController.iniciarAplicacao();
    }
}

