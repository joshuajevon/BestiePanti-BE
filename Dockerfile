FROM openjdk:21
EXPOSE 8080
ADD target/bestiepanti-images.jar bestiepanti-images.jar
ADD storage /storage
ENTRYPOINT ["java", "-jar", "/bestiepanti-images.jar"]