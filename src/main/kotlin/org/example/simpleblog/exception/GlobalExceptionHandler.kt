package org.example.simpleblog.exception

import jakarta.persistence.NoResultException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    val log = KotlinLogging.logger {}

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        log.error { "handleMethodArgumentNotValidException $ex" }

        val of = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, ex.bindingResult)

        return ResponseEntity(of, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(ex: EntityNotFoundException): ResponseEntity<ErrorResponse> {

        log.error { "handleEntityNotFoundException $ex" }

        val of = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND)
        return ResponseEntity(of, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(NoResultException::class)
    fun handleNoResultException(ex: EntityNotFoundException): ResponseEntity<ErrorResponse> {

        log.error { "NoResultException $ex" }
        val of = ErrorResponse.of(ErrorCode.ENTITY_NOT_FOUND)
        return ResponseEntity(of, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}