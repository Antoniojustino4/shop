package br.com.shop.service

import br.com.shop.exception.IdNoExistException
import br.com.shop.exception.InsufficientBalanceException
import br.com.shop.exception.ProductIsNotOfThisStoreException
import br.com.shop.model.Product
import br.com.shop.model.enums.OrderStatus
import br.com.shop.model.enums.ProductStatus
import br.com.shop.model.store.Extract
import br.com.shop.model.store.Store
import br.com.shop.repository.StoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.jvm.Throws

@Service
class StoreService {

    @Autowired
    private lateinit var storeRepository: StoreRepository

    fun findAll(name: Optional<String>, pageable: Pageable): Page<Store> {
        val products = if (name.isEmpty) {
            storeRepository.findAll(pageable)
        } else {
            storeRepository.findByName(name.get(), pageable)
        }
        return products
    }

    fun findById(id: Long): Optional<Store> {
        return storeRepository.findById(id)
    }

    fun findAllProductsByStoreId(id: Long, pageable: Pageable): Page<Product> {
        return storeRepository.findAllProductsByIdStore(id, pageable)
    }

    @Throws(ProductIsNotOfThisStoreException::class)
    fun findByIdProductByStoreId(id: Long, idProduct: Long): Product {
        val productString = storeRepository.findByIdProductByIdStore(id, idProduct)
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

    @Throws(IdNoExistException::class)
    fun findExtractByStoreId(id: Long): Extract {
        validId(id)
        return storeRepository.findExtractById(id)
    }

    @Throws(IdNoExistException::class, InsufficientBalanceException::class)
    fun withdraw(id: Long, value: Double): Extract {
        validId(id)
        hasBalance(id, value)
        storeRepository.withdraw(id, value)
        return storeRepository.findExtractById(id)
    }

    @Throws(IdNoExistException::class)
    fun save(store: Store): Store {
        return if (store.id == 0L) {
            storeRepository.save(store)
        } else {
            validId(store.id)
            store.extract = storeRepository.findExtractById(store.id)
            storeRepository.save(store)
        }
    }

    fun updateOrderStatus(id: Long, idOrder: Long, status: OrderStatus) {
        //storeRepository.updateOrderStatus(id, idOrder, status)
    }

    fun delete(id: Long) {
        storeRepository.deleteById(id)
    }

    @Throws(IdNoExistException::class)
    private fun validId(id: Long) {
        if (!storeRepository.existsById(id)) {
            throw IdNoExistException(this.javaClass.name)
        }
    }


    @Throws(InsufficientBalanceException::class)
    private fun hasBalance(id: Long, value: Double) {
        val isBalance = storeRepository.hasBalance(id, value)
        if (!isBalance) {
            throw InsufficientBalanceException()
        }
    }

    @Throws(IdNoExistException::class)
    fun isProductThisStore(id: Long, idProduct: Long) {
        validId(id)
        if (!storeRepository.isProductThisStore(id, idProduct)) {
            throw ProductIsNotOfThisStoreException(this.javaClass.name)
        }
    }
}