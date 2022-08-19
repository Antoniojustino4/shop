package br.com.shop.model

import javax.persistence.*

@Entity
class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    @ManyToOne(cascade = [CascadeType.REFRESH])
    var product: Product,
    var name: String,
    var quantity: Int,
    var price: Double,
)