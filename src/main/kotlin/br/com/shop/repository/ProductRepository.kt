package br.com.shop.repository

import br.com.shop.model.Product
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
interface ProductRepository: PagingAndSortingRepository<Product, Long>{

    fun findByName(nameProduct: String, pageable: Pageable): Page<Product>
}