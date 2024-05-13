# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory in the container
WORKDIR /code

# Copy the application JAR file into the container at /usr/src/app
COPY ./target/your-spring-app.jar ./

# Run the application when the container launches
CMD ["java", "-jar", "your-spring-app.jar"]
