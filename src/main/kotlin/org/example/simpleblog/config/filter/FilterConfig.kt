package org.example.simpleblog.config.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    /**
     * 인증 처리
     *
     * 스프링 시큐리티
     *  - 필터나 인터셉터를 이용해 만든 강력한 인증처리 관련 프레임워크
     */

    @Bean
    fun registMyAuthFilter(): FilterRegistrationBean<MyAuthFilter> {

        val bean = FilterRegistrationBean(MyAuthFilter())
        bean.addUrlPatterns("/api/*")
        bean.order = 0

        return bean
    }

}