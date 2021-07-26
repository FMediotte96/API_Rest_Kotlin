package api.product

import api.product.domain.Product
import api.product.service.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.*

@SpringBootTest
//@AutoConfigureMockMvc
class ProductApiApplicationTests {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private val mockMvc: MockMvc by lazy {
        MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .build()
    }

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var productService: ProductService

    private val productEndPoint = "/api/v1/product"

    @Test
    fun findAll() {
        val productsFromService = productService.findAll()

        val products: List<Product> = mockMvc
            .perform(MockMvcRequestBuilders.get(productEndPoint))
            .andExpect(status().isOk)
            .bodyTo(mapper)

        assertThat(productsFromService, Matchers.`is`(Matchers.equalTo(products)))
    }

    @Test
    fun findById() {
        val productsFromService = productService.findAll()
        assert(productsFromService.isNotEmpty()) { "Should not be empty" }

        val product = productsFromService.first()

        mockMvc.perform(MockMvcRequestBuilders.get("$productEndPoint/${product.name}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name", Matchers.`is`(product.name)))
    }

    @Test
    fun findByIdEmpty() {
        mockMvc.perform(MockMvcRequestBuilders.get("$productEndPoint/${UUID.randomUUID()}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun saveSuccessfully() {
        val product = Product(name = "PineApple", price = 50.0)

        val result: Boolean = mockMvc.perform(
            MockMvcRequestBuilders.post(productEndPoint).body(product, mapper)
        ).andExpect(status().isOk).bodyTo(mapper)

        assert(result)
    }

    @Test
    fun saveFail() {
        val productsFromService = productService.findAll()
        assert(productsFromService.isNotEmpty()) { "Should not be empty" }

        val product = productsFromService.first()

        val result: Boolean = mockMvc.perform(
            MockMvcRequestBuilders.post(productEndPoint).body(product, mapper)
        ).andExpect(status().isOk).bodyTo(mapper)

        assert(!result) { "Should be false" }
    }

    @Test
    fun updateSuccessfully() {
        val productsFromService = productService.findAll()
        assert(productsFromService.isNotEmpty()) { "Should not be empty" }

        //nos permite copiar el estado de dicho objeto y también nos permite cambiar sus atributos
        val product = productsFromService.first().copy(price = 44.23)

        val result: Boolean = mockMvc.perform(
            MockMvcRequestBuilders.put(productEndPoint).body(product, mapper)
        ).andExpect(status().isOk).bodyTo(mapper)

        assert(result)
    }

    @Test
    fun updateFail() {
        val product = Product(name = UUID.randomUUID().toString(), price = 123.123)

        val result: Boolean = mockMvc.perform(
            MockMvcRequestBuilders.put(productEndPoint).body(product, mapper)
        ).andExpect(status().isOk).bodyTo(mapper)

        assert(!result) { "Should be false" }
    }

    @Test
    fun deleteById() {
        val productsFromService = productService.findAll()
        assert(productsFromService.isNotEmpty()) { "Should not be empty" }

        val product = productsFromService.last()
        val result: Boolean = mockMvc.perform(
            MockMvcRequestBuilders.delete("$productEndPoint/${product.name}")
        ).andExpect(status().isOk).bodyTo(mapper)

        assert(result)
        assert(!productService.findAll().contains(product))
    }

    @Test
    fun deleteByIdFail() {
        val result: Boolean = mockMvc.perform(
            MockMvcRequestBuilders.delete("$productEndPoint/${UUID.randomUUID()}")
        ).andExpect(status().isOk).bodyTo(mapper)

        assert(!result) { "Should be false" }
    }
}
