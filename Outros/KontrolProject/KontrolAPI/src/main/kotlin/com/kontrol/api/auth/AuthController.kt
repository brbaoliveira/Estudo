package com.kontrol.api.auth

import com.kontrol.api.user.User
import com.kontrol.api.user.UserRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

data class RegisterRequest(@field:NotBlank val name:String,@field:Email @field:NotBlank val email:String,@field:Size(min=6) val password:String)
data class LoginRequest(@field:Email @field:NotBlank val email:String,@field:NotBlank val password:String)
data class UserResponse(val id:Long,val name:String,val email:String)
data class AuthResponse(val token:String,val user:UserResponse)

@RestController @RequestMapping("/api/auth")
class AuthController(private val users:UserRepository,private val encoder:PasswordEncoder,private val jwt:JwtService){
 @PostMapping("/register") fun register(@Valid @RequestBody r:RegisterRequest):AuthResponse{val email=r.email.trim().lowercase();if(users.findByEmail(email)!=null)throw ResponseStatusException(HttpStatus.CONFLICT,"E-mail já cadastrado");val u=users.save(User(name=r.name.trim(),email=email,password=encoder.encode(r.password)));return AuthResponse(jwt.generate(u.email),u.toResponse())}
 @PostMapping("/login") fun login(@Valid @RequestBody r:LoginRequest):AuthResponse{val u=users.findByEmail(r.email.trim().lowercase())?:throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciais inválidas");if(!encoder.matches(r.password,u.password))throw ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciais inválidas");return AuthResponse(jwt.generate(u.email),u.toResponse())}
 private fun User.toResponse()=UserResponse(id,name,email)
}
