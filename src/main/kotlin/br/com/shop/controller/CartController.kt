package br.com.shop.controller

import br.com.shop.dto.CartDto
import br.com.shop.model.Cart
import br.com.shop.service.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@ExposesResourceFor(Cart::class)
@RequestMapping("carts")
class CartController {

    @Autowired
    lateinit var cartService: CartService

    @GetMapping
    fun findAll(): ResponseEntity<Page<CartDto>> {
        val pageable: Pageable = PageRequest.of(0, 10)
        val carts: Page<Cart> = cartService.findAll(pageable)
        val cartsDto = CartDto.converter(carts)
        return ResponseEntity(cartsDto, HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun findById(@PathVariable id:Long): ResponseEntity<CartDto> {
        val cart = cartService.findById(id)
        val cartDto = CartDto(cart)
        return ResponseEntity(cartDto, HttpStatus.OK)
    }

    @PostMapping
    fun save(@RequestBody @PathVariable cartDto: CartDto): ResponseEntity<CartDto> {
        val cart = cartDto.converter()
        val cartDto = CartDto(cartService.save(cart))
        return ResponseEntity(cartDto, HttpStatus.CREATED)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id:Long): ResponseEntity<Unit> {
        return ResponseEntity(cartService.delete(id), HttpStatus.NO_CONTENT)
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id:Long, @RequestBody cartDto: CartDto): ResponseEntity<CartDto> {
        val cart = cartDto.converter(id)
        val cartDto = CartDto(cartService.save(cart))
        return ResponseEntity(cartDto, HttpStatus.NO_CONTENT)
    }
}