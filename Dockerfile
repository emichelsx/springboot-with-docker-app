FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080

# Build imagem
# $ docker build -f Dockerfile -t $(whoami)/springboot-with-docker-app .

# Verificar se a imagem criada
# $ docker images

# $ docker run -d -p 8080:8080 -v "trocar_pelo_path_do_direto_do_volume:/var/www" $(whoami)/springboot-with-docker-app
