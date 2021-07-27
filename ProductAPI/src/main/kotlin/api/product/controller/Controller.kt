package api.product.controller

import api.product.domain.Product
import api.product.domain.Provider
import api.product.service.ProductService
import api.product.service.ProviderService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.FileNotFoundException

@RestController
@RequestMapping("/api/v1/product")
class ProductController(
    productService: ProductService
) : BasicController<Product, String>(productService) {

    @GetMapping("/test")
    fun fileNotFoundTest() {
        throw FileNotFoundException("just test")
    }
}

@RestController
@RequestMapping("api/v1/provider")
class ProviderController(
    providerService: ProviderService
) : BasicController<Provider, Int>(providerService)