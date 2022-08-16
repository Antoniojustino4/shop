package br.com.shop.controller

import br.com.shop.dto.CartDto
import br.com.shop.model.Cart
import br.com.shop.service.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import javax.validation.Valid


@RestController
@ExposesResourceFor(Cart::class)
@RequestMapping("carts")
class CartController {

    @Autowired
    private lateinit var cartService: CartService

    @GetMapping
    fun findAll(@PageableDefault(sort = ["id"], direction = Sort.Direction.DESC, page = 0, size = 10) pageable: Pageable): ResponseEntity<Page<CartDto>> {
        val carts: Page<Cart> = cartService.findAll(pageable)
        return ResponseEntity(CartDto.converter(carts), HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun findById(@PathVariable id: Long): ResponseEntity<CartDto> {
        if (cartService.existsById(id)){
            val cart = cartService.findById(id)
            return ResponseEntity(CartDto(cart.get()), HttpStatus.OK)
        }
        return  ResponseEntity.notFound().build()
    }

    @PostMapping
    fun save(@RequestBody @Valid cartDto: CartDto, uriBuilder: UriComponentsBuilder): ResponseEntity<CartDto> {
        val cartSaved = cartService.save(cartDto.converter())
        val newCartDto = CartDto(cartSaved)
        val uri: URI = uriBuilder.path("/carts/{id}").buildAndExpand(cartSaved.id).toUri()
        return ResponseEntity.created(uri).body(newCartDto)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        if (cartService.existsById(id)){
            return ResponseEntity(cartService.delete(id), HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.notFound().build()
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id: Long, @RequestBody @Valid cartDto: CartDto): ResponseEntity<CartDto> {
        if (cartService.existsById(id)){
            val cart = cartDto.converter(id)
            val newCartDto = CartDto(cartService.save(cart))
            return ResponseEntity(newCartDto, HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.notFound().build()
    }
}