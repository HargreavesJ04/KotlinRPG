package io.github.hargreavesj04


data class PositionUpdatePacket(var playerId: Int = 0, var x: Float = 0f, var y: Float = 0f, var PlayerTextureID: Int = 0 ) //0 is for blue and 1 is for orange

data class MoveInputPacket(var inputX: Float = 0f, var inputY: Float = 0f)

class StartGamePacket

