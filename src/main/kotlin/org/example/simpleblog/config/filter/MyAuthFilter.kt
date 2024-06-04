package org.example.simpleblog.config.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging

class MyAuthFilter : Filter{

    val log = KotlinLogging.logger {  }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val servletRequest = request as HttpServletRequest
        val principal = servletRequest.session.getAttribute("principal")?: throw RuntimeException("session not found")

        chain?.doFilter(request, response)

    }
}