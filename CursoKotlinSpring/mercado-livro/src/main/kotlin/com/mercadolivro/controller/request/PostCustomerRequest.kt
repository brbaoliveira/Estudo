package com.mercadolivro.controller.request

data class PostCustomerRequest (var name: String, var email: String) {
    /*fun toCustomerModel(): CustomerModel {
        return CustomerModel(nome = this.nome, email = this.email)
    }*/
}