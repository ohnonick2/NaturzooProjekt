# Verwende ein Basis-Image mit Java
FROM eclipse-temurin:17-jdk

# Setze das Arbeitsverzeichnis
WORKDIR /app

# Kopiere nur die Gradle-Dateien, um Abhängigkeiten zu installieren
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Installiere Gradle-Abhängigkeiten im Cache (schrittweise, um den Cache bei Änderungen nicht zu verlieren)
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon

# Kopiere den Quellcode erst nach den Gradle-Abhängigkeiten (schnellerer Build, weil der Cache nicht invalidiert wird)
COPY src src

# Baue das Projekt (inkl. Boot JAR)
RUN ./gradlew build --no-daemon
# Kopiere die endgültige JAR-Datei
COPY build/libs/server.jar server.jar

# Setze den ENTRYPOINT für die ausführbare JAR-Datei
ENTRYPOINT ["java", "-jar", "server.jar"]

# Exponiere den Port
EXPOSE 8080
