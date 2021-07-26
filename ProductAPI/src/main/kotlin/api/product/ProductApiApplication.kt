package api.product

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class ProductApiApplication

fun main(args: Array<String>) {
    runApplication<ProductApiApplication>(*args)
}

@RestController
@RequestMapping("/api/v1/product")
class ProductController(private val productService: ProductService) {

    @GetMapping
    fun findAll() = productService.findAll()
}

@Service
class ProductService {
    private val products: Set<Product> = setOf(
        Product("Apple", 22.2), Product(price = 33.3, name = "Samsung")
    )

    fun findAll(): List<Product> = products.toList()

}

data class Product(val name: String, var price: Double? = 55.5) {
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as Product

        return this.name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

}