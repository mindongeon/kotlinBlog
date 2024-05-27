package org.example.simpleblog.exception

/**
 * sealed class는 자신을 상속받는 클래스들을 제한하여 특정 타입 계층을 정의할 때 사용됨.
 * -> 컴파일러는 모든 상속받는 클래스를 알 수 있음.
 */
sealed class BusinessException : RuntimeException {
    // get 메서드는 무한 재귀 호출을 일으킬 수 있음.
    // Kotlin에서는 프로퍼티를 직접 접근 가능해 불필요한 코드.
/*
    private var errorCode: ErrorCode
        get() {
            return this.errorCode
        }
*/
    val errorCode: ErrorCode

    constructor(errorCode: ErrorCode):super(errorCode.message) {
        this.errorCode = errorCode
    }

    constructor(message: String?, errorCode: ErrorCode):super(message) {
        this.errorCode = errorCode
    }

}