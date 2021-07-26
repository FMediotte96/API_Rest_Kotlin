package api.product.controller

import api.product.domain.Product
import api.product.service.ProductService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/product")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun findAll() = productService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String) = productService.findById(id)

    @PostMapping
    fun save(@RequestBody product: Product) = productService.save(product)

    @PutMapping
    fun update(@RequestBody product: Product) = productService.update(product)

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: String) = productService.deleteById(id)
}