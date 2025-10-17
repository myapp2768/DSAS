@echo off
echo 启动DSAS后端服务...
cd /d E:\DSAS\backend
echo 当前目录: %CD%
echo 检查JAR文件...
if exist target\dsas-backend-1.0.0.jar (
    echo JAR文件存在，启动服务...
    java -jar target\dsas-backend-1.0.0.jar
) else (
    echo JAR文件不存在，重新构建...
    mvn clean package -DskipTests
    if exist target\dsas-backend-1.0.0.jar (
        echo 构建成功，启动服务...
        java -jar target\dsas-backend-1.0.0.jar
    ) else (
        echo 构建失败！
    )
)
pause





