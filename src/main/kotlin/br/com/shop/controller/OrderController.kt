package br.com.shop.controller

import br.com.shop.dto.OrderDto
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
    fun findAll(): ResponseEntity<Page<OrderDto>> {
        val pageable: Pageable = PageRequest.of(0, 10)
        val orders: Page<Order> = orderService.findAll(pageable)
        val ordersDto: Page<OrderDto> = OrderDto.converter(orders)
        return ResponseEntity(ordersDto, HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun findById(@PathVariable id:Long): ResponseEntity<OrderDto> {
        val order = orderService.findById(id)
        val orderDto: OrderDto = OrderDto(order)
        return ResponseEntity(orderDto, HttpStatus.OK)
    }

    @PostMapping
    fun save(@RequestBody @PathVariable orderDto: OrderDto): ResponseEntity<OrderDto> {
        val order = orderDto.converter()
        val orderDto = OrderDto(orderService.save(order))
        return ResponseEntity(orderDto, HttpStatus.CREATED)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id:Long): ResponseEntity<Unit> {
        return ResponseEntity(orderService.delete(id), HttpStatus.NO_CONTENT)
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id:Long, @RequestBody orderDto: OrderDto): ResponseEntity<OrderDto> {
        val order = orderDto.converter(id)
        val orderDto = OrderDto(orderService.save(order))
        return ResponseEntity(orderDto, HttpStatus.NO_CONTENT)
    }
}