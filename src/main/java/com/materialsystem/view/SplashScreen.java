package com.materialsystem.view;

public class SplashScreen {

    public static void exibir() {
        System.out.println("===============================================");
        System.out.println("||                                           ||");
        System.out.println("||    SISTEMA DE GESTÃO DE MATERIAIS         ||");
        System.out.println("||                                           ||");
        System.out.println("||        Desenvolvido por: Carlos           ||");
        System.out.println("||        Versão: 0.1                        ||");
        System.out.println("||                                           ||");
        System.out.println("===============================================\n");

        try {
            Thread.sleep(1000); // Pequena pausa para efeito
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
