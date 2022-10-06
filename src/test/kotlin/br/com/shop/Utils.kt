package br.com.shop.controller

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

fun getId(response: MvcResult, domain: String): Long {
    val locale = response.response.getHeaderValue("Location").toString()

    val regex = "(/$domain/)([1-9][0-9]?[0-9]?)".toRegex()
    val matchResult: MatchResult? = regex.find(locale)
    return matchResult?.groupValues?.get(2)?.toLong() ?: 0
}

fun saveObjects(mockMvc: MockMvc): Long {
    val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : 1.0," +
            "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"
    val storeJson = "{\"name\" : \"Test\"}"

    var response = mockMvc.perform(
        MockMvcRequestBuilders.post("/stores").content(storeJson)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated).andReturn()

    val a = response.response.redirectedUrl
    response = mockMvc.perform(
        MockMvcRequestBuilders.post("$a/products").content(productJson)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated).andReturn()

    return getId(response,"products")
}