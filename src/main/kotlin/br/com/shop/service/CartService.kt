package br.com.shop.service

import br.com.shop.model.Cart
import br.com.shop.repository.CartRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CartService {

    @Autowired
    lateinit var cartRepository: CartRepository

    fun list(): List<Cart>{
        return cartRepository.findAll()
    }

    fun findById(id: Long): Cart {
        return cartRepository.findById(id).get()
    }

    fun save(cart: Cart): Cart {
        return cartRepository.save(cart)
    }

    fun delete(id: Long) {
        cartRepository.deleteById(id)
    }
}