# 1.2.0+1.21.1

## Additions

- Concrete roads of all 16 colours!
- Safari Net, an item that can capture and release entities.
- Rancher, a machine that collects certain resources from specific mobs.
- Fertilizer, a machine that automatically bonemeals crops using Industrial Fertilizer.
- Sewer, a block that collects sewage when living entities step on it.
- Industrial Composter, a machine that converts sewage into Industrial Fertilizer.
- Breeder, a machine that breeds animals within a range.
- Add redstone controls for machinery, powering a machine will disable it.

## Bug Fixes

- Make the Hopping Conveyor's item texture match the Conveyor's texture.
- Change the recipe for Plastic Blocks to not conflict with Plastic Sheets.
- Fix the harvester not invalidating its working zone on block state changes.
- Fix the planter's inventory not properly round-robin'ing when a non-stackable+non-plantable item is inside.
- Fix hopping conveyors voiding items when the target container is full.

## Changes

- Increase speed gain from roads

## Miscellaneous

- Update to Regolith 1.2.1-beta
- Update internals for hover text and extended hover text (this does not affect players, only developers)
- Gut the foundation in favour of Lazuli

# 1.1.3+1.21.1

## Bug Fixes

- Fixed `clientTick` existing on the server side and crashing
- Fixed Tweakeruler sending undo/redo debug information to the chat

# 1.1.2+1.21.1

## Bug Fixes

- Fix mod-publish-plugin using the wrong archive file
  - It was using the `dev` build, which does not have anything JiJ'ed, causing a crash due to Regolith classes being missing.

# 1.1.1+1.21.1

## Changes

- Heavily rework controls for Tweakeruler

## Bug Fixes

- Fixed pumps not filling their tanks properly

# 1.1.0+1.21.1

## Additions

- Meat processing!
	- Slaughterhouse machine
	- Liquid Meat
	- Raw/Cooked Meat "Ingots"
	- Meat Packer machine
- Ruler item for measuring distance
- Tweakeruler item to function as a fast and simple "mini world-edit"
- Planter machine
- Harvester machine
- Hopping Conveyor
- Add a tooltip explanation to Conveyors and Ejectors (Hopping Conveyors also have one!)
- Item Router
- Add compatibility with Cable Facades
- Add connected textures for decor blocks
	- Requires Fusion for the connected textures to be applied

## Changes

- **Machines no longer auto-eject by default**
- Conveyors no longer push players that are flying

## Bug Fixes

- Fixed wrench not updating a machine's work zone when rotating
- Fixed pump and mob grinder allowing fluid inserts in the output slots

# 1.0.2+1.21.1

## Bug Fixes

- Fixed straws from turning waterlogged blocks into air.

# 0.1.2-modfest

## Bug Fixes

- Fixed straws from turning waterlogged blocks into air.

# 1.0.1+1.21.1

## Additions

- Add min and max to Redstone Clock
- Add new fluids: Beetroot Soup, Mushroom Stew, Suspicious Stew, Rabbit Stew and Honey

## Changes

- Lock Redstone Clocks when given a direct signal
- Removed instabreak from Conveyors

## Bug Fixes

- Fixed Macerator animation not starting
- Fixed tree overpopulation
- Fixed backwards conveyor animations
- Fixed Mob Grinder not properly skipping drops for hostile mobs
- Fixed machines that have a changing ("dynamic") work/idle time not reflecting that in its menu
- Fixed Rubber Wood's textures on horizontal models

# 1.0.0+1.21.1

## Additions

- Placer
- Rubber Trees
- Macerator
- Smasher
- EMI Support
- Recipes for almost everything! The mod should be survival playable now.

## Changes

- **Many** textures updated ([Thepigcat76](https://modrinth.com/user/Thepigcat76))
- Gave the Breaker an inventory
- Changed Breaker to use a FakePlayer to better simulate drops

# 0.1.1-modfest

## Additions

- Add compat with [Legacy Landscape](https://modrinth.com/mod/legacy-landscape). Mega Straws can be crafted by throwing a normal Straw into Liquid Void.

## Bug Fixes

- Fix item group having no name

# 0.1-modfest

The very first release of MineFactorial, made for ModFest 1.21.1.
This build is in a very alpha state and does not contain nearly everything planned for MineFactorial.

## Additions

- Conveyors
- Ejectors
- Fluid Extractors
- Energy Cables
- Fluid Pipes
- Breaker
- Fountain
- Mob Grinder
- Pump
- Steam Boiler
- Steam Turbine
- Capacitor (+creative variant)
- Plastic Tank (+creative variant)
- Straw (+mega straw)
- Wrench (i feel like this one was obvious considering this is a tech mod...)
