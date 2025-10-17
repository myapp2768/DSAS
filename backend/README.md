# DSAS Backend - 农业数字化智能系统后端

## 项目简介

DSAS (Digital Smart Agriculture System) 是一个农业数字化智能系统的后端服务，基于Spring Boot框架开发。

## 技术栈

- **Java 17**
- **Spring Boot 2.7.18**
- **Spring Data JPA**
- **Spring Security**
- **H2 Database** (开发环境)
- **Maven**

## 项目结构

```
src/main/java/com/dsas/
├── config/                 # 配置类
│   ├── AppConfig.java     # 应用配置
│   ├── SecurityConfig.java # 安全配置
│   └── ServletTest.java   # Servlet测试
├── controller/            # 控制器层
│   ├── ApiController.java # 统一API控制器
│   └── AgriculturalMaterialController.java # 农资编码控制器
├── entity/               # 实体类
│   ├── AgriculturalMaterial.java # 农资编码实体
│   └── User.java         # 用户实体
├── repository/           # 数据访问层
│   ├── AgriculturalMaterialRepository.java
│   └── UserRepository.java
├── service/             # 业务逻辑层
│   ├── AgriculturalMaterialService.java
│   └── impl/
│       └── AgriculturalMaterialServiceImpl.java
├── exception/           # 异常处理
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
└── DsasApplication.java # 主应用类
```

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.6+

### 2. 运行应用

```bash
# 编译项目
mvn clean compile

# 运行应用
mvn spring-boot:run
```

### 3. 访问应用

- 应用地址: http://localhost:8080
- H2数据库控制台: http://localhost:8080/h2-console
- API文档: http://localhost:8080/api/health

## API接口

### 系统接口

- `GET /api/health` - 健康检查
- `GET /api/info` - 系统信息
- `GET /api/test` - 测试接口

### 农资编码管理

- `GET /api/agricultural-materials` - 获取所有农资编码
- `GET /api/agricultural-materials/{id}` - 根据ID获取农资编码
- `POST /api/agricultural-materials` - 创建农资编码
- `PUT /api/agricultural-materials/{id}` - 更新农资编码
- `DELETE /api/agricultural-materials/{id}` - 删除农资编码

### 库存管理

#### 库存查询
- `GET /api/inventory/stocks` - 获取所有库存
- `GET /api/inventory/stocks/material/{materialId}` - 根据农资ID获取库存
- `GET /api/inventory/stocks/low-stock` - 获取库存不足的农资
- `GET /api/inventory/stocks/over-stock` - 获取库存过量的农资

#### 入库管理
- `POST /api/inventory/stock-in` - 创建入库单
- `PUT /api/inventory/stock-in/{id}/complete` - 完成入库
- `PUT /api/inventory/stock-in/{id}/cancel` - 取消入库
- `GET /api/inventory/stock-in` - 获取所有入库记录
- `GET /api/inventory/stock-in/{id}` - 根据ID获取入库记录
- `GET /api/inventory/stock-in/material/{materialId}` - 根据农资ID获取入库记录

#### 出库管理
- `POST /api/inventory/stock-out` - 创建出库单
- `PUT /api/inventory/stock-out/{id}/complete` - 完成出库
- `PUT /api/inventory/stock-out/{id}/cancel` - 取消出库
- `GET /api/inventory/stock-out` - 获取所有出库记录
- `GET /api/inventory/stock-out/{id}` - 根据ID获取出库记录
- `GET /api/inventory/stock-out/material/{materialId}` - 根据农资ID获取出库记录

#### 库存计算
- `GET /api/inventory/stocks/material/{materialId}/current` - 计算农资当前库存
- `PUT /api/inventory/stocks/material/{materialId}/update` - 更新农资库存
- `PUT /api/inventory/stocks/update-all` - 批量更新所有农资库存

#### 统计报表
- `GET /api/inventory/statistics` - 获取库存统计信息
- `GET /api/inventory/statistics/material/{materialId}` - 获取农资库存统计
- `GET /api/inventory/alerts` - 获取库存预警信息
- `GET /api/inventory/reports` - 生成库存报表

### 测试接口

- `GET /api/inventory-test/test` - 测试库存管理功能
- `POST /api/inventory-test/create-test-data` - 创建测试数据
- `GET /api/inventory-test/test-calculation/{materialId}` - 测试库存计算
- `GET /api/inventory-test/test-alerts` - 测试库存预警

## 数据库配置

### MySQL数据库配置

项目已配置为使用MySQL数据库，支持阿里云RDS等云数据库。

#### 1. 配置数据库连接

编辑 `src/main/resources/application.properties` 文件，更新以下配置：

```properties
# MySQL数据库配置
spring.datasource.url=jdbc:mysql://你的MySQL主机:3306/你的数据库名?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=你的用户名
spring.datasource.password=你的密码
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

#### 2. 使用环境变量配置（推荐）

```properties
spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:${MYSQL_PORT:3306}/${MYSQL_DATABASE:dsas_db}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
spring.datasource.username=${MYSQL_USERNAME:root}
spring.datasource.password=${MYSQL_PASSWORD:password}
```

#### 3. 快速配置

运行配置脚本：
```bash
# Windows
configure-mysql.bat

# 或手动设置环境变量
export MYSQL_HOST=your-mysql-host
export MYSQL_USERNAME=your-username
export MYSQL_PASSWORD=your-password
```

#### 4. 数据库初始化

执行 `src/main/resources/sql/init.sql` 脚本创建表结构：

```sql
-- 创建数据库
CREATE DATABASE dsas_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 执行初始化脚本
source src/main/resources/sql/init.sql;
```

#### 5. 测试数据库连接

启动应用后访问：
- `GET /api/database/test-connection` - 测试数据库连接
- `GET /api/database/info` - 获取数据库信息

## 开发说明

### 代码规范

1. 使用统一的包结构
2. 控制器使用`@RestController`和`@RequestMapping`
3. 实体类使用JPA注解和验证注解
4. 服务层使用`@Service`注解
5. 异常处理使用全局异常处理器

### 新增功能

1. 在`entity`包下创建实体类
2. 在`repository`包下创建Repository接口
3. 在`service`包下创建Service接口和实现
4. 在`controller`包下创建Controller
5. 在`exception`包下添加自定义异常

## 常见问题

### 1. 端口冲突

如果8080端口被占用，可以在`application.properties`中修改：

```properties
server.port=8081
```

### 2. 数据库连接问题

确保H2数据库依赖已正确添加，检查`pom.xml`文件。

### 3. 跨域问题

CORS配置在`AppConfig.java`中，可根据需要修改允许的域名。

## 版本历史

- v1.0.0 - 初始版本，包含农资编码管理功能
