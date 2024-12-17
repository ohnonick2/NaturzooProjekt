FROM amazoncorretto:17-alpine-jdk

# Überprüfe die Java-Version
RUN java -version

# Installiere notwendige Pakete
RUN apk add --no-cache \
    bash \
    curl \
    git

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere nur die Dateien, die für den Build-Prozess erforderlich sind
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# Baue das Projekt und erzeuge das JAR
RUN ./gradlew clean build bootJar --no-daemon

# Kopiere die endgültige JAR-Datei
COPY build/libs/*.jar /server.jar

# Überprüfe, ob die Datei da ist
RUN ls -la /app/build/libs

# Setze den ENTRYPOINT für die ausführbare JAR-Datei
ENTRYPOINT ["java", "-jar", "/server.jar"]
