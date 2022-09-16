package br.com.shop.controller

import br.com.shop.controller.dto.StoreDto
import br.com.shop.model.Store
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

    @GetMapping
    fun findAll(@PageableDefault(sort = ["name"], direction = Sort.Direction.DESC, page = 0, size = 10) pageable: Pageable,
                @RequestParam(required = false) name: Optional<String>): ResponseEntity<Page<Store>> {
        val stores = storeService.findAll(name, pageable)
        return ResponseEntity(stores, HttpStatus.OK)
    }

    @GetMapping(path = ["/{id}"])
    //@PreAuthorize("hasRole('ADMIN')")
    fun findById(@PathVariable id:Long): ResponseEntity<Store> {
        if (storeService.existsById(id)) {
            val store = storeService.findById(id).get()
            return ResponseEntity(store, HttpStatus.OK)
        }
        return ResponseEntity.notFound().build()
    }

    @PatchMapping(path = ["/{id}/toggleFavorite"])
    fun toggleFavorite(@PathVariable id:Long): ResponseEntity<Unit>{
        if (storeService.existsById(id)) {
            storeService.toggleFavorite(id)
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.notFound().build()
    }

//    @GetMapping(path = ["by-id/{id}"])
//    fun findByIdAuthenticationPrincipal(@PathVariable id:Long, @AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Product> {
//        println(userDetails)
//        return ResponseEntity(productService.findById(id), HttpStatus.OK)
//    }

    @PostMapping
    fun save(@RequestBody @Valid storeDto: StoreDto, uriBuilder: UriComponentsBuilder): ResponseEntity<Store> {
        val storeSaved= storeService.save(storeDto.convert())
        val uri: URI = uriBuilder.path("/stores/{id}").buildAndExpand(storeSaved.id).toUri()
        return ResponseEntity.created(uri).body(storeSaved)
    }

    @DeleteMapping(path = ["/{id}"])
    fun delete(@PathVariable id:Long): ResponseEntity<Unit> {
        if(storeService.existsById(id)) {
            return ResponseEntity(storeService.delete(id), HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.notFound().build()
    }

    @PutMapping(path = ["/{id}"])
    fun replace(@PathVariable id:Long, @RequestBody @Valid storeDto: StoreDto): ResponseEntity<Store> {
        if(storeService.existsById(id)) {
            val store = storeDto.convert(id)
            val newStoreDto = storeService.save(store)
            return ResponseEntity(newStoreDto, HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.notFound().build()
    }
}