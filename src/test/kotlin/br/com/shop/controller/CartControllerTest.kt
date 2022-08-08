package br.com.shop.controller

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import java.net.URI
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest(
    @Autowired
    val mockMvc: MockMvc
) {
    private var id :Long = 0
    private var idProduct :Long = 0
    private val url = URI("/carts")
    private val cartJson = "{\"productId\" : \"$idProduct\", \"name\" : \"Pan\", \"quantity\" : 1, \"price\" : 49.00"
    private val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : 1.0," +
            "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"


    @AfterEach
    fun `cleaning repository`() {
        if (id != 0L) {
            mockMvc.perform(delete("$url/$id")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
            )
        }
//        if (idProduct == 0L){
//            val response = mockMvc.perform(post("/products").content(productJson)
//                    .accept(APPLICATION_JSON)
//                    .contentType(APPLICATION_JSON))
//                .andExpect(status().isCreated).andReturn()
//            idProduct = getId(response, "products")
//        }
        id = 0L
    }

    @Test
    fun `Get isEmpty`() {
        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content").isEmpty)
    }


    @Test
    fun `Get isNotEmpty`() {
        val response = mockMvc.perform(post(url).content(cartJson)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated).andReturn()

        //id = getId(response)

        mockMvc.perform(get(url)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content").isNotEmpty)
    }

    @Test
    fun `Post`() {
        val response = mockMvc.perform(post(url).content(cartJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated).andReturn()

        //id = getId(response)

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content").isNotEmpty)
    }

    @Test
    fun `Delete`() {
        val response = mockMvc.perform(post(url).content(cartJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated).andReturn()

        //id = getId(response)

        mockMvc.perform(delete("$url/$id")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
        )

        id = 0L

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Put`() {
        val response = mockMvc.perform(post(url).content(cartJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated).andReturn()

        //id = getId(response)

        var newJson = "{\"productId\" : \"1\", \"name\" : \"Pan\", \"quantity\" : 2, \"price\" : 98.00"

        mockMvc.perform(put("$url/$id").content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent)
    }
}