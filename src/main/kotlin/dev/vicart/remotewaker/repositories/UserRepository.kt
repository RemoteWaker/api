package dev.vicart.remotewaker.repositories

import dev.vicart.remotewaker.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {

    fun findUserByUsern(usern: String) : Optional<User>
}