package br.com.shop

import br.com.shop.model.Cart
import br.com.shop.model.Order
import br.com.shop.model.Product
import br.com.shop.model.ShopUser
import br.com.shop.repository.CartRepository
import br.com.shop.repository.OrderRepository
import br.com.shop.repository.ProductRepository
import br.com.shop.repository.ShopUserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate

@Configuration
internal class LoadDatabase {
    @Bean
    fun initDatabase(
        productRepository: ProductRepository,
        shopUserRepository: ShopUserRepository,
        orderRepository: OrderRepository,
        cartRepository: CartRepository
    ): CommandLineRunner {
        return CommandLineRunner {
            shopUserRepository.save(ShopUser(0, "antonio", "antonio",
                "{bcrypt}$2a$10$1/5QvpwyzCvYVUsvfmpCo.hnr9JdQy6Cy4knk8Kvp9EVrQKtp1m3G", "ROLE_ADMIN,ROLE_USER"))
            shopUserRepository.save(ShopUser(0, "justino", "justino",
                "{bcrypt}$2a$10$1/5QvpwyzCvYVUsvfmpCo.hnr9JdQy6Cy4knk8Kvp9EVrQKtp1m3G", "ROLE_USER"))

            val product1=Product(0,"Red Shirt","A red shirt - it is pretty red!",29.99,"https://cdn.pixabay.com/photo/2016/10/02/22/17/red-t-shirt-1710578_1280.jpg")
            val product2=Product(0,"Trousers","A nice pair of trousers.",59.99,"https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Trousers%2C_dress_%28AM_1960.022-8%29.jpg/512px-Trousers%2C_dress_%28AM_1960.022-8%29.jpg")
            val product3=Product(0,"Yellow Scarf","Warm and cozy - exactly what you need for the winter.",19.99,"https://live.staticflickr.com/4043/4438260868_cc79b3369d_z.jpg")
            val product4=Product(0,"A Pan","Prepare any meal you want.",49.99,"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg")

            productRepository.save(product1)
            productRepository.save(product2)
            productRepository.save(product3)
            productRepository.save(product4)

            val cart1 = Cart(0, product1, "a", 1, 12.0)
            val cart2 = Cart(0, product2, "a", 1, 12.0)

            cartRepository.save(cart1)
            cartRepository.save(cart2)

            orderRepository.save(Order(0, 1.0, arrayListOf(cart1), LocalDate.now()))
            orderRepository.save(Order(0, 1.0, arrayListOf(cart2), LocalDate.now()))


        }
    }
    companion object {
        private val log: Logger = LoggerFactory.getLogger(LoadDatabase::class.java)
    }
}