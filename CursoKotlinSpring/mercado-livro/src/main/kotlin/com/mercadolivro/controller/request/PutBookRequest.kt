package com.mercadolivro.controller.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class PutBookRequest(
    var name: String?,
    var price: BigDecimal?
)