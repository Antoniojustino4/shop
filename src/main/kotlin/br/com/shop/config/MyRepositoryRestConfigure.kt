package br.com.shop.config

import br.com.shop.model.*
import org.springframework.context.annotation.Configuration
import org.springframework.data.rest.core.config.RepositoryRestConfiguration
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry

@Configuration
class MyRepositoryRestConfigure : RepositoryRestConfigurer {
    override fun configureRepositoryRestConfiguration(
            config: RepositoryRestConfiguration,
            cors: CorsRegistry) {
        config.exposeIdsFor(Product::class.java, Order::class.java, Product::class.java)
    }
}