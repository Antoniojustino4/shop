package br.com.shop.dto

import br.com.shop.model.Product
import org.springframework.data.domain.Page

class ProductDto(
    var name: String,
    var description: String,
    var price: Double,
    var imageUrl: String,
    var isFavorite: Boolean= false,
) {

    constructor(product: Product): this(product.name, product.description, product.price, product.imageUrl, product.isFavorite)

    fun converter(): Product {
        return Product(0, name, description, price, imageUrl, isFavorite)
    }

    fun converter(id: Long): Product {
        return Product(id, name, description, price, imageUrl, isFavorite)
    }


    companion object {
        fun converter(products: Page<Product>): Page<ProductDto> {
            return products.map { product -> ProductDto(product.name, product.description, product.price, product.imageUrl, product.isFavorite) }
        }
    }
}