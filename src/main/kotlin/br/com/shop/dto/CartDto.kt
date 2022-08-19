package br.com.shop.dto

import br.com.shop.model.Cart
import br.com.shop.model.Product
import org.springframework.data.domain.Page
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class CartDto(cart: Cart) {
    @NotNull(message = "The name field is mandatory")
    var product: Product
    @NotBlank(message = "The name field is mandatory")
    var name: String
    @DecimalMin(value = "1", message = "The quantity field cannot is smaller that one")
    var quantity: Int
    @DecimalMin(value = "1", message = "The price field cannot is smaller that one")
    var price: Double

    init {
        this.product = cart.product
        this.name = cart.name
        this.quantity = cart.quantity
        this.price = cart.price
    }

    fun converter(): Cart {
        return Cart(0, product, name, quantity, price)
    }

    fun converter(id: Long): Cart {
        return Cart(id, product, name, quantity, price)
    }

    companion object {
        fun converter(carts: Page<Cart>): Page<CartDto> {
            return carts.map { cart -> CartDto(cart) }
        }
    }
}