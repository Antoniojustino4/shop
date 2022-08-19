package br.com.shop.dto.modelAssembler

import br.com.shop.controller.CartController
import br.com.shop.model.Cart
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class CartModelAssembler: RepresentationModelAssembler<Cart, EntityModel<Cart>> {

    override fun toModel(cart: Cart): EntityModel<Cart> {
        val cartEntityModel = EntityModel.of(cart)
        val selfLink: Link = linkTo(methodOn(CartController::class.java).findById(cart.id)).withSelfRel()

        cartEntityModel.add(selfLink)
        return cartEntityModel
    }
}