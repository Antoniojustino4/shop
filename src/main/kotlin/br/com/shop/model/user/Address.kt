package br.com.shop.model.user

import br.com.shop.model.enums.State
import jakarta.persistence.*

@Entity
class Address(
    var street: String,
    var district: String,
    var zipCode: String,
    var city: String,
    @Enumerated(EnumType.STRING)
    var state: State,
    var number: Int,
    var complement: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
)