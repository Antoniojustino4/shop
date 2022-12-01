package br.com.shop

import br.com.shop.model.Product
import br.com.shop.repository.StoreRepository
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import br.com.shop.model.store.Store

@Component
class Utils(
    @Autowired
    private val mockMvc: MockMvc,
    @Autowired
    val storeRepository: StoreRepository
) {
    val productJson = "{\"name\" : \"Pan\", \"description\" : \"Red\", \"price\" : 1.0," +
            "\"imageUrl\" : \"https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg\"}"

    val storeJson = "{\"name\" : \"Test\"}"

    fun getId(url: String?): String {
        if (url != null) {
            return url.split("/")[4]
        }
        return Assertions.fail()
    }

    fun request(mockRequest: MockHttpServletRequestBuilder, status: ResultMatcher): ResultActions {
        return mockMvc.perform(
            mockRequest.accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status)
    }

    fun saveObjectsWithReturnIds(): List<Long> {
        val storeAndProduct = saveStoreWithProduct()
        return listOf(storeAndProduct.store.id, storeAndProduct.product.id)
    }

    fun saveProductWithReturnId(): Long {
        val storeAndProduct = saveStoreWithProduct()
        return storeAndProduct.product.id
    }

    private fun saveStoreWithProduct(): StoreAndProduct {
        val store = Store("Test")
        val product = Product(
            "Pan", "Red pan", 49.99,
            "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg"
        )
        store.products.add(product)
        storeRepository.save(store)
        return StoreAndProduct(store, product)
    }

    fun saveStore(): Store {
        val store = Store("Test")

        return storeRepository.save(store)
    }


}

private class StoreAndProduct(val store: Store, val product: Product)