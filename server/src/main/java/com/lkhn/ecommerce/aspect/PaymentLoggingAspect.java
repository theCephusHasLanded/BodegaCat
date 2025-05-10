package com.lkhn.ecommerce.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class PaymentLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(PaymentLoggingAspect.class);

    @Before("execution(* com.lkhn.ecommerce.service.StripeService.*(..))")
    public void logBeforePaymentOperation(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // Mask sensitive data in logs
        Object[] maskedArgs = maskSensitiveData(joinPoint.getArgs());

        logger.info("Executing payment operation: {}.{} with arguments: {}",
                className, methodName, Arrays.toString(maskedArgs));
    }

    @AfterReturning(
            pointcut = "execution(* com.lkhn.ecommerce.service.StripeService.*(..))",
            returning = "result")
    public void logAfterPaymentOperation(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        // Don't log the actual result, just indicate success
        logger.info("Successfully completed payment operation: {}.{}",
                className, methodName);
    }

    @AfterThrowing(
            pointcut = "execution(* com.lkhn.ecommerce.service.StripeService.*(..))",
            throwing = "error")
    public void logPaymentError(JoinPoint joinPoint, Throwable error) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        logger.error("Error in payment operation: {}.{} - Error: {}",
                className, methodName, error.getMessage());
    }

    /**
     * Masks sensitive data like card numbers and tokens in logs
     */
    private Object[] maskSensitiveData(Object[] args) {
        Object[] maskedArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                String arg = (String) args[i];
                // Mask card numbers
                if (arg.matches("\\d{13,19}")) {
                    maskedArgs[i] = "XXXX-XXXX-XXXX-" + arg.substring(arg.length() - 4);
                }
                // Mask tokens
                else if (arg.startsWith("tok_")) {
                    maskedArgs[i] = "tok_***MASKED***";
                }
                // Mask payment method IDs
                else if (arg.startsWith("pm_")) {
                    maskedArgs[i] = "pm_***MASKED***";
                }
                else {
                    maskedArgs[i] = args[i];
                }
            } else {
                maskedArgs[i] = args[i];
            }
        }
        return maskedArgs;
    }
}
