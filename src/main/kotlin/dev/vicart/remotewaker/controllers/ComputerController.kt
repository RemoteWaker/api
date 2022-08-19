package dev.vicart.remotewaker.controllers

import dev.vicart.remotewaker.config.MQTTConfig
import dev.vicart.remotewaker.entities.User
import dev.vicart.remotewaker.models.ComputerAction
import dev.vicart.remotewaker.models.ComputerActionRequest
import dev.vicart.remotewaker.models.MqttComputerActionPayload
import dev.vicart.remotewaker.repositories.ComputerRepository
import dev.vicart.remotewaker.repositories.DeviceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource

@RestController
@RequestMapping("/api")
class ComputerController {

    @Resource
    private lateinit var mqttGateway: MQTTConfig.RemoteWakerMessagingGateway

    @Autowired
    private lateinit var computerRepo: ComputerRepository

    @PutMapping("/user/computer/{computerId}/state")
    fun putUserDeviceComputerState(@PathVariable computerId: Long, @RequestBody actionReq: ComputerActionRequest) : ResponseEntity<Any> {
        val jwtUser = SecurityContextHolder.getContext().authentication.principal as User
        val computer = computerRepo.findById(computerId).get()
        if(computer.device!!.user!!.id == jwtUser.id) {
            val action = ComputerAction.valueOf(actionReq.action.uppercase())
            mqttGateway.sendToMqtt("remotewaker/${computer.device!!.id}", MqttComputerActionPayload(computer.networkIp!!, action).toString())
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }
}