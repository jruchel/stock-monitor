package com.jruchel.stockmonitor.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
@Slf4j
public class CircuitBreakerAspect {

    private Long lastErrorTime;

    @Around("@annotation(com.jruchel.stockmonitor.aspects.BreakCircuit)")
    public Object breakCircuitOnException(ProceedingJoinPoint joinPoint) {
        if (lastErrorTime == null || lastErrorTime + 30000 < new Date().getTime()) {
            try {
                Object result = joinPoint.proceed();
                return result;
            } catch (Throwable throwable) {
                lastErrorTime = new Date().getTime();
                log.error("Api calls exceeded, waiting 30 seconds");
                return null;
            }
        }
        return null;
    }

}
