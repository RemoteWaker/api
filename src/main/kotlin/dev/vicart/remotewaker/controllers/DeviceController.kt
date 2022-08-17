package dev.vicart.remotewaker.controllers

import dev.vicart.remotewaker.entities.Device
import dev.vicart.remotewaker.entities.User
import dev.vicart.remotewaker.models.DevicePost
import dev.vicart.remotewaker.repositories.DeviceRepository
import dev.vicart.remotewaker.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api")
class DeviceController {

    @Autowired
    private lateinit var userRepo: UserRepository

    @Autowired
    private lateinit var deviceRepo: DeviceRepository

    @GetMapping("/user/devices")
    fun getUserDevices() : List<Device> {
        val jwtUser = SecurityContextHolder.getContext().authentication.principal as User
        val user = userRepo.findById(jwtUser.id!!)
        return user.get().devices.orEmpty()
    }

    @PostMapping("/user/device")
    fun postUserDevice(@RequestBody dev: DevicePost) : ResponseEntity<Device> {
        val jwtUser = SecurityContextHolder.getContext().authentication.principal as User
        val user = userRepo.findById(jwtUser.id!!)
        val device = Device()
        device.name = dev.name
        device.deviceId = dev.deviceId
        device.user = user.get()
        deviceRepo.save(device)
        return ResponseEntity.created(URI.create("/api/user/device")).body(device)
    }
}