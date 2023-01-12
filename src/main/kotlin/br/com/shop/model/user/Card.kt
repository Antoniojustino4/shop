package br.com.shop.model.user

import java.time.LocalDate
import javax.persistence.*

@Entity
class Card() {
    lateinit var number: String
    lateinit var holder: String
    var securityCode: Int = 0
    lateinit var dueDate: LocalDate

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}