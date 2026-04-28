package io.github.hargreavesj04

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle

class LevelHandler
{
    private val map: TiledMap = TmxMapLoader().load("Maps/MainMap.tmx")
    private val mapRenderer = OrthogonalTiledMapRenderer(map)

    // a list of rectangles that represent solid walls for the client-side collision check
    val walls = mutableListOf<Rectangle>()

    init
    {
        //checking object layer walls from tiled
        val wallLayer = map.layers.get("Walls")
        if (wallLayer != null) {
            for (mapObject in wallLayer.objects)
            {
                if (mapObject is RectangleMapObject)
                {
                    walls.add(mapObject.rectangle)
                }
            }
        }
    }

    //tells the renderer which part of the map to draw based on the players camera
    fun render(camera: OrthographicCamera)
    {
        mapRenderer.setView(camera)
        mapRenderer.render()
    }

    fun dispose()
    {
        map.dispose()
        mapRenderer.dispose()
    }
}
