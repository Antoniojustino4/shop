package br.com.shop.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class InsufficientBalanceException(
    var title: String = "Insufficient Balance exception",
    var status: Int = HttpStatus.NOT_FOUND.value(),
    var details: String = "The withdrawal amount is greater than the balance",
    var developerMessage: String? = null,
    var timestamp: LocalDateTime? = LocalDateTime.now(),
): Throwable("The withdrawal amount is greater than the balance"){


    init {
        super.setStackTrace(arrayOf(super.getStackTrace()[0]))
    }
}