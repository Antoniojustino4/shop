package br.com.shop.controller

import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import java.net.URI
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartControllerTest(
    @Autowired
    val mockMvc: MockMvc
) {
    private var id :Long = 0
    private var idProduct :Long = 0
    private val url = URI("/carts")
    private val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : 1.0," +
            "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
    private lateinit var response: MvcResult

    private fun getId(){
        id = getId(response, "carts")
    }

    private fun getJson(): String {
        return "{\"productId\" : \"$idProduct\", \"name\" : \"Pan\", \"quantity\" : 1, \"price\" : 49.00}"
    }

    @AfterEach
    fun `cleaning repository`() {
        if (id != 0L) {
            mockMvc.perform(delete("$url/$id")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
            )
        }
        id = 0L
    }

    @BeforeEach
    fun `add product in repository`() {
        if (idProduct == 0L){
            val response = mockMvc.perform(post("/products").content(productJson)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated).andReturn()
            idProduct = getId(response, "products")
        }
    }

    @Test
    fun `Get isEmpty`() {
        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Get by id`() {
        response = mockMvc.perform(post(url).content(getJson())
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated).andReturn()

        getId()

        mockMvc.perform(get("$url/$id")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").isNotEmpty)
            .andExpect(jsonPath("$.name").value("Pan"))
    }

    @Test
    fun `Get by id with id not correspondence`() {
        mockMvc.perform(get("$url/2789")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Post`() {
        response = mockMvc.perform(post(url).content(getJson())
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated).andReturn()

        getId()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isNotEmpty)

        Assertions.assertNotNull(response.response.redirectedUrl)
    }

    @Test
    fun `Post with field productId empty`() {
        val newJson = "{\"productId\" : \"\", \"name\" : \"Pan\", \"quantity\" : 2, \"price\" : 98.00}"
        response = mockMvc.perform(post(url).content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.fields").value("productId"))
            .andReturn()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Post with field productId non-existent`() {
        val newJson = "{\"productId\" : \"7985\", \"name\" : \"Pan\", \"quantity\" : 2, \"price\" : 98.00}"
        response = mockMvc.perform(post(url).content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.fields").value("productId"))
            .andReturn()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Post with field name empty`() {
        val newJson = "{\"productId\" : \"$idProduct\", \"name\" : \"\", \"quantity\" : 2, \"price\" : 98.00}"
        response = mockMvc.perform(post(url).content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.fields").value("name"))
            .andReturn()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Post with field quantity empty`() {
        val newJson = "{\"productId\" : \"$idProduct\", \"name\" : \"Pan\", \"quantity\" : , \"price\" : 98.00}"
        response = mockMvc.perform(post(url).content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andReturn()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Post with field quantity negative`() {
        val newJson = "{\"productId\" : \"$idProduct\", \"name\" : \"Pan\", \"quantity\" : -2, \"price\" : 98.00}"
        response = mockMvc.perform(post(url).content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.fields").value("quantity"))
            .andReturn()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Post with field price empty`() {
        val newJson = "{\"productId\" : \"$idProduct\", \"name\" : \"Pan\", \"quantity\" : 2, \"price\": }"
        response = mockMvc.perform(post(url).content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andReturn()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Post with field price negative`() {
        val newJson = "{\"productId\" : \"$idProduct\", \"name\" : \"Pan\", \"quantity\" : 2, \"price\" : -98.00}"
        response = mockMvc.perform(post(url).content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.fields").value("price"))
            .andReturn()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Delete`() {
        response = mockMvc.perform(post(url).content(getJson())
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated).andReturn()

        getId()

        mockMvc.perform(delete("$url/$id")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNoContent)

        id = 0L

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Delete with id not corresponding`() {
        mockMvc.perform(delete("$url/1")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound)


        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Put`() {
        response = mockMvc.perform(post(url).content(getJson())
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated).andReturn()

        getId()

        var newJson = "{\"productId\" : \"$idProduct\", \"name\" : \"Pan\", \"quantity\" : 2, \"price\" : 98.00}"

        mockMvc.perform(put("$url/$id").content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent)
    }

    @Test
    fun `Put with id non-existent`() {
        mockMvc.perform(put("$url/1").content(getJson())
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }
}