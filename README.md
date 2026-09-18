
Mycological
=======

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

