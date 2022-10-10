package br.com.shop.model.enums

import java.lang.IllegalArgumentException
import kotlin.jvm.Throws

enum class ProductStatus {
    AVAILABLE,
    UNAVAILABLE,
    OUT_OF_STOCK;

    companion object {
        @Throws(IllegalArgumentException::class)
        fun convert(type: String): ProductStatus{
           if (type == "AVAILABLE"){
               return AVAILABLE
           }else if(type == "UNAVAILABLE"){
               return UNAVAILABLE
           }else if(type == "OUT_OF_STOCK") {
               return OUT_OF_STOCK
           }
            throw IllegalArgumentException()
        }
    }
}