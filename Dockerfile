FROM eclipse-temurin:21-jdk

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="$JAVA_HOME/bin:$PATH"

WORKDIR /app
COPY . /app

RUN ./mvnw clean install
CMD ["java", "-jar", "target/your-app.jar"]