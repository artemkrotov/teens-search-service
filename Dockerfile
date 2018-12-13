FROM java:8
WORKDIR /
ADD teens-search-service-2.1.1.RELEASE.jar teens-search-service.jar
EXPOSE 8080
CMD java - jar teens-search-service.jar