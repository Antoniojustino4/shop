package br.com.shop

import br.com.shop.model.*
import br.com.shop.model.enums.TypeTransaction
import br.com.shop.model.store.Store
import br.com.shop.model.store.Transaction
import br.com.shop.repository.ProductRepository
import br.com.shop.repository.StoreRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
//@Profile("dev")
internal class LoadDatabase {
    @Bean
    fun initDatabase(
        productRepository: ProductRepository,
        storeRepository: StoreRepository
    ): CommandLineRunner {
        return CommandLineRunner {
//            shopUserRepository.save(ShopUser(0, "antonio", "antonio",
//                "{bcrypt}$2a$10$1/5QvpwyzCvYVUsvfmpCo.hnr9JdQy6Cy4knk8Kvp9EVrQKtp1m3G", "ROLE_ADMIN,ROLE_USER"))
//            shopUserRepository.save(ShopUser(0, "justino", "justino",
//                "{bcrypt}$2a$10$1/5QvpwyzCvYVUsvfmpCo.hnr9JdQy6Cy4knk8Kvp9EVrQKtp1m3G", "ROLE_USER"))
//
            val product1 = Product(
                "Red Shirt",
                "A red shirt - it is pretty red!",
                29.99,
                "https://cdn.pixabay.com/photo/2016/10/02/22/17/red-t-shirt-1710578_1280.jpg"
            )
            val product2 = Product(
                "Trousers",
                "A nice pair of trousers.",
                59.99,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e8/Trousers%2C_dress_%28AM_1960.022-8%29.jpg/512px-Trousers%2C_dress_%28AM_1960.022-8%29.jpg"
            )
            val product3 = Product(
                "Yellow Scarf",
                "Warm and cozy - exactly what you need for the winter.",
                19.99,
                "https://live.staticflickr.com/4043/4438260868_cc79b3369d_z.jpg"
            )
            val product4 = Product(
                "A Pan",
                "Prepare any meal you want.",
                49.99,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg"
            )

            val store = Store("test",
                arrayListOf(product1,product2))

            val store2 = Store("test2",
                arrayListOf(product3,product4))

//            store.extract.addTransaction(Transaction(TypeTransaction.DEPOSIT, 200.0))
            storeRepository.save(store)
            storeRepository.save(store2)
            println("aaaaaa")

            store.extract.addTransaction(Transaction(TypeTransaction.DEPOSIT, 500.0))
            storeRepository.save(store)

//
//            productRepository.save(product1)
//            productRepository.save(product2)
//            productRepository.save(product3)
//            productRepository.save(product4)
//
//            val cart1 = Cart(0, product1, "a", 1, 12.0)
//            val cart2 = Cart(0, product2, "a", 1, 12.0)
//
////            cartRepository.save(cart1)
////            cartRepository.save(cart2)
//
//            orderRepository.save(Order(0, 1.0, arrayListOf(cart1)))
//            orderRepository.save(Order(0, 1.0, arrayListOf(cart2)))
//
//

        }
    }
    companion object {
        private val log: Logger = LoggerFactory.getLogger(LoadDatabase::class.java)
    }
}