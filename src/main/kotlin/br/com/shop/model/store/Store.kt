package br.com.shop.model.store

import br.com.shop.model.Product
import javax.persistence.*

@Entity
class Store(
    var name: String,
    @OneToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    val products: MutableList<Product> = mutableListOf(),
    @OneToOne(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    var extract: Extract = Extract(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
)