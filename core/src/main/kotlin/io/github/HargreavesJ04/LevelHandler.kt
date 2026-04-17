package io.github.HargreavesJ04

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer


class LevelHandler
{
    private val map: TiledMap = TmxMapLoader().load("Maps/MainMap.tmx")
    private val mapRenderer = OrthogonalTiledMapRenderer(map)


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
