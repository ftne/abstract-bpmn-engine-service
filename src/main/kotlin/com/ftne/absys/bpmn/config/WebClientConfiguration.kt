package com.ftne.absys.bpmn.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class WebClientConfiguration {
    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate =
        builder.build()
}
