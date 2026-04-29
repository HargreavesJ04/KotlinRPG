package io.github.hargreavesj04

import com.badlogic.gdx.Gdx
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import java.util.concurrent.ConcurrentHashMap


class NetworkClient(val game: Main)
{
    val client: Client = Client()
    val networkPlayers = ConcurrentHashMap<Int, PositionUpdatePacket>()

    init
    {
        client.kryo.register(MoveInputPacket::class.java)
        client.kryo.register(PositionUpdatePacket::class.java)
        client.kryo.register(StartGamePacket::class.java)

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


                if (`object` is StartGamePacket) {
                    println("Server said GO! Swapping to game screen...")

                    // Safely push the screen swap back to the Main OpenGL thread
                    Gdx.app.postRunnable {
                        game.setScreen(FirstScreen(game))
                    }
                }
            }
        })
    }

    fun connectToServer(ip: String, tcpPort: Int)
    {
        try
        {
            client.connect(5000, ip, tcpPort, 54777)
        } catch (e: Exception)
        {
            throw Exception("Failed to connect: ${e.message}")
        }
    }

    fun dispose()
    {
        client.stop()
    }
}
