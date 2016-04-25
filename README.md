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

TODO:
    - Decide on scripts
    - Decide on particle effects (create tool?)
    - Decide on animations and systems (and those not associated with an entity?)
    - Figure out caching of resources
    - Start implementing Player