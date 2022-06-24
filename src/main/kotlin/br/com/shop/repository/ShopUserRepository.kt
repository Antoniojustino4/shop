package br.com.shop.repository

import br.com.shop.model.ShopUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShopUserRepository: JpaRepository<ShopUser, Long> {

    fun findByUsername(username: String): ShopUser
}