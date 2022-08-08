package br.com.shop.handler

import br.com.shop.exception.ExceptionDetails
import br.com.shop.exception.ValidationExceptionDetails
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
import java.util.stream.Collectors

@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleException(exception: MethodArgumentNotValidException): ResponseEntity<ValidationExceptionDetails> {
        var ex = ValidationExceptionDetails()

        val fieldErrors = exception.bindingResult.fieldErrors
        val fields: String = fieldErrors.stream().map { obj: FieldError -> obj.field }.collect(Collectors.joining(", "))
        val fieldsMessage: String =
            fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "))

        ex.fields = fields
        ex.fieldsMessage = fieldsMessage
        ex.title = "Bad Resquest Exception, Invalid Fields"
        ex.status = HttpStatus.BAD_REQUEST.value()
        ex.details = exception.message
        ex.developerMessage = exception.javaClass.name
        ex.timestamp = LocalDateTime.now()
        return ResponseEntity(ex, HttpStatus.BAD_REQUEST)
    }

    @Override
    fun handleException(exception: Exception): ResponseEntity<ExceptionDetails> {
        var ex = ExceptionDetails()

        ex.title = "Bad Resquest Exception, Invalid Fields"
        ex.status = HttpStatus.BAD_REQUEST.value()
        ex.details = exception.message
        ex.developerMessage = exception.javaClass.name
        ex.timestamp = LocalDateTime.now()
        return ResponseEntity(ex, HttpStatus.BAD_REQUEST)
    }
}