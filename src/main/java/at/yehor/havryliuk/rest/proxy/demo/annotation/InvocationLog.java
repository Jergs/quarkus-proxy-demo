package at.yehor.havryliuk.rest.proxy.demo.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.annotation.Priority;
import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InterceptorBinding;
import jakarta.interceptor.InvocationContext;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * do not log method retries (@Retry)
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@Documented
@InterceptorBinding
public @interface InvocationLog {

    @Nonbinding boolean enableLogAfter() default false;

    @Slf4j
    @InvocationLog
    @Interceptor
    @Priority(200)
    class LogMethodCall {

        @AroundInvoke
        Object log(InvocationContext context) throws Exception {
            String className = context.getMethod().getDeclaringClass().getSimpleName();
            String methodName = context.getMethod().getName();
            Object[] parameterValues = context.getParameters();
            List<String> paramNames = Arrays.stream(context.getMethod().getParameters())
                    .map(Parameter::getName)
                    .toList();

            log.info("{}.{} triggered with params: {} with values: {}",
                    className,
                    methodName,
                    paramNames,
                    parameterValues);

            Object obj = context.proceed();

            // Log after execution if enabled
            InvocationLog annotation = context.getMethod().getAnnotation(InvocationLog.class);
            if (annotation.enableLogAfter()) {
                log.info("{}.{} processed.", className, methodName);
            }

            return obj;
        }
    }

}

