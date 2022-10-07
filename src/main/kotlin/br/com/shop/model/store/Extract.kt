package br.com.shop.model.store

import br.com.shop.exception.InsufficientBalanceException
import br.com.shop.model.enums.TypeTransaction
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import kotlin.jvm.Throws

@Entity
class Extract(
    var balance: Double = 200.0,
    @OneToMany
    private val transactions: MutableList<Transaction> = mutableListOf(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
){

    @Throws(InsufficientBalanceException::class)
    fun addTransaction(transaction: Transaction){
        if (transaction.type == TypeTransaction.WITHDRAW){
            if(balance >= transaction.value){
                balance -=transaction.value
            }
            throw InsufficientBalanceException(this.javaClass.name)
        }else if (transaction.type == TypeTransaction.DEPOSIT){
            balance +=transaction.value
        }
        transactions.add(transaction)
    }

    fun transactions(): MutableList<Transaction> {
        return Collections.unmodifiableList(transactions)
    }

}