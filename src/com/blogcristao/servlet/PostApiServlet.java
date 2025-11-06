package com.blogcristao.servlet;

import com.blogcristao.dao.postDAO;
import com.blogcristao.model.Post;
import com.google.gson.*;
import com.blogcristao.servlet.validators.ValidatorPostApiServlet;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@WebServlet("/api/posts/*")
public class PostApiServlet extends HttpServlet {
    postDAO dao = new postDAO();
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)(src, _, _)
            -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))).create();
    ValidatorPostApiServlet validator = new ValidatorPostApiServlet();

    private void enviarRepostaJson(HttpServletResponse response, Object objetoJson, int status) throws IOException {
        String json = gson.toJson(objetoJson);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        if (json.equals("[]")){
            response.getWriter().write("{\"Error:\": \"Nenhum dado encontrado\"}");
        }
        else{
            response.getWriter().write(gson.toJson(objetoJson));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        configurarCORS(response);
        try {
            if (validator.ValidateHome(request.getPathInfo())) {//se passar na validação (true) pode listar todos
                enviarRepostaJson(response, dao.listarTodos(), 200);
            }
            else {
                if (validator.ValidateListaPorID(request.getPathInfo()) != null) {
                    enviarRepostaJson(response, validator.ValidateListaPorID(request.getPathInfo()), 200);
                }
                else {
                    MensagensError.handleJsonError(response, 400, "Nenhum dado encontrado");
                }
            }
        } catch (SQLException e) {
            MensagensError.handleJsonError(response, 500, e.getMessage());
        }
        catch (NumberFormatException e) {
            MensagensError.handleJsonError(response, 400, e.getMessage());
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        configurarCORS(response);
        String pathInfo = request.getPathInfo();
        try{
            if (!validator.ValidateToDelete(pathInfo)) {
                MensagensError.handleJsonError(response, 400, "ID não fornecido");
                return;
            }
            if (validator.ValidateToDelete(pathInfo)){
                int idString = Integer.parseInt(pathInfo.substring(1));
                dao.excluirPostId(idString);
                response.getWriter().write("{\"mensagem\": \"Post Deletado com Sucesso!!\"}");
                enviarRepostaJson(response, dao.listarTodos(), 200);
            }
        }
        catch (SQLException e) {
            MensagensError.handleJsonError(response, 400, e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        configurarCORS(response);
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(400);
        }
        Post post = null;
        try {

            post = gson.fromJson(request.getReader(), Post.class);
            try{
                dao.criarPost(post);
                response.setStatus(201);
                response.getWriter().write("{\"mensagem\": \"Post criado!\"}");
            }
            catch(Exception e) {
                response.setStatus(400);
            }
        }
        catch (Exception e) {
            response.setStatus(400);
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configurarCORS(response);
        String pathInfo = request.getPathInfo();
        Post postJson = null;
        Post postPut = null;
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(400);
        }
        postJson = gson.fromJson(request.getReader(), Post.class);
        try{
            dao.editarPost(postJson);
            response.setStatus(200);
            response.getWriter().write(postJson.toString());
        }
        catch (Exception e) {
            response.setStatus(400);
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