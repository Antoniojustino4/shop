package br.com.shop.model.user

import br.com.shop.model.enums.State
import javax.persistence.*

@Entity
class Address(
    var street: String,
    var district: String,
    var zipCode: String,
    var city: String,
    @Enumerated
    var state: State,
    var number: Int,
    var complement: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
)