package br.com.shop.model

import javax.persistence.*

@Entity
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var name: String,
    var description: String,
    var price: Double,
    var imageUrl: String,
    var isFavorite: Boolean= false,
) {

    fun toggleFavorite(){
        isFavorite = !isFavorite
    }
}