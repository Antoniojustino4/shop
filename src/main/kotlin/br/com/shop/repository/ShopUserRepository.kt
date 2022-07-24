package br.com.shop.repository

import br.com.shop.model.ShopUser
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface ShopUserRepository: PagingAndSortingRepository<ShopUser, Long> {

    fun findByUsername(username: String): ShopUser
}