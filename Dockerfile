FROM java:8-jre
MAINTAINER Artem Krotov <akrotov.direct@gmail.com>
ADD ./target/teens-search-service.jar /app/
CMD ["java", "-jar", "/app/teens-search-service.jar"]
EXPOSE 8080
