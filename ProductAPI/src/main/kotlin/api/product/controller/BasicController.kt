package api.product.controller

import api.product.service.BasicCrud
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

abstract class BasicController<T, ID>(private val basicCrud: BasicCrud<T, ID>) {
    @GetMapping
    fun findAll() = basicCrud.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: ID): ResponseEntity<T> {
        val entity = basicCrud.findById(id)
        return ResponseEntity.status(entity?.let { HttpStatus.OK } ?: HttpStatus.NO_CONTENT)
            .body(entity)
    }

    @PostMapping
    fun save(@RequestBody body: T): ResponseEntity<Boolean> {
        val entity = basicCrud.save(body)
        return ResponseEntity.status(if (entity) HttpStatus.CREATED else HttpStatus.CONFLICT)
            .body(entity)
    }

    @PutMapping
    fun update(@RequestBody body: T): ResponseEntity<Boolean> {
        val entity = basicCrud.update(body)
        return ResponseEntity.status(if (entity) HttpStatus.OK else HttpStatus.CONFLICT)
            .body(entity)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: ID): ResponseEntity<Boolean> {
        val entity = basicCrud.deleteById(id)
        return ResponseEntity.status(if (entity) HttpStatus.OK else HttpStatus.NO_CONTENT)
            .body(entity)
    }
}