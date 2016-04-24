# Collector_V2

Driver.show()
    World.load(IWorldLoadable)
        onWorldLoadBegin()
            - Clear PhysicsWorld
            onRoomLoadBegin()
                - Canvas clears itself
                - ParticleEffectManager clears itself
            Room.load(IRoomLoadable)
                - Background is changed
                - Canvas is filled
                - PhysicsWorld is filled
            onRoomLoadEnd()
        onWorldLoadEnd()

