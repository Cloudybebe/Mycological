
Mycological
=======

## Named asset blocks

All registered blocks appear in the Mycological creative tab. Texture suffixes
such as `_top` and `_down` are paired model faces, not separate block items.
Names preserve the supplied `prototaxies` and `sporatic` spelling.
Inventory names use vanilla-style modifier ordering (Stripped Prototaxies Log
and Wood, Short/Tall Cordyceps Grass) and Trapdoor spelling. Registry IDs are
kept stable when display names change so saved blocks and items still load.

| Block ID | Texture pairing and behavior |
| --- | --- |
| `cordyceps_grass_block` | Side + `_top`, vanilla dirt bottom; snowy dirt state and plant support, without grass spreading |
| `cordyceps_grass` | Crossed, transparent foliage; bonemeal grows the matching large grass; shears drops |
| `cordyceps_large_grass` | `_down` + `_top` form a two-block plant; shears drops |
| `cordyceps_lichen` | Transparent, glowing, waterloggable face attachment; bonemeal spreading and shears drops |
| `prototaxies_log` | Side + `_top`; three-axis placement and axe stripping |
| `prototaxies_stripped_log` | Side + `_top`; three-axis placement |
| `prototaxies_wood` | Log side texture on all six faces; three-axis placement and axe stripping |
| `prototaxies_stripped_wood` | Stripped log side texture on all six faces; three-axis placement |
| `prototaxies_spore` | Spore sides, spore top texture on both ends; three-axis placement |
| `prototaxies_planks` | Same texture on all six faces |
| `prototaxies_door` | `_down` + `_top`; two-block wooden door, hand and redstone opening |
| `prototaxies_trap_door` | Wooden trapdoor with top/bottom/open states and waterlogging |
| `sporatic_sand` | Brushable into vanilla sand; floating spore break particles |
| `red_sporatic_sand` | Brushable into vanilla red sand; floating spore break particles |
| `cordyceps_stem` | Full block with side texture + `_top`, side texture on bottom |
| `cordyceps_stem_spore` | Full block with side texture + `_top`, side texture on bottom |

Cordyceps stems and Prototaxies logs, wood, and spores use `SoundType.STEM`,
the same break, step, place, hit, and fall sounds as crimson and warped stems.
The former `cordyceps_shelf` and `cordyceps_shelf_spore` registry names have
been replaced by `cordyceps_stem` and `cordyceps_stem_spore`; replace old
placed shelf blocks in saved test worlds with the renamed items.

Door inventory art uses the supplied lower-door texture because a separate
vanilla-style door item icon was not supplied. Plants retain their authored
colors rather than receiving vanilla biome tint. Recipes and world generation
are not included.

### Named block verification

- Find all 17 registered items in the Mycological tab and place each in a test world.
- Place log, wood, and spore blocks on all three axes; strip logs and wood and check the axis is preserved.
- Check spore blocks use the spore top texture on both ends, including when rotated.
- Check both wood variants use their respective side texture on all six faces.
- Compare stem, log, and wood sounds to crimson or warped stems.
- Open doors and trapdoors by hand and redstone; break both door halves and check for one item.
- Check both large-grass halves, bonemeal growth from short grass, and placement on the custom grass block.
- Attach lichen to walls, floor, and ceiling; add faces, waterlog it, and use bonemeal.
- Brush each sand variant until it becomes vanilla sand or red sand (no treasure reward).
- Partially brush a sand block, reload the world, and check it remains brushable.
- Break both sand variants and check that small textured spores drift upward.
- Remove support beneath both sand variants and check that they fall and break like suspicious sand.
- Check side/top texture alignment and transparent grass, lichen, door, and trapdoor pixels.

## Leaf layers

`mycologicalmod:leaf_layer` is available in the Mycological and Natural Blocks creative tabs,
or with `/give @s mycologicalmod:leaf_layer`.

Place it on a supported surface, then use more leaf layers on its top face
to stack from one to eight layers. An eight-layer stack supports another
stack above it. The block uses vanilla snow's selection, support,
and placement rules, including
the ability to replace a single layer with another block.

Leaf layers use grass sounds and do not melt. Only breaking with a hoe or
shears drops leaf-layer items, one per layer; other tools and bare hands
destroy the block without drops. Hoes are the preferred mining tool.
Supporting surfaces follow vanilla snow's support tags; removing
support removes the layer block.

The eight model variants inherit Minecraft's seven snow height models and
full snow-block model, with the leaf texture assigned in the mod namespace.
No vanilla models or textures are replaced.

All leaf models use cutout rendering for transparent texture pixels. Every
layer height is passable. Single layers cause no slowdown, making them useful
for detailing. Players intersecting the visible height of two or more layers receive
moderate horizontal drag (a 0.94 horizontal velocity multiplier per intersecting
block callback), preserving momentum and vertical movement instead of using
powder snow's stuck-block mechanic. There is no freezing. Spectators and flying
creative players are unaffected.

The Mycological tab uses the leaf layer as its icon and automatically includes
all items registered through `ModItems.ITEMS`, including future block items.

### In-game verification

- Place and stack all eight heights; a ninth placement starts a stack above.
- Check selection against vanilla snow of the same layer count.
- Walk through a single layer and check that movement is unaffected.
- Walk through two to eight layers and check the slowdown stops outside the leaves.
- Check transparent gaps in the placed block and inventory model.
- Break stacks of one, four, and eight layers with hoes and shears and check their item counts.
- Break stacks by hand and with other tools (including Silk Touch) and check that no items drop.
- Stack above a full leaf stack, then remove its supporting block.
- Place leaf layers beside a bright light and check that they do not melt.
- Check that vanilla snow still renders with its original snow texture.

