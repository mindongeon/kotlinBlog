package org.example.simpleblog.config.aop

import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes


/**
 * 전통적인 방식의 프록시 기반 스프링 AOP
 *
 * Kotlin에서는 AOP를 고차함수로 구분할 수 있다.
 */

@Component
@Aspect
class LoggerAspect {

    val log = KotlinLogging.logger {  }

    @Pointcut("execution(* org.example.simpleblog.api.*Controller.*(..))")
    private fun controllerCut() = Unit


    @Before("controllerCut()")
    fun controllerLoggerAdvice(joinPoint: JoinPoint) {

        val typeName = joinPoint.signature.declaringTypeName
        val methodName = joinPoint.signature.name

        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request

        log.info("""
            request url: ${request.servletPath}
            request method: $methodName
            request type: $typeName
        """.trimIndent())

    }

    @AfterReturning(pointcut = "controllerCut()", returning = "result")
    fun controllerLogAfter(joinPoint: JoinPoint, result: Any) {

        log.info {"""
         ${joinPoint.signature.name}   
         Method return value : $result
        """.trimIndent()
        }
    }
}