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

# STAGE 2: Criar imagem final com Tomcat
FROM eclipse-temurin:25-jdk-jammy

# Instalar Tomcat
ENV TOMCAT_VERSION=10.1.28
RUN apt-get update && apt-get install -y wget && \
    wget https://dlcdn.apache.org/tomcat/tomcat-10/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    tar xvf apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    mv apache-tomcat-${TOMCAT_VERSION} /opt/tomcat && \
    rm apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    rm -rf /opt/tomcat/webapps/*

# Copiar o WAR compilado do stage anterior
COPY --from=builder /app/target/blog-cristao.war /opt/tomcat/webapps/ROOT.war

# Variável de ambiente
ENV CATALINA_HOME=/opt/tomcat

# Expõe porta
EXPOSE 8080

# Inicia Tomcat
CMD ["/opt/tomcat/bin/catalina.sh", "run"]