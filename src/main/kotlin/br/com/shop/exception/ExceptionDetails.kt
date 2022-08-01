package br.com.shop.exception

import java.time.LocalDateTime
open class ExceptionDetails(
    var title: String? = null,
    var status: Int = 0,
    var details: String? = null,
    var developerMessage: String? = null,
    var timestamp: LocalDateTime? = null,
)