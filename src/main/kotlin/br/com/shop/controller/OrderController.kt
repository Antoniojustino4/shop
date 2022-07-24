package br.com.shop.controller

import br.com.shop.model.Cart
import br.com.shop.model.Order
import br.com.shop.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@ExposesResourceFor(Order::class)
@RequestMapping("orders")
class OrderController {

    @Autowired
    lateinit var orderService: OrderService

    @GetMapping
    fun list(): ResponseEntity<Page<Order>> {
        val pageable: Pageable = PageRequest.of(0, 10)
        val orders: Page<Order> = orderService.findAll(pageable)
        return ResponseEntity(orders, HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun findById(@PathVariable id:Long): ResponseEntity<Order> {
        return ResponseEntity(orderService.findById(id), HttpStatus.OK)
    }

    @PostMapping
    fun save(@RequestBody @PathVariable order: Order): ResponseEntity<Order> {
        return ResponseEntity(orderService.save(order), HttpStatus.CREATED)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id:Long): ResponseEntity<Unit> {
        return ResponseEntity(orderService.delete(id), HttpStatus.NO_CONTENT)
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id:Long, @RequestBody order: Order): ResponseEntity<Order> {
        order.id = id
        return ResponseEntity(orderService.save(order), HttpStatus.NO_CONTENT)
    }
}