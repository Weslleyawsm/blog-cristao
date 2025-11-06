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
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@WebServlet(urlPatterns = {"/usuario", "/home","/post/*"})
public class Paginas extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String contextPath = request.getContextPath(); //somente a primeiro caminho, o "principal"
        String path = request.getRequestURI(); //pega todo o caminho

        String pagina = path.substring(contextPath.length()); //retorna somente o que vier dps de contextPath

        if (pagina.equals("/usuario") || pagina.equals("/home")){
            OpenPaginas(request, response, "/blog-cristao-usuario.html");
        }
        else if(pagina.startsWith("/post/")) { //vai verificar se a pagina começa com "/post/", se sim, abre um arquivo html abaixo.
            OpenPaginas(request, response, "/blog-cristao-post.html"); //obs: essa pagina somente vai abrir. a url irá permanecer a mesma!
        }
        else{
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Post não encontrado");
            }
    }

    public String OpenPaginas (HttpServletRequest request, HttpServletResponse response, String file)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        String htmlFilePath = getServletContext().getRealPath(file); //getRealPath() pega o caminho absoluto do aquivo no servidor
        //getServletContext() é usado como armario para armazenar o arquivo, no caso file...por isso getServletContext().getRealPath(file);
        try{
            String htmlContent = new String(Files.readAllBytes(Paths.get(htmlFilePath))); //Paths.get() vai pegar o caminho do arquivo
            //Files.readAllBytes() vai ler o arquivo em Bytes

            out.println(htmlContent); //Vai enviar o arquivo em Bytes pro navegador.
        }
        catch (IOException e){
            out.println("<h1>Erro ao carregar a página HTML.</h1>");
            e.printStackTrace();
        }
        finally {
            // Fecha o PrintWriter
            out.close();
        }
        return htmlFilePath;
    }
}

//O Processo de Atendimento de uma Requisição Web (Comandos Correspondentes)

//1. Mapeamento e Espera
//O servidor web sabe qual classe Java deve ser executada quando um URL específico é acessado.
//Comando: @WebServlet("/minha-pagina")
//Explicação: Esta anotação "registra" o Servlet no servidor para que ele responda ao caminho /minha-pagina.


//2. Recebendo e Preparando a Resposta
//O método principal que lida com requisições do tipo GET é acionado. Configuramos a resposta antes de enviar qualquer dado.
//Comando: protected void doGet(...)
//Explicação: Este método é o ponto de entrada que o servidor chama quando recebe uma requisição HTTP GET para o URL mapeado.
//Comando: response.setContentType("text/html;charset=UTF-8");
//Explicação: Define o cabeçalho HTTP Content-Type, informando ao navegador que o que está por vir é HTML e deve ser interpretado usando a codificação UTF-8.


//3. Lendo o Arquivo HTML
//O código localiza e lê o arquivo HTML estático armazenado no sistema de arquivos do servidor.
//Comando: String htmlFilePath = getServletContext().getRealPath("/index.html");
//Explicação: Encontra o caminho completo e absoluto do arquivo index.html no disco rígido do servidor.
//Comando: String htmlContent = new String(Files.readAllBytes(Paths.get(htmlFilePath)));
//Explicação: Lê todo o conteúdo do arquivo no caminho especificado e armazena o texto lido em uma variável (htmlContent).


//4. Enviando a Resposta ao Navegador
//O conteúdo lido é finalmente transmitido de volta ao cliente (navegador).
//Comando: PrintWriter out = response.getWriter();
//Explicação: Obtém o objeto PrintWriter, que é a ferramenta usada para escrever dados no "corpo" da resposta HTTP.
//Comando: out.println(htmlContent);
//Explicação: Escreve a string contendo o HTML no fluxo de saída, enviando-a para o navegador do usuário.
//Comando: out.close();
//Explicação: Fecha o fluxo de comunicação, sinalizando ao servidor e ao navegador que a resposta foi concluída.