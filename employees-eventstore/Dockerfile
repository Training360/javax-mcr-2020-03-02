FROM adoptopenjdk:13-jre-hotspot
RUN mkdir /opt/app
ADD target/*.jar /opt/app/eventstore.jar
CMD ["java", "-jar", "/opt/app/eventstore.jar"]
