package api.product

import api.product.domain.Product
import api.product.service.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
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
            MockMvcRequestBuilders.post(productEndPoint)
                .content(mapper.writeValueAsBytes(product))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
        ).andExpect(status().isOk).bodyTo(mapper)

        assert(result)
    }

}
