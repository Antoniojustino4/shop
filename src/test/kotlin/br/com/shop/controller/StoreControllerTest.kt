package br.com.shop.controller

import br.com.shop.Utils
import org.hamcrest.Matchers.contains
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.net.URI

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StoreControllerTest(
    @Autowired
    val utils: Utils
) {

    private var id: Long = 0
    private val url = URI("/stores")

    private fun saveStore() {
        id = utils.saveStore().id
    }

    private fun urlProducts(): URI {
        saveStore()
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

        val result = utils.request(post("$url").content(storeJson), status().isCreated)
            .andExpect(jsonPath("$.name").isNotEmpty)
            .andExpect(jsonPath("$.name").value(name)).andReturn()
        val redirectedUrl = result.response.redirectedUrl

        Assertions.assertNotNull(redirectedUrl)
    }

    @Test
    fun `should not save with name empty`() {
        val storeJson = "{\"name\" : \"\"}"
        val result = utils.request(post("$url").content(storeJson), status().isBadRequest).andReturn()
        Assertions.assertTrue(result.response.contentAsString.contains("The name field is mandatory"))
    }

    @Test
    fun `replace store with success`() {
        val result = utils.request(post("$url").content(utils.storeJson), status().isCreated).andReturn()
        val redirectedUrl = result.response.redirectedUrl
        val id = utils.getId(redirectedUrl)

        Assertions.assertNotNull(redirectedUrl)
        Assertions.assertNotNull(id)

        val storeEdited = "{\"name\" : \"otherTest\"}"
        val otherResult= utils.request(put("$redirectedUrl").content(storeEdited), status().isNoContent)
            .andExpect(jsonPath(".id", contains(id.toInt())))
            .andExpect(jsonPath("$.name").value("otherTest")).andReturn()

        val stringList = otherResult.response.contentAsString.split("\"", ":", ",")
        Assertions.assertTrue(stringList.contains("otherTest"))
    }

    @Test
    fun `should not replace with id non-existent`() {
        val result = utils.request(put("$url/-1").content(utils.storeJson), status().isNotFound).andReturn()
        Assertions.assertTrue(result.response.contentAsString.contains("The searched id does not exist"))
    }

    @Test
    fun `find all products by idStore`() {
        val listId = utils.saveObjectsWithReturnIds()

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

    @Test
    fun `find product by id corresponding id store`() {
        val listId = utils.saveObjectsWithReturnIds()

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
        utils.request(get("${urlProducts()}/-1"), status().isNotFound)
    }


    @Test
    fun `save store with product`() {
        val result = utils.request(post(urlProducts()).content(utils.productJson), status().isCreated)
            .andReturn()

        Assertions.assertNotNull(result.response.redirectedUrl)
    }

    @Test
    fun `should not save product with field name empty`() {
        val productJson = "{\"name\" : \"\", \"description\" : \"Red\", \"price\" : 1.0," +
                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"

        val result = utils.request(post(urlProducts()).content(productJson), status().isBadRequest)
            .andReturn()

        Assertions.assertTrue(result.response.contentAsString.contains("The name field is mandatory"))
    }

    @Test
    fun `should not save product with field description empty`() {
        val productJson = "{\"name\" : \"Pan\", \"description\" : \"\", \"price\" : 1.0," +
                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
        val result = utils.request(post(urlProducts()).content(productJson), status().isBadRequest)
            .andReturn()

        Assertions.assertTrue(result.response.contentAsString.contains("The description field is mandatory"))
    }

    @Test
    fun `should not save product with field price empty`() {
        val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : ," +
                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
        utils.request(post(urlProducts()).content(productJson), status().isBadRequest)
    }

    @Test
    fun `should not save product with field price negative`() {
        val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : -1.0," +
                "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
        val result= utils.request(post(urlProducts()).content(productJson), status().isBadRequest).andReturn()

        Assertions.assertTrue(result.response.contentAsString.contains("The price field cannot is smaller that one"))
    }

    @Test
    fun `should not save product with field imageUrl empty`() {
        val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : -1.0," +
                "\"imageUrl\" : \"\"}"
        val result= utils.request(post(urlProducts()).content(productJson), status().isBadRequest).andReturn()

        Assertions.assertTrue(result.response.contentAsString.contains("The imageUrl field is mandatory"))
    }

    @Test
    fun `replace product`(){
        val urlProducts = urlProducts()
        val result = utils.request(post(urlProducts).content(utils.productJson), status().isCreated)
            .andReturn()

        val url = result.response.redirectedUrl
        val id = utils.getId(url)

        Assertions.assertNotNull(url)

        val productJson = "{\"name\": \"Hat\", \"description\": \"Red hat\", \"price\": 12.0," +
                "\"imageUrl\": \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"

        val otherResult = utils.request(put("$urlProducts/$id").content(productJson), status().isNoContent).andReturn()

        val stringList = otherResult.response.contentAsString.split("\"", ":", ",")
        Assertions.assertTrue(stringList.contains("Hat"))
        Assertions.assertTrue(stringList.contains("Red hat"))
        Assertions.assertTrue(stringList.contains("12.0"))
    }

    @Test
    fun `replace product with id not-existent`(){
        val urlProducts = urlProducts()
        val productJson = "{\"name\": \"Hat\", \"description\": \"Red hat\", \"price\": 12.0," +
                "\"imageUrl\": \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"

        utils.request(put("$urlProducts/-1").content(productJson), status().isNotFound)
    }

    @Test
    fun `change status`(){
        val store = utils.saveObjectsWithReturnIds()

        utils.request(patch("$url/${store[0]}/products/${store[1]}/changeStatus").content("\"UNAVAILABLE\""), status().isOk)
    }

    @Test
    fun `get extract`(){
        saveStore()

        val result = utils.request(get("$url/$id/extract"), status().isOk).andReturn()

        Assertions.assertTrue(result.response.contentAsString.contains("balance"))
    }

    @Test
    fun `to do withdraw`(){
        val value = 500.0
        val withdraw= 100.0
        val store = utils.saveStoreWithBalance(value)

       val result = utils.request(patch("$url/${store.id}/extract/withdraw").content("{\"balance\": $withdraw}"), status().isNoContent).andReturn()

        Assertions.assertTrue(result.response.contentAsString.contains("balance"))

        val stringList = result.response.contentAsString.split("\"", ":", ",", "}")

        Assertions.assertEquals(stringList[3].toDouble(), (value-withdraw))
    }

    @Test
    fun `to do withdraw with value bigger than balance`(){
        val value = 500.0
        val withdraw= 600.0
        val store = utils.saveStoreWithBalance(value)

        val result = utils.request(patch("$url/${store.id}/extract/withdraw").content("{\"balance\": $withdraw}"), status().isNotFound).andReturn()

        //todo mudar todos os nomes das variaveis que tem split
        val stringList = result.response.contentAsString.split("\"", ":", ",", "}")

        Assertions.assertTrue(stringList.contains("Insufficient Balance exception"))
    }
    
}