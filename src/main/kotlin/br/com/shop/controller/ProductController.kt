package br.com.shop.controller

import br.com.shop.dto.ProductDto
import br.com.shop.model.Product
import br.com.shop.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.Optional
import javax.validation.Valid

@RestController
@ExposesResourceFor(Product::class)
@RequestMapping("products")
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ProductController {

    @Autowired
    private lateinit var productService: ProductService

    @GetMapping
    fun findAll(@PageableDefault(sort = ["name"], direction = Sort.Direction.DESC, page = 0, size = 10) pageable: Pageable,
                @RequestParam(required = false) name: Optional<String>): ResponseEntity<Page<ProductDto>> {
        var products = if (name.isEmpty){
            productService.findAll(pageable)
        }else{
            productService.findAll(name.get(), pageable)
        }

        return ResponseEntity(ProductDto.converter(products), HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    //@PreAuthorize("hasRole('ADMIN')")
    fun findById(@PathVariable id:Long): ResponseEntity<ProductDto> {
        if (productService.existsById(id)) {
            val product = productService.findById(id)
            return ResponseEntity(ProductDto(product.get()), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

//    @GetMapping(path = ["by-id/{id}"])
//    fun findByIdAuthenticationPrincipal(@PathVariable id:Long, @AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Product> {
//        println(userDetails)
//        return ResponseEntity(productService.findById(id), HttpStatus.OK)
//    }


    //PAREI AQUI
    @PostMapping
    fun save(@RequestBody @Valid productDto: ProductDto, uriBuilder: UriComponentsBuilder): ResponseEntity<ProductDto> {
        val productSaved= productService.save(productDto.converter())
        val newProductDto = ProductDto(productSaved)
        val uri: URI = uriBuilder.path("/products/{id}").buildAndExpand(productSaved.id).toUri()
        return ResponseEntity.created(uri).body(newProductDto)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id:Long): ResponseEntity<Unit> {
        if(productService.existsById(id)) {
            return ResponseEntity(productService.delete(id), HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.notFound().build()
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id:Long, @RequestBody @Valid productDto: ProductDto): ResponseEntity<ProductDto> {
        if(productService.existsById(id)) {
            val product = productDto.converter(id)
            val newProductDto = ProductDto(productService.save(product))
            return ResponseEntity(newProductDto, HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.notFound().build()
    }
}