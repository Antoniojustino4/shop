package br.com.shop.repository

import br.com.shop.model.Cart
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "carts", path = "carts")
interface CartRepository: PagingAndSortingRepository<Cart, Long>