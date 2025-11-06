package com.blogcristao.dao;
import com.blogcristao.util.ConexaoDB;
import com.blogcristao.model.Post;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.util.Objects;

class UtilsDAO {
    public List<Post> PostAddList(ResultSet rs, List<Post> posts) throws SQLException {
        while(rs.next()){ //enquanto tiver linhas...
            Post post = new Post();
            post.setId(rs.getInt("id"));
            post.setTitulo(rs.getString("titulo"));
            post.setResumo(rs.getString("resumo"));
            post.setConteudo(rs.getString("conteudo"));
            post.setCategoria(rs.getString("categoria"));
            post.setTempo_leitura(rs.getInt("tempo_leitura"));
            post.setImagem_url(rs.getString("imagem_url"));
            posts.add(post);
        }
        return posts;
    }
}

public class postDAO {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    public List<Post> listarTodos() throws SQLException {
        List<Post> posts = new ArrayList<>();

        String sql = "SELECT * FROM posts ORDER BY data_criacao DESC";
        Connection conn = null;//define um varivel do tipo connection
        Statement stmt = null;//define um varivel que representa um comando sql
        ResultSet rs = null;//representa uma lista de dados de retorno

        try{
            conn = ConexaoDB.getConexao(); //cria a conexão
            stmt = conn.createStatement(); //cria o comando
            rs = stmt.executeQuery(sql); //executa o comando e retorna uma lista com os resultados
            UtilsDAO utilsDAO = new UtilsDAO();
            posts = utilsDAO.PostAddList(rs, posts);
        }
        finally {
            if(rs != null){
                rs.close();
            }
            if(stmt != null){
                stmt.close();
            }
            if(conn != null){
                conn.close();
            }

        }
        return posts;
    }
    public Post listarPorId(int id) throws SQLException {

        Post post = new Post();
        String sql = "SELECT * FROM posts WHERE id = ?";

        Connection conn = null;
        PreparedStatement  stmt = null;
        ResultSet rs = null;

        try{
            conn = ConexaoDB.getConexao();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if(rs.next()) {
                post.setId(rs.getInt("id"));
                post.setTitulo(rs.getString("titulo"));
                post.setResumo(rs.getString("resumo"));
                post.setConteudo(rs.getString("conteudo"));
                post.setCategoria(rs.getString("categoria"));
                post.setTempo_leitura(rs.getInt("tempo_leitura"));
                post.setImagem_url(rs.getString("imagem_url"));
            }
        }
        finally {
            if(rs != null){
                rs.close();
            }
            if (stmt != null){
                stmt.close();
            }
            if (conn != null){
                conn.close();
            }
        }
        return post;
    }

    public Post criarPost(Post post) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "INSERT INTO posts (titulo, resumo, conteudo, categoria, tempo_leitura, imagem_url) " +
                "VALUES (?, ?, ?, ?, ?, ?) ";

        try{
            conn = ConexaoDB.getConexao();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, post.getTitulo());
            stmt.setString(2, post.getResumo());
            stmt.setString(3, post.getConteudo());
            stmt.setString(4, post.getCategoria());
            stmt.setInt(5, post.getTempo_leitura());
            stmt.setString(6, post.getImagem_url());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if(rs.next()){
                int idGerado = rs.getInt(1);
                post.setId(idGerado);
                System.out.println("✅ Post criado com ID: " + idGerado);
            }
        }
        finally {
            if (conn != null){
                conn.close();
            }
            if (stmt != null){
                stmt.close();
            }
            if (rs != null){
                rs.close();
            }
        }
        return  post;
    }

    public Integer excluirPostId(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        String sql = "DELETE FROM posts WHERE id = ?";
        int linhasAfetadas = 0;
        try {
            conn = ConexaoDB.getConexao();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Linhas afetadas: " + linhasAfetadas);
            } else {
                System.out.println("Não há registros com esse id!!");

            }

        }
        finally {
            if (conn != null){
                conn.close();
            }
            if (stmt != null){
                stmt.close();
            }

        }
        return linhasAfetadas;

    }

    public boolean editarPost(Post post) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        StringBuilder sql = new StringBuilder("UPDATE posts SET "); //use StingBilder qundo for fazer add com Strings...
        List<Object> parametros = new ArrayList<>();
        boolean primeiro = true;

        try{
            if(post.getTitulo() !=null){
                if (!primeiro){sql.append(", ");}
                sql.append("titulo = ?");
                parametros.add(post.getTitulo());
                primeiro = false;
            }
            if(post.getResumo() !=null){
                if (!primeiro){sql.append(", ");}
                sql.append("resumo = ?");
                parametros.add(post.getResumo());
                primeiro = false;
            }
            if(post.getConteudo() !=null){
                if (!primeiro){sql.append(", ");}
                sql.append("conteudo = ?");
                parametros.add(post.getConteudo());
                primeiro = false;
            }
            if(post.getCategoria() !=null){
                if (!primeiro){sql.append(", ");}
                sql.append("categoria = ?");
                parametros.add(post.getCategoria());
                primeiro = false;
            }
            if(post.getTempo_leitura() != 0){
                if (!primeiro){sql.append(", ");}
                sql.append("tempo_leitura = ?");
                parametros.add(post.getTempo_leitura());
                primeiro = false;
            }
            if(post.getImagem_url() !=null){
                if (!primeiro){sql.append(", ");}
                sql.append("imagem_url = ?");
                parametros.add(post.getImagem_url());
                primeiro = false;
            }
            if (primeiro){
                return false;
            }
            sql.append(" WHERE id = ?");
            parametros.add(post.getId());
            conn = ConexaoDB.getConexao(); //abra a conexão!
            stmt = conn.prepareStatement(sql.toString()); //prepare a QUERY!!
            for (int i = 0; i < parametros.size(); i++) {
                stmt.setObject(i + 1, parametros.get(i));
            }
            int linhsAfetadas = stmt.executeUpdate(); //E executa  QUERY
            if  (linhsAfetadas > 0) {
                System.out.println("Linhas afetadas: " + linhsAfetadas);
            }
            else{
                System.out.println("Nenhum linha afetadas!!");
            }
        }
        finally{
            //FECHA TUDO!!
            if(stmt != null){
                stmt.close();
            }
            if(conn != null){
                conn.close();
            }
        }
        return true;
    }
    public List<Post> filtroCategoria(String categoria) throws SQLException {
        List<Post> posts = new ArrayList<>();
        try {
            conn = ConexaoDB.getConexao();
            String Sql = "SELECT * FROM posts WHERE categoria = ? ORDER BY id DESC";
            stmt = conn.prepareStatement(Sql);
            stmt.setString(1, categoria);
            rs =  stmt.executeQuery();
            UtilsDAO utilsDAO = new UtilsDAO();
            posts = utilsDAO.PostAddList(rs, posts);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return posts;
    }

    public List<Post> paginacao(int offset) throws SQLException {
        List<Post> posts = new ArrayList<>();
        conn = null;
        stmt = null;
        rs = null;
        String Sql = "SELECT * FROM posts ORDER BY id DESC LIMIT 5 OFFSET ?";
        try {
            conn = ConexaoDB.getConexao();
            stmt = conn.prepareStatement(Sql);
            stmt.setInt(1, offset);
            rs =  stmt.executeQuery();
            UtilsDAO utilsDAO = new UtilsDAO();
            posts = utilsDAO.PostAddList(rs, posts);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return posts;
    }

}