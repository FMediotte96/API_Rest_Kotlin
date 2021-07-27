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

    val defaultProvider = providerService.save(
        Provider(name = "Facundo", email = "facumediotte@gmail.com")
    )

    override fun run(args: ApplicationArguments?) {
        listOf(
            Product("Apple", 22.2, 5, defaultProvider),
            Product(stock = 1, price = 33.8, name = "Orange", provider = defaultProvider)
        ).forEach {
            println("Saving -> ${it.name}")
            productService.save(it)
        }
    }

}