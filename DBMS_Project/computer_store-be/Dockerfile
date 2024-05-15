# Use the official Maven image to build the project
FROM jelastic/maven:3.9.5-openjdk-21 AS build

# Copy the project files to the container
COPY ./pom.xml /usr/src/app/pom.xml
COPY ./src /usr/src/app/src

# Set the working directory
WORKDIR /usr/src/app

# Build the application
RUN mvn clean package -DskipTests

# Use OpenJDK for the runtime
FROM openjdk:21-jdk

# Copy the built jar file from the build stage
COPY --from=build /usr/src/app/target/*.jar /usr/app/app.jar

# Set the working directory for the application
WORKDIR /usr/app

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "app.jar"]
