FROM openjdk:21
EXPOSE 8080
ADD target/bestiepanti-images.jar bestiepanti-images.jar
ENTRYPOINT ["java","-jar","/bestiepanti-images.jar"]