package br.com.shop.service

import br.com.shop.model.Store
import br.com.shop.repository.StoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional

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

    fun save(store: Store): Store {
        return storeRepository.save(store)
    }

    fun delete(id: Long) {
        storeRepository.deleteById(id)
    }

    fun existsById(id: Long): Boolean {
        return storeRepository.existsById(id)
    }

    fun toggleFavorite(id: Long) {
        //TODO
    }
}