# Use Amazon Corretto 22 as the base image
FROM amazoncorretto:22-alpine

# Set the working directory inside the container
WORKDIR /opt

# Set environment variable for the application port
ENV PORT=8080

# Expose the application port
EXPOSE 8080

# Copy the application JAR file to the working directory
COPY target/notes-0.0.1-SNAPSHOT.jar /opt/notes-0.0.1-SNAPSHOT.jar

# $JAVA_OPTS is an environment variable that can be used to pass additional options to the JVM.
# It's commonly used to specify memory settings, system properties, and other JVM options.
# If not defined, it will be empty, and the JVM will start with its default settings.
#
# Using $JAVA_OPTS allows you to customize the JVM options without changing the Dockerfile.
# This can be useful for tuning performance or debugging.
#
# docker run -p 8080:8080 -e JAVA_OPTS="-Xms512m -Xmx1024m" notes-application:latest
#
# Set the entry point for the container
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar notes-0.0.1-SNAPSHOT.jar"]
