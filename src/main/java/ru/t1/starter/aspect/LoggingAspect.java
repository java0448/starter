package ru.t1.starter.aspect;

import ru.t1.starter.config.LoggingProperties;
import ru.t1.starter.annotation.Loggable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Аспект для логирования запросов и ответов методов, аннотированных {@link Loggable}.
 */
@Aspect
@Component
public class LoggingAspect {

    private final LoggingProperties loggingProperties;
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Конструктор для инициализации аспекта с настройками логирования.
     *
     * @param loggingProperties настройки логирования
     */
    @Autowired
    public LoggingAspect(LoggingProperties loggingProperties) {
        this.loggingProperties = loggingProperties;
    }

    /**
     * Логирует запрос перед выполнением метода, аннотированного {@link Loggable}.
     *
     * @param joinPoint информация о точке соединения
     */
    @Before("@annotation(ru.t1.starter.annotation.Loggable)")
    public void logRequest(JoinPoint joinPoint) {
        if (loggingProperties.isEnabled()) {
            log(joinPoint, "Request");
        }
    }

    /**
     * Логирует ответ после выполнения метода, аннотированного {@link Loggable}.
     *
     * @param joinPoint информация о точке соединения
     * @param result результат выполнения метода
     */
    @AfterReturning(pointcut = "@annotation(ru.t1.starter.annotation.Loggable)", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {
        if (loggingProperties.isEnabled()) {
            log(joinPoint, "Response: " + result);
        }
    }

    /**
     * Логирует запрос и ответ метода, аннотированного {@link Loggable}, а также исключения и время выполнения.
     *
     * @param joinPoint информация о точке соединения
     * @return результат выполнения метода
     * @throws Throwable если метод выбрасывает исключение
     */
    @Around("@annotation(ru.t1.starter.annotation.Loggable)")
    public Object object(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        if (loggingProperties.isEnabled()) {
            log(joinPoint, "Request");
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            if (loggingProperties.isEnabled()) {
                log(joinPoint, "Exception: " + throwable.getMessage());
            }
            throw throwable;
        }
        long endTime = System.currentTimeMillis();
        if (loggingProperties.isEnabled()) {
            log(joinPoint, "Response: " + result + " (Execution time: " + (endTime - startTime) + " ms)");
        }
        return result;
    }

    /**
     * Вспомогательный метод для логирования сообщений.
     *
     * @param joinPoint информация о точке соединения
     * @param message сообщение для логирования
     */
    private void log(JoinPoint joinPoint, String message) {
        String logMessage = joinPoint.getSignature().toShortString() + " - " + message;
        switch (loggingProperties.getLevel().toLowerCase()) {
            case "debug":
                logger.debug(logMessage);
                break;
            case "warn":
                logger.warn(logMessage);
                break;
            case "error":
                logger.error(logMessage);
                break;
            case "info":
            default:
                logger.info(logMessage);
                break;
        }
    }
}