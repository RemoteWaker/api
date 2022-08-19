package dev.vicart.remotewaker.entities

import javax.persistence.*

@Entity
class Computer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var networkIp: String? = null

    var hostname: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var device: Device? = null
}