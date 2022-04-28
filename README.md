# Tech Mod

A Tech mod for Fabric 1.16.

## Source Organization/Plans/To-Do

### AbstractProcessingBlock

A set of classes to build all machines from:

- AbstractProcessingBlock
- AbstractProcessingBlockEntity
- AbstractProcessingScreenHandler
- AbstractProcessingScreen
- AbstractProcessingRecipe
- AbstractProcessingRecipeSerializer

Possibly builders for each of these? Could be cool.

All processing blocks take items/fluids/energy input and produce items/fluids/
energy output after a time step. The screen can have item I/O slots, fluid I/O
bars, and an energy bar. Recipes can have items, fluids, energy I/O ingredients
and a processing time specified.

It may be possible to have an AbstractProcessingScreenBuilder that dynamically
adds the bars, item slots, progress indicators, etc. to the right place. Or
even a single builder that can produce all of the classes??? That'd be sick.

idk if that's possible with java.

## Plans

This mod contains ores, metals, energy transportation, energy generation, and
machines.

### Ores

- Copper
- Tin
- Lead
- Silver
- Aluminum
- Nickel
- Cobalt
- Tungsten
- Uranium
- Boron
- Lithium
- Magnesium
- Thorium

All of these are mined for ores which are processed exactly like iron and gold
ore into their respective ingots.

Additionally, there is:

- Baryte (drops barium gems)
- Rare Metal Ore (drops rare metals, for example Yttrium)

### Metals and Metals

You can get the metals/gems from the ores added. There are also new alloys:

- Bronze (Copper + Tin)
- Ferroboron Alloy (Iron + Boron)
- Tough Alloy  (Ferroboron Alloy + Lithium)
- YBCO (Yttrium, Barium, Copper, this one takes special processing)
- Alnico (Aluminum + Nickel + Cobalt, for magnets)
- Steel (Iron + Carbon)
- Tool Steel (Iron + Carbon + Tungsten)

### Electricity

Two type of electricity: AC and DC. Generators generally output AC, machines
take DC. DC is analogous to the classical modded minecraft energy system.

AC wires are much cheaper to transport large amounts of power over a distance,
but they have some risks (higher power = more deadly).

There are 4 types of wire (ordered from better to worse):

- Copper
- Silver
- Gold
- YBCO

Each of these tiers can transport more power. Superconducting YBCO can transport
a nearly limitless amount of power, but is difficult to make and has an active
cost of use. The same wires are used for AC and DC.

Connecting a machine to a live AC line will cause the machine to explode.

Need to use rectifiers to convert from AC to DC, and inverters to convert from
DC to AC. Rectifiers and inverters will explode if given too much power passes
through them (there are different tiers, corresponding to the wires).

When too much power is passed through a wire, it will "melt" and drop as an item.

Breaking a live AC line will damage you, proportional to the voltage. At or
above 150KV, you will instantly die no matter your armor. Use transformers
to step up or down an AC voltage for transportation or before rectifying it to
DC. Higher voltages allow more power to be transported on a wire, but passes
more power through the transformer, so make sure the transformer can handle it.

YBCO wires require liquid nitrogen to be pumped through them in order to work.
The amount of nitrogen needed is proportional to the length of the wire and the
power passed through it. Use a nitrogen filter and gas condenser to collect
liquid nitrogen from the atmosphere using electricity.

### Electricity Generation

There are three generators:

- Kinetic Generator
- Steam Generator
- Solar Generator

The kinetic generator has a spot for an axle that needs to be rotated by
a rotor (see rotors). Makes AC power.

The steam generator has a heat pipe and water pipe input,
and a steam output. Heat comes from a heater, and steam exits the generator as
power is produced. The steam can be cooled back to water in a condenser. Makes
AC power.

The solar generator is active during the day and produces a small amount of DC
power.

Types of rotors:

- Windmill: spins from wind
- Hydroelectric Rotor: spins from flowing water
- Handcrank: right click to spin

Types of heaters:

- Fuel Heater: Burns fuel (coal, wood, etc.) to produce heat
- Geothermal Heater: Converts lava to stone and produces heat
- Fission Reactor: Produces large amounts of heat and radioactive products
  from radioactive fuel.
- Fusion Reactor: Produces huge amounts of heat from fusable fuel. The heat is
  outputted as a hot gas (the product of the fusion reaction).

Fission and fusion are things to implement after everything else.

### Machines

#### Electric Furnace

Uses electricity to heat items and cook them. Higher tiers heat items faster.

#### Grinder

Uses electricity to grind items. One use is ore doubling. Another is as part
of YBCO production.

#### Alloyer

Uses electricity to melt and mix items into an alloy. All alloys are made using
this machine.

#### Vacuum Manufactury

Combines crafting ingredients under a vacuum/other gas. Necessary for some
crafting recipies.

#### Vacuum Chamber

Multi-block structure to enclose a space in a vacuum/a different atmosphere.
Players inside without a spacesuit will die if the atmosphere is toxic or the
pressure is too low. Used as part of YBCO production.

#### Fluid Collector

Uses electricity to collect fluid from the world.

#### Air Collector

Uses electricity to collect air gas.

#### Nitrogen Filter

Uses electricity to separate nitrogen gas from air.

#### Condenser

Cools down a gas using electricity and outputs it as a liquid.

#### Quarry

Mines blocks from the world using electricity.

### Pipes

#### Fluid Pipes

Multiple tiers. First tier can't transport gases or lava. Higher tiers
transport liquids faster.

#### Item Pipes

Multiple tiers. Higher tiers transport items faster. Also item filters.

#### Heat Pipes

Multiple tiers. Do research on heat conductivity. Higher tiers transport heat
more efficiently and faster (some heat is lost based on pipe length).
