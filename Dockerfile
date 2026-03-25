# 1. THE BASE: A pre-configured Linux server with Java 21, Maven, and Chrome already installed
FROM markhobson/maven-chrome:jdk-21

# 2. THE WORKSPACE: Create a folder inside the virtual container to hold our code
WORKDIR /usr/src/app

# 3. THE INJECTION: Copy your pom.xml and your src folder from your Windows laptop into the Linux container
COPY pom.xml .
COPY src ./src

# 4. THE IGNITION: The exact command the container will run the second it boots up in the cloud
CMD ["mvn", "clean", "test", "-Dtest=TestRunner"]