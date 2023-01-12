package br.com.shop.model.store

import br.com.shop.model.enums.TypeTransaction
import javax.persistence.*

@Entity
class Transaction() {
    @Enumerated(EnumType.STRING)
    lateinit var type: TypeTransaction
    var value: Double = 0.0
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    constructor(typeTransaction: TypeTransaction, value: Double): this(){
        this.type = typeTransaction
        this.value = value
    }
}