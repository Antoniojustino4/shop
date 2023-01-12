package br.com.shop.model.user

import br.com.shop.model.enums.State
import javax.persistence.*

@Entity
class Address() {
    lateinit var street: String
    lateinit var district: String
    lateinit var zipCode: String
    lateinit var city: String
    @Enumerated(EnumType.STRING)
    lateinit var state: State
    var number: Int = 0
    lateinit var complement: String
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}