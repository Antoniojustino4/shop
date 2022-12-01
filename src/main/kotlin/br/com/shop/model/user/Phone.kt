package br.com.shop.model.user

import jakarta.persistence.*

@Entity
class Phone(
    var DDD: Int,
    var number: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
)