package com.materialsystem.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ConsoleInputUtils {

    private static final Scanner scanner = new Scanner(System.in);

    public static int lerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    public static double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double valor = Double.parseDouble(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número decimal válido.");
            }
        }
    }

    public static String lerString() {
        // System.out.print(prompt);
        return scanner.nextLine();
    }

    public static String lerOpcaoSN(String prompt) {
        while (true) {
            System.out.print(prompt + " (S/N): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("S") || input.equals("N")) {
                return input;
            } else {
                System.out.println("Entrada inválida. Digite 'S' para Sim ou 'N' para Não.");
            }
        }
    }
    public static LocalDate lerDataValida() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            String dataStr = lerString();
            try {
                return LocalDate.parse(dataStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Informe no formato AAAA-MM-DD.");
            }
        }
    }

}
