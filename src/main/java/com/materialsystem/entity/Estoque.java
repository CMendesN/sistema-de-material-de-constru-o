package com.materialsystem.entity;

public class Estoque {
    private int idEstoque;
    private String localizacao;
    private double capacidade;

    public Estoque() {}

    public Estoque(int idEstoque, String localizacao, double capacidade) {
        this.idEstoque = idEstoque;
        this.localizacao = localizacao;
        this.capacidade = capacidade;
    }

    public int getIdEstoque() { return idEstoque; }
    public void setIdEstoque(int idEstoque) { this.idEstoque = idEstoque; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public double getCapacidade() { return capacidade; }
    public void setCapacidade(double capacidade) { this.capacidade = capacidade; }

    @Override
    public String toString() {
        return "Estoque{" +
                "idEstoque=" + idEstoque +
                ", localizacao='" + localizacao + '\'' +
                ", capacidade=" + capacidade +
                '}';
    }
}
