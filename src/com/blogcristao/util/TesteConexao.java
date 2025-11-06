package com.blogcristao.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.blogcristao.dao.postDAO;
import com.blogcristao.model.Post;

public class TesteConexao {
    public static void main(String[] args){
        try{
            System.out.println("=== Testando conexao ===");
            Connection conn = ConexaoDB.getConexao();
            System.out.println("Conectado com sucesso! Banco: " + conn.getCatalog());
            postDAO postDAO = new postDAO();
            System.out.println("Post " + postDAO.listarPorId(1));
            Post post = new Post();
            post.setId(1);
            post.setTitulo("TESTE");
            post.setResumo("Eden");
            post.setConteudo("Gênesis 3");
            post.setCategoria("Exposição Bíblica");
            post.setTempo_leitura(5);
            System.out.println("Deletando Post " + postDAO.excluirPostId(2));
            System.out.println("Editando Post " + postDAO.editarPost(post));
            List<Post> posts = new ArrayList<>();
            posts.addAll(postDAO.filtroCategoria("Reforma Protestante"));

            for (int i=0; i<posts.size(); i++){
                System.out.println("id: " + posts.get(i).getId());
                System.out.println("titulo: " + posts.get(i).getTitulo());
                System.out.println("resumo: " + posts.get(i).getResumo());
                System.out.println("conteudo: " + posts.get(i).getConteudo());
                System.out.println("categoria: " + posts.get(i).getCategoria());
                System.out.println("tempo de leitura: " + posts.get(i).getTempo_leitura());
                System.out.println("imagem: " + posts.get(i).getImagem_url());
            }
            List<Post> posts2 = new ArrayList<>();
            posts2.addAll(postDAO.paginacao(0));
            System.out.println("Paginação: ");
            for (int i=0; i<posts2.size(); i++){
                System.out.println("id: " + posts2.get(i).getId());
                System.out.println("titulo: " + posts2.get(i).getTitulo());
                System.out.println("resumo: " + posts2.get(i).getResumo());
                System.out.println("conteudo: " + posts2.get(i).getConteudo());
                System.out.println("categoria: " + posts2.get(i).getCategoria());
                System.out.println("tempo de leitura: " + posts2.get(i).getTempo_leitura());
                System.out.println("imagem: " + posts2.get(i).getImagem_url());
            }
        }
        catch (SQLException e){
            System.out.println("Erro ao conectar com o banco de dados: " + e.getMessage());
        }
    }
}
