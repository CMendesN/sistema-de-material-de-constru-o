package com.materialsystem.entity;

import java.time.LocalDate;

public class Vendedor {
    private int idVendedor;
    private String nome;
    private String cpf;
    private String contato;
    private double salario;
    private LocalDate dataContratacao;
    

    public Vendedor() {}

    public Vendedor(int idVendedor, String nome, String cpf, String contato, double salario, LocalDate dataContratacao) {
        this.idVendedor = idVendedor;
        this.nome = nome;
        this.cpf = cpf;
        this.contato = contato;
        this.salario = salario;
        this.dataContratacao = dataContratacao;
    }

    public int getIdVendedor() { return idVendedor; }
    public void setIdVendedor(int idVendedor) { this.idVendedor = idVendedor; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public LocalDate getDataContratacao() { return dataContratacao; }
    public void setDataContratacao(LocalDate dataContratacao) { this.dataContratacao = dataContratacao; }


}
