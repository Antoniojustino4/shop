package br.com.shop.service

import br.com.shop.model.Order
import br.com.shop.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class OrderService {

    @Autowired
    lateinit var orderRepository: OrderRepository

    fun findAll(pageable: Pageable): Page<Order> {
        return orderRepository.findAll(pageable)
    }

    fun findById(id: Long): Order {
        return orderRepository.findById(id).get()
    }

    fun save(order: Order): Order {
        return orderRepository.save(order)
    }

    fun delete(id: Long) {
        orderRepository.deleteById(id)
    }
}