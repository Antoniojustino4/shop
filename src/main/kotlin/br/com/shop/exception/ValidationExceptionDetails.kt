package br.com.shop.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ValidationExceptionDetails(
    var fields: String? = null,
    var fieldsMessage: String? = null
) : ExceptionDetails()