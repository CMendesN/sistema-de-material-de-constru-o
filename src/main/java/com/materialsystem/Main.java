package com.materialsystem;

import com.materialsystem.controller.MainController;
import com.materialsystem.util.DatabaseInitializer;
import com.materialsystem.view.SplashScreen;

public class Main {
    public static void main(String[] args) {
        SplashScreen.exibir();
        DatabaseInitializer.inicializarBanco();  // Primeiro inicializa banco e cria o primeiro usuário
        MainController mainController = new MainController();
        mainController.iniciarAplicacao();
    }
}

