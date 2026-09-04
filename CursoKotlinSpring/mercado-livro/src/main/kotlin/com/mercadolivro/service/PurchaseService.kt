package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.repository.PurchaseRepository
import jakarta.transaction.Transactional
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    @Transactional
    fun create (purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)

        println("Disparando evento de compra")
        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchaseModel))
        println("Finalizando o processamento!")
    }

    fun update(purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)
    }

    fun findBooksByCustomer(customerId: Int): List<BookModel> {
        return purchaseRepository.findAllByCustomerId(customerId).flatMap { it.books }.filter { it.status == BookStatus.VENDIDO }
    }

    fun delete(ids: List<Int>) {
        for (id in ids) {
            purchaseRepository.deleteById(id)
        }
    }
}
