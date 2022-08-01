package br.com.shop.controller

import br.com.shop.dto.OrderDto
import br.com.shop.model.Order
import br.com.shop.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.Optional
import java.util.OptionalLong
import javax.validation.Valid

@RestController
@ExposesResourceFor(Order::class)
@RequestMapping("orders")
class OrderController {

    @Autowired
    lateinit var orderService: OrderService

    @GetMapping
    fun findAll(@PageableDefault(sort = ["id"], direction = Sort.Direction.DESC, page = 0, size = 10) pageable: Pageable): ResponseEntity<Page<OrderDto>> {
        val orders: Page<Order> = orderService.findAll(pageable)
        return ResponseEntity(OrderDto.converter(orders), HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun findById(@PathVariable id: Long): ResponseEntity<OrderDto> {
        val order:Optional<Order> = Optional.ofNullable(orderService.findById(id))
        if (order.isEmpty){
            return ResponseEntity(OrderDto(order.get()), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    @PostMapping
    fun save(@RequestBody @Valid orderDto: OrderDto, uriBuilder: UriComponentsBuilder): ResponseEntity<OrderDto> {
        val orderSaved = orderService.save(orderDto.converter())
        val newOrderDto = OrderDto(orderSaved)
        val uri: URI = uriBuilder.path("/orders/{id}").buildAndExpand(orderSaved.id).toUri()
        return ResponseEntity.created(uri).body(newOrderDto)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        return ResponseEntity(orderService.delete(id), HttpStatus.NO_CONTENT)
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id: Long, @RequestBody @Valid orderDto: OrderDto): ResponseEntity<OrderDto> {
        val order = orderDto.converter(id)
        val newOrderDto = OrderDto(orderService.save(order))
        return ResponseEntity(newOrderDto, HttpStatus.NO_CONTENT)
    }
}