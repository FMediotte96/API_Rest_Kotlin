package api.product.controller

import api.product.service.BasicCrud
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

abstract class BasicController<T, ID>(private val basicCrud: BasicCrud<T, ID>) {

    @ApiOperation("Get all entities")
    @GetMapping
    fun findAll() = basicCrud.findAll()

    @ApiOperation("Get by Id")
    @GetMapping("/{id}")
    fun findById(@PathVariable id: ID): ResponseEntity<T> {
        val entity = basicCrud.findById(id)
        return ResponseEntity.status(entity?.let { HttpStatus.OK } ?: HttpStatus.NO_CONTENT)
            .body(entity)
    }

    @ApiOperation("Create entity")
    @PostMapping
    fun save(@Valid @RequestBody body: T): ResponseEntity<Boolean> {
        val entity = basicCrud.save(body)
        return ResponseEntity.status(if (entity) HttpStatus.CREATED else HttpStatus.CONFLICT)
            .body(entity)
    }

    @ApiOperation("Update entity")
    @PutMapping
    fun update(@RequestBody body: T): ResponseEntity<Boolean> {
        val entity = basicCrud.update(body)
        return ResponseEntity.status(if (entity) HttpStatus.OK else HttpStatus.CONFLICT)
            .body(entity)
    }

    @ApiOperation("Delete entity")
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: ID): ResponseEntity<Boolean> {
        val entity = basicCrud.deleteById(id)
        return ResponseEntity.status(if (entity) HttpStatus.OK else HttpStatus.NO_CONTENT)
            .body(entity)
    }
}