package ru.t1.starter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.t1.starter.aspect.LoggingAspect;

/**
 * Автоконфигурация для аспекта логирования.
 */
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnProperty(prefix = "logging.api", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LoggingAutoConfiguration {

    /**
     * Создает бин {@link LoggingAspect} при наличии соответствующих настроек.
     *
     * @param loggingProperties настройки логирования
     * @return бин {@link LoggingAspect}
     */
    @Bean
    public LoggingAspect loggingAspect(LoggingProperties loggingProperties) {
        return new LoggingAspect(loggingProperties);
    }
}
