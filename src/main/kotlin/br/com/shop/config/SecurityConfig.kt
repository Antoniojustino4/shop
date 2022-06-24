package br.com.shop.config

import br.com.shop.service.ShopUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.factory.PasswordEncoderFactories

@EnableWebSecurity
class SecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    private lateinit var shopUserService: ShopUserService

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            //http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .authorizeRequests()
            .antMatchers("/products/**").hasRole("USER")
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .and()
            .httpBasic()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        println(passwordEncoder.encode("academy"))
        auth.inMemoryAuthentication()
            .withUser("antonio")
            .password(passwordEncoder.encode("academy"))
            .roles("USER", "ADMIN")
            .and()
            .withUser("justino")
            .password(passwordEncoder.encode("academy"))
            .roles("USER")
        auth.userDetailsService(shopUserService).passwordEncoder(passwordEncoder)
    }
}