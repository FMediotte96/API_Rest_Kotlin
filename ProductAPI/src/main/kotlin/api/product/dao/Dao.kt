package api.product.dao

import api.product.domain.Product
import api.product.domain.Provider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductDAO : JpaRepository<Product, String>

@Repository
interface ProviderDAO: JpaRepository<Provider,Int>