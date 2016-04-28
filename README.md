# Collector_V2

- Complete ParticleEffectDefinition and XMLParticleEffectDefinition
- ParticleEffectManager.loadDefinitions
    - Just assume XML file for now
    - Each effect root will be passed to an XMLParticleEffectDefinition
- Add createParticleEffect functions to ParticleEffectManager
- Fill out ParticleEffect and Particle
- Create EntityType enum class (instead of passing around String types)


Driver -> World Load - chain of calls:
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

Scripts:
    Script objects are built, by mapping their types to classes (from ScriptConfig)
    Then each script object is given a ScriptDefinition (from 'script' objects in Tiled)
        - Which means IRoomLoadable will have to included getScriptDefinitions

Particle Effects:
      Particle Effect objects are built, by mapping their types to a ParticleEffectDefinition
      Each time the factory builds from type, the type is mapped to a definition loaded into memory
      The definition will set all necessary fields
      Then caller can set custom fields as needed

Animation:
    Is simply a black box that gives you the current TextureRegion frame when polled

Caching:
    No statics