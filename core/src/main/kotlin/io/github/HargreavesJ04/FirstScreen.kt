package io.github.HargreavesJ04

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import ktx.app.KtxScreen

class FirstScreen(val game: Main) : KtxScreen {

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(800f, 480f, camera)
    private val batch = SpriteBatch()

    private val levelManager = LevelHandler()

    private val player = Player(x = 100f, y = 100f, TeamColor.BLUE)

    private val stage = Stage(ScreenViewport())

    private val customTouchpadStyle = TouchpadStyle().apply {
        val bg = Pixmap(200, 200, Pixmap.Format.RGBA8888)
        bg.setColor(1f, 1f, 1f, 0.2f)
        bg.fillCircle(100, 100, 100)
        background = TextureRegionDrawable(Texture(bg))
        bg.dispose()

        val knobPix = Pixmap(50, 50, Pixmap.Format.RGBA8888)
        knobPix.setColor(1f, 1f, 1f, 0.8f)
        knobPix.fillCircle(25, 25, 25)
        knob = TextureRegionDrawable(Texture(knobPix))
        knobPix.dispose()
    }

    private val touchpad = Touchpad(10f, customTouchpadStyle)

    private val playerTexture = Texture("Textures/player.png")

    init {
        camera.position.set(viewport.worldWidth / 2, viewport.worldHeight / 2, 0f)

        touchpad.setBounds(50f, 50f, 250f, 250f)
        stage.addActor(touchpad)

        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        var inputX = touchpad.knobPercentX
        var inputY = touchpad.knobPercentY

        player.move(delta, inputX, inputY)

        camera.position.set(player.x, player.y, 0f)
        camera.update()

        levelManager.render(camera)

        batch.projectionMatrix = camera.combined
        batch.begin()
        batch.draw(playerTexture, player.x, player.y, player.size, player.size)
        batch.end()

        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        stage.viewport.update(width, height, true)
    }

    override fun show() {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}

    override fun dispose() {
        batch.dispose()
        playerTexture.dispose()
        levelManager.dispose()
        stage.dispose()
    }
}
