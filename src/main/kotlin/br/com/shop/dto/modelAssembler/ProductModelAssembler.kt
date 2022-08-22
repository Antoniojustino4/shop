package br.com.shop.dto.modelAssembler

import br.com.shop.controller.ProductController
import br.com.shop.model.Product
import br.com.shop.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.hateoas.server.mvc.add
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponents
import java.util.*

@Component
class ProductModelAssembler: RepresentationModelAssembler<Product, EntityModel<Product>> {

    override fun toModel(product: Product): EntityModel<Product> {
        val productEntityModel = EntityModel.of(product)
        val selfLink: Link = linkTo(methodOn(ProductController::class.java).findById(product.id)).withSelfRel()

        productEntityModel.add(selfLink)
        return productEntityModel
    }
}
