FROM openjdk:21
EXPOSE 8080
ADD target/bestiepanti-images.jar bestiepanti-images.jar

# Copy all folder
# ADD storage /storage

# Copy only the specified images
ADD storage/profile/defaultProfileFemale.png storage/profile/defaultProfileFemale.png
ADD storage/profile/defaultProfileMale.png storage/profile/defaultProfileMale.png
ADD storage/profile/defaultProfileGeneral.png storage/profile/defaultProfileGeneral.png

ENTRYPOINT ["java", "-jar", "/bestiepanti-images.jar"]