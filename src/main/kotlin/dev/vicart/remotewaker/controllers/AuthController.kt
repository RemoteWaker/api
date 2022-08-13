package dev.vicart.remotewaker.controllers

import dev.vicart.remotewaker.config.JwtUtil
import dev.vicart.remotewaker.entities.User
import dev.vicart.remotewaker.models.UserLogin
import dev.vicart.remotewaker.models.UserLoginResponse
import dev.vicart.remotewaker.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api")
class AuthController {

    @Autowired
    private lateinit var authManager: AuthenticationManager

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var userRepo: UserRepository

    @PostMapping("/login")
    fun postLogin(@RequestBody login: UserLogin): UserLoginResponse {
        val auth = authManager.authenticate(UsernamePasswordAuthenticationToken(login.username, login.password))
        val user = auth.principal as User
        val jwt = jwtUtil.generateAccessToken(user)

        return UserLoginResponse(jwt)
    }

    @PostMapping("/register")
    fun postRegister(@RequestBody login: UserLogin) : ResponseEntity<Any> {
        try {
            userDetailsService.loadUserByUsername(login.username)
            return ResponseEntity.badRequest().body(mapOf("message" to "Username already taken"))
        }
        catch (e: Exception) {

        }
        val user = User()
        user.usern = login.username
        user.pass = passwordEncoder.encode(login.password)
        userRepo.save(user)
        return ResponseEntity.created(URI.create("/api/register")).body(user)
    }

    @GetMapping("/me")
    fun getMe() : User {
        val jwtUser = SecurityContextHolder.getContext().authentication.principal as User
        val user = userRepo.findById(jwtUser.id!!)
        return user.get()
    }
}