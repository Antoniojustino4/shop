package br.com.shop.controller

import br.com.shop.dto.OrderDto
import br.com.shop.dto.modelAssembler.OrderModelAssembler
import br.com.shop.model.Order
import br.com.shop.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import javax.validation.Valid

@RestController
@ExposesResourceFor(Order::class)
@RequestMapping("orders")
class OrderController {

    @Autowired
    private lateinit var orderService: OrderService

    @Autowired
    private lateinit var orderModelAssembler: OrderModelAssembler

    @GetMapping
    fun findAll(@PageableDefault(sort = ["id"], direction = Sort.Direction.DESC, page = 0, size = 10) pageable: Pageable): ResponseEntity<CollectionModel<EntityModel<Order>>> {
        val orders: Page<Order> = orderService.findAll(pageable)

        val entityModels = orderModelAssembler.toCollectionModel(orders)
        return ResponseEntity(entityModels, HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun findById(@PathVariable id: Long): ResponseEntity<EntityModel<Order>> {
        if(orderService.existsById(id)){
            val order = orderService.findById(id)
            val entityModel = orderModelAssembler.toModel(order.get())
            return ResponseEntity(entityModel, HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    @PostMapping
    fun save(@RequestBody @Valid orderDto: OrderDto, uriBuilder: UriComponentsBuilder): ResponseEntity<EntityModel<Order>> {
        val orderSaved = orderService.save(orderDto.converter())
        val newOrderDto = orderModelAssembler.toModel(orderSaved)
        val uri: URI = uriBuilder.path("/orders/{id}").buildAndExpand(orderSaved.id).toUri()
        return ResponseEntity.created(uri).body(newOrderDto)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        if(orderService.existsById(id)) {
            return ResponseEntity(orderService.delete(id), HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.notFound().build()
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id: Long, @RequestBody @Valid orderDto: OrderDto): ResponseEntity<EntityModel<Order>> {
        if(orderService.existsById(id)) {
            val order = orderDto.converter(id)
            orderService.save(order)
            val newOrderDto = orderModelAssembler.toModel(order)
            return ResponseEntity(newOrderDto, HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.notFound().build()
    }
}