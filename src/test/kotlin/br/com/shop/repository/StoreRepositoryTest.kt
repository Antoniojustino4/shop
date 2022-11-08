package br.com.shop.repository

import br.com.shop.exception.ProductIsNotOfThisStoreException
import br.com.shop.model.Product
import br.com.shop.model.enums.ProductStatus
import br.com.shop.model.enums.TypeTransaction
import br.com.shop.model.store.Store
import br.com.shop.model.store.Transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import kotlin.streams.toList

@DataJpaTest
@ActiveProfiles("test")
class StoreRepositoryTest(
    @Autowired
    val repository: StoreRepository,
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
    fun `find store by name`(){
        repository.saveAll(arrayListOf(store, storeOther))
        val stores= repository.findByName(store.name, Pageable.unpaged()).get()
        val list= stores.toList()

        Assertions.assertNotNull(stores)
        Assertions.assertTrue(list.contains(store))
        Assertions.assertFalse(list.contains(storeOther))
    }

    @Test
    fun `find store by name not exist`() {
        repository.saveAll(arrayListOf(store, storeOther))
        val stores= repository.findByName("store.name", Pageable.unpaged()).get()
        val list= stores.toList()

        Assertions.assertNotNull(stores)
        Assertions.assertTrue(list.isEmpty())
    }

    @Test
    fun `find all products of one store`(){
        repository.saveAll(arrayListOf(store, storeOther))

        val list = repository.findAllProductsByIdStore(store.id, Pageable.unpaged()).get()
        val products = list.toList()

        Assertions.assertNotNull(list)
        Assertions.assertTrue(products.contains(product))
        Assertions.assertFalse(products.contains(productOther))
    }


    @Test
    fun `find all products of one store non-existent`(){
        repository.saveAll(arrayListOf(store, storeOther))

        val list = repository.findAllProductsByIdStore(store.id, Pageable.unpaged()).get()
        val products = list.toList()

        Assertions.assertNotNull(list)
        Assertions.assertTrue(products.contains(product))
        Assertions.assertFalse(products.contains(productOther))
    }

    @Test
    fun `find product from store`(){
        repository.save(store)
        val product = findByIdProductByIdStore(store.id,product.id)

        Assertions.assertNotNull(product)
        Assertions.assertEquals(this.product.id, product.id)
        Assertions.assertTrue(product == this.product)
    }

    @Test
    fun `find product non-existent from store`(){
        repository.save(store)

        Assertions.assertThrows(ProductIsNotOfThisStoreException::class.java) { findByIdProductByIdStore(store.id, 0); }
    }

    @Test
    fun `find product of one store non-existent`(){
        repository.save(store)

        Assertions.assertThrows(ProductIsNotOfThisStoreException::class.java) { findByIdProductByIdStore(0, product.id); }
    }

    @Test
    fun `is this product of this store`(){
        repository.saveAll(arrayListOf(store, storeOther))

        Assertions.assertTrue(repository.isProductThisStore(store.id, product.id))
        Assertions.assertFalse(repository.isProductThisStore(store.id, productOther.id))
    }

    @Test
    fun `is this product of this store non-existent`(){
        repository.save(store)
        Assertions.assertFalse(repository.isProductThisStore(store.id, -1))
    }

    @Test
    fun `find extract from store id`(){
        repository.saveAll(arrayListOf(store, storeOther))
        val extract = repository.findExtractById(storeOther.id)

        Assertions.assertNotNull(extract)
        Assertions.assertTrue(extract.idExtract == storeOther.extract.idExtract)
    }

    @Test
    fun `find extract from store id not-existent`(){
        Assertions.assertThrows(EmptyResultDataAccessException::class.java){repository.findExtractById(0)}
    }

    @Test
    fun `withdraw`(){
        store.extract.addTransaction(Transaction(TypeTransaction.DEPOSIT, 200.00))
        repository.save(store)

        val returned = repository.withdraw(store.id, 100.0)

        Assertions.assertEquals(1, returned)
    }

    private fun findByIdProductByIdStore(id: Long, idProduct: Long): Product {
        val productString = repository.findByIdProductByIdStore(id, idProduct)
        if (productString == null || productString.javaClass != String::class.java) {
            throw ProductIsNotOfThisStoreException()
        }
        val list = productString.split(",")
        return Product(
            list[3],
            list[1],
            list[4].toDouble(),
            list[2],
            ProductStatus.convert(list[5]),
            list[0].toLong(),
        )
    }
}