package org.example.simpleblog.exception

import com.fasterxml.jackson.annotation.JsonFormat

// ENUM을 JSON 객체로 변환
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
enum class ErrorCode (
    val code:String,
    val message:String
){
    INVALID_INPUT_VALUE("C001", "invalid input value"),
    ENTITY_NOT_FOUND("C002", "entity not found")
}