package com.mercadolivro.controller

import com.mercadolivro.controller.mapper.PurchaseMapper
import com.mercadolivro.controller.request.PostPurchaseRequest
import com.mercadolivro.model.BookModel
import com.mercadolivro.service.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("purchase")
class PurchaseController(
    private val purchaseService: PurchaseService,
    private val purchaseMapper: PurchaseMapper
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun purcase(@RequestBody request: PostPurchaseRequest) {
        purchaseService.create(purchaseMapper.toModel(request))
    }

    @GetMapping("/{customerId}/books")
    fun getBooksCustumer(@PathVariable customerId: Int): List<BookModel> {
        return purchaseService.findBooksByCustomer(customerId)
    }

    @DeleteMapping("/{ids}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable ids: List<Int>) {
        purchaseService.delete(ids)
    }
}