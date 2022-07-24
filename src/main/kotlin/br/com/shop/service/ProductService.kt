package br.com.shop.service

import br.com.shop.model.Product
import br.com.shop.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProductService {

    @Autowired
    lateinit var productRepository: ProductRepository

    fun findAll(pageable: Pageable): Page<Product> {
        return productRepository.findAll(pageable)
    }

    fun findById(id: Long): Product {
        return productRepository.findById(id).get()
    }

    fun save(product: Product): Product {
        return productRepository.save(product)
    }

    fun delete(id: Long) {
        productRepository.deleteById(id)
    }
}