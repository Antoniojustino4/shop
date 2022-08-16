package br.com.shop.repository

import br.com.shop.controller.OrderController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc

@SpringBootTest
@AutoConfigureMockMvc
class OrderRepositoryTest(
    @Autowired
    val orderRepository: OrderController
) {

    @Test
    fun a(){
        val a= orderRepository.findById(1)
        print(a)
    }
}