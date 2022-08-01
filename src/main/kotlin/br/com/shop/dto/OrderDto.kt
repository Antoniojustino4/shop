package br.com.shop.dto

import br.com.shop.model.Cart
import br.com.shop.model.Order
import org.springframework.data.domain.Page
import java.time.LocalDate
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

class OrderDto{
    @DecimalMin(value = "1", message = "The total field cannot is smaller that one")
    var total: Double
    @NotEmpty(message = "The carts field is mandatory")
    var carts: List<Cart>
    @NotEmpty(message = "The date field is mandatory")
    var date: LocalDate

    constructor(order: Order){
        this.total = order.total
        this.carts = order.carts
        this.date = order.date
    }

    fun converter(): Order {
        return Order(0, total, carts, date)
    }

    fun converter(id: Long): Order {
        return Order(id, total, carts, date)
    }

    companion object {
        fun converter(orders: Page<Order>): Page<OrderDto> {
            return orders.map { order -> OrderDto(order) }
        }
    }

}