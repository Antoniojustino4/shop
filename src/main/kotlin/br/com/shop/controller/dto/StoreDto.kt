package br.com.shop.controller.dto

import br.com.shop.model.Product
import br.com.shop.model.Store
import org.springframework.data.domain.Page
import javax.validation.constraints.NotBlank

class StoreDto(store: Store) {
    @NotBlank(message = "The name field is mandatory")
    var name: String
    var products: List<Product>


    init {
        this.name = store.name
        this.products = store.products
    }

    fun convert(): Store {
        return Store(0, name, products.toMutableList(), 0.0)
    }

    fun convert(id: Long): Store {
        return Store(id, name, products.toMutableList(), 0.0)
    }

    companion object {
        fun convert(stores: Page<Store>): Page<StoreDto> {
            return stores.map { store -> StoreDto(store) }
        }
    }
}