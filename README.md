
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

Leaf layers use grass sounds, do not melt, and can be broken by hand. Hoes
are the preferred mining tool. Breaking a stack drops one leaf-layer item
per layer. Supporting surfaces follow vanilla snow's support tags; removing
support removes the layer block.

The eight model variants inherit Minecraft's seven snow height models and
full snow-block model, with the leaf texture assigned in the mod namespace.
No vanilla models or textures are replaced.

All leaf models use cutout rendering for transparent texture pixels. Every
layer height is passable. Players intersecting the visible leaf height receive
mild horizontal drag (0.95 compared with powder snow's 0.9), with no vertical
speed multiplier or freezing. Spectators and flying creative players are unaffected.

The Mycological tab uses the leaf layer as its icon and automatically includes
all items registered through `ModItems.ITEMS`, including future block items.

### In-game verification

- Place and stack all eight heights; a ninth placement starts a stack above.
- Check selection against vanilla snow of the same layer count.
- Walk through all eight heights and check the mild slowdown stops outside the leaves.
- Check transparent gaps in the placed block and inventory model.
- Break stacks of one, four, and eight layers and check their item counts.
- Stack above a full leaf stack, then remove its supporting block.
- Place leaf layers beside a bright light and check that they do not melt.
- Check that vanilla snow still renders with its original snow texture.

