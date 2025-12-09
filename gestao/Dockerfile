# ============================
# Etapa 1: Build com Maven
# ============================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia o POM para cache de dependências
COPY pom.xml .

# Baixa dependências sem compilar o código
RUN mvn dependency:go-offline -B

# Copia o código-fonte
COPY src ./src

# Compila + empacota o JAR final
RUN mvn -e -DskipTests package


# ============================
# Etapa 2: Imagem Final
# ============================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o jar criado
COPY --from=builder /app/target/*.jar app.jar

# Porta externa da API
EXPOSE 8084

# Usa porta e perfil dinâmicos
ENTRYPOINT ["sh", "-c", "java -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-admin} --server.port=${PORT:-8084}"]
