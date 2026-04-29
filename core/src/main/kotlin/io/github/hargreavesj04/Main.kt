package io.github.hargreavesj04

import com.badlogic.gdx.Game


class Main : Game()
{

    lateinit var networkClient: NetworkClient

    override fun create()
    {
        networkClient = NetworkClient(this)
        setScreen(MenuScreen(this))
    }

    override fun dispose()
    {
        super.dispose()
        networkClient.dispose()
    }


}

