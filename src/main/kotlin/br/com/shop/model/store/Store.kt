package br.com.shop.model.store

import br.com.shop.model.Order
import br.com.shop.model.Product
import javax.persistence.*

@Entity
class Store() {
    lateinit var name: String
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    val products: MutableList<Product> = mutableListOf()
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    val orders: MutableList<Order> = mutableListOf()
    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var extract: Extract = Extract()
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0


    constructor(name: String, products: List<Product>, id: Long): this(name, products){
        this.id = id
    }

    constructor(name: String, products: List<Product>): this(name){
        this.products.addAll(products)
    }

    constructor(name: String): this(){
        this.name = name
    }

}