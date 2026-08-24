package com.mercadolivro.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PutCustomerRequest (

    @field:NotEmpty(message = "Nome deve ser informado")
    var name: String,

    @field:NotEmpty(message = "E-mail deve ser informado")
    @field:Email(message = "E-mail deve ser válido")
    var email: String
)