package io.github.hargreavesj04

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen

private const val width = 800f
private const val height = 480f

class MenuScreen(val game: Main) : KtxScreen
{
    private val stage = Stage(FitViewport(width, height))
    private val skin = Skin(Gdx.files.internal("Textures/clean-crispy-ui.skin"))

    init
    {
        val table = Table()
        table.setFillParent(true)

        val startButton = TextButton("Start Game", skin)
        val exitButton = TextButton("Exit", skin)

        startButton.addListener(object : ClickListener()
        {
            override fun clicked(event: InputEvent?, x: Float, y: Float)
            {
                game.setScreen(LobbyScreen(game))
            }
        })

        exitButton.addListener(object : ClickListener()
        {
            override fun clicked(event: InputEvent?, x: Float, y: Float)
            {
                Gdx.app.exit()
                System.exit(0) //stop the process and background threads
            }
        })

        // Sizing the buttons so they aren't tiny on high-res screens
        table.add(startButton).width(200f).height(60f).pad(10f)
        table.row()
        table.add(exitButton).width(200f).height(60f).pad(10f)

        stage.addActor(table)
    }

    override fun show() {
        Gdx.input.inputProcessor = stage
    }

    override fun render(delta: Float)
    {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.act(delta)
        stage.draw()
    }

    override fun resize(w: Int, h: Int)
    {
        stage.viewport.update(w, h, true)
    }

    override fun dispose()
    {
        stage.dispose()
        skin.dispose()
    }
}
