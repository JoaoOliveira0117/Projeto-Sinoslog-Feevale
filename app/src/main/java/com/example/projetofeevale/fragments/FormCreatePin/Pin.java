package com.example.projetofeevale.fragments.FormCreatePin;

public class Pin {
    private int id;
    private byte[] imagemBytes; // Alterado para armazenar os bytes da imagem em vez do caminho do arquivo
    private String endereco;
    private String tipo;
    private String dataHora;
    private String titulo;
    private String descricao;

    public Pin(int id, byte[] imagemBytes, String endereco, String tipo, String dataHora, String titulo, String descricao) {
        this.id = id;
        this.imagemBytes = imagemBytes;
        this.endereco = endereco;
        this.tipo = tipo;
        this.dataHora = dataHora;
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public byte[] getImagemBytes() {
        return imagemBytes;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDataHora() {
        return dataHora;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }
}
