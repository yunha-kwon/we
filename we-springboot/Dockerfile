FROM openjdk:17

# 시간대 설정
RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

CMD ["./gradlew","clean", "build"]

VOLUME /tmp

ARG JAR_FILE=we-springboot/build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]
