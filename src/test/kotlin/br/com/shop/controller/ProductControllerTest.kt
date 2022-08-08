package br.com.shop.controller

import org.junit.jupiter.api.AfterEach
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
class ProductControllerTest(
    @Autowired
    val mockMvc: MockMvc
) {
    private var id :Long = 0
    private val url = URI("/products")
    private val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : 1.0," +
            "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
    private lateinit var response: MvcResult

    fun getId(){
        id = getId(response, "products")
    }


    @AfterEach
    fun `cleaning repository`() {
        if (id != 0L) {
            mockMvc.perform(delete("$url/$id")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent)
            println("-----------------------------------------------------------")
        }
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
        response = mockMvc.perform(post(url).content(productJson)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated).andReturn()

        mockMvc.perform(get(url)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content").isNotEmpty)
    }

    @Test
    fun `Post`() {
        response = mockMvc.perform(post(url).content(productJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated).andReturn()

        getId()

        mockMvc.perform(get(url)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content").isNotEmpty)
    }

    @Test
    fun `Delete`() {
        response = mockMvc.perform(post(url).content(productJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andDo(print())
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
            .andExpect(jsonPath("$.content").isArray)
            .andExpect(jsonPath("$.content").isEmpty)
    }

    @Test
    fun `Put`() {
        response = mockMvc.perform(post(url).content(productJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated).andReturn()

        getId()

        var newJson = "{\"name\" : \"new Pan\", \"description\" : \"new Pan Red\", \"price\" : 2.0," +
                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"

        mockMvc.perform(put("$url/$id").content(newJson)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent)
    }
}