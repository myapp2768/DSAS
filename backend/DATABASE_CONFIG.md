# DSAS MySQL数据库配置指南

## 1. 配置步骤

### 步骤1：更新数据库连接信息
编辑 `backend/src/main/resources/application.properties` 文件，将以下配置替换为你的实际阿里云MySQL信息：

```properties
# 替换为你的实际数据库连接信息
spring.datasource.url=jdbc:mysql://你的MySQL主机地址:3306/你的数据库名?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=你的数据库用户名
spring.datasource.password=你的数据库密码
```

### 步骤2：示例配置
```properties
# 示例：阿里云RDS MySQL配置
spring.datasource.url=jdbc:mysql://rm-xxxxx.mysql.rds.aliyuncs.com:3306/dsas_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=dsas_user
spring.datasource.password=your_password_here
```

## 2. 数据库准备

### 创建数据库
在MySQL中创建数据库：
```sql
CREATE DATABASE dsas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 执行初始化脚本
运行 `backend/src/main/resources/sql/init.sql` 脚本来创建表结构。

## 3. 配置说明

### 连接参数说明
- `useUnicode=true&characterEncoding=utf8`: 支持中文字符
- `useSSL=false`: 禁用SSL（根据你的MySQL配置调整）
- `serverTimezone=Asia/Shanghai`: 设置时区为上海
- `allowPublicKeyRetrieval=true`: 允许公钥检索

### JPA配置说明
- `spring.jpa.hibernate.ddl-auto=update`: 自动更新表结构（生产环境建议使用`validate`）
- `spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect`: 使用MySQL8方言

## 4. 测试连接

### 启动应用测试
```bash
cd backend
mvn clean compile
mvn spring-boot:run
```

### 检查日志
启动时查看控制台日志，确认数据库连接成功：
```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```

## 5. 常见问题

### 连接超时
如果遇到连接超时，检查：
1. MySQL服务是否运行
2. 防火墙设置
3. 网络连接

### 字符编码问题
确保数据库和表都使用utf8mb4字符集：
```sql
ALTER DATABASE dsas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### SSL连接问题
如果遇到SSL相关错误，可以在连接URL中添加：
```
&useSSL=false&allowPublicKeyRetrieval=true
```

## 6. 默认账号

系统会创建默认管理员账号：
- 用户名：`admin`
- 密码：`admin123`

## 7. 生产环境建议

1. 将 `spring.jpa.hibernate.ddl-auto` 设置为 `validate`
2. 使用环境变量或配置文件管理敏感信息
3. 配置适当的连接池参数
4. 启用数据库连接监控





