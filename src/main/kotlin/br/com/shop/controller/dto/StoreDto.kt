package br.com.shop.controller.dto

import br.com.shop.model.Product
import br.com.shop.model.store.Extract
import br.com.shop.model.store.Store
import jakarta.validation.constraints.NotBlank
import org.springframework.data.domain.Page

class StoreDto(store: Store):Dto<Store> {
    @NotBlank(message = "The name field is mandatory")
    var name: String
    var products: List<Product>

    init {
        this.name = store.name
        this.products = store.products
    }

    override fun convert(): Store {
        return Store(name, products.toMutableList())
    }

    override fun convert(id: Long): Store {
        return Store(name, products.toMutableList(), id = id)
    }

    companion object {
        fun convert(stores: Page<Store>): Page<StoreDto> {
            return stores.map { store -> StoreDto(store) }
        }
    }
}