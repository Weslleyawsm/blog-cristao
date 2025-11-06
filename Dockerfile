# STAGE 1: Compilar a aplicação com Maven
FROM maven:3.9-eclipse-temurin-25 AS builder

# Copiar arquivos do projeto
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY lib ./lib
COPY web ./web

# Compilar o projeto
RUN mvn clean package -DskipTests

# STAGE 2: Usar imagem oficial do Tomcat
FROM tomcat:10.1-jdk21

# Remover aplicações padrão
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar o WAR compilado
COPY --from=builder /app/target/blog-cristao.war /usr/local/tomcat/webapps/ROOT.war

# Expor porta
EXPOSE 8080

# Iniciar Tomcat
CMD ["catalina.sh", "run"]