package com.kontrol.api.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.kontrol.api.user.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Date

@Component
class JwtService(@Value("\${app.jwt-secret}") secret:String,@Value("\${app.jwt-expiration-ms}") private val expiration:Long){
 private val algorithm=Algorithm.HMAC256(secret)
 fun generate(email:String):String=JWT.create().withSubject(email).withExpiresAt(Date(System.currentTimeMillis()+expiration)).sign(algorithm)
 fun email(token:String):String?=try{JWT.require(algorithm).build().verify(token).subject}catch(_:JWTVerificationException){null}
}

@Component
class JwtFilter(private val jwt:JwtService,private val users:UserRepository):OncePerRequestFilter(){
 override fun doFilterInternal(req:HttpServletRequest,res:HttpServletResponse,chain:FilterChain){
  val h=req.getHeader("Authorization")
  if(h?.startsWith("Bearer ")==true){val email=jwt.email(h.removePrefix("Bearer "));val user=email?.let{users.findByEmail(it)};if(user!=null){val auth=UsernamePasswordAuthenticationToken(user.email,null,listOf(SimpleGrantedAuthority("ROLE_USER")));org.springframework.security.core.context.SecurityContextHolder.getContext().authentication=auth}}
  chain.doFilter(req,res)
 }
}

@Configuration
class SecurityConfig(private val filter:JwtFilter){
 @Bean fun passwordEncoder():PasswordEncoder=BCryptPasswordEncoder()
 @Bean fun securityFilterChain(http:HttpSecurity):SecurityFilterChain=http.csrf{it.disable()}.cors{it.disable()}.sessionManagement{it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}.authorizeHttpRequests{it.requestMatchers("/api/auth/**","/api/health","/error").permitAll().anyRequest().authenticated()}.addFilterBefore(filter,UsernamePasswordAuthenticationFilter::class.java).build()
}
