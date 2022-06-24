package br.com.shop.service

import br.com.shop.model.Product
import br.com.shop.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductService {

    @Autowired
    lateinit var productRepository: ProductRepository

    fun list(): List<Product>{
        return productRepository.findAll()
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