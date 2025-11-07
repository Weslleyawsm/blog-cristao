# STAGE 1: Compilar com Java 25
FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY lib ./lib
COPY web ./web

RUN mvn clean package -DskipTests

# STAGE 2: Usar Java 25 + Tomcat
FROM eclipse-temurin:25-jdk-jammy

# Variáveis
ENV TOMCAT_VERSION=10.1.33
ENV CATALINA_HOME=/opt/tomcat

# Instalar Tomcat manualmente
RUN apt-get update && \
    apt-get install -y wget && \
    wget https://archive.apache.org/dist/tomcat/tomcat-10/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    tar xvf apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    mv apache-tomcat-${TOMCAT_VERSION} ${CATALINA_HOME} && \
    rm apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    rm -rf ${CATALINA_HOME}/webapps/* && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copiar WAR
COPY --from=builder /app/target/blog-cristao.war ${CATALINA_HOME}/webapps/ROOT.war

# Expor porta
EXPOSE 8080

# Iniciar Tomcat
CMD ["/opt/tomcat/bin/catalina.sh", "run"]