package br.com.shop.service

import br.com.shop.exception.IdNoExistException
import br.com.shop.model.Order
import br.com.shop.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.Throws

@Service
class OrderService {

    @Autowired
    private lateinit var orderRepository: OrderRepository

    fun findAll(pageable: Pageable): Page<Order> {
        return orderRepository.findAll(pageable)
    }

    @Throws(IdNoExistException::class)
    fun findById(id: Long): Optional<Order> {
        return orderRepository.findById(id)
    }

    fun save(order: Order): Order {
        return orderRepository.save(order)
    }

    fun existsById(id: Long): Boolean {
        return orderRepository.existsById(id)
    }
}