package br.com.shop.service

import br.com.shop.exception.IdNoExistException
import br.com.shop.model.Product
import br.com.shop.model.enums.ProductStatus
import br.com.shop.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.Optional
import kotlin.jvm.Throws

@Service
class ProductService {

    @Autowired
    private lateinit var productRepository: ProductRepository

    fun findAll(name: Optional<String>, pageable: Pageable): Page<Product> {
        val products = if (name.isEmpty){
            productRepository.findAll(pageable)
        }else{
            productRepository.findByName(name.get(), pageable)
        }
        return products
    }

    fun findById(id: Long): Optional<Product> {
        return productRepository.findById(id)
    }

    fun save(product: Product): Product {
        return if (product.id == 0L){
            productRepository.save(product)
        }else{
            validId(product.id)
            productRepository.save(product)
        }
    }

    fun changeStatus(id: Long, status: ProductStatus) {
        validId(id)
        productRepository.changeStatus(status, id)
    }

    @Throws(IdNoExistException::class)
    private fun validId(id: Long) {
        if (!productRepository.existsById(id)){
            throw IdNoExistException(this.javaClass.name)
        }
    }
}