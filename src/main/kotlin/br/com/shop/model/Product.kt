package br.com.shop.model

import br.com.shop.model.enums.ProductStatus
import javax.persistence.*

@Entity
class Product(
    var name: String,
    var description: String,
    var price: Double,
    var imageUrl: String,
    @Enumerated(EnumType.STRING)
    var status: ProductStatus = ProductStatus.AVAILABLE,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (name != other.name) return false
        if (description != other.description) return false
        if (price != other.price) return false
        if (imageUrl != other.imageUrl) return false
        if (status != other.status) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + id.hashCode()
        return result
    }
}