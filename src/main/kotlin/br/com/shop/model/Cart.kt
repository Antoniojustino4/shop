package br.com.shop.model

import javax.persistence.*

@Entity
class Cart() {
    @ManyToOne(cascade = [CascadeType.REFRESH])
    var product: Product = Product()
    var quantity: Int = 0
    var name: String = ""
    var price: Double = 0.0
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0


    init {
        val name: String = product.name
        val price: Double = product.price
    }

    constructor(product: Product, quantity: Int) : this() {
        this.product = product
        this.quantity = quantity
    }

    constructor(product: Product, quantity: Int, id: Long) : this(product, quantity){
        this.id = id
    }
}