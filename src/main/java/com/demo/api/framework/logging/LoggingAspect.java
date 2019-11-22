package com.demo.api.framework.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

// @Aspect
@Component
@Slf4j
public class LoggingAspect {

	@Before("execution(* com..*Controller.*(..)) || execution(* com..service.*.*(..)) || execution(* com..repository.*.*(..))")
	public void logAccessBefore(JoinPoint joinPoint){
	
		StringBuffer sb = new StringBuffer("BEFORE ");
		
//		result += "<br>*****<br>";
		sb.append(joinPoint.getTarget().getClass().getName())
		.append(".")
		.append(joinPoint.getSignature().getName())
		.append("(");
		
		int index = joinPoint.getArgs().length;
		for( Object o : joinPoint.getArgs() ){
			
			sb.append(o);
			if( --index != 0 ){
				sb.append(", ");
			}
			
		}
		sb.append(")");
		
//		sb.append("Entered!!!");
//		result += "*****<br>";		
		
		log.debug(sb.toString());
	}

	@AfterReturning(pointcut = "execution(* com..*Controller.*(..)) || execution(* com..service.*.*(..)) || execution(* com..repository.*.*(..))", returning = "result")
	public void logAccessAfter(JoinPoint joinPoint, Object result) {
	
		StringBuffer sb = new StringBuffer("AFTER_RETURNING ");
		
////		result += "<br>*****<br>";
		sb.append(joinPoint.getTarget().getClass().getName())
		.append(".")
		.append(joinPoint.getSignature().getName())
		.append("(");
//		
//		int index = joinPoint.getArgs().length;
//		for( Object o : joinPoint.getArgs() ){
//			
//			result.append(o);
//			if( --index != 0 ){
//				result.append(", ");
//			}
//			
//		}
//		result.append(")");
		
		sb.append("Completed: ");
		sb.append("return: ").append(result);
//		result += "*****<br>";		
		
		log.debug(sb.toString());
	}
}