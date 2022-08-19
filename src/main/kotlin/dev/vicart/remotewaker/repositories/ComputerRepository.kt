package dev.vicart.remotewaker.repositories

import dev.vicart.remotewaker.entities.Computer
import org.springframework.data.jpa.repository.JpaRepository

interface ComputerRepository : JpaRepository<Computer, Long>