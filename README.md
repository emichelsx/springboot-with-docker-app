# springboot-with-docker-app

### Objetivo
É um guia introdutório para trabalhar com uma aplicação em springboot dentro de um docker container.

### Pré-requisitos
Possuir o docker instalado na maquina local. **[Manual de instalação Docker para Linux](https://github.com/emichelsx/linux-docker-installation-manual).**

Java na versão 8 (caso queira construir manualmente a imagem)

Maven (caso queira construir manualmente a imagem)

### Criação da imagem docker do zero
1. Baixe este repositório
2. Acesse o pasta do repositório na sua maquina local e execute os comandos:
```
mvn clear install
mvn package
```
3. Crie a imagem do docker:
```
docker build -f Dockerfile -t $(whoami)/springboot-with-docker-app .
```
- Obs(1): o script _$(whoami)_ deve trazer o nome do usuário logado para compor o nome da imagem, porem pode ser substituido por qualquer valor fixo como fulano/springboot-with-docker-app
- Obs(2): caso possua apenas um arquivo Dockerfile não é necessário informar o parâmentro -f
- Obs(3): podemos ter varios arquivos Dockerfile e podemos nomeá-los como node.dockefile, ngix.dockerfile, meuapp.dockerfile, etc, devemos informar explicitamente o parametro -f passando o nome do arquivo.
- Obs(4): o ponto ( . ) no final do comando indica aonde está o Dockerfile, e como estamos executando da mesma pasta utilizamos ponto.

4. Verifique a imagem:
```
$ docker images
REPOSITORY                              TAG                 IMAGE ID            CREATED             SIZE
emichels/springboot-with-docker-app     latest              6b928e9a96dd        4 hours ago         123MB
```

### O que é o Dockerfile
É o arquivo que contem a receita para criação da nosso imagem do docker.

```
FROM openjdk:8-jdk-alpine               > imagem base que vamos reaproveitar para criação nossa imagem
MAINTAINER Eduardo Michels              > responsável por manter a imagem
ARG JAR_FILE=target/*.jar               > criação de uma variável que pode ser acessada somente durante a criação da imagem
COPY ${JAR_FILE} app.jar                > copia o .jar referenciado pela variável **JAR_FILE**, que neste caso é o nosso app em springboot
ENTRYPOINT ["java","-jar","/app.jar"]   > comando que será executado assim que o container estiver pronto
EXPOSE 8080                             > expõe a porta 8080 para ser acessada de fora do container
```
### Executando o container
Como a imagem criada, podemos finalmente executar uma container apartir dela:
```
docker run -d -p 8080:8080 emichels/springboot-with-docker-app
```
- Obs(1): emichels/springboot-with-docker-app é nome definido na criação da imagem, substituir pelo nome definido por você
- Obs(2): -d vai fazer com que o processo de execução do container seja executado em background
- Obs(3): caso queria visualizar o logs de execução de container você pode omitit o parametro -d
- Obs(4): os containers possuem um rede própria e para conseguir acessar os recursos de fora da rede do container devemos fazer o mapeamento PORTA_LOCAL:PORTA_CONTAINER

### Teste do container
Podemos listar todos os containers ativos com o comando:
```
docker ps
```
Obs: passando o parametro -a podemos visualizar containers parados

Se o nosso container estiver listado nos containers ativos, devemos conseguir acessar a nossa aplicação springboot 
http://localhost:8080/springboot-with-docker-app

### Volume de dados
Quando escrevemos em um container, assim que ele for removido, os dados também serão. Mas podemos criar um local especial dentro dele, e especificamos que esse local será o nosso volume de dados.

Quando criamos um volume de dados, o que estamos fazendo é apontá-lo para uma pequena pasta no Docker Host. Então, quando criamos um volume, criamos uma pasta dentro do container, e o que escrevermos dentro dessa pasta na verdade estaremos escrevendo do Docker Host, patrica é como uma pasta compartilhada entro docker e a sua maquina local.

Isso faz com que não percamos os nossos dados, pois o container até pode ser removido, mas a pasta no Docker Host ficará intacta.

Dentro do nosso projeto existe um endpoint para exemplicar o uso do volumes de dados, mas precisamos antes definir o volume.

#### Criação do Volume de dados
1. Pare o container caso ele esteja em execução
1.1 Obtenha o id do container
```
docker ps 
```
1.2 Pare o container
```
docker stop -t 0 ID_DO_CONTAINER
docker container prune
```
2. Execute o container informando o volume de dados:
```
docker run -d -p 8080:8080 -v "trocar_pelo_path_do_direto_do_volume:/var/www" emichels/springboot-with-docker-app
```
- Obs(1): -v "trocar_pelo_path_do_direto_do_volume:/var/www" parametro para apontamento do nosso volume de dados
- Obs(2): emichels/springboot-with-docker-app é nome definido na criação da imagem, substituir pelo nome definido por você

3. Teste

Dentro da pasta apontada no volume de dados crie um arquivo com nome _exemplo-volume-docker.txt_ e escreva algum texto dentro, como:
```
Batatinha quando nasce espalha a rama pelo chão.
Menininha quando dorme põe a mão no coração.
```
Acesse o endpoint abaixo e verifique conteudo do arquivo compartilhado através do volume de dados:
http://localhost:8080/springboot-with-docker-app/volume
