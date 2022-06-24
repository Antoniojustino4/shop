package br.com.shop.controller

import br.com.shop.model.Product
import br.com.shop.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@ExposesResourceFor(Product::class)
@RequestMapping("products")
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ProductController {

    @Autowired
    lateinit var productService: ProductService

    @GetMapping
    fun list(): List<Product>{
        return productService.list()
    }

    @GetMapping(path = ["/{id}"])
    //@PreAuthorize("hasRole('ADMIN')")
    fun findById(@PathVariable id:Long): ResponseEntity<Product> {
        return ResponseEntity(productService.findById(id), HttpStatus.OK)
    }

    @GetMapping(path = ["by-id/{id}"])
    fun findByIdAuthenticationPrincipal(@PathVariable id:Long, @AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Product> {
        println(userDetails)
        return ResponseEntity(productService.findById(id), HttpStatus.OK)
    }

    @PostMapping
    fun save(@RequestBody product: Product): ResponseEntity<Product> {
        return ResponseEntity(productService.save(product), HttpStatus.CREATED)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id:Long): ResponseEntity<Unit> {
        return ResponseEntity(productService.delete(id), HttpStatus.NO_CONTENT)
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id:Long, @RequestBody product: Product): ResponseEntity<Product> {
        product.id = id
        return ResponseEntity(productService.save(product), HttpStatus.NO_CONTENT)
    }
}