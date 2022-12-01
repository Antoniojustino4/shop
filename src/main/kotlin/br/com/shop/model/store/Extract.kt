package br.com.shop.model.store

import br.com.shop.exception.InsufficientBalanceException
import br.com.shop.model.enums.TypeTransaction
import jakarta.persistence.*
import java.util.*
import kotlin.jvm.Throws

@Entity
class Extract(
    var balance: Double = 0.0,
    @OneToMany
    private val transactions: MutableList<Transaction> = mutableListOf(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val idExtract: Long = 0,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Extract

        if (balance != other.balance) return false
        if (transactions != other.transactions) return false
        if (idExtract != other.idExtract) return false

        return true
    }

    override fun hashCode(): Int {
        var result = balance.hashCode()
        result = 31 * result + transactions.hashCode()
        result = 31 * result + idExtract.hashCode()
        return result
    }
}