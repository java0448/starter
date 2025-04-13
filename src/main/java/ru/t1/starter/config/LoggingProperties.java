package ru.t1.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.t1.starter.aspect.LoggingAspect;

/**
 * Настройки логирования для аспекта {@link LoggingAspect}.
 */
@Component
@ConfigurationProperties(prefix = "logging.api")
public class LoggingProperties {
    private boolean enabled = true;
    private String level = "info";

    /**
     * Возвращает состояние включения логирования.
     *
     * @return true, если логирование включено, иначе false
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Устанавливает состояние включения логирования.
     *
     * @param enabled true для включения логирования, иначе false
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Возвращает уровень логирования.
     *
     * @return уровень логирования
     */
    public String getLevel() {
        return level;
    }

    /**
     * Устанавливает уровень логирования.
     *
     * @param level уровень логирования
     */
    public void setLevel(String level) {
        this.level = level;
    }
}