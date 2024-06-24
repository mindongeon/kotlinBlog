package org.example.simpleblog.domain

import mu.KotlinLogging
import net.jodah.expiringmap.ExpirationPolicy
import net.jodah.expiringmap.ExpiringMap
import java.util.*
import java.util.concurrent.TimeUnit

open class HashMapRepositoryImpl : InMemoryRepository {

    private val log = KotlinLogging.logger { }

    //    private val store = ConcurrentHashMap<String, Any>()
    /*
         메모리 DB는 WAS를 여러개 띄울 경우 공유성 데이터가 아니기 때문에
         외부 의존성 데이터를 두어 한다.
         -> 비슷한 역할을 하는 redis 를 활용한다.
         관계형 데이터베이스보다는 속도도 빠르고 부하도 적음.
         여러가지 WAS들이 공유할 수 있게끔.
    */
    private val store: MutableMap<String, Any> = ExpiringMap.builder()
//            .expiration(JwtManager.getRefreshTokenDay(), TimeUnit.DAYS)
        .expiration(1000, TimeUnit.MILLISECONDS).expirationPolicy(ExpirationPolicy.CREATED)
        .expirationListener { key: String, value: Any ->
            log.info { "key = $key, value = $value expired" }
        }.maxSize(1000000000).build()

    override fun clear() {
        store.clear()
    }

    override fun remove(key: String): Any? {
        return store.remove(key)
    }

    override fun findAll(): ArrayList<Any> {
        return ArrayList<Any>(store.values)
    }

    override fun findByKey(key: String): Any {
        return Optional.ofNullable(store[key]).orElseThrow {
            throw RuntimeException("not found refresh token")
        }
    }

    override fun save(key: String, value: Any) {
        Thread.sleep(50)
        store[key] = value
    }
}