package br.com.shop.controller.dto.modelAssembler

import br.com.shop.controller.ProductController
import br.com.shop.model.Product
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class ProductModelAssembler: RepresentationModelAssembler<Product, EntityModel<Product>> {

    override fun toModel(product: Product): EntityModel<Product> {
        val productEntityModel = EntityModel.of(product)
        val selfLink: Link = linkTo(methodOn(ProductController::class.java).findById(product.id)).withSelfRel()

        productEntityModel.add(selfLink)
        return productEntityModel
    }
}
