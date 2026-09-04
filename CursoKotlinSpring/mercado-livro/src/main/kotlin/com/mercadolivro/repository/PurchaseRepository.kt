package com.mercadolivro.repository

import com.mercadolivro.model.PurchaseModel
import org.springframework.data.repository.CrudRepository

interface PurchaseRepository: CrudRepository<PurchaseModel, Int> {
    fun findAllByCustomerId(customerId: Int): List<PurchaseModel>
}
