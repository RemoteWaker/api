package dev.vicart.remotewaker.models

data class MqttComputerActionPayload(val computerAddress: String, val action: ComputerAction) {

    override fun toString(): String {
        return "computer:$computerAddress,action:${action.name.lowercase()}"
    }
}

enum class ComputerAction {
    ON,
    OFF
}
