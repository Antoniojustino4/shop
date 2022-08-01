package br.com.shop.dto

import br.com.shop.model.Cart
import org.springframework.data.domain.Page
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

class CartDto{
    @DecimalMin(value = "1", message = "The price field is mandatory")
    var productId: Long
    @NotBlank(message = "The name field is mandatory")
    var name: String
    @DecimalMin(value = "1", message = "The quantity field cannot is smaller that one")
    var quantity: Int
    @DecimalMin(value = "1", message = "The price field cannot is smaller that one")
    var price: Double

    constructor(cart: Cart) {
        this.productId = cart.productId
        this.name = cart.name
        this.quantity = cart.quantity
        this.price = cart.price
    }

    fun converter(): Cart {
        return Cart(0, productId, name, quantity, price)
    }

    fun converter(id: Long): Cart {
        return Cart(id, productId, name, quantity, price)
    }

    companion object {
        fun converter(carts: Page<Cart>): Page<CartDto> {
            return carts.map { cart -> CartDto(cart) }
        }
    }
}