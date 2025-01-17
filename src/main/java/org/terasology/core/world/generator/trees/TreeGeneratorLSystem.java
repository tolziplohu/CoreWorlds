// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.core.world.generator.trees;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.terasology.engine.math.LSystemRule;
import org.terasology.engine.utilities.collection.CharSequenceIterator;
import org.terasology.engine.utilities.random.Random;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.block.BlockUri;
import org.terasology.engine.world.chunks.CoreChunk;

import java.util.Map;

/**
 * Allows the generation of complex trees based on L-Systems.
 */
public class TreeGeneratorLSystem extends AbstractTreeGenerator {

    public static final float MAX_ANGLE_OFFSET = (float) Math.toRadians(5);

    /* SETTINGS */
    private BlockUri leafType;
    private BlockUri barkType;

    /* RULES */
    private final String initialAxiom;
    private RecursiveTreeGeneratorLSystem recursiveGenerator;

    /**
     * Init. a new L-System based tree generator.
     *
     * @param initialAxiom The initial axiom to use
     * @param ruleSet The rule set to use
     * @param maxDepth The maximum recursion depth
     * @param angle The angle
     */
    public TreeGeneratorLSystem(String initialAxiom, Map<Character, LSystemRule> ruleSet, int maxDepth, float angle) {
        this(initialAxiom, ruleSet, maxDepth, angle, 3f);
    }

    public TreeGeneratorLSystem(String initialAxiom, Map<Character, LSystemRule> ruleSet, int maxDepth, float angle,
                                float thickness) {
        this.initialAxiom = initialAxiom;

        recursiveGenerator = new RecursiveTreeGeneratorLSystem(maxDepth, angle, ruleSet, thickness);
    }

    @Override
    public void generate(BlockManager blockManager, CoreChunk view, Random rand, int posX, int posY, int posZ) {

        final Quaternionf rotation = new Quaternionf().setAngleAxis(Math.PI / 2f, 0, 0, 1);

        float angleOffset = rand.nextFloat(-MAX_ANGLE_OFFSET, MAX_ANGLE_OFFSET);

        Block bark = blockManager.getBlock(barkType);
        Block leaf = blockManager.getBlock(leafType);
        recursiveGenerator.recurse(view, rand, posX, posY, posZ, angleOffset, new CharSequenceIterator(initialAxiom),
                new Vector3f(), rotation, bark, leaf, 0, this);
    }

    public TreeGeneratorLSystem setLeafType(BlockUri b) {
        leafType = b;
        return this;
    }

    public TreeGeneratorLSystem setBarkType(BlockUri b) {
        barkType = b;
        return this;
    }
}
