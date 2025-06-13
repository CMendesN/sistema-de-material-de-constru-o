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
        LocalDate dataContratacao = ConsoleInputUtils.lerDataValida();


        return new Vendedor(0, nome, cpf, contato, salario, dataContratacao);
    }

    public void exibirListaVendedores(List<Vendedor> lista) {
        for (Vendedor v : lista) {
            System.out.println(v);
        }
    }
    
    public Vendedor solicitarDadosAtualizacao(Vendedor vendedorExistente) {
        System.out.println("Atualizando vendedor ID: " + vendedorExistente.getIdVendedor());

        System.out.print("Nome [" + vendedorExistente.getNome() + "]: ");
        String nomeInput = ConsoleInputUtils.lerString();
        String nome = nomeInput.isBlank() ? vendedorExistente.getNome() : nomeInput;

        System.out.print("CPF [" + vendedorExistente.getCpf() + "]: ");
        String cpfInput = ConsoleInputUtils.lerString();
        String cpf = cpfInput.isBlank() ? vendedorExistente.getCpf() : cpfInput;

        System.out.print("Contato [" + vendedorExistente.getContato() + "]: ");
        String contatoInput = ConsoleInputUtils.lerString();
        String contato = contatoInput.isBlank() ? vendedorExistente.getContato() : contatoInput;

        System.out.print("Salário [" + vendedorExistente.getSalario() + "]: ");
        String salarioInput = ConsoleInputUtils.lerString();
        double salario;
        try {
            salario = salarioInput.isBlank() ? vendedorExistente.getSalario() : Double.parseDouble(salarioInput);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Usando salário atual.");
            salario = vendedorExistente.getSalario();
        }

        System.out.print("Data de contratação [" + vendedorExistente.getDataContratacao() + "] (AAAA-MM-DD): ");
        String dataInput = ConsoleInputUtils.lerString();
        LocalDate dataContratacao;
        try {
            dataContratacao = dataInput.isBlank() ?
                    vendedorExistente.getDataContratacao() :
                    LocalDate.parse(dataInput, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida. Usando data atual.");
            dataContratacao = vendedorExistente.getDataContratacao();
        }



        return new Vendedor(vendedorExistente.getIdVendedor(), nome, cpf, contato, salario, dataContratacao);
    }


    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }
}
