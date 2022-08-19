package br.com.shop.repository

import br.com.shop.model.Order
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "orders", path = "orders")
interface OrderRepository: PagingAndSortingRepository<Order, Long>