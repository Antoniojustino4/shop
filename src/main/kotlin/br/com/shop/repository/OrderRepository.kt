package br.com.shop.repository

import br.com.shop.model.Order
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository: PagingAndSortingRepository<Order, Long>