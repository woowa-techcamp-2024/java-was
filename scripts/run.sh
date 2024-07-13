./gradlew clean fatJar
chmod +x $(pwd)/build/libs/java-was-1.0-SNAPSHOT.jar
nohup java -jar $(pwd)/build/libs/java-was-1.0-SNAPSHOT.jar &