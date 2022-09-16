package br.com.shop.model

import javax.persistence.*

@Entity
class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
     var products: MutableList<Product> = mutableListOf(),
    private var balance: Double = 0.0
)