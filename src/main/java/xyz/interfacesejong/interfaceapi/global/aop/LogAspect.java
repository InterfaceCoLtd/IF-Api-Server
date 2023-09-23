package xyz.interfacesejong.interfaceapi.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
public class LogAspect {
    private final static Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("execution(* xyz.interfacesejong.interfaceapi..*Controller.*(..))")
    public void controller(){
    }

    @Pointcut("@annotation(xyz.interfacesejong.interfaceapi.global.aop.Timer)")
    public void timer(){
    }

    @Around("controller() && timer()")
    public Object timerAOP(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = joinPoint.getArgs();
        StringBuilder params = new StringBuilder("Parameters : ");
        if (args.length == 0){
            params.append("null");
        }else {
            for (Object arg : args){
                params.append(arg).append(", ");
            }
        }

        LOGGER.info("[{}] {}", method.getName(), params);

        Object proceed = joinPoint.proceed();

        long end = System.currentTimeMillis();

        LOGGER.info("[{}] executed time {} ms", method.getName(), end - start);
        return proceed;
    }

}
