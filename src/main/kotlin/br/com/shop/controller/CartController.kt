package br.com.shop.controller

import br.com.shop.model.Cart
import br.com.shop.service.CartService
import org.springframework.beans.factory.annotation.Autowired
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
    fun list(): ResponseEntity<List<Cart>> {
        return ResponseEntity(cartService.list(),HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun findById(@PathVariable id:Long): ResponseEntity<Cart> {
        return ResponseEntity(cartService.findById(id), HttpStatus.OK)
    }

    @PostMapping
    fun save(@RequestBody @PathVariable cart: Cart): ResponseEntity<Cart> {
        return ResponseEntity(cartService.save(cart), HttpStatus.CREATED)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id:Long): ResponseEntity<Unit> {
        return ResponseEntity(cartService.delete(id), HttpStatus.NO_CONTENT)
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id:Long, @RequestBody cart: Cart): ResponseEntity<Cart> {
        cart.id = id
        return ResponseEntity(cartService.save(cart), HttpStatus.NO_CONTENT)
    }
}