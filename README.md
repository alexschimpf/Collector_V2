# Collector_V2

- Remove WorldLoader and RoomLoader
	- Create IWorldLoadable
	- Add load methods to World and Room respectively
	- ILoadables should not do ANY loading
		- Give enough information for Room to be able to load it (but not too much)
			- Use EntityDefinition instead of Entity?? (don't load into PhysicsWorld before calling Room.load(...)
- Add IOnWorldLoadBegin, IOnWorldLoadEnd, IOnRoomLoadBegin, IOnRoomLoadEnd
- Add IOnEntityDone
	- Canvas listens to Room's onEntityDone event, instead of onDone directly removing from Canvas
- Remove onDone from IUpdate
	- Reason: The updater must remember to call onDone, but the IUpdate object should take care of this, itself
	- Add protected onDone method in Entity
	- Make update in Entity final
		- Make update call abstract update method that subclasses implement
		- Make onDone be last call
- Instead of just blindly clearing a Room, each Entity is individually marked as done, so that listeners who care can react
- Create IWorldLoadable