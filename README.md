# Collector_V2

- Driver determines last visited world and loads it
- Reads world's config in WorldLoader
	- Clears world's physics world
	- Sets any necessary world properties
	- Creates IRoomLoadable object from room's config
		- Creates a list of layers (background, tiled map layers, foreground, etc.)
			- So need to set ParallaxBackground
		- Creates all entities and non-entity bodies
	- Loads the room from the loadable, using RoomLoader
		- Clears all non-particle, non-world layers
		- Adds new render layers (background, tiled map layers, foreground, etc.)
		- Uses Room.getInstance().set(IRoomLoadable) to set the room