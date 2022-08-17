package dev.vicart.remotewaker.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import javax.persistence.*

@Entity
class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    var deviceId: Long? = null

    var name: String? = null

    var isUp: Boolean? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    var user: User? = null
}