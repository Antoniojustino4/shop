package br.com.shop.model.user

import java.time.LocalDate
import jakarta.persistence.*

@Entity
class Card(
    var number: String,
    var holder: String,
    var securityCode: Int,
    var dueDate: LocalDate,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
)