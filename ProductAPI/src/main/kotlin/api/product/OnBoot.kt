package api.product

import api.product.domain.Product
import api.product.domain.Provider
import api.product.service.ProductService
import api.product.service.ProviderService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class OnBoot(
    private val productService: ProductService,
    private val providerService: ProviderService
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val defaultProvider = Provider(id = 1, name = "Facundo", email = "facumediotte@gmail.com")

        if (!providerService.providerDAO.existsById(defaultProvider.id)) {
            this.providerService.save(defaultProvider)
        }

        listOf(
            Product("Apple", 22.2, 5, defaultProvider),
            Product(stock = 1, price = 33.8, name = "Orange", provider = defaultProvider)
        ).forEach {
            if (!productService.productDAO.existsById(it.name)) {
                println("Saving -> ${it.name}")
                productService.save(it)
            }
        }
    }
}
