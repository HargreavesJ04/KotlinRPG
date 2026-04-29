package io.github.hargreavesj04

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.app.KtxScreen

class FirstScreen(val game: Main) : KtxScreen
{


    private val camera = OrthographicCamera()
    private val viewport = FitViewport(400f, 200f, camera)
    private val batch = SpriteBatch()
    private val stage = Stage(ScreenViewport())


    private val networkClient = game.networkClient


    private val levelManager = LevelHandler()
    private val player = Player(x = 100f, y = 100f, Team.BLUE)


    private val playerTexture = Texture("Textures/player.png")
    private val player2Texture = Texture("Textures/Player2.png")
    private val padBg = Texture("Textures/Pan_Blue_Circle.png")
    private val padKnob = Texture("Textures/HealthPotion.png")


    private val touchpad = createTouchpad()

    init // Initializes UI input and starts the background server connection
    {
        stage.addActor(touchpad)
        Gdx.input.inputProcessor = stage

    }

    private fun createTouchpad(): Touchpad
    {
        val customTouchpadStyle = TouchpadStyle().apply{

            background = TextureRegionDrawable(padBg)
            knob = TextureRegionDrawable(padKnob)
        }

        val pad = Touchpad(10f, customTouchpadStyle)
        pad.setBounds(50f, 50f, 250f, 250f)
        return pad
    }

    override fun render(delta: Float)
    {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) //erases the previous sprite on screen so you dont have a ghost effect

        val inputX = touchpad.knobPercentX
        val inputY = touchpad.knobPercentY

        if (Math.abs(inputX) > 0.05f || Math.abs(inputY) > 0.05f) //deadzone that wont move if the stick moves below 5%
        {
            networkClient.client.sendUDP(MoveInputPacket(inputX, inputY)) //tells the server that the player is trying to move
        }

        val myServerPos = networkClient.networkPlayers[networkClient.client.id]

        if (myServerPos != null) //ensures players  local position lines up with the servers cooridantes so player stays in sync
        {
            player.x = myServerPos.x
            player.y = myServerPos.y
        }


        //camera logic to keep it centered to player and follows them
        camera.position.set(player.x, player.y, 0f)
        camera.update()
        levelManager.render(camera)

        batch.projectionMatrix = camera.combined
        batch.begin()


        //draws player with the correct texture and properties
        val myColor = Team.fromInt(myServerPos?.PlayerTextureID ?: 0)
        val myTex = if (myColor == Team.ORANGE) player2Texture else playerTexture
        batch.draw(myTex, player.x, player.y, player.size, player.size)

        for (netPlayer in networkClient.networkPlayers.values)
        {
            if (netPlayer.playerId != networkClient.client.id)
            {
                val otherColor = Team.fromInt(netPlayer.PlayerTextureID)
                val otherTex = if (otherColor == Team.ORANGE) player2Texture else playerTexture
                batch.draw(otherTex, netPlayer.x, netPlayer.y, player.size, player.size)
            }
        }

        batch.end()
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) //if the phone rotates the game camera matches new window dimensions while keeping the correct aspect ratio
    {
        viewport.update(width, height)
        stage.viewport.update(width, height, true)
    }

    override fun dispose() //deletes memory to stop leaks
    {
        batch.dispose()
        playerTexture.dispose()
        player2Texture.dispose()
        padBg.dispose()
        padKnob.dispose()
        levelManager.dispose()
        stage.dispose()
    }

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
}
