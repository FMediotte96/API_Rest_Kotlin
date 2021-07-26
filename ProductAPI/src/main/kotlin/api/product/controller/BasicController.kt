package api.product.controller

import api.product.service.BasicCrud
import org.springframework.web.bind.annotation.*

abstract class BasicController<T, ID>(private val basicCrud: BasicCrud<T, ID>) {
    @GetMapping
    fun findAll() = basicCrud.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: ID) = basicCrud.findById(id)

    @PostMapping
    fun save(@RequestBody body: T) = basicCrud.save(body)

    @PutMapping
    fun update(@RequestBody body: T) = basicCrud.update(body)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: ID) = basicCrud.deleteById(id)
}