package io.github.HargreavesJ04


data class PositionUpdatePacket(var playerId: Int = 0, var x: Float = 0f, var y: Float = 0f)
data class MoveInputPacket(var inputX: Float = 0f, var inputY: Float = 0f)
