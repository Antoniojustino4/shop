package br.com.shop.controller

import br.com.shop.Utils
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

    private var id :Long = 0
    private val url = URI("/stores")
    private val utils = Utils(mockMvc)

    private fun saveStore(){
        id = utils.saveStore()
    }

    @Test
    fun `find All`(){
        utils.saveStore()
        utils.request(get(url), status().isOk)
            .andExpect(jsonPath("content").isArray)
            .andExpect(jsonPath("content").isNotEmpty)
    }

    @Test
    fun `find by Id`(){
        saveStore()
        utils.request(get("$url/$id"), status().isOk)
            .andExpect(jsonPath("$.name").isNotEmpty)
            .andExpect(jsonPath("$.name").value("Test")).andReturn()
    }

    @Test
    fun `find by Id with id non-existent`(){
        utils.request(get("$url/0"), status().isNotFound)
    }

    @Test
    fun `save`(){
        val storeJson = "{\"name\" : \"Test\"}"

        val response = utils.request(post("$url").content(storeJson), status().isCreated).andReturn()
        val redirectedUrl = response.response.redirectedUrl

        Assertions.assertNotNull(redirectedUrl)

        utils.request(get("$redirectedUrl"), status().isOk)
            .andExpect(jsonPath("$.name").isNotEmpty)
            .andExpect(jsonPath("$.name").value("Test"))
    }

    @Test
    fun `save without name`(){
        val storeJson = "{\"name\" : \"\"}"
        val response = utils.request(post("$url").content(storeJson), status().isBadRequest).andReturn()
        Assertions.assertTrue(response.response.contentAsString.contains("The name field is mandatory"))
    }

    @Test
    fun `replace`(){
        val storeJson = "{\"name\" : \"Test\"}"
        val response = utils.request(post("$url").content(storeJson), status().isCreated).andReturn()
        val redirectedUrl = response.response.redirectedUrl
        val id = response.response.redirectedUrl.toString().split("/")[4]
        Assertions.assertNotNull(redirectedUrl)
        Assertions.assertNotNull(id)

        val storeEdited= "{\"name\" : \"otherTest\"}"
        val otherResponse = utils.request(put("$redirectedUrl").content(storeEdited), status().isNoContent).andReturn()
        Assertions.assertTrue(otherResponse.response.contentAsString.contains("\"id\":${id}"))

        utils.request(get("$redirectedUrl"), status().isOk)
            .andExpect(jsonPath("$.name").isNotEmpty)
            .andExpect(jsonPath("$.name").value("otherTest"))
    }


    @Test
    fun `replace with id not-existent`(){
        val storeEdited= "{\"name\" : \"otherTest\"}"
        val otherResponse = utils.request(put("$url/-1").content(storeEdited), status().isNotFound).andReturn()
        Assertions.assertTrue(otherResponse.response.contentAsString.contains("The searched id does not exist"))
    }


    //fun replace
    //fun findAllProductsByStoreId
    //fun findByIdProductByStoreId
    //fun saveProducts
    //fun replaceProducts
    //fun status
    //fun extract
    //fun withdraw



}