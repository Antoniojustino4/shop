package br.com.shop.model

import javax.persistence.*

@Entity
class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @ManyToOne(cascade = [CascadeType.REFRESH])
    val product: Product,
    val quantity: Int,
    val name: String = product.name,
    val price: Double = product.price
)