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
class OrderControllerTest(
    @Autowired
    val mockMvc: MockMvc
) {
    private var id :Long = 0
    private val url = URI("/orders")
    private var cartId: Long = 0
    private lateinit var response: MvcResult

    private fun getJson(): String{
        return "{\"total\": 150.0, \"carts\": [ ${getJsonCarts()} ]}"
    }

    private fun getJsonCarts(): String{
        return "{\"id\": $cartId, \"productId\": 1, \"name\": \"Pan\", \"quantity\": 1, \"price\": 49.00}"
    }

    private fun getId(){
        id = getId(response, "orders")
    }

    @AfterEach
    fun `cleaning repository`() {
        if (id != 0L) {
            mockMvc.perform(delete("$url/$id")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent)
        }
        id = 0L
    }

    @BeforeEach
    fun `add product in repository`() {
        if (cartId == 0L){
            val cartJson = "{\"productId\": 1, \"name\": \"Pan\", \"quantity\": 1, \"price\": 49.00}"
            val response = mockMvc.perform(post("/carts").content(cartJson)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated).andReturn()
            cartId = getId(response, "carts")
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
            .andExpect(jsonPath("$.content").isNotEmpty)

        Assertions.assertNotNull(response.response.redirectedUrl)
    }

    @Test
    fun `Post with field total empty`() {
        val newJson = "{\"total\": , \"carts\": [ ${getJsonCarts()} ]}"
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
    fun `Post with field carts empty`() {
        val newJson = "{\"total\": 150.0, \"carts\": []}"
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
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Post with field total negative`() {
        val newJson = "{\"total\": -150.0, \"carts\": [ ${getJsonCarts()} ]}"
        response = mockMvc.perform(post(url).content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.fields").value("total"))
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
            .andDo(print())
            .andExpect(status().isCreated).andReturn()

        getId()

        mockMvc.perform(delete("$url/$id")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
        )

        id = 0L

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Delete with id non-existent`() {
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
            .andDo(print())
            .andExpect(status().isCreated).andReturn()

        getId()

        var newJson =  "{\"total\": 150.0, \"carts\": [ ${getJsonCarts()} ]}"

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