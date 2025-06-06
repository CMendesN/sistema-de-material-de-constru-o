package com.materialsystem.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.materialsystem.entity.Vendedor;
import com.materialsystem.util.ConsoleInputUtils;

public class VendedorView {
    

    public Vendedor solicitarDadosNovoVendedor() {
        System.out.print("Nome: ");
        String nome= ConsoleInputUtils.lerString();

        System.out.print("CPF: ");
        String cpf= ConsoleInputUtils.lerString();

        System.out.print("Contato: ");
        String contato= ConsoleInputUtils.lerString();

        
        double salario = ConsoleInputUtils.lerDouble("Salário: ");

        System.out.print("Data de contratação (AAAA-MM-DD): ");
        LocalDate dataContratacao = lerDataValida();

        System.out.print("ID do usuário (ou 0 se não houver): ");
        int idUsuario = ConsoleInputUtils.lerInt("Escolha: ");
        
        Integer idUsuarioFinal = (idUsuario == 0) ? null : idUsuario;

        return new Vendedor(0, nome, cpf, contato, salario, dataContratacao, idUsuarioFinal);
    }
     // Método auxiliar para validar a data com tratamento de erro
    private LocalDate lerDataValida() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            String dataStr = ConsoleInputUtils.lerString();
            try {
                return LocalDate.parse(dataStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Informe no formato AAAA-MM-DD.");
            }
        }
    }

    public void exibirListaVendedores(List<Vendedor> lista) {
        for (Vendedor v : lista) {
            System.out.println(v);
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
