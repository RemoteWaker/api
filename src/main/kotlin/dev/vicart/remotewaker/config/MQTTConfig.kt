package dev.vicart.remotewaker.config

import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.annotation.IntegrationComponentScan
import org.springframework.integration.annotation.MessagingGateway
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.core.MqttPahoClientFactory
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler
import org.springframework.integration.mqtt.support.MqttHeaders
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.handler.annotation.Header

@Configuration
@IntegrationComponentScan
class MQTTConfig {

    @Bean
    fun mqttClientFactory() : MqttPahoClientFactory {
        val factory = DefaultMqttPahoClientFactory()
        val options = MqttConnectOptions()
        options.serverURIs = arrayOf("tcp://192.168.1.155:1883")
        options.userName = "guest"
        options.password = "guest".toCharArray()
        options.isAutomaticReconnect = true
        options.isCleanSession = true
        factory.connectionOptions = options
        return factory
    }

    @Bean
    fun mqttOutboundChannel() : MessageChannel {
        return DirectChannel()
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    fun mqttOutbound() : MessageHandler {
        val messageHandler = MqttPahoMessageHandler("api", mqttClientFactory())
        messageHandler.setAsync(true)
        return messageHandler
    }

    @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
    interface RemoteWakerMessagingGateway {
        fun sendToMqtt(@Header(MqttHeaders.TOPIC) topic: String, data: String)
    }
}