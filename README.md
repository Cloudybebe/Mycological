
Mycological
=======

## Leaf layers

`mycologicalmod:leaf_layer` is available in the Natural Blocks creative tab,
or with `/give @s mycologicalmod:leaf_layer`.

Place it on a supported surface, then use more leaf layers on its top face
to stack from one to eight layers. An eight-layer stack supports another
stack above it. The block uses vanilla snow's selection, collision, support,
and placement rules, including its slightly lower collision surface and
the ability to replace a single layer with another block.

Leaf layers use grass sounds, do not melt, and can be broken by hand. Hoes
are the preferred mining tool. Breaking a stack drops one leaf-layer item
per layer. Supporting surfaces follow vanilla snow's support tags; removing
support removes the layer block.

The eight model variants inherit Minecraft's seven snow height models and
full snow-block model, with the leaf texture assigned in the mod namespace.
No vanilla models or textures are replaced.

### In-game verification

- Place and stack all eight heights; a ninth placement starts a stack above.
- Check selection and collision against vanilla snow of the same layer count.
- Break stacks of one, four, and eight layers and check their item counts.
- Stack above a full leaf stack, then remove its supporting block.
- Place leaf layers beside a bright light and check that they do not melt.
- Check that vanilla snow still renders with its original snow texture.

