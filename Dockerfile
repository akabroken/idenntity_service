FROM openjdk:8
MAINTAINER Linus Ndege <linus.ndege@interswitchgroup.com>

LABEL Description="This image is used to run tims service"

LABEL Vendor="Interswitch East Africa Ltd."

LABEL Version="1.0.0"

ENV TZ=Africa/Nairobi

ENV CONFIG_LOCATION="/src/main/resources/application.properties"

ADD target/tims-service-1.0.0-SNAPSHOT.jar tims-service-1.0.0-SNAPSHOT.jar

EXPOSE 8003

ENTRYPOINT java -cp "identity-service-1.0.0-SNAPSHOT.jar" -Dloader.main=com.interswitch.identity.Application org.springframework.boot.loader.PropertiesLauncher --spring.config.location=${CONFIG_LOCATION}