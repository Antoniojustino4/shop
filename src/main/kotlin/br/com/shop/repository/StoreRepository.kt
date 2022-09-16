package br.com.shop.repository

import br.com.shop.model.Store
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "stores", path = "stores")
interface StoreRepository: PagingAndSortingRepository<Store, Long> {

    fun findByName(nameStore: String, pageable: Pageable): Page<Store>
}