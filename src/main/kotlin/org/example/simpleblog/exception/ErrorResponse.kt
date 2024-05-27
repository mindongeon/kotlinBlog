package org.example.simpleblog.exception

import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult

/**
 * 오류 응답 처리를 위한 클래스
 */
class ErrorResponse (
    errorCode: ErrorCode,
    var errors:List<FieldError> = listOf()
){
    var code: String = errorCode.code
        private set

    var message: String = errorCode.message
        private set

    companion object {
        fun of(code: ErrorCode, bindingResult: BindingResult) = ErrorResponse(errorCode = code, FieldError.of(bindingResult))
        fun of(code: ErrorCode): ErrorResponse {
            return ErrorResponse(
                errorCode = code
            )
        }
    }

    class FieldError private constructor(
        val field: String,
        val value: String,
        val reason: String?
    ){
        /**
         * Companion object 사용 이유
         *
         * 클래스 내에서 하나의 정적 객체를 생성할 수 있는 방법
         * 자바의 static method 와 비슷한 개념
         *
         * 사용 이유
         * 1. 정적 메서드와 변수 제공
         * `companion object`내에 정의된 메서드와 변수는 클래스의 인스턴스를 생성하지 않고도 접근 가능.
         * -> 정적 유틸리티 메서드를 제공한는데 유용
         * 2. 팩토리 메서드 구현
         * 정적 팩토리 메서드를 제공하여 객체 생성 로직을 캡슐화할 수 있음.
         * -> 객체의 유연성은 높이고, 생성자를 직접 호출하는 대신 좀 더 직관적인 메서드 이름 사용 가능
         * 3. 동반 객체의 접근
         * 클래스와 밀접하게 관련된 함수를 정의할 수 있어, 외부에서 쉽게 접근 가능하며,
         * 내부 클래스와의 상호작용이 용이.
         */
        companion object {

            /**
             * of 메서드 사용 이유
             * 1. 명시적 생성
             * `of`는 객체를 생성하는 팩토리 메서드의 이름으로 자주 사용됨.
             * 객체를 생성하는 역할을 명확하게 나타냄.
             * `ErrorResponse.of(...)`는 `ErrorResponse`객체를 생성하는 메서드임.
             * 2. 짧고 직관적
             * 짧고 직관적인 이름으로, 메서드의 목적을 명확히 전달.
             * -> 코드의 가독성을 높임.
             * 3. 관례
             * 많은 라이브러리와 프레임워크에서 팩토리 메서드를 `of`를 사용하는 관례가 있음.
             * 자바의 `Optional.of(...)` 이나 `Stream.of(...)`와 같은 메서드들은 객체를 생성하는 용도.
             */
            fun of(bindingResult: BindingResult): List<FieldError> {
                return bindingResult.fieldErrors.map {
                    error -> FieldError(
                        field = error.field,
//                        value = if (error.rejectedValue == null) "" else error.rejectedValue.toString(),
                        value = error.rejectedValue.toString()?: "",
                        reason = error.defaultMessage
                    )
                }
            }
        }
    }
}