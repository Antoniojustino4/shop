package br.com.shop.repository

import br.com.shop.model.Product
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest(
    @Autowired
    val repository: ProductRepository
) {
    private val product = Product(0, "Pan", "Red pan", 49.99, "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg")

    @AfterEach
    fun `cleaning repository`() {
        repository.deleteAll()
    }

    @Test
    fun `Get by id`(){
        val productSaved = repository.save(product)
        val product= repository.findById(productSaved.id)

        Assertions.assertNotNull(product.get())
    }

    @Test
    fun `Post`(){
        val productSaved = repository.save(product)

        Assertions.assertNotNull(productSaved)
        Assertions.assertNotNull(productSaved.id)
    }

    @Test
    fun `Put`(){
        var productSaved = repository.save(product)

        productSaved.description = "Blue Hat"
        productSaved.name = "Hat"
        productSaved.price = 25.99
        productSaved = repository.save(product)

        Assertions.assertNotNull(productSaved)
        Assertions.assertNotNull(productSaved.id)
        Assertions.assertEquals("Blue Hat", productSaved.description)
        Assertions.assertEquals("Hat", productSaved.name)
        Assertions.assertEquals(25.99, productSaved.price)
    }

    @Test
    fun `ToggleFavorite`(){
        val productSaved = repository.save(product)

        repository.toggleFavorite(!productSaved.isFavorite, productSaved.id)
        val productOther = repository.findById(productSaved.id).get()

        Assertions.assertEquals(productSaved.isFavorite, productOther.isFavorite)
    }

    @Test
    fun `Delete`(){
        val productSaved = repository.save(product)

        repository.delete(productSaved)

        val existsId = repository.existsById(productSaved.id)

        Assertions.assertFalse(existsId)
    }

}