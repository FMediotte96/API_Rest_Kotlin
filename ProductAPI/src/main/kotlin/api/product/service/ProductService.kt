package api.product.service

import api.product.dao.ProductDAO
import api.product.domain.Product
import api.product.utils.update
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductService(private val productDAO: ProductDAO) : BasicCrud<Product, String> {

    override fun findAll(): List<Product> = this.productDAO.findAll()

    override fun findById(id: String): Product? = this.productDAO.findByIdOrNull(id)

    override fun save(t: Product): Boolean = this.productDAO.save(t).let {
        return true
    }

    override fun update(t: Product): Boolean = this.productDAO.save(t).let {
        return true
    }

    override fun deleteById(id: String): Boolean = this.productDAO.deleteById(id).let {
        return true
    }
}