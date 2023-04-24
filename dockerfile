# Use an OpenJDK runtime as the base image
FROM openjdk:19-jdk-slim

# Set working directory
WORKDIR /

# custom installation for RPI Grove dependencies defined in requirements.txt
RUN apt-get update
RUN apt-get install -y curl
# Copy the dependencies file to the working directory
COPY pom.xml .
COPY target/gdn-intership1.jar /app/
COPY lib/* /app/lib/
# Install any dependencies


# Copy the content of the local src directory to the working directory
COPY . .

# Specify the command to run on container start
CMD ["java", "-jar", "/app/gdn-intership1.jar"]