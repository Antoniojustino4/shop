package br.com.shop.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ProductIsNotOfThisStoreException(
    var title: String = "Product is not of this Store",
    var status: Int = HttpStatus.NOT_FOUND.value(),
    var developerMessage: String? = null,
    var timestamp: LocalDateTime? = LocalDateTime.now(),
): Throwable("Change not made, product does not belong to the informed store"){


    init {
        super.setStackTrace(arrayOf(super.getStackTrace()[0]))
    }
}