package io.github.HargreavesJ04

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

    val walls = mutableListOf<Rectangle>()

    init
    {
        val wallLayer = map.layers.get("Walls")
        if (wallLayer != null) {
            for (mapObject in wallLayer.objects) {
                if (mapObject is RectangleMapObject) {
                    walls.add(mapObject.rectangle)
                }
            }
        }
    }

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
