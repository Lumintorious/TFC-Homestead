package com.lumintorious.tfchomestead.common.villagers;

import net.dries007.tfc.common.blocks.Gem;
import net.dries007.tfc.common.blocks.GroundcoverBlockType;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.crop.Crop;
import net.dries007.tfc.common.blocks.plant.fruit.FruitBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.soil.SandBlockType;
import net.dries007.tfc.common.blocks.soil.SoilBlockType;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.items.Powder;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;
import oshi.util.tuples.Pair;

import java.util.*;
import java.util.function.Supplier;

public class HomesteadTrades {
    private static Map<VillagerProfession, List<Supplier<MerchantOffer>>> OFFERS = new HashMap<>();

    public static void register(VillagerProfession profession, int weight, Supplier<MerchantOffer> offerSupplier) {
        if(!OFFERS.containsKey(profession)) {
            OFFERS.put(profession, new LinkedList<>());
        }

        for(int i = 0 ; i < weight; i++) {
            OFFERS.get(profession).add(offerSupplier);
        }
    }

    public static Optional<MerchantOffer> getRandomTrade(VillagerProfession profession) {
        var list = OFFERS.get(profession);
        if(list == null || list.isEmpty()) return Optional.empty();
        return Optional.of(list.get((int) (Math.random() * list.size())).get());
    }

    static {
        register(TFCVillagerProfessions.DIGGER.get(), 10, HomesteadTrades::getRockOffer);
        register(TFCVillagerProfessions.DIGGER.get(), 10, HomesteadTrades::getCobbleOffer);
        register(TFCVillagerProfessions.DIGGER.get(), 10, HomesteadTrades::getGravelOffer);
        register(TFCVillagerProfessions.LUMBERJACK.get(), 10, HomesteadTrades::getLogOffer);
        register(TFCVillagerProfessions.LUMBERJACK.get(), 10, HomesteadTrades::getLeavesOffer);
        register(TFCVillagerProfessions.LUMBERJACK.get(), 10, HomesteadTrades::getPlankOffer);
        register(TFCVillagerProfessions.FARMER.get(), 4, HomesteadTrades::getCropOffer);
        register(TFCVillagerProfessions.FARMER.get(), 4, HomesteadTrades::getFruitOffer);
        register(TFCVillagerProfessions.FARMER.get(), 1, HomesteadTrades::getNutrientOffer);
        register(TFCVillagerProfessions.BUILDER.get(), 4, HomesteadTrades::getSandOffer);
        register(TFCVillagerProfessions.BUILDER.get(), 4, HomesteadTrades::getSoilOffer);
        register(TFCVillagerProfessions.MINER.get(), 4, HomesteadTrades::getOreOffer);
    }


    public static <T> T randomElem(T[] arr) {
        return arr[(int) (Math.random() * arr.length)];
    }

    private static ItemStack getRandomGem(int count) {
        return new ItemStack(TFCItems.GEMS.get(randomElem(Gem.values())).get(), count);
    }

    private static int orLess(int num) {
        return Math.max(1, num - (int) (num * (Math.random() / 4 + 0.35)));
    }

    private static int orMore(int num) {
        return Math.max(1, num - (int) (num * (Math.random() / 4)));
    }

    private static MerchantOffer makeReversibleOffer(ItemStack input, ItemStack output) {
        boolean isReversed = Math.random() > 0.5;
        if(isReversed) {
            ItemStack temp = input;
            input = output;
            output = temp;
        }
        input.setCount(orMore(input.getCount()));
        output.setCount(orLess(output.getCount()));
        return new MerchantOffer(
            input,
            output,
            64,
            5,
            1
        );
    }

    public static MerchantOffer getRockOffer() {
        Rock rock = randomElem(Rock.values());
        Block block = TFCBlocks.ROCK_BLOCKS.get(rock).get(Rock.BlockType.RAW).get();
        return makeReversibleOffer(
            new ItemStack(block, 12),
            getRandomGem(4)
        );
    }

    public static MerchantOffer getCobbleOffer() {
        Rock rock = randomElem(Rock.values());
        Block block = TFCBlocks.ROCK_BLOCKS.get(rock).get(Rock.BlockType.COBBLE).get();
        return makeReversibleOffer(
            new ItemStack(block, 24),
            getRandomGem(4)
        );
    }

    public static MerchantOffer getGravelOffer() {
        Rock rock = randomElem(Rock.values());
        Block block = TFCBlocks.ROCK_BLOCKS.get(rock).get(Rock.BlockType.GRAVEL).get();
        return makeReversibleOffer(
                new ItemStack(block, 32),
                getRandomGem(4)
        );
    }

    public static MerchantOffer getLogOffer() {
        Wood wood = randomElem(Wood.values());
        Block block = TFCBlocks.WOODS.get(wood).get(Wood.BlockType.LOG).get();
        return makeReversibleOffer(
            new ItemStack(block, block.asItem().getMaxStackSize()),
            getRandomGem(6)
        );
    }

    public static MerchantOffer getLeavesOffer() {
        Wood wood = randomElem(Wood.values());
        Block block = TFCBlocks.WOODS.get(wood).get(Wood.BlockType.LEAVES).get();
        return makeReversibleOffer(
            new ItemStack(block, block.asItem().getMaxStackSize()),
            getRandomGem(3)
        );
    }

    public static MerchantOffer getPlankOffer() {
        Wood wood = randomElem(Wood.values());
        Block block = TFCBlocks.WOODS.get(wood).get(Wood.BlockType.PLANKS).get();
        return makeReversibleOffer(
            new ItemStack(block, block.asItem().getMaxStackSize()),
            getRandomGem(6)
        );
    }

    public static MerchantOffer getSoilOffer() {
        SoilBlockType.Variant variant = randomElem(SoilBlockType.Variant.values());
        SoilBlockType type = randomElem(new SoilBlockType[] {
            SoilBlockType.DIRT,
            SoilBlockType.MUD,
            SoilBlockType.MUD_BRICKS
        });
        Block block = TFCBlocks.SOIL.get(type).get(variant).get();
        return makeReversibleOffer(
            new ItemStack(block, block.asItem().getMaxStackSize()),
            getRandomGem(4)
        );
    }

    public static MerchantOffer getSandOffer() {
        SandBlockType variant = randomElem(SandBlockType.values());
        Block block = TFCBlocks.SAND.get(variant).get();
        return makeReversibleOffer(
            new ItemStack(block, block.asItem().getMaxStackSize()),
            getRandomGem(5)
        );
    }

    public static MerchantOffer getCropOffer() {
        Crop variant1 = randomElem(Crop.values());
        Crop variant2;
        do {
            variant2 = randomElem(Crop.values());
        } while(variant1 == variant2);
        Item requirement = TFCItems.CROP_SEEDS.get(variant1).get();
        Item reward = TFCItems.CROP_SEEDS.get(variant2).get();
        return new MerchantOffer(
                new ItemStack(requirement, orMore(12)),
                getRandomGem(8),
                new ItemStack(reward, orLess(6)),
            64,
            0,
            1
        );
    }

    public static MerchantOffer getFruitOffer() {
        Item reqItem = getFruitSeedStack();
        Item resItem;
        do {
            resItem = getFruitSeedStack();
        } while(reqItem == resItem);
        return new MerchantOffer(
            new ItemStack(reqItem, orMore(10)),
            getRandomGem(8),
            new ItemStack(resItem, orLess(5)),
            64,
            0,
            1
        );
    }

    public static Item getFruitSeedStack() {
        Item result;
        switch ((int) (Math.random() * 3)) {
            case 0 -> {
                FruitBlocks.Tree tree = randomElem(FruitBlocks.Tree.values());
                result = (TFCBlocks.FRUIT_TREE_SAPLINGS.get(tree).get().asItem());
            }
            case 1 -> {
                FruitBlocks.StationaryBush bush = randomElem(FruitBlocks.StationaryBush.values());
                result = TFCBlocks.STATIONARY_BUSHES.get(bush).get().asItem();
            }
            default -> {
                FruitBlocks.SpreadingBush bush = randomElem(FruitBlocks.SpreadingBush.values());
                result = TFCBlocks.SPREADING_BUSHES.get(bush).get().asItem();
            }
        }
        return result;
    }

    public static MerchantOffer getOreOffer() {
        Item item;
        Ore ore;
        do {
            ore = randomElem(new Ore[] {
                    Ore.CASSITERITE,
                    Ore.BISMUTHINITE,
                    Ore.BITUMINOUS_COAL,
                    Ore.MAGNETITE,
                    Ore.LIMONITE,
                    Ore.BORAX,
                    Ore.KAOLINITE,
                    Ore.CINNABAR,
                    Ore.GRAPHITE,
                    Ore.GARNIERITE,
                    Ore.NATIVE_COPPER,
                    Ore.NATIVE_SILVER,
                    Ore.NATIVE_GOLD,
                    Ore.SPHALERITE,
                    Ore.GYPSUM,
                    Ore.SYLVITE,
                    Ore.HALITE
            });
        } while (TFCItems.ORES.get(ore) == null);
        item = TFCItems.ORES.get(ore).get();
        return makeReversibleOffer(
                new ItemStack(item, 4),
                getRandomGem(16)
        );
    }

    @SuppressWarnings("all")
    public static MerchantOffer getNutrientOffer() {
        Pair<Integer, Item> item = randomElem(new Pair[] {
            new Pair(5, TFCItems.COMPOST.get()),
            new Pair(14, Items.BONE_MEAL),
            new Pair(24, TFCItems.POWDERS.get(Powder.WOOD_ASH).get()),
            new Pair(2, TFCBlocks.GROUNDCOVER.get(GroundcoverBlockType.GUANO).get().asItem()),
        });

        return makeReversibleOffer(
            new ItemStack(item.getB(), item.getA()),
            getRandomGem(4)
        );
    }

}
