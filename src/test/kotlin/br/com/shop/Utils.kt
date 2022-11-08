package br.com.shop

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


class Utils(
    private val mockMvc: MockMvc
) {
    val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : 1.0," +
            "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"

    val storeJson = "{\"name\" : \"Test\"}"

    private fun getId(response: MvcResult, domain: String): Long {
        val locale = response.response.getHeaderValue("Location").toString()

        val regex = "(/$domain/)([1-9][0-9]?[0-9]?)".toRegex()
        val matchResult: MatchResult? = regex.find(locale)
        return matchResult?.groupValues?.get(2)?.toLong() ?: 0
    }

    fun request(mockRequest: MockHttpServletRequestBuilder, status: ResultMatcher): ResultActions {
        return mockMvc.perform(
            mockRequest.accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status)
    }


    fun saveObjectsWithReturnId(): List<Long> {
        val idStore = saveStoreWithReturnId()
        val idProduct = saveProductWithReturnId(idStore.toString())

        return listOf(idStore, idProduct)
    }

    fun saveProductWithReturnId(): Long {
        val idStore = saveStoreWithReturnId()
        return saveProductWithReturnId(idStore.toString())
    }

    private fun saveProductWithReturnId(idStore: String?): Long {
        val response = mockMvc.perform(
            MockMvcRequestBuilders.post("/stores/$idStore/products").content(productJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated).andReturn()

        return getId(response, "products")
    }


    fun saveStoreWithReturnId(): Long {
        val response = saveStore()
        return getId(response, "stores")
    }

    private fun saveStore(): MvcResult {
        val storeJson = "{\"name\" : \"Test\"}"

        return mockMvc.perform(
            MockMvcRequestBuilders.post("/stores").content(storeJson)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isCreated).andReturn()
    }
}