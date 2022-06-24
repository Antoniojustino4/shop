package br.com.shop.service

import br.com.shop.repository.ShopUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ShopUserService: UserDetailsService{

    @Autowired
    private lateinit var shopUserRepository: ShopUserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        return Optional.ofNullable(shopUserRepository.findByUsername(username)).orElseThrow()
    }

}