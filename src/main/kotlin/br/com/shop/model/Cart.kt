package br.com.shop.model

import javax.persistence.*

@Entity
class Cart(
    @ManyToOne(cascade = [CascadeType.REFRESH])
    val product: Product,
    val quantity: Int,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String = product.name,
    val price: Double = product.price,

)