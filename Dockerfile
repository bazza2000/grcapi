FROM java:8
WORKDIR /
ADD ./target/grcapi-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar grcapi-0.0.1-SNAPSHOT.jar

