package br.com.shop.repository

import br.com.shop.model.Product
import br.com.shop.model.store.Store
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import kotlin.streams.toList

@DataJpaTest
@ActiveProfiles("test")
class StoreRepositoryTest(
    @Autowired
    val repository: StoreRepository,
    @Autowired
    val productRepository: ProductRepository,
) {
    private val product = Product( "Pan", "Red pan", 49.99,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg",)
    private val productOther = Product( "Laptop", "Black Laptop", 4999.99,
        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/14/Cast-Iron-Pan.jpg/1024px-Cast-Iron-Pan.jpg",)
    private val store = Store("Test", arrayListOf(product))
    private val storeOther = Store("Apple", arrayListOf(productOther))

    @AfterEach
    fun `cleaning repository`() {
        repository.deleteAll()
    }

    @Test
    fun `Get by name`(){
        repository.saveAll(arrayListOf(store, storeOther))
        val stores= repository.findByName(store.name, Pageable.unpaged()).get()
        val list= stores.toList()

        Assertions.assertNotNull(stores)
        Assertions.assertTrue(list.contains(store))
        Assertions.assertFalse(list.contains(storeOther))
    }

    @Test
    fun `Find All Products By IdStore`(){
        repository.saveAll(arrayListOf(store, storeOther))

        val stores = repository.findAllProductsByIdStore(store.id, Pageable.unpaged()).get()
        val list= stores.toList()

        Assertions.assertNotNull(stores)
        Assertions.assertTrue(list.contains(store.products))
        Assertions.assertFalse(list.contains(storeOther.products))
    }

    @Test
    fun `Find By IdProduct By IdStore`(){
        repository.saveAll(arrayListOf(store, storeOther))
        repository.findByIdProductByIdStore(store.id,product.id)

        //TODO FALTA TERMINAR
    }

    @Test
    fun `Find Extract By Id`(){}

    @Test
    fun `Withdraw`(){}


}