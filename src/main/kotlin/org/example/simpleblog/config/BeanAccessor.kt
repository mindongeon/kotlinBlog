package org.example.simpleblog.config

import mu.KotlinLogging
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Configuration
import kotlin.reflect.KClass

@Configuration(proxyBeanMethods = false)
class BeanAccessor : ApplicationContextAware {

    private val log = KotlinLogging.logger { }

    init {
        log.info { "this BeanAccessor = $this" }
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        BeanAccessor.applicationContext = applicationContext
    }

    companion object {
        private lateinit var applicationContext: ApplicationContext

        fun <T : Any> getBean(type: KClass<T>): T {
            return applicationContext.getBean(type.java)
        }

        // 같은 타입의 빈이 두개 이상 있을 수 있어서 이름도 받아줌
        fun <T : Any> getBean(name: String, type: KClass<T>): T {
            return applicationContext.getBean(name, type.java)
        }
    }
}