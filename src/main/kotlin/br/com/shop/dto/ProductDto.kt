package br.com.shop.dto

import br.com.shop.model.Product
import org.springframework.data.domain.Page
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

class ProductDto(
    @NotBlank(message = "The name field is mandatory")
    var name: String,
    @NotBlank(message = "The description field is mandatory")
    var description: String,
    @DecimalMin(value = "1", message = "The price field cannot is smaller that one")
    var price: Double,
    @NotBlank(message = "The imageUrl field is mandatory")
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