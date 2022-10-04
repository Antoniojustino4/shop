package br.com.shop.model.store

import br.com.shop.model.enums.TypeTransaction
import javax.persistence.*

@Entity
class Transaction(
    @Enumerated
    val type: TypeTransaction,
    var value: Double,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
)