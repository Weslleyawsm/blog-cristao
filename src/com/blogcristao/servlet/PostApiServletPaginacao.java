package com.blogcristao.servlet;
import com.blogcristao.dao.postDAO;
import com.blogcristao.model.Post;
import com.blogcristao.servlet.validators.ValidatorPostApiServletPaginacao;
import com.google.gson.*;


import com.blogcristao.servlet.PostUtils.*;
import com.blogcristao.servlet.MensagensError;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.xml.validation.Validator;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@WebServlet("/api/posts/paginacao/*")
public class PostApiServletPaginacao extends HttpServlet{
    private final postDAO dao = new postDAO();
    private final PostUtils postUtils = new PostUtils();
    private final ValidatorPostApiServletPaginacao Validator = new ValidatorPostApiServletPaginacao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configurarCORS(response);
        String pathInfo = request.getPathInfo();
        if (pathInfo != null){
            String[] IndexString = pathInfo.split("/");
            try {
                if (IndexString[1].equals("categoria")) {//verifica se o segundo index do caminho é "categoria".
                    postUtils.enviarRepostaJson(response, dao.filtroCategoria(IndexString[2]), 200); //se for deve ser retornado a categoria selecionada
                }
                else if (Validator.ValidarInteger(Integer.parseInt(IndexString[1]))){ // senao for "categoria", é paginacao por padrão, então valida o offset passado.
                    postUtils.enviarRepostaJson(response, dao.paginacao(Integer.parseInt(IndexString[1])), 200); //se passou, processa a reposta e envia para o cliente.
                }
                else {
                    MensagensError.handleJsonError(response, 422, "Dado passado inconsistente");
                }
            }
            catch (SQLSyntaxErrorException e){
                MensagensError.handleJsonError(response, 400, e.getMessage());
            }
            catch (SQLException e) {
                MensagensError.handleJsonError(response, 500, e.getMessage());
            }
        }
    }
    private void configurarCORS(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Max-Age", "300");
    }
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        configurarCORS(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
