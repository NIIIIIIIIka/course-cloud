FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN pwd
# 打印当前目录下的文件（验证COPY前的目录结构）
COPY target/enrollment-service.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]