FROM openjdk:8
MAINTAINER Linus Ndege <linus.ndege@interswitchgroup.com>

LABEL Description="This image is used to run Identity service"

LABEL Vendor="Interswitch East Africa Ltd."

LABEL Version="1.0.0"

ENV TZ=Africa/Nairobi

ENV CONFIG_LOCATION="/opt/service/identity-service/config/application.properties"

ADD target/identity-service-1.0.0-SNAPSHOT.jar identity-service-1.0.0-SNAPSHOT.jar

EXPOSE 8003

ENTRYPOINT java -cp "identity-service-1.0.0-SNAPSHOT.jar" -Dloader.main=com.interswitch.identity.Application org.springframework.boot.loader.PropertiesLauncher --spring.config.location=${CONFIG_LOCATION}