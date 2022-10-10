package br.com.shop.controller

import br.com.shop.controller.dto.ExtractDto
import br.com.shop.controller.dto.ProductDto
import br.com.shop.controller.dto.StoreDto
import br.com.shop.exception.IdNoExistException
import br.com.shop.exception.ProductIsNotOfThisStoreException
import br.com.shop.model.Product
import br.com.shop.model.store.Store
import br.com.shop.model.enums.ProductStatus
import br.com.shop.service.OrderService
import br.com.shop.service.ProductService
import br.com.shop.service.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.server.ExposesResourceFor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.util.*
import javax.validation.Valid

@RestController
@ExposesResourceFor(Store::class)
@RequestMapping("stores")
class StoreController {

    @Autowired
    private lateinit var storeService: StoreService
    @Autowired
    private lateinit var productService: ProductService
    @Autowired
    private lateinit var orderService: OrderService

    @GetMapping
    fun findAll(@PageableDefault(sort = ["name"], direction = Sort.Direction.DESC, page = 0, size = 10) pageable: Pageable,
                @RequestParam(required = false) name: Optional<String>): ResponseEntity<Page<Store>> {
        val stores = storeService.findAll(name, pageable)
        if(stores.isEmpty){
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity(stores, HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    fun findById(@PathVariable id:Long): ResponseEntity<Store> {
        val optional = storeService.findById(id)
        if (optional.isPresent){
            return ResponseEntity(optional.get(), HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    @PostMapping
    fun save(@RequestBody @Valid storeDto: StoreDto, uriBuilder: UriComponentsBuilder): ResponseEntity<Store> {
        val storeSaved= storeService.save(storeDto.convert())
        val uri: URI = uriBuilder.path("/stores/{id}").buildAndExpand(storeSaved.id).toUri()
        return ResponseEntity.created(uri).body(storeSaved)
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id:Long, @RequestBody @Valid storeDto: StoreDto): ResponseEntity<Any> {
        return try{
            val store = storeDto.convert(id)
            val newStoreDto = storeService.save(store)
            return ResponseEntity(newStoreDto, HttpStatus.NO_CONTENT)
        }catch (ex: IdNoExistException) {
            ResponseEntity(ex, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping(path = ["/{id}/products"])
    fun findAllProductsByStoreId(@PageableDefault(sort = ["id"], direction = Sort.Direction.DESC,
        page = 0, size = 10) pageable: Pageable, @PathVariable id:Long): ResponseEntity<Page<List<Product>>> {
        val store = storeService.findAllProductsByStoreId(id, pageable)
        return ResponseEntity(store, HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}/products/{idProduct}"])
    fun findByIdProductByStoreId(@PathVariable id:Long, @PathVariable idProduct: Long): ResponseEntity<Any> {
        return try {
            val store = storeService.findByIdProductByStoreId(id, idProduct)
            ResponseEntity(store, HttpStatus.OK)
        }catch (ex: ProductIsNotOfThisStoreException){
            ResponseEntity(ex, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(path = ["/{id}/products"])
    fun saveProducts(@PathVariable id:Long, @RequestBody @Valid productDto: ProductDto, uriBuilder: UriComponentsBuilder): Any {
        val optional = storeService.findById(id)
        if (optional.isPresent){
            val product = productDto.convert()
            val store = optional.get()
            store.products.add(product)
            productService.save(product)

            val uri: URI = uriBuilder.path("/products/{id}").buildAndExpand(product.id).toUri()
            return ResponseEntity.created(uri).body(product)
        }
        return ResponseEntity.badRequest()
    }

    @PutMapping(path = ["/{id}/products/{idProduct}"]) //TODO PRODUTO É DA LOJA?
    fun replaceProducts(@PathVariable id:Long, @PathVariable idProduct: Long, @RequestBody @Valid productDto: ProductDto, uriBuilder: UriComponentsBuilder): Any {
        return try {
            if (storeService.isProductThisStore(id, idProduct)) {
                val product = productDto.convert(idProduct)
                val newProductDto = productService.save(product)
                ResponseEntity(newProductDto, HttpStatus.NO_CONTENT)
            } else {
                throw ProductIsNotOfThisStoreException(this.javaClass.name)
            }
        }catch (ex: IdNoExistException) {
            ResponseEntity(ex, HttpStatus.NOT_FOUND)
        }
    }
    @PatchMapping(path = ["/{id}/products/{idProduct}"]) //TODO PRODUTO É DA LOJA?
    fun status(@PathVariable id:Long, @PathVariable idProduct:Long, @RequestBody status: ProductStatus): ResponseEntity<Any>{
        return try {
            if (storeService.isProductThisStore(id, idProduct)) {
                productService.changeStatus(idProduct, status)
                ResponseEntity.ok().build()
            }else {
                throw ProductIsNotOfThisStoreException(this.javaClass.name)
            }
        }catch (ex: IdNoExistException) {
            ResponseEntity(ex, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping(path = ["/{id}/extract"])
    fun extract(@PathVariable id:Long): ResponseEntity<Any> {
        return try {
            val extract = storeService.findExtractByStoreId(id)
            val extractDto = ExtractDto(extract)
            return ResponseEntity(extractDto, HttpStatus.OK)
        }catch (ex: IdNoExistException) {
            ResponseEntity(ex, HttpStatus.NOT_FOUND)
        }
    }

    @PatchMapping(path = ["/{id}/extract/withdraw"])
    fun withdraw(@PathVariable id:Long, @RequestBody value: ExtractDto): ResponseEntity<Any> {
        return try {
            storeService.withdraw(id, value.balance)
            return ResponseEntity.noContent().build()
        }catch (ex: IdNoExistException) {
            ResponseEntity(ex, HttpStatus.NOT_FOUND)
        }
    }

//    @PatchMapping(path = ["/{id}/order/{idOrder}"])
//    fun order(@PathVariable id:Long,@PathVariable idOrder:Long, @RequestBody status: OrderStatus): ResponseEntity<Any> {
//        return try {
//            storeService.updateOrderStatus(id, idOrder, status)
//            return ResponseEntity.noContent().build()
//        }catch (ex: IdNoExistException) {
//            ResponseEntity(ex, HttpStatus.NOT_FOUND)
//        }
//    }

}