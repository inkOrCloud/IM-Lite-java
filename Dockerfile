# ========================
# 阶段1: Maven 构建阶段
# ========================
FROM maven:3.9.11-amazoncorretto-21-debian AS builder

# 设置工作目录
WORKDIR /app

# 复制项目文件（包括pom.xml和源代码）
COPY pom.xml ./
COPY src ./src

# 仅下载依赖（避免重复下载）
RUN mvn dependency:resolve dependency:resolve-plugins

# 构建项目并生成JAR文件
RUN mvn clean package -DskipTests

# ========================
# 阶段2: 最终运行阶段
# ========================
FROM openjdk:21-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制构建好的JAR文件（使用通配符匹配，确保只复制目标JAR）
COPY --from=builder /app/target/IM-Lite-java-*.jar /app/app.jar

# 暴露应用端口（根据实际需求修改）
EXPOSE 8080

# 启动命令（使用重命名后的固定文件名，避免通配符问题）
CMD ["java", "-jar", "app.jar"]