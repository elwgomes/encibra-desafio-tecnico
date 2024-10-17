# encibra-desafio-tecnico

### Tecnologias
- Java 17
- Spring Boot 3.3.4
    - spring-boot-starter-web
    - spring-data-jpa
    - spring-security
- JWT
- Swagger OpenAPI
- Lombok
- ModelMapper
- H2 (in memory database)
- Tomcat (embedded on spring Boot)
- Git (code versioning)

### Execute
Vá ao diretório raiz e execute no terminal:
```
$ docker compose up -d
```
para acompanhar o console:
```
$ docker-compose logs -f 
```
ou, também no diretório raiz:

```
$ mvn clean spring-boot:run
```
para executar os testes:
```
$ mvn clean test
```

Documentação:
```
localhost:8080/swagger-ui/index.html
```
