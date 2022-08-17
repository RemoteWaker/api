package dev.vicart.remotewaker.repositories

import dev.vicart.remotewaker.entities.Device
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceRepository : JpaRepository<Device, Long>