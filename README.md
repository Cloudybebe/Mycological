
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
| `cordyceps_mushroom_block` | Supplied shelf spore top texture on all six sides; full mushroom block |
| `cordyceps_stem` | Full block with side texture + `_top`, side texture on bottom |
| `cordyceps_stem_spore` | Full block with side texture + `_top`, side texture on bottom |
| `prototaxies_fence` | Vanilla fence models, plank texture, wooden fence connections |
| `prototaxies_fence_gate` | Vanilla directional gate models, hand/redstone opening |
| `prototaxies_pressure_plate` | Wooden pressure plate, triggered by entities |
| `prototaxies_button` | Wooden button, wall/floor/ceiling placement, arrow activation |
| `prototaxies_stairs` | Plank texture, all straight/inner/outer shapes, waterlogging |
| `prototaxies_slab` | Plank texture, bottom/top/double slabs, waterlogging; double slabs drop two |
| `prototaxies_sign` / `prototaxies_wall_sign` | Editable vanilla signs, plank texture reused in the sign atlas |
| `prototaxies_hanging_sign` / `prototaxies_wall_hanging_sign` | Editable vanilla hanging signs, stripped-log texture reused in the sign atlas |

The Prototaxies boat uses vanilla oak boat geometry and the existing plank texture.
Signs and the boat use vanilla oak inventory icons. Their reused entity textures
are provisional and may stretch; dedicated UV textures can replace them later.
The trapdoor now uses directional vanilla templates and rotates both its closed
texture and open hinge for all facings and both halves.

Cordyceps stems and Prototaxies logs, wood, and spores use `SoundType.STEM`,
the same break, step, place, hit, and fall sounds as crimson and warped stems.
The former `cordyceps_shelf` and `cordyceps_shelf_spore` registry names have
been replaced by `cordyceps_stem` and `cordyceps_stem_spore`; replace old
placed shelf blocks in saved test worlds with the renamed items.

Door inventory art uses the supplied lower-door texture because a separate
vanilla-style door item icon was not supplied. Plants retain their authored
colors rather than receiving vanilla biome tint. The added woodset has crafting recipes; world generation is not included.

### Named block verification

- Find all 28 registered items in the Mycological tab and place each in a test world.
- Place log, wood, and spore blocks on all three axes; strip logs and wood and check the axis is preserved.
- Check spore blocks use the spore top texture on both ends, including when rotated.
- Check both wood variants use their respective side texture on all six faces.
- Compare stem, log, and wood sounds to crimson or warped stems.
- Open doors and trapdoors by hand and redstone; break both door halves and check for one item.
- Check both large-grass halves, bonemeal growth from short grass, and placement on the custom grass block.
- Attach lichen to walls, floor, and ceiling; add faces, waterlog it, and use bonemeal.
- Brush each sand variant until it becomes vanilla sand or red sand (no treasure reward).
- Partially brush a sand block, reload the world, and check it remains brushable.
- Break both sand variants and check that larger textured spores drift upward; walking on either sand also releases spores.
- Remove support beneath both sand variants and check that they land intact and remain brushable.
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


## Cordyceps spore and Mithridatism

`mycologicalmod:cordyceps_spore` appears in the Mycological creative tab and
uses the supplied spore item artwork. It can be eaten at full hunger and
always applies `mycologicalmod:cordycepual_poison` for 30 seconds.

The effect lowers maximum health by one full heart (2 health points) and
reduces attack damage by 4, matching Weakness I. Existing health is capped
at the reduced maximum; the effect does not deal periodic poison damage.
Eating more spores refreshes the duration without stacking heart loss.
Expiry or milk restores the original attribute capacity and attack damage,
without instantly healing the lost health. The display name is Mithridatism; the registry ID remains stable for saved worlds. The effect icon is the supplied
Cordyceps Lichen texture.

A dedicated GLSL screen shader draws procedural branching vines in the
lichen palette. Pixelated slime-mold veins with pointed geometric tips and stronger tip-weighted wriggling, thick six-color lichen shading driven by analytic curvature, with warm brown-olive rims, mustard and gold bodies, and pale wet highlights in one render pass, anchored nodules that generate larger secondary branches and forks, and root-heavy film spreading quickly across a wide area before narrowing and slowing toward the tips of the full vein network appear and grow outward over the first 12 seconds and retract over the
last eight seconds. Refreshing the effect preserves growth; clearing it
early smoothly retracts the vines. The paths are stable for each player, and corner sources enter diagonally.
The center remains clear and the HUD draws above the shader. Rendering is
first-person only and respects hidden HUD and paused singleplayer. Player
or world changes reset the visual state. It does not replace another mod's
world post-processing chain. Compatibility with third-party shader packs
has not been tested.

### Spore verification

- Eat a spore at full hunger; check the lichen icon and 30-second effect timer.
- Check maximum health changes from 10 hearts to 9 and attacks become weaker.
- Eat another spore and check that heart loss stays at one heart and vines do not reset.
- Watch vines advance and retract near expiry; check the central view and HUD remain clear.
- Drink milk while vines are grown; check attributes restore and vines smoothly retract.
- Switch perspectives, pause singleplayer, reload resources, die, and leave/rejoin the world.
- Check normal maximum health and attack damage after natural expiry.
