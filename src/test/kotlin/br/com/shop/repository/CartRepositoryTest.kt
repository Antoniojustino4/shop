package br.com.shop.repository

import br.com.shop.model.Cart
import br.com.shop.model.Product
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class CartRepositoryTest(
    @Autowired
    val repository: CartRepository,
    @Autowired
    val productRepository: ProductRepository
) {
    private val product = Product(0, "Pan", "Red pan", 49.99, "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg")
    private val cart = Cart(0, product, "Pan", 1, 49.99)
    private var cont = 0

    @AfterEach
    fun `cleaning repository`() {
        cont++
        if (product.id == 0L){
            productRepository.save(product)
        }
        repository.deleteAll()
        if (cont==4){
            productRepository.deleteAll()
        }
    }

    @Test
    fun `Get by id`(){
        val cartSaved = repository.save(cart)
        val cart= repository.findById(cartSaved.id)

        Assertions.assertNotNull(cart.get())
    }

    @Test
    fun `Post`(){
        val cartSaved = repository.save(cart)

        Assertions.assertNotNull(cartSaved)
        Assertions.assertNotNull(cartSaved.id)
    }

    @Test
    fun `Put`(){
        var cartSaved = repository.save(cart)

        cartSaved.name= "Hat"
        cartSaved.price = 25.99
        cartSaved.quantity = 2
        cartSaved = repository.save(cart)

        Assertions.assertNotNull(cartSaved)
        Assertions.assertNotNull(cartSaved.id)
        Assertions.assertEquals("Hat", cartSaved.name)
        Assertions.assertEquals(25.99, cartSaved.price)
        Assertions.assertEquals(2, cartSaved.quantity)
    }

    @Test
    fun `Delete`(){
        val cartSaved = repository.save(cart)

        repository.delete(cartSaved)

        val existsId = repository.existsById(cartSaved.id)

        Assertions.assertFalse(existsId)
    }

}