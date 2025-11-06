package com.blogcristao.servlet;

import com.blogcristao.servlet.validators.ValidatorPostApiServlet;
import com.google.gson.*;
import com.google.gson.internal.Streams;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PostUtils {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>)(src, _, _)
            -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))).create();
    ValidatorPostApiServlet validator = new ValidatorPostApiServlet();
    public void enviarRepostaJson(HttpServletResponse response, Object objetoJson, int status) throws IOException {
        String json = gson.toJson(objetoJson);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        if (json.equals("[]")){
            response.getWriter().write("{\"Error:\": \"Nenhum dado encontrado\"}");
        }
        else{
            response.getWriter().write(gson.toJson(objetoJson));
        }
        writer.flush();  // Garante que tudo foi enviado
        writer.close();
    }
}
