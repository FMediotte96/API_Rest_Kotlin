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
    fun save(@Valid @RequestBody body: T) =
        ResponseEntity.status(HttpStatus.CREATED).body(this.basicCrud.save(body))

    @ApiOperation("Update entity")
    @PutMapping
    fun update(@RequestBody body: T) = this.basicCrud.update(body)

    @ApiOperation("Delete entity")
    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: ID) = this.basicCrud.deleteById(id)
}