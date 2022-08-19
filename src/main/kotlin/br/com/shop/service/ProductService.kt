package br.com.shop.service

import br.com.shop.model.Product
import br.com.shop.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class ProductService {

    @Autowired
    private lateinit var productRepository: ProductRepository

    fun findAll(pageable: Pageable): Page<Product> {
        return productRepository.findAll(pageable)
    }
    fun findAll(nameProduct: String, pageable: Pageable): Page<Product> {
        return productRepository.findByName(nameProduct, pageable)
    }

    fun findById(id: Long): Optional<Product> {
        return productRepository.findById(id)
    }

    fun save(product: Product): Product {
        return productRepository.save(product)
    }

    fun delete(id: Long) {
        productRepository.deleteById(id)
    }

    fun existsById(id: Long): Boolean {
        return productRepository.existsById(id)
    }
}