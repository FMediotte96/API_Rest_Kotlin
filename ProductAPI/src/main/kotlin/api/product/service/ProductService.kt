package api.product.service

import api.product.domain.Product
import org.springframework.stereotype.Service

@Service
class ProductService : BasicCrud<Product, String> {
    private val products: MutableSet<Product> = mutableSetOf(
        Product("Apple", 22.2), Product(price = 33.3, name = "Samsung")
    )

    override fun findAll(): List<Product> = products.toList()

    override fun findById(id: String): Product? = this.products.find { it.name == id }

    override fun save(t: Product): Boolean = this.products.add(t)

    override fun update(t: Product): Boolean = this.products.remove(t) && this.products.add(t)

    override fun deleteById(id: String): Boolean = this.products.remove(findById(id))
}