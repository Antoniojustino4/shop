package br.com.shop.repository

import br.com.shop.model.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository: JpaRepository<Cart, Long>