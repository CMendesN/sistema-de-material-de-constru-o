package com.materialsystem.entity;

public class ProdutoEstoque {
    private int idProduto;
    private int idEstoque;
    private int quantidade;

    public ProdutoEstoque() {}

    public ProdutoEstoque(int idProduto, int idEstoque, int quantidade) {
        this.idProduto = idProduto;
        this.idEstoque = idEstoque;
        this.quantidade = quantidade;
    }

    public int getIdProduto() { return idProduto; }
    public void setIdProduto(int idProduto) { this.idProduto = idProduto; }

    public int getIdEstoque() { return idEstoque; }
    public void setIdEstoque(int idEstoque) { this.idEstoque = idEstoque; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    @Override
    public String toString() {
        return "ProdutoEstoque{" +
                "idProduto=" + idProduto +
                ", idEstoque=" + idEstoque +
                ", quantidade=" + quantidade +
                '}';
    }
}
