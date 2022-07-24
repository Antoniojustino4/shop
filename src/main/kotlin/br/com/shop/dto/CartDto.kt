package br.com.shop.dto

import br.com.shop.model.Cart
import br.com.shop.model.Product
import org.springframework.data.domain.Page

class CartDto (
    var product: Product,
    var name: String,
    var quantity: Int,
    var price: Double,
){
    companion object {
        fun converter(carts: Page<Cart>): Page<CartDto> {
            return carts.map { c -> CartDto(c.product, c.name, c.quantity, c.price) }
        }
    }
}