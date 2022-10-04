package br.com.shop.controller.dto

import br.com.shop.model.Cart
import br.com.shop.model.Order
import org.springframework.data.domain.Page
import javax.validation.constraints.NotEmpty

class OrderDto(order: Order):Dto<Order> {
    @NotEmpty(message = "The carts field is mandatory")
    var carts: List<Cart>

    init {
        this.carts = order.carts
    }

    override fun convert(): Order {
        return Order(0, carts.toMutableList())
    }

    override fun convert(id: Long): Order {
        return Order(id, carts.toMutableList())
    }

    companion object {
        fun convert(orders: Page<Order>): Page<OrderDto> {
            return orders.map { order -> OrderDto(order) }
        }
    }

}