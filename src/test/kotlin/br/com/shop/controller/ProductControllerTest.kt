package br.com.shop.controller

import br.com.shop.Utils
import org.hamcrest.Matchers.contains
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
    private var id: Long = 0
    private val url = URI("/products")
    private val utils = Utils(mockMvc)

    fun saveProduct() {
        id = utils.saveProductWithReturnId()
    }

    @Test
    fun `get products list empty`() {
        utils.request(get(url), status().isOk)
    }

    @Test
    fun `get products list with more one product`() {
        saveProduct()

        val name = "Pan"

        utils.request(get("$url").requestAttr("name", name), status().isOk)
            .andExpect(jsonPath(".content..name").isArray)
            .andExpect(jsonPath(".content..name").isNotEmpty)
    }

    @Test
    fun `get product by id`() {
        saveProduct()

        utils.request(get("$url/$id"), status().isOk)
            .andExpect(jsonPath(".id", contains(id.toInt())))
    }

    @Test
    fun `return list empty when search for with id non-existent`() {
        utils.request(get("$url/-1"), status().isNotFound)
    }
}