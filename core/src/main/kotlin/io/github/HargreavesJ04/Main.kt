package io.github.HargreavesJ04

import com.badlogic.gdx.Game


class Main : Game() {
    override fun create() {

        setScreen(FirstScreen(this))
    }
}

