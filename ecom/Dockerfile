# ---------- Build stage ----------
FROM eclipse-temurin:23 AS build
WORKDIR /workspace

# Install Maven manually
RUN apt-get update && apt-get install -y maven

# Copy pom and source
COPY ./ecom/pom.xml .
COPY src ./ecom/src

# Build jar (skip tests for faster builds)
RUN mvn -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:23-jre
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /workspace/target/*.jar app.jar

# Render sets the PORT environment variable automatically
EXPOSE 8080

# Run the app on Render's PORT
ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar /app/app.jar"]
