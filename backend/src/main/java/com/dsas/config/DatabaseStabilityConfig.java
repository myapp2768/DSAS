package com.dsas.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库连接监控和自动恢复配置
 * 确保数据库连接的长期稳定性
 */
@Configuration
@EnableScheduling
public class DatabaseStabilityConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseStabilityConfig.class);

    @Autowired
    private DataSource dataSource;

    /**
     * 数据库连接健康检查
     */
    @Bean
    public HealthIndicator databaseHealthIndicator() {
        return new HealthIndicator() {
            @Override
            public Health health() {
                try (Connection connection = dataSource.getConnection()) {
                    if (connection.isValid(5)) {
                        return Health.up()
                                .withDetail("database", "MySQL")
                                .withDetail("status", "连接正常")
                                .withDetail("url", connection.getMetaData().getURL())
                                .build();
                    } else {
                        return Health.down()
                                .withDetail("database", "MySQL")
                                .withDetail("status", "连接无效")
                                .build();
                    }
                } catch (SQLException e) {
                    logger.error("数据库健康检查失败: {}", e.getMessage());
                    return Health.down()
                            .withDetail("database", "MySQL")
                            .withDetail("status", "连接失败")
                            .withDetail("error", e.getMessage())
                            .build();
                }
            }
        };
    }

    /**
     * 定期检查数据库连接状态
     * 每5分钟检查一次
     */
    @Scheduled(fixedRate = 300000) // 5分钟
    public void checkDatabaseConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(5)) {
                logger.debug("数据库连接检查正常");
            } else {
                logger.warn("数据库连接无效，尝试重新连接");
                if (dataSource instanceof HikariDataSource) {
                    ((HikariDataSource) dataSource).close();
                    logger.info("HikariCP连接池已重置");
                }
            }
        } catch (SQLException e) {
            logger.error("数据库连接检查失败: {}", e.getMessage());
            // 记录错误但不中断应用运行
        }
    }

    /**
     * 定期输出连接池状态
     * 每10分钟输出一次
     */
    @Scheduled(fixedRate = 600000) // 10分钟
    public void logConnectionPoolStatus() {
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            logger.info("HikariCP连接池状态 - 活跃连接: {}, 空闲连接: {}, 总连接: {}, 等待线程: {}",
                    hikariDataSource.getHikariPoolMXBean().getActiveConnections(),
                    hikariDataSource.getHikariPoolMXBean().getIdleConnections(),
                    hikariDataSource.getHikariPoolMXBean().getTotalConnections(),
                    hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
    }
}





