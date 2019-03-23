FROM openjdk:8-alpine

COPY target/booking-*.jar /demo.jar

CMD ["java", "-jar", "/demo.jar"]