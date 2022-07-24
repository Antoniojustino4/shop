package br.com.shop.repository

import br.com.shop.model.Cart
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository: PagingAndSortingRepository<Cart, Long>