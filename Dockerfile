# Use Java 17
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy all files
COPY . .

# 🔥 IMPORTANT: give execute permission to mvnw
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "target/*.jar"]
