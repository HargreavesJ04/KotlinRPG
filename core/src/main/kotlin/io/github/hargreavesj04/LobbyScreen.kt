package io.github.hargreavesj04

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen

private const val width = 800f
private const val height = 480f

private const val standardPadding = 10f
private const val largePadding = 20f
private const val fieldWidth = 300f
private const val buttonWidth = 250f
private const val buttonHeight = 60f

class LobbyScreen(val game: Main) : KtxScreen {
    private val stage = Stage(FitViewport(width, height))
    private val skin = Skin(Gdx.files.internal("Textures/clean-crispy-ui.skin"))

    private val statusLabel = Label("Enter server details to join.", skin)
    private val connectButton = TextButton("Connect", skin)


    //default values below 10.0.2.2 is for local testing
    private val ipField = TextField("10.0.2.2", skin)
    private val portField = TextField("54555", skin)

    private var isConnecting = false

    init {
        val table = Table()
        table.setFillParent(true)

        val ipLabel = Label("Server IP:", skin)
        val portLabel = Label("Port:", skin)

        connectButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                if (isConnecting) return

                val ip = ipField.text
                val port = portField.text.toIntOrNull()

                if (port == null) {
                    statusLabel.setText("Error: Port must be a number!")
                    return
                }

                isConnecting = true
                statusLabel.setText("Connecting to $ip:$port...")
                connectButton.setText("Connecting...")

                Thread{
                    try
                    {
                        game.networkClient.connectToServer(ip, port)

                        Gdx.app.postRunnable {
                            statusLabel.setText("Connected! Waiting for Player 2...")
                            connectButton.setText("Waiting...")
                        }

                    } catch (e: Exception) {
                        Gdx.app.postRunnable {
                            statusLabel.setText("Connection Failed: ${e.message}")
                            connectButton.setText("Retry")
                            isConnecting = false
                        }
                    }
                }.start()
            }
        })

        table.apply{
            defaults().pad(standardPadding)

            add(ipLabel)
            add(ipField).width(fieldWidth)
            row()

            add(portLabel)
            add(portField).width(fieldWidth).padBottom(largePadding)
            row()

            add(connectButton).colspan(2).size(buttonWidth, buttonHeight).padBottom(largePadding)
            row()

            add(statusLabel).colspan(2)
        }

        stage.addActor(table)
    }

    override fun show()
    {
        Gdx.input.inputProcessor = stage
        // Safely set focus using the direct variable reference instead of guessing index
        stage.keyboardFocus = ipField
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        stage.act(delta)
        stage.draw()
    }

    override fun resize(w: Int, h: Int) = stage.viewport.update(w, h, true)

    override fun dispose()
    {
        stage.dispose()
        skin.dispose()
    }
}
