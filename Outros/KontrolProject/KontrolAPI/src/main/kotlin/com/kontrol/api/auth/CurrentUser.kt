package com.kontrol.api.auth
import com.kontrol.api.user.User
import com.kontrol.api.user.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus

@Component
class CurrentUser(private val users:UserRepository){
 fun get():User{val email=SecurityContextHolder.getContext().authentication?.name?:throw ResponseStatusException(HttpStatus.UNAUTHORIZED);return users.findByEmail(email)?:throw ResponseStatusException(HttpStatus.UNAUTHORIZED)}
}
