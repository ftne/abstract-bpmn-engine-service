package com.ftne.absys.bpmn.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.DefaultSecurityFilterChain

@Configuration
class WebSecurityConfig {

    @Value("\${camunda.bpm.admin-user.id}")
    lateinit var camundaAdminId: String

    @Value("\${camunda.bpm.admin-user.password}")
    lateinit var camundaAdminPwd: String

    @Bean
    fun filterChain(http: HttpSecurity): DefaultSecurityFilterChain =
        http.authorizeHttpRequests {
            it.anyRequest().authenticated()
        }
            .httpBasic(Customizer.withDefaults())
            .csrf().disable()
            .build()

    @Bean
    fun inMemoryUserDetails(): InMemoryUserDetailsManager {
        val user = User.withUsername(camundaAdminId)
            .password("{noop}$camundaAdminPwd")
            .roles("ADMIN")
            .build()
        return InMemoryUserDetailsManager(user)
    }
}