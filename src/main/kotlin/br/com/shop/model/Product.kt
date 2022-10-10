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
)