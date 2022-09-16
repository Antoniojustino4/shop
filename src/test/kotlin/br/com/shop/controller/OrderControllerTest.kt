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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest(
    @Autowired
    val mockMvc: MockMvc
) {
    private var id :Long = 0
    private val url = URI("/orders")
    private var cartId: Long = 0
    private lateinit var response: MvcResult
    private var cont = 0
    private var productId: Long = 0

    private fun getJson(): String{
        return "{\"carts\": [ ${getJsonCarts()} ]}"
    }

    private fun getJsonCarts(): String{
        return "{\"id\": $cartId, \"product\": ${getJsonProduct()}, \"name\": \"Pan\", \"quantity\": 1, \"price\": 49.00}"
    }

    private fun getJsonProduct(): String{
        return "{\"id\" : \"$productId\", \"name\" : \"Pan Order\", \"description\" : \"Red\", \"price\" : 1.0," +
                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
    }

    private fun getId(){
        id = getId(response, "orders")
    }

    @BeforeEach
    fun `add product in repository`() {
        cont++
        if (cartId == 0L){
            val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : 1.0," +
                    "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
            val response = mockMvc.perform(post("/products").content(productJson)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated).andReturn()
            productId = getId(response, "products")
        }
        if (cont == 11){
            mockMvc.perform(delete("/carts/$cartId")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound)
            mockMvc.perform(delete("/products/$productId")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound)
        }

    }

    @Test
    fun `Get isEmpty`() {
        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
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
    }

    @Test
    fun `Get by id with id non-existent`() {
        mockMvc.perform(get("$url/2786")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `Post`() {
        response = mockMvc.perform(post(url).content(getJson())
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andReturn()

        getId()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)

        Assertions.assertNotNull(response.response.redirectedUrl)
    }

    @Test
    fun `Post with field carts empty`() {
        val newJson = "{\"carts\": []}"
        response = mockMvc.perform(post(url).content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.fields").value("carts"))
            .andReturn()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
    }

}