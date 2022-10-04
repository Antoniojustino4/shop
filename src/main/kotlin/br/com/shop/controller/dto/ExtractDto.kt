package br.com.shop.controller.dto

import br.com.shop.model.store.Extract
import br.com.shop.model.store.Transaction
import org.springframework.data.domain.Page
import javax.validation.constraints.DecimalMin

class ExtractDto(extract: Extract):Dto<Extract> {
    @DecimalMin(value = "0.01", message = "The price field cannot is smaller that one")
    var balance: Double = 0.0
    private var transactions: MutableList<Transaction>

    init {
        this.balance = extract.balance
        this.transactions = extract.transactions()
    }

    override fun convert(): Extract {
        return Extract(this.balance, this.transactions)
    }

    override fun convert(id: Long): Extract {
        return Extract(this.balance, this.transactions, id= id)
    }

    companion object {
        fun convert(extracts: Page<Extract>): Page<ExtractDto> {
            return extracts.map { extract -> ExtractDto(extract) }
        }
    }
}