package com.sivalabs.moviefinder.support.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This is a Spring AOP Aspect to log method entry/exit
 * along with execution time when debug log level enabled.
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {

  @Around("@within(com.sivalabs.moviefinder.support.logging.Loggable) || @annotation(com.sivalabs.moviefinder.support.logging.Loggable)")
  public Object logMethodEntryExit(ProceedingJoinPoint pjp) throws Throwable {

    long start = System.currentTimeMillis();

    String className = "";
    String methodName = "";
    if(log.isDebugEnabled()) {
      className = pjp.getSignature().getDeclaringTypeName();
      methodName = pjp.getSignature().getName();

      Object[] args = pjp.getArgs();
      String argumentsToString = "";
      if (args != null) {
        argumentsToString = Arrays.stream(args)
                .map(arg -> (arg == null) ? null : arg.toString())
                .collect(Collectors.joining(","));
      }
      log.debug(String.format("Entering method %s.%s(%s)", className, methodName, argumentsToString));
    }

    Object result = pjp.proceed();

    if(log.isDebugEnabled()) {
      long elapsedTime = System.currentTimeMillis() - start;
      log.debug(String.format("Exiting method %s.%s; Execution time (ms): %s", className, methodName, elapsedTime));
    }

    return result;
  }
}
