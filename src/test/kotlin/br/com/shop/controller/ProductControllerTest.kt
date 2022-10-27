package br.com.shop.controller

import br.com.shop.Utils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.net.URI

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest(
    @Autowired
    val mockMvc: MockMvc
) {
    private var id :Long = 0
    private val url = URI("/products")
    private val utils = Utils(mockMvc)

    fun saveObject() {
        id = utils.saveProduct()
    }

    @Test
    fun `Get isEmpty`() {
        utils.request(get(url), status().isOk)
    }


    @Test
    fun `Get by name`() {
        saveObject()

        utils.request(get("$url").requestAttr("name", "Pan"), status().isOk)
    }

    @Test
    fun `Get by id`() {
        saveObject()

        utils.request(get("$url/$id"), status().isOk)
            .andExpect(jsonPath("$.name").isNotEmpty)
            .andExpect(jsonPath("$.name").value("Pan")).andReturn()
    }

    @Test
    fun `Get by id with id non-existent`() {
        utils.request(get("$url/0"), status().isNotFound)
    }

//    @Test
//    fun `Post`() {
//        response = mockMvc.perform(post(url).content(productJson)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isCreated).andReturn()
//
//        getId()
//
//        mockMvc.perform(get(url)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk)
//
//        Assertions.assertNotNull(response.response.redirectedUrl)
//    }

//    @Test
//    fun `Post with field name empty`() {
//        val productJson = "{\"name\" : \"\", \"description\" : \"Red\", \"price\" : 1.0," +
//                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
//        response = mockMvc.perform(post(url).content(productJson)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isBadRequest)
//            .andExpect(jsonPath("$.fields").value("name"))
//            .andReturn()
//
//        mockMvc.perform(get(url)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk)
//    }

//    @Test
//    fun `Post with field description empty`() {
//        val productJson = "{\"name\" : \"Pan\", \"description\" : \"\", \"price\" : 1.0," +
//                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
//        response = mockMvc.perform(post(url).content(productJson)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isBadRequest)
//            .andExpect(jsonPath("$.fields").value("description"))
//            .andReturn()
//
//        mockMvc.perform(get(url)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk)
//    }

//    @Test
//    fun `Post with field price empty`() {
//        val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : ," +
//                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
//        response = mockMvc.perform(post(url).content(productJson)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isBadRequest)
//            .andReturn()
//
//        mockMvc.perform(get(url)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk)
//    }

//    @Test
//    fun `Post with field price negative`() {
//        val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : -1," +
//                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
//        response = mockMvc.perform(post(url).content(productJson)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isBadRequest)
//            .andExpect(jsonPath("$.fields").value("price"))
//            .andReturn()
//
//        mockMvc.perform(get(url)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk)
//    }

//    @Test
//    fun `Post with field imageUrl empty`() {
//        val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : 1.0," +
//                "\"imageUrl\" : \"\"}"
//        response = mockMvc.perform(post(url).content(productJson)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isBadRequest)
//            .andExpect(jsonPath("$.fields").value("imageUrl"))
//            .andReturn()
//
//        mockMvc.perform(get(url)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk)
//    }

//    @Test
//    fun `Delete`() {
//        response = mockMvc.perform(post(url).content(productJson)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isCreated).andReturn()
//
//        getId()
//
//        mockMvc.perform(delete("$url/$id")
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isNoContent)
//
//        id = 0L
//
//        mockMvc.perform(get(url)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk)
//    }

//    @Test
//    fun `Delete with id non-existent`() {
//        mockMvc.perform(delete("$url/796842")
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isNotFound)
//
//
//        mockMvc.perform(get(url)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isOk)
//    }

//    @Test
//    fun `Put`() {
//        response = mockMvc.perform(post(url).content(productJson)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isCreated).andReturn()
//
//        getId()
//
//        val newJson = "{\"name\" : \"new Pan\", \"description\" : \"new Pan Red\", \"price\" : 2.0," +
//                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
//
//        mockMvc.perform(put("$url/$id").content(newJson)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isNoContent)
//    }

//    @Test
//    fun `Put with id non-existent`() {
//        mockMvc.perform(put("$url/87654").content(productJson)
//            .accept(APPLICATION_JSON)
//            .contentType(APPLICATION_JSON))
//            .andExpect(status().isNotFound)
//    }
}
