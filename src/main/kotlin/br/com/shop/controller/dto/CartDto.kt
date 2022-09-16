package br.com.shop.controller.dto

import br.com.shop.model.Cart
import br.com.shop.model.Product
import org.springframework.data.domain.Page
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotNull

class CartDto(cart: Cart) {
    @NotNull(message = "The name field is mandatory")
    var product: Product
    @DecimalMin(value = "1", message = "The quantity field cannot is smaller that one")
    var quantity: Int

    init {
        this.product = cart.product
        this.quantity = cart.quantity
    }

    fun convert(): Cart {
        return Cart(0, product, quantity)
    }

    fun convert(id: Long): Cart {
        return Cart(id, product, quantity)
    }

    companion object {
        fun convert(carts: Page<Cart>): Page<CartDto> {
            return carts.map { cart -> CartDto(cart) }
        }
    }
}