@echo off
echo DSAS MySQL数据库配置助手
echo ================================

echo.
echo 请提供你的阿里云MySQL数据库连接信息：
echo.

set /p MYSQL_HOST="MySQL主机地址 (例如: rm-xxxxx.mysql.rds.aliyuncs.com): "
set /p MYSQL_PORT="端口号 (默认3306): "
if "%MYSQL_PORT%"=="" set MYSQL_PORT=3306

set /p MYSQL_DATABASE="数据库名 (默认dsas_db): "
if "%MYSQL_DATABASE%"=="" set MYSQL_DATABASE=dsas_db

set /p MYSQL_USERNAME="用户名: "
set /p MYSQL_PASSWORD="密码: "

echo.
echo 配置信息确认：
echo MySQL主机: %MYSQL_HOST%
echo 端口: %MYSQL_PORT%
echo 数据库: %MYSQL_DATABASE%
echo 用户名: %MYSQL_USERNAME%
echo.

set /p CONFIRM="确认配置正确吗? (y/n): "
if /i not "%CONFIRM%"=="y" (
    echo 配置已取消
    pause
    exit /b
)

echo.
echo 正在更新配置文件...

REM 创建临时配置文件
echo # MySQL数据库配置 > temp_config.properties
echo spring.datasource.url=jdbc:mysql://%MYSQL_HOST%:%MYSQL_PORT%/%MYSQL_DATABASE%?useUnicode=true^&characterEncoding=utf8^&useSSL=false^&serverTimezone=Asia/Shanghai^&allowPublicKeyRetrieval=true >> temp_config.properties
echo spring.datasource.username=%MYSQL_USERNAME% >> temp_config.properties
echo spring.datasource.password=%MYSQL_PASSWORD% >> temp_config.properties
echo spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver >> temp_config.properties
echo. >> temp_config.properties
echo # 连接池配置 >> temp_config.properties
echo spring.datasource.hikari.maximum-pool-size=20 >> temp_config.properties
echo spring.datasource.hikari.minimum-idle=5 >> temp_config.properties
echo spring.datasource.hikari.connection-timeout=30000 >> temp_config.properties
echo spring.datasource.hikari.idle-timeout=600000 >> temp_config.properties
echo spring.datasource.hikari.max-lifetime=1800000 >> temp_config.properties
echo. >> temp_config.properties
echo # JPA配置 >> temp_config.properties
echo spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect >> temp_config.properties
echo spring.jpa.hibernate.ddl-auto=update >> temp_config.properties
echo spring.jpa.show-sql=true >> temp_config.properties
echo spring.jpa.properties.hibernate.format_sql=true >> temp_config.properties
echo spring.jpa.properties.hibernate.use_sql_comments=true >> temp_config.properties
echo. >> temp_config.properties
echo # 字符编码配置 >> temp_config.properties
echo spring.jpa.properties.hibernate.connection.characterEncoding=utf8 >> temp_config.properties
echo spring.jpa.properties.hibernate.connection.useUnicode=true >> temp_config.properties

echo 配置文件已生成: temp_config.properties
echo.
echo 请手动将以下内容复制到 src/main/resources/application.properties 文件中：
echo.
type temp_config.properties

echo.
echo 配置完成！现在可以启动应用测试数据库连接。
echo.
pause





