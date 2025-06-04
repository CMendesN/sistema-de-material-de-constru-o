package com.materialsystem.entity;

public class Fabricante {
    private int idFabricante;
    private String nomeFabricante;
    private String contato;
    private String endereco;

    public Fabricante() {}

    public Fabricante(int idFabricante, String nomeFabricante, String contato, String endereco) {
        this.idFabricante = idFabricante;
        this.nomeFabricante = nomeFabricante;
        this.contato = contato;
        this.endereco = endereco;
    }

    public int getIdFabricante() { return idFabricante; }
    public void setIdFabricante(int idFabricante) { this.idFabricante = idFabricante; }

    public String getNomeFabricante() { return nomeFabricante; }
    public void setNomeFabricante(String nomeFabricante) { this.nomeFabricante = nomeFabricante; }

    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    @Override
    public String toString() {
        return "Fabricante{" +
                "idFabricante=" + idFabricante +
                ", nomeFabricante='" + nomeFabricante + '\'' +
                ", contato='" + contato + '\'' +
                ", endereco='" + endereco + '\'' +
                '}';
    }
}
