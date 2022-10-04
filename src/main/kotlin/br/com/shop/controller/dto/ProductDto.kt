package br.com.shop.controller.dto

import br.com.shop.model.Product
import org.springframework.data.domain.Page
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

class ProductDto(product: Product):Dto<Product> {
    @NotBlank(message = "The name field is mandatory")
    var name: String
    @NotBlank(message = "The description field is mandatory")
    var description: String
    @DecimalMin(value = "0.01", message = "The price field cannot is smaller that one")
    var price: Double
    @NotBlank(message = "The imageUrl field is mandatory")
    var imageUrl: String

    init {
        this.name = product.name
        this.description = product.description
        this.price = product.price
        this.imageUrl = product.imageUrl
    }

    override fun convert(): Product {
        return Product(name, description, price, imageUrl)
    }

    override fun convert(id: Long): Product {
        return Product(name, description, price, imageUrl,id = id)
    }

    companion object {
        fun convert(products: Page<Product>): Page<ProductDto> {
            return products.map { product -> ProductDto(product) }
        }
    }
}