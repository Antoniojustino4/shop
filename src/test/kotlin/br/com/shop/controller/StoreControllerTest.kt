package br.com.shop.controller

import br.com.shop.Utils
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.net.URI

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StoreControllerTest(
    @Autowired
    val mockMvc: MockMvc
) {

    private var id: Long = 0
    private val url = URI("/stores")
    private val utils = Utils(mockMvc)

    private fun saveStore() {
        id = utils.saveStoreWithReturnId()
    }

    private fun urlProducts(): URI {
        return URI("/stores/$id/products")
    }

    @Test
    fun `get all stores`() {
        saveStore()
        utils.request(get(url), status().isOk)
            .andExpect(jsonPath("content").isArray)
            .andExpect(jsonPath("content").isNotEmpty)
    }

    @Test
    fun `find store by id`() {
        saveStore()
        utils.request(get("$url/$id"), status().isOk)
            .andExpect(jsonPath(".id", contains(id.toInt())))
    }

    @Test
    fun `return list empty when search for with id non-existent`() {
        utils.request(get("$url/0"), status().isNotFound)
    }

    @Test
    fun `save store with success`() {
        val name = "Test"
        val storeJson = "{\"name\" : \"$name\"}"

        val response = utils.request(post("$url").content(storeJson), status().isCreated)
            .andExpect(jsonPath("$.name").isNotEmpty)
            .andExpect(jsonPath("$.name").value(name)).andReturn()
        val redirectedUrl = response.response.redirectedUrl

        Assertions.assertNotNull(redirectedUrl)
    }

    @Test
    fun `should not save with name empty`() {
        val storeJson = "{\"name\" : \"\"}"
        val response = utils.request(post("$url").content(storeJson), status().isBadRequest).andReturn()
        Assertions.assertTrue(response.response.contentAsString.contains("The name field is mandatory"))
    }

    @Test
    fun `replace store with success`() {
        val response = utils.request(post("$url").content(utils.storeJson), status().isCreated).andReturn()
        val redirectedUrl = response.response.redirectedUrl
        val id = redirectedUrl.toString().split("/")[4]

        Assertions.assertNotNull(redirectedUrl)
        Assertions.assertNotNull(id)

        val storeEdited = "{\"name\" : \"otherTest\"}"
        utils.request(put("$redirectedUrl").content(storeEdited), status().isNoContent)
            .andExpect(jsonPath(".id", contains(id.toInt())))
            .andExpect(jsonPath("$.name").value("otherTest"))
    }

    @Test
    fun `should not replace with id non-existent`() {
        val otherResponse = utils.request(put("$url/-1").content(utils.storeJson), status().isNotFound).andReturn()
        Assertions.assertTrue(otherResponse.response.contentAsString.contains("The searched id does not exist"))
    }

    @Test
    fun `find all products by idStore`() {
        val listId = utils.saveObjectsWithReturnId()

        if (listId.isEmpty()) {
            Assertions.fail<String>()
        }
        val idStore = listId[0]
        val idProduct = listId[1].toInt()

        utils.request(get("$url/$idStore/products"), status().isOk)
            .andExpect(jsonPath("content").isArray)
            .andExpect(jsonPath("content").isNotEmpty)
            .andExpect(jsonPath(".content..id", contains(idProduct)))
    }

    @Test
    fun `return empty in find by idStore non-existent`() {
        utils.request(get("$url/-1/products"), status().isOk)
            .andExpect(jsonPath("content").isEmpty)
    }

    //todo renomear os metodos

    @Test
    fun `find product by id corresponding id store`() {
        val listId = utils.saveObjectsWithReturnId()

        if (listId.isEmpty()) {
            Assertions.fail<String>()
        }
        val idStore = listId[0]
        val idProduct = listId[1].toInt()

        utils.request(get("$url/$idStore/products/$idProduct"), status().isOk)
            .andExpect(jsonPath(".id", contains(idProduct)))
    }

    @Test
    fun `return empty in get by idProduct by idStore non-existent`() {
        saveStore()

        utils.request(get("${urlProducts()}/-1"), status().isNotFound)
    }


    @Test
    fun `save store with product`() {
        saveStore()

        val result = utils.request(post(urlProducts()).content(utils.productJson), status().isCreated)
            .andReturn()

        Assertions.assertNotNull(result.response.redirectedUrl)
    }

    @Test
    fun `try save product with field name empty`() {
        val productJson = "{\"name\" : \"\", \"description\" : \"Red\", \"price\" : 1.0," +
                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"

        val response = utils.request(post(urlProducts()).content(productJson), status().isBadRequest)
            .andReturn()

        Assertions.assertTrue(response.response.contentAsString.contains("The name field is mandatory"))
    }

    @Test
    fun `try save product with field description empty`() {
        val productJson = "{\"name\" : \"Pan\", \"description\" : \"\", \"price\" : 1.0," +
                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
        val response = utils.request(post(urlProducts()).content(productJson), status().isBadRequest)
            .andReturn()

        Assertions.assertTrue(response.response.contentAsString.contains("The description field is mandatory"))
    }

//    @Test
//    fun `Post with field price empty`() {
//        val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : ," +
//                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
//        response = mockMvc.perform(
//            post(url).content(productJson)
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//        )
//            .andExpect(status().isBadRequest)
//            .andReturn()
//
//        mockMvc.perform(
//            get(url)
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//        )
//            .andExpect(status().isOk)
//    }
//
//    @Test
//    fun `Post with field price negative`() {
//        val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : -1," +
//                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
//        response = mockMvc.perform(
//            post(url).content(productJson)
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//        )
//            .andExpect(status().isBadRequest)
//            .andExpect(jsonPath("$.fields").value("price"))
//            .andReturn()
//
//        mockMvc.perform(
//            get(url)
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//        )
//            .andExpect(status().isOk)
//    }
//
//    @Test
//    fun `Post with field imageUrl empty`() {
//        val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : 1.0," +
//                "\"imageUrl\" : \"\"}"
//        response = mockMvc.perform(
//            post(url).content(productJson)
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//        )
//            .andExpect(status().isBadRequest)
//            .andExpect(jsonPath("$.fields").value("imageUrl"))
//            .andReturn()
//
//        mockMvc.perform(
//            get(url)
//                .accept(APPLICATION_JSON)
//                .contentType(APPLICATION_JSON)
//        )
//            .andExpect(status().isOk)
//    }





    //fun saveProducts
    //fun replaceProducts
    //fun status
    //fun extract
    //fun withdraw
}