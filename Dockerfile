# Usar Java 25 + baixar Tomcat manualmente
FROM eclipse-temurin:25-jdk-jammy

# Instalar Tomcat
ENV TOMCAT_VERSION=10.1.28
RUN apt-get update && apt-get install -y wget && \
    wget https://dlcdn.apache.org/tomcat/tomcat-10/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    tar xvf apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    mv apache-tomcat-${TOMCAT_VERSION} /opt/tomcat && \
    rm apache-tomcat-${TOMCAT_VERSION}.tar.gz && \
    rm -rf /opt/tomcat/webapps/*

# Copia o WAR
COPY target/blog-cristao.war /opt/tomcat/webapps/ROOT.war

# Variável de ambiente
ENV CATALINA_HOME=/opt/tomcat

# Expõe porta
EXPOSE 8080

# Inicia Tomcat
CMD ["/opt/tomcat/bin/catalina.sh", "run"]