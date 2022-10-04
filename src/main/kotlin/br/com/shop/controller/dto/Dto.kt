package br.com.shop.controller.dto

interface Dto<T> {

    fun convert(): T

    fun convert(id: Long): T
}