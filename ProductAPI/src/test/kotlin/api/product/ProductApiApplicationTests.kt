package api.product

import api.product.domain.Product
import api.product.service.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
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
@TestMethodOrder(MethodOrderer.Alphanumeric::class)
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
    fun a_findAll() {
        val productsFromService = productService.findAll()

        val products: List<Product> = mockMvc
            .perform(MockMvcRequestBuilders.get(productEndPoint))
            .andExpect(status().isOk)
            .bodyTo(mapper)

        assertThat(productsFromService, Matchers.`is`(Matchers.equalTo(products)))
    }

    @Test
    fun b_findById() {
        val productsFromService = productService.findAll()
        assert(productsFromService.isNotEmpty()) { "Should not be empty" }

        val product = productsFromService.first()

        mockMvc.perform(MockMvcRequestBuilders.get("$productEndPoint/${product.name}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name", Matchers.`is`(product.name)))
    }

    @Test
    fun c_findByIdEmpty() {
        mockMvc.perform(MockMvcRequestBuilders.get("$productEndPoint/${UUID.randomUUID()}"))
            .andExpect(status().isNoContent)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun d_saveSuccessfully() {
        val product = Product(name = "PineApple", price = 50.0)

        val productFromApi: Product = mockMvc.perform(
            MockMvcRequestBuilders.post(productEndPoint).body(product, mapper)
        ).andExpect(status().isCreated).bodyTo(mapper)

        assert(productService.findAll().contains(productFromApi))
    }

    @Test
    fun d2_saveCheckRules() {
        mockMvc.perform(
            MockMvcRequestBuilders.post(productEndPoint).body(Product("", -50.0), mapper)
        ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.price").exists())
    }

    @Test
    fun e_saveDuplicateEntity() {
        val productsFromService = productService.findAll()
        assert(productsFromService.isNotEmpty()) { "Should not be empty" }

        val product = productsFromService.first()

        mockMvc.perform(
            MockMvcRequestBuilders.post(productEndPoint).body(product, mapper)
        ).andExpect(status().isConflict)
            .andExpect(jsonPath("$.title", Matchers.`is`("DuplicateKeyException")))
    }

    @Test
    fun f_updateSuccessfully() {
        val productsFromService = productService.findAll()
        assert(productsFromService.isNotEmpty()) { "Should not be empty" }

        //nos permite copiar el estado de dicho objeto y también nos permite cambiar sus atributos
        val product = productsFromService.first().copy(price = 44.23)

        val productFromApi: Product = mockMvc.perform(
            MockMvcRequestBuilders.put(productEndPoint).body(product, mapper)
        ).andExpect(status().isOk).bodyTo(mapper)

        assertThat(productService.findById(product.name), Matchers.`is`(productFromApi))
    }

    @Test
    fun g_updateEntityNotFound() {
        val product = Product(name = UUID.randomUUID().toString(), price = 123.123)

        mockMvc.perform(
            MockMvcRequestBuilders.put(productEndPoint).body(product, mapper)
        ).andExpect(status().isConflict)
            .andExpect(jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))
    }

    @Test
    fun h_deleteById() {
        val productsFromService = productService.findAll()
        assert(productsFromService.isNotEmpty()) { "Should not be empty" }

        val product = productsFromService.last()
        val productFromApi: Product = mockMvc.perform(
            MockMvcRequestBuilders.delete("$productEndPoint/${product.name}")
        ).andExpect(status().isOk).bodyTo(mapper)

        assert(!productService.findAll().contains(productFromApi))
    }

    @Test
    fun i_deleteByIdEntityNotFound() {
        mockMvc.perform(
            MockMvcRequestBuilders.delete("$productEndPoint/${UUID.randomUUID()}")
        ).andExpect(status().isConflict)
            .andExpect(jsonPath("$.title", Matchers.`is`("EntityNotFoundException")))
    }

}
