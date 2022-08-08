package br.com.shop.controller

import org.springframework.test.web.servlet.MvcResult

fun getId(response: MvcResult, domain: String): Long {
    val locale = response.response.getHeaderValue("Location").toString()

    val regex = "(/$domain/)([1-9][0-9]?[0-9]?)".toRegex()
    val matchResult: MatchResult? = regex.find(locale)
    return matchResult?.groupValues?.get(2)?.toLong() ?: 0
}
