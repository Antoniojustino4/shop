package br.com.shop.controller

import br.com.shop.model.Product
import br.com.shop.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@ExposesResourceFor(Product::class)
@RequestMapping("products")
class ProductController {

    @Autowired
    private lateinit var productService: ProductService

    @GetMapping
    fun findAll(@PageableDefault(sort = ["name"], direction = Sort.Direction.DESC, page = 0, size = 10) pageable: Pageable,
                @RequestParam(required = false) name: Optional<String>): ResponseEntity<Page<Product>> {
        val products = productService.findAll(name, pageable)
        if (products.isEmpty){
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity(products, HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun findById(@PathVariable id:Long): ResponseEntity<Product> {
        val optional = productService.findById(id)
        if (optional.isPresent){
            return ResponseEntity(optional.get(), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }
}