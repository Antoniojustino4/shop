package br.com.shop.dto

import br.com.shop.model.Cart
import br.com.shop.model.Product
import org.springframework.data.domain.Page

class CartDto(
    var product: Product,
    var name: String,
    var quantity: Int,
    var price: Double,
){
    constructor(cart: Cart) : this(cart.product, cart.name, cart.quantity, cart.price)

    fun converter(): Cart {
        return Cart(0, product, name, quantity, price)
    }

    fun converter(id: Long): Cart {
        return Cart(id, product, name, quantity, price)
    }

    companion object {
        fun converter(carts: Page<Cart>): Page<CartDto> {
            return carts.map { cart -> CartDto(cart.product, cart.name, cart.quantity, cart.price) }
        }
    }
}