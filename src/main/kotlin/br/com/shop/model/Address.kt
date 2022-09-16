package br.com.shop.model

import br.com.shop.model.enums.State
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Address(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var street: String,
    var district: String,
    var zipCode: String,
    var city: String,
    var state: State,
    var number: Int,
    var complement: String
)