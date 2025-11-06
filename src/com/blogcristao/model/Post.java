package com.blogcristao.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//- int id
//- String titulo
//- String resumo
//- String conteudo
//- String categoria
//- LocalDateTime data_criacao
//- int tempo_leitura
//- String imagem_url

//Representa um post do blog
public class Post {
    private int id;
    private String titulo;
    private String resumo;
    private String conteudo;
    private String categoria;
    private LocalDateTime data_criacao;
    private int tempo_leitura;
    private String imagem_url;

    public Post(int id,
                String titulo,
                String resumo,
                String conteudo,
                String categoria,
                LocalDateTime dataCriacao,
                int tempoLeitura,
                String imagemUrl) {
        this.id = id;
        this.titulo = titulo;
        this.resumo = resumo;
        this.conteudo = conteudo;
        this.categoria = categoria;
        this.data_criacao = dataCriacao;
        this.tempo_leitura = tempoLeitura;
        this.imagem_url = imagemUrl;
    }
    public Post(
                String titulo,
                String resumo,
                String conteudo,
                String categoria,
                LocalDateTime dataCriacao,
                int tempoLeitura,
                String imagemUrl) {
        this.titulo = titulo;
        this.resumo = resumo;
        this.conteudo = conteudo;
        this.categoria = categoria;
        this.data_criacao = dataCriacao;
        this.tempo_leitura = tempoLeitura;
        this.imagem_url = imagemUrl;
    }
    public Post(){
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(LocalDateTime data_criacao) {
        this.data_criacao = data_criacao;
    }

    public int getTempo_leitura() {
        return tempo_leitura;
    }

    public void setTempo_leitura(int tempo_leitura) {
        this.tempo_leitura = tempo_leitura;
    }

    public String getImagem_url() {
        return imagem_url;
    }

    public void setImagem_url(String imagem_url) {
        this.imagem_url = imagem_url;
    }

    public String getDataFormatada(){
        if (this.data_criacao != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return this.data_criacao.format(formatter);
        }
        return "";
    }
}
