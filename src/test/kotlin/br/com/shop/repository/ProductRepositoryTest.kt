package br.com.shop.repository

import br.com.shop.model.Product
import br.com.shop.model.enums.ProductStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
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
        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg")


    @BeforeEach
    fun `save product`() {
        repository.save(product)
    }

    @Test
    fun `find product by name`(){
        val products = repository.findByName(product.name, Pageable.unpaged()).get()
        val list= products.toList()

        Assertions.assertNotNull(products)
        Assertions.assertTrue(list.contains(product))
    }

    @Test
    fun `return list empty when search for with name non-existent`(){
        val products = repository.findByName("", Pageable.unpaged())

        Assertions.assertEquals(0, products.totalElements)
    }

    @Test
    fun `change status product`(){
        val returned = repository.changeStatus(ProductStatus.UNAVAILABLE, product.id)
        if (returned != 1) {
            Assertions.fail<Any>()
        }
    }

    @Test
    fun `try change status with product not-existent`(){
        val returned = repository.changeStatus(ProductStatus.UNAVAILABLE, -9999)
        if (returned == 1) {
            Assertions.fail<Any>()
        }
    }
}