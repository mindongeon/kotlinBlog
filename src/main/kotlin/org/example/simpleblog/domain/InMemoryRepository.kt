package org.example.simpleblog.domain

interface InMemoryRepository {

    fun clear()
    fun remove(key: String): Any?
    fun findAll(): ArrayList<Any>
    fun findByKey(key: String): Any
    fun save(key: String, value: Any)
}