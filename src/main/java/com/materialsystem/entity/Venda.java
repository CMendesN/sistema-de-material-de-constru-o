package com.materialsystem.entity;

import java.time.LocalDateTime;

public class Venda {
    private int idVenda;
    private LocalDateTime dataVenda;
    private Integer idVendedor;
    private Integer idComprador;
    private double valorTotal;

    public Venda() {}

    public Venda(int idVenda, LocalDateTime dataVenda, Integer idVendedor, Integer idComprador, double valorTotal) {
        this.idVenda = idVenda;
        this.dataVenda = dataVenda;
        this.idVendedor = idVendedor;
        this.idComprador = idComprador;
        this.valorTotal = valorTotal;
    }

    public int getIdVenda() { return idVenda; }
    public void setIdVenda(int idVenda) { this.idVenda = idVenda; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    public Integer getIdVendedor() { return idVendedor; }
    public void setIdVendedor(Integer idVendedor) { this.idVendedor = idVendedor; }

    public Integer getIdComprador() { return idComprador; }
    public void setIdComprador(Integer idComprador) { this.idComprador = idComprador; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
}
