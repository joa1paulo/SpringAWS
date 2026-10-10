# --- Etapa 1: Build da aplicação usando Maven ---
FROM maven:3.9-eclipse-temurin-26 AS build
WORKDIR /app

# Copia os arquivos de configuração do Maven e o código fonte
COPY pom.xml .
COPY src ./src

# Executa o empacotamento pulando os testes
RUN mvn clean package -DskipTests

# --- Etapa 2: Execução da aplicação ---
FROM eclipse-temurin:26-jre
WORKDIR /app

# Copia o jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta da aplicação
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]