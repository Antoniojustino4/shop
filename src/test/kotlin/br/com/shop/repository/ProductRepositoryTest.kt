package br.com.shop.repository

import br.com.shop.model.Product
import br.com.shop.model.enums.ProductStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import kotlin.streams.toList

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest(
    @Autowired
    val repository: ProductRepository,
) {

    private val product = Product( "Pan", "Red pan", 49.99,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg",)

    @AfterEach
    fun `cleaning repository`() {
        repository.deleteAll()
    }

    @Test
    fun `Get by name`(){
        repository.save(product)
        val products = repository.findByName(product.name, Pageable.unpaged()).get()
        val list= products.toList()

        Assertions.assertNotNull(products)
        Assertions.assertTrue(list.contains(product))
    }

    @Test
    fun `Change status`(){
        repository.save(product)
        repository.changeStatus(ProductStatus.UNAVAILABLE, product.id)
        val productFound = repository.findById(product.id)
        if (productFound.isPresent) {
            Assertions.assertNotEquals(ProductStatus.UNAVAILABLE, productFound.get().status)
        }else{
            Assertions.fail()
        }
    }
}