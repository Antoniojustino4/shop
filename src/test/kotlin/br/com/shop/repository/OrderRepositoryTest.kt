package br.com.shop.repository

import br.com.shop.model.Order
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest(
    @Autowired
    val repository: OrderRepository
) {
    private val order = Order(0, 15.0, listOf())

    @AfterEach
    fun `cleaning repository`() {
        repository.deleteAll()
    }

    @Test
    fun `Get by id`(){
        val orderSaved = repository.save(order)
        val order= repository.findById(orderSaved.id)

        Assertions.assertNotNull(order.get())
    }

    @Test
    fun `Post`(){
        val orderSaved = repository.save(order)

        Assertions.assertNotNull(orderSaved)
        Assertions.assertNotNull(orderSaved.id)
    }

    @Test
    fun `Put`(){
        var orderSaved = repository.save(order)

        orderSaved.total = 20.0
        orderSaved = repository.save(order)

        Assertions.assertNotNull(orderSaved)
        Assertions.assertNotNull(orderSaved.id)
        Assertions.assertEquals(20.0, orderSaved.total)
    }

    @Test
    fun `Delete`(){
        val orderSaved = repository.save(order)

        repository.delete(orderSaved)

        val existsId = repository.existsById(orderSaved.id)

        Assertions.assertFalse(existsId)
    }

}