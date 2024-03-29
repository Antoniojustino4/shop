package br.com.shop.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class IdNoExistException(
    var title: String = "Id no exist exception",
    var status: Int = HttpStatus.NOT_FOUND.value(),
    var developerMessage: String? = null,
    var timestamp: LocalDateTime? = LocalDateTime.now(),
): Throwable("The searched id does not exist"){


    init {
        super.setStackTrace(arrayOf(super.getStackTrace()[0]))
    }
}