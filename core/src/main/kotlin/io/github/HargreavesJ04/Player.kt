package io.github.HargreavesJ04

enum class TeamColor
{
    BLUE, ORANGE
}

class Player(var x: Float, var y: Float, val team: TeamColor)
{
    val size = 16f
    val speed = 100f

    fun move(delta: Float, movePercentX: Float, movePercentY: Float)
    {
        x += movePercentX * speed * delta
        y += movePercentY * speed * delta
    }
}
