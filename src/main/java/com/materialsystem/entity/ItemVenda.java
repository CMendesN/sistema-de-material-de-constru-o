package com.materialsystem.entity;

public class ItemVenda {
    private int idItemVenda;
    private int idVenda;
    private int idProduto;
    private int quantidade;
    private double precoUnitarioVenda;

    public ItemVenda() {}

    public ItemVenda(int idItemVenda, int idVenda, int idProduto, int quantidade, double precoUnitarioVenda) {
        this.idItemVenda = idItemVenda;
        this.idVenda = idVenda;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.precoUnitarioVenda = precoUnitarioVenda;
    }

    public int getIdItemVenda() { return idItemVenda; }
    public void setIdItemVenda(int idItemVenda) { this.idItemVenda = idItemVenda; }

    public int getIdVenda() { return idVenda; }
    public void setIdVenda(int idVenda) { this.idVenda = idVenda; }

    public int getIdProduto() { return idProduto; }
    public void setIdProduto(int idProduto) { this.idProduto = idProduto; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double getPrecoUnitarioVenda() { return precoUnitarioVenda; }
    public void setPrecoUnitarioVenda(double precoUnitarioVenda) { this.precoUnitarioVenda = precoUnitarioVenda; }

    @Override
    public String toString() {
        return "ItemVenda {idItemVenda=" + idItemVenda + ", idVenda=" + idVenda + ", idProduto=" + idProduto
                + ", quantidade=" + quantidade + ", precoUnitarioVenda=" + precoUnitarioVenda + "}";
    }
    
}
