package br.com.shop.service

import br.com.shop.exception.IdNoExistException
import br.com.shop.model.Product
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
        val products = if (name.isEmpty){
            storeRepository.findAll(pageable)
        }else{
            storeRepository.findByName(name.get(), pageable)
        }
        return products
    }

    fun findById(id: Long): Optional<Store> {
        return storeRepository.findById(id)
    }

    fun findAllProductsByStoreId(id: Long, pageable: Pageable): Page<List<Product>> {
        return storeRepository.findAllProductsByStoreId(id, pageable)
    }

    @Throws(IdNoExistException::class)
    fun findExtractByStoreId(id: Long): Extract {
        validId(id)
        return storeRepository.findExtractById(id)
    }

    @Throws(IdNoExistException::class)
    fun withdraw(id: Long, value: Double) {
        validId(id)
        storeRepository.withdraw(id, value)
    }

    @Throws(IdNoExistException::class)
    fun save(store: Store): Store {
        return if (store.id == 0L){
            storeRepository.save(store)
        }else{
            validId(store.id)
            storeRepository.save(store)
        }
    }

    fun delete(id: Long) {
        storeRepository.deleteById(id)
    }

    @Throws(IdNoExistException::class)
    private fun validId(id: Long) {
        if (!storeRepository.existsById(id)){
            throw IdNoExistException(this.javaClass.name)
        }
    }

    fun toggleFavorite(id: Long) {
        //TODO
    }
}