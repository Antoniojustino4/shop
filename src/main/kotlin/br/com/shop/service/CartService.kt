package br.com.shop.service

import br.com.shop.model.Cart
import br.com.shop.repository.CartRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class CartService {

    @Autowired
    private lateinit var cartRepository: CartRepository

    fun findAll(pageable: Pageable): Page<Cart> {
        return cartRepository.findAll(pageable)
    }

    fun findById(id: Long): Optional<Cart> {
        return cartRepository.findById(id)
    }

    fun save(cart: Cart): Cart {
        return cartRepository.save(cart)
    }

    fun delete(id: Long) {
        cartRepository.deleteById(id)
    }

    fun existsById(id: Long): Boolean {
        return cartRepository.existsById(id)
    }
}