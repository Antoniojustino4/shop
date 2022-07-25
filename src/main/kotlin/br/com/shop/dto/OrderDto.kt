package br.com.shop.dto

import br.com.shop.model.Cart
import br.com.shop.model.Order
import org.springframework.data.domain.Page
import java.time.LocalDate

class OrderDto(
    var total: Double,
    var carts: List<Cart>,
    var date: LocalDate,
){
    constructor(order: Order): this(order.total, order.carts, order.date)

    fun converter(): Order {
        return Order(0, total, carts, date)
    }

    fun converter(id: Long): Order {
        return Order(id, total, carts, date)
    }

    companion object {
        fun converter(orders: Page<Order>): Page<OrderDto> {
            return orders.map { order -> OrderDto(order.total, order.carts, order.date) }
        }
    }

}