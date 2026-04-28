package io.github.hargreavesj04

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import java.util.concurrent.ConcurrentHashMap

class NetworkClient {

    val client: Client = Client()
    val networkPlayers = ConcurrentHashMap<Int, PositionUpdatePacket>()

    init {
        client.kryo.register(MoveInputPacket::class.java)
        client.kryo.register(PositionUpdatePacket::class.java)
        client.start()

        client.addListener(object : Listener() {
            override fun connected(connection: Connection?) {
                println("Connected to the server")
            }

            override fun disconnected(connection: Connection?) {
                println("Disconnected from the server.")
            }

            override fun received(connection: Connection?, `object`: Any?) {
                if (`object` is PositionUpdatePacket) {
                    networkPlayers[`object`.playerId] = `object`
                }
            }
        })
    }

    fun connectToServer() {
        try {
            client.connect(5000, "10.0.2.2", 54555, 54777)
        } catch (e: Exception) {
            println("FAILED to connect: ${e.message}")
        }
    }

    fun dispose() {
        client.stop()
    }
}
