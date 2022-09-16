package br.com.shop.controller.dto.modelAssembler

import br.com.shop.controller.OrderController
import br.com.shop.model.Order
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class OrderModelAssembler: RepresentationModelAssembler<Order, EntityModel<Order>> {

    override fun toModel(order: Order): EntityModel<Order> {
        val orderEntityModel = EntityModel.of(order)
        val selfLink: Link = linkTo(methodOn(OrderController::class.java).findById(order.id)).withSelfRel()
        val cartLink: Link = linkTo(methodOn(OrderController::class.java).findById(order.id)).withRel("cart")

        orderEntityModel.add(cartLink)
        orderEntityModel.add(selfLink)
        return orderEntityModel
    }
}