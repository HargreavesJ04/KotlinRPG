package io.github.HargreavesJ04

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.kotcrab.vis.ui.VisUI
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.async.KtxAsync
import ktx.graphics.use

enum class TeamColor {
    BLUE, ORANGE
}

class Player(var x: Float, var y: Float, val team: TeamColor) {
    val size = 16f
    val speed = 100f

    fun move(delta: Float, movePercentX: Float, movePercentY: Float) {
        x += movePercentX * speed * delta
        y += movePercentY * speed * delta
    }
}

class Main : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()
        VisUI.load()
        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }

    override fun dispose() {
        super.dispose()
        VisUI.dispose()
    }
}

class FirstScreen : KtxScreen {
    private val shapeRenderer = ShapeRenderer()

    private val stage = Stage(ScreenViewport())
    private val touchpad = Touchpad(10f, VisUI.getSkin())

    private val camera = OrthographicCamera()
    private val map = TmxMapLoader().load("Maps/MainMap.tmx")
    private val mapRenderer = OrthogonalTiledMapRenderer(map)

    private val myPlayer = Player(100f, 100f, TeamColor.BLUE)

    init {
        touchpad.setBounds(50f, 50f, 250f, 250f)
        stage.addActor(touchpad)
        Gdx.input.inputProcessor = stage

        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.zoom = 0.2f
    }

    override fun render(delta: Float) {
        var moveX = touchpad.knobPercentX
        var moveY = touchpad.knobPercentY

        if (Gdx.input.isKeyPressed(Keys.A)) moveX = -1f
        if (Gdx.input.isKeyPressed(Keys.D)) moveX = 1f
        if (Gdx.input.isKeyPressed(Keys.S)) moveY = -1f
        if (Gdx.input.isKeyPressed(Keys.W)) moveY = 1f

        myPlayer.move(delta, moveX, moveY)

        camera.position.set(myPlayer.x + (myPlayer.size / 2f), myPlayer.y + (myPlayer.size / 2f), 0f)
        camera.update()

        clearScreen(red = 0.1f, green = 0.1f, blue = 0.1f)

        mapRenderer.setView(camera)
        mapRenderer.render()

        shapeRenderer.projectionMatrix = camera.combined
        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            if (myPlayer.team == TeamColor.BLUE) {
                it.setColor(0f, 0.5f, 1f, 1f)
            } else {
                it.setColor(1f, 0.5f, 0f, 1f)
            }
            it.rect(myPlayer.x, myPlayer.y, myPlayer.size, myPlayer.size)
        }

        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        shapeRenderer.dispose()
        stage.dispose()
        map.dispose()
        mapRenderer.dispose()
    }
}
