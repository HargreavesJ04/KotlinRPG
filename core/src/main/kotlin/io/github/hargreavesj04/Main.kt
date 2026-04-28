package io.github.hargreavesj04

import com.badlogic.gdx.Game


class Main : Game()
{
    override fun create()
    {

        setScreen(FirstScreen(this))
    }
}

