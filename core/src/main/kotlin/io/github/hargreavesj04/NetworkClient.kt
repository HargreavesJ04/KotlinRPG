package io.github.hargreavesj04

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import java.util.concurrent.ConcurrentHashMap

class NetworkClient
{
    // Using ConcurrentHashMap because networking runs on a separate thread;
    // this prevents crashes when the render loop reads while the network thread writes.
    val client: Client = Client()
    val networkPlayers = ConcurrentHashMap<Int, PositionUpdatePacket>()

    init
    {
        client.kryo.register(MoveInputPacket::class.java)
        client.kryo.register(PositionUpdatePacket::class.java)
        client.start() //background thread for handling buffers

        client.addListener(object : Listener() {
            override fun connected(connection: Connection?) {
                println("Connected to the server")
            }

            override fun disconnected(connection: Connection?) {
                println("Disconnected from the server.")
            }

            //update the map so the firstscreen can draw the players
            override fun received(connection: Connection?, `object`: Any?) {
                if (`object` is PositionUpdatePacket) {
                    networkPlayers[`object`.playerId] = `object`
                }
            }
        })
    }

    fun connectToServer()
    {
        try
        {
            client.connect(5000, "10.0.2.2", 54555, 54777)
        } catch (e: Exception)
        {
            println("Failed to connect: ${e.message}")
        }
    }

    fun dispose()
    {
        client.stop()
    }
}
