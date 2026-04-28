package io.github.hargreavesj04

import com.badlogic.gdx.math.Rectangle



enum class Team {
    BLUE, ORANGE;

    companion object {

        fun fromInt(value: Int) = entries.getOrNull(value) ?: BLUE
    }
}



class Player(var x: Float, var y: Float, val team: Team) {
    val size = 16f
    val speed = 100f

    val bounds = Rectangle(x, y, size, size)


    fun move(delta: Float, movePercentX: Float, movePercentY: Float, walls: List<Rectangle>)
    {
        val moveAmountX = movePercentX * speed * delta
        val moveAmountY = movePercentY * speed * delta

        bounds.x += moveAmountX
        var collidedX = false
        for (wall in walls)
        {
            if (bounds.overlaps(wall)) collidedX = true
        }

        if (collidedX)
        {
            bounds.x -= moveAmountX
        } else {
            x += moveAmountX
        }

        bounds.y += moveAmountY
        var collidedY = false
        for (wall in walls)
        {
            if (bounds.overlaps(wall)) collidedY = true
        }

        if (collidedY)
        {
            bounds.y -= moveAmountY
        } else {
            y += moveAmountY
        }
    }
}
