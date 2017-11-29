FROM openjdk:8-jre-alpine

ENV APP_FAT_JAR="random-maps-0.0.0-SNAPSHOT-fat.jar"

EXPOSE 8080

ADD target/$APP_FAT_JAR /app/
RUN chmod 777 /app/

WORKDIR /app/
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $APP_FAT_JAR"]
