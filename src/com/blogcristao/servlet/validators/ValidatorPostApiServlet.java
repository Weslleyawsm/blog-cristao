package com.blogcristao.servlet.validators;

import com.blogcristao.model.Post;
import com.blogcristao.dao.postDAO;

import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

public class ValidatorPostApiServlet {
    private boolean Validator = false;
    private postDAO dao = new postDAO();
    public boolean ValidateHome(String pathInfo) throws SQLException {
        Validator = false;
        if (pathInfo == null || pathInfo.equals("/")){ //dps de uma estudada do pq deve ser nessa ordem...
            postDAO dao = new postDAO();
            Validator = dao.listarTodos() != null;
        }
        return Validator;
    }
    public List<Post> ValidateListaPorID(String pathInfo) throws SQLException {
        //esse validador pega o caminho da url, faz uma limpeza na url, após isso, se sobrar um número acima de zero (bd não tem indice 0!) irá retornar o post com o id.
        boolean Validator = true;
        String parteNumericaString = pathInfo.replace("/", "");
        int parteNumericaInt = Integer.parseInt(parteNumericaString);
        List<Post> posts = null;
        try {
            posts = Collections.singletonList(dao.listarPorId(parteNumericaInt));
            Post post = posts.getFirst();
            if (post.getId() == 0){
                posts = null;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return posts;
    }
    public boolean ValidateToDelete(String pathInfo) throws SQLException {
        boolean Validator = true;
        if (pathInfo.equals("/")){
            Validator = false;
        }
        else {
            int idString = Integer.parseInt(pathInfo.substring(1));
            Validator = idString != 0;
        }
        return Validator;
    }
    public boolean ValidatePost(BufferedReader body) throws SQLException {
        boolean Validator = false;
        JsonElement json = JsonParser.parseReader(body);
        JsonObject jsonObject = json.getAsJsonObject();
        return Validator;
    }
}

