package com.lumintorious.tfchomestead.common.villagers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.lumintorious.tfchomestead.common.entity.HomesteadEntities;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TFCHomesteadVillager extends Villager {
    public TFCHomesteadVillager(EntityType<? extends TFCHomesteadVillager> tfcHomesteadVillagerEntityType, Level level) {
        super(tfcHomesteadVillagerEntityType, level, VillagerType.SWAMP);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 50f).add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getCorePackage(VillagerProfession pProfession, float p_24587_) {
        return ImmutableList.of(
            Pair.of(0, new Swim(0.8F)),
            Pair.of(0, new InteractWithDoor()),
            Pair.of(0, new LookAtTargetSink(45, 90)),
            Pair.of(0, new VillagerPanicTrigger()),
            Pair.of(0, new WakeUp()),
            Pair.of(0, new ReactToBell()),
            Pair.of(0, new SetRaidStatus()),
//            Pair.of(0, new ValidateNearbyPoi(pProfession.getJobPoiType(), MemoryModuleType.JOB_SITE)),
//            Pair.of(0, new ValidateNearbyPoi(pProfession.getJobPoiType(), MemoryModuleType.POTENTIAL_JOB_SITE)),
            Pair.of(1, new MoveToTargetSink()),
//            Pair.of(2, new PoiCompetitorScan(pProfession)),
            Pair.of(3, new LookAndFollowTradingPlayerSink(p_24587_)),
            Pair.of(5, new GoToWantedItem(p_24587_, false, 4)));
//            Pair.of(6, new AcquirePoi(pProfession.getJobPoiType(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())),
//            Pair.of(7, new GoToPotentialJobSite(p_24587_)),
//            Pair.of(8, new YieldJobSite(p_24587_)),
//            Pair.of(10, new AcquirePoi(PoiType.HOME, MemoryModuleType.HOME, false, Optional.of((byte) 14))),
//            Pair.of(10, new AcquirePoi(PoiType.MEETING, MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14))),
//            Pair.of(10, new AssignProfessionFromJobSite()),
//            Pair.of(10, new ResetProfession()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getWorkPackage(VillagerProfession pProfession, float p_24591_) {
        WorkAtPoi workatpoi;
        if (pProfession == VillagerProfession.FARMER) {
            workatpoi = new WorkAtComposter();
        } else {
            workatpoi = new WorkAtPoi();
        }

        return ImmutableList.of(getMinimalLookBehavior(), 
            Pair.of(5, new RunOne<>(ImmutableList.of(
//            Pair.of(workatpoi, 7),
//            Pair.of(new StrollAroundPoi(MemoryModuleType.JOB_SITE, 0.4F, 4), 2),
//            Pair.of(new StrollToPoi(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5),
//            Pair.of(new StrollToPoiList(MemoryModuleType.SECONDARY_JOB_SITE, p_24591_, 1, 6, MemoryModuleType.JOB_SITE), 5),
            Pair.of(new HarvestFarmland(), pProfession == VillagerProfession.FARMER ? 2 : 5),
            Pair.of(new UseBonemeal(), pProfession == VillagerProfession.FARMER ? 4 : 7)))),
            Pair.of(10, new ShowTradesToPlayer(400, 1600)),
            Pair.of(10, new SetLookAndInteract(EntityType.PLAYER, 4)),
            Pair.of(2, new SetWalkTargetFromBlockMemory(MemoryModuleType.JOB_SITE, p_24591_, 9, 100, 1200)),
            Pair.of(3, new GiveGiftToHero(100)),
            Pair.of(99, new UpdateActivityFromSchedule()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getPlayPackage(float pWalkingSpeed) {
        return ImmutableList.of(
            Pair.of(0, new MoveToTargetSink(80, 120)), getFullLookBehavior(),
            Pair.of(5, new PlayTagWithOtherKids()),
            Pair.of(5, new RunOne<>(ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT), ImmutableList.of(
            Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, pWalkingSpeed, 2), 2),
            Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, pWalkingSpeed, 2), 1),
            Pair.of(new VillageBoundRandomStroll(pWalkingSpeed), 1),
            Pair.of(new SetWalkTargetFromLookTarget(pWalkingSpeed, 2), 1),
            Pair.of(new JumpOnBed(pWalkingSpeed), 2),
            Pair.of(new DoNothing(20, 40), 2)))),
            Pair.of(99, new UpdateActivityFromSchedule()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getRestPackage(VillagerProfession pProfession, float pWalkingSpeed) {
        return ImmutableList.of(
            Pair.of(2, new SetWalkTargetFromBlockMemory(MemoryModuleType.HOME, pWalkingSpeed, 1, 150, 1200)),
//            Pair.of(3, new ValidateNearbyPoi(PoiType.HOME, MemoryModuleType.HOME)),
            Pair.of(3, new SleepInBed()),
            Pair.of(5, new RunOne<>(ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT), ImmutableList.of(
            Pair.of(new SetClosestHomeAsWalkTarget(pWalkingSpeed), 1),
            Pair.of(new InsideBrownianWalk(pWalkingSpeed), 4),
            Pair.of(new GoToClosestVillage(pWalkingSpeed, 4), 2),
            Pair.of(new DoNothing(20, 40), 2)))), getMinimalLookBehavior(),
            Pair.of(99, new UpdateActivityFromSchedule()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getMeetPackage(VillagerProfession pProfession, float p_24597_) {
        return ImmutableList.of(
            Pair.of(2, new RunOne<>(ImmutableList.of(
//            Pair.of(new StrollAroundPoi(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2),
            Pair.of(new SocializeAtBell(), 2)))),
            Pair.of(10, new ShowTradesToPlayer(400, 1600)),
            Pair.of(10, new SetLookAndInteract(EntityType.PLAYER, 4)),
            Pair.of(2, new SetWalkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, p_24597_, 6, 100, 200)),
            Pair.of(3, new GiveGiftToHero(100)),
//            Pair.of(3, new ValidateNearbyPoi(PoiType.MEETING, MemoryModuleType.MEETING_POINT)),
            Pair.of(3, new GateBehavior<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, ImmutableList.of(
            Pair.of(new TradeWithVillager(), 1)))), getFullLookBehavior(),
            Pair.of(99, new UpdateActivityFromSchedule()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getIdlePackage(VillagerProfession pProfession, float p_24600_) {
        return ImmutableList.of(
            Pair.of(2, new RunOne<>(ImmutableList.of(
            Pair.of(InteractWith.of(EntityType.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, p_24600_, 2), 2),
            Pair.of(new InteractWith<>(EntityType.VILLAGER, 8, AgeableMob::canBreed, AgeableMob::canBreed, MemoryModuleType.BREED_TARGET, p_24600_, 2), 1),
            Pair.of(InteractWith.of(EntityType.CAT, 8, MemoryModuleType.INTERACTION_TARGET, p_24600_, 2), 1),
            Pair.of(new VillageBoundRandomStroll(p_24600_), 1),
            Pair.of(new SetWalkTargetFromLookTarget(p_24600_, 2), 1),
            Pair.of(new JumpOnBed(p_24600_), 1),
            Pair.of(new DoNothing(30, 60), 1)))),
            Pair.of(3, new GiveGiftToHero(100)),
            Pair.of(3, new SetLookAndInteract(EntityType.PLAYER, 4)),
            Pair.of(3, new ShowTradesToPlayer(400, 1600)),
            Pair.of(3, new GateBehavior<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, ImmutableList.of(
            Pair.of(new TradeWithVillager(), 1)))),
            Pair.of(3, new GateBehavior<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.BREED_TARGET), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.RUN_ONE, ImmutableList.of(
            Pair.of(new VillagerMakeLove(), 1)))), getFullLookBehavior(),
            Pair.of(99, new UpdateActivityFromSchedule()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getPanicPackage(VillagerProfession pProfession, float p_24603_) {
        float f = p_24603_ * 1.5F;
        return ImmutableList.of(
            Pair.of(0, new VillagerCalmDown()),
            Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_HOSTILE, f, 6, false)),
            Pair.of(1, SetWalkTargetAwayFrom.entity(MemoryModuleType.HURT_BY_ENTITY, f, 6, false)),
            Pair.of(3, new VillageBoundRandomStroll(f, 2, 2)), getMinimalLookBehavior());
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getPreRaidPackage(VillagerProfession pProfession, float p_24606_) {
        return ImmutableList.of(
            Pair.of(0, new RingBell()),
            Pair.of(0, new RunOne<>(ImmutableList.of(
            Pair.of(new SetWalkTargetFromBlockMemory(MemoryModuleType.MEETING_POINT, p_24606_ * 1.5F, 2, 150, 200), 6),
            Pair.of(new VillageBoundRandomStroll(p_24606_ * 1.5F), 2)))), getMinimalLookBehavior(),
            Pair.of(99, new ResetRaidStatus()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getRaidPackage(VillagerProfession pProfession, float p_24609_) {
        return ImmutableList.of(
            Pair.of(0, new RunOne<>(ImmutableList.of(
            Pair.of(new GoOutsideToCelebrate(p_24609_), 5),
            Pair.of(new VictoryStroll(p_24609_ * 1.1F), 2)))),
            Pair.of(0, new CelebrateVillagersSurvivedRaid(600, 600)),
            Pair.of(2, new LocateHidingPlaceDuringRaid(24, p_24609_ * 1.4F)), getMinimalLookBehavior(),
            Pair.of(99, new ResetRaidStatus()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super Villager>>> getHidePackage(VillagerProfession pProfession, float p_24612_) {
        int i = 2;
        return ImmutableList.of(
            Pair.of(0, new SetHiddenState(15, 3)),
            Pair.of(1, new LocateHidingPlace(32, p_24612_ * 1.25F, 2)), getMinimalLookBehavior());
    }

    private static Pair<Integer, Behavior<LivingEntity>> getFullLookBehavior() {
        return 
            Pair.of(5, new RunOne<>(ImmutableList.of(
            Pair.of(new SetEntityLookTarget(EntityType.CAT, 8.0F), 8),
            Pair.of(new SetEntityLookTarget(EntityType.VILLAGER, 8.0F), 2),
            Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 2),
            Pair.of(new SetEntityLookTarget(MobCategory.CREATURE, 8.0F), 1),
            Pair.of(new SetEntityLookTarget(MobCategory.WATER_CREATURE, 8.0F), 1),
            Pair.of(new SetEntityLookTarget(MobCategory.AXOLOTLS, 8.0F), 1),
            Pair.of(new SetEntityLookTarget(MobCategory.UNDERGROUND_WATER_CREATURE, 8.0F), 1),
            Pair.of(new SetEntityLookTarget(MobCategory.WATER_AMBIENT, 8.0F), 1),
            Pair.of(new SetEntityLookTarget(MobCategory.MONSTER, 8.0F), 1),
            Pair.of(new DoNothing(30, 60), 2))));
    }

    private static Pair<Integer, Behavior<LivingEntity>> getMinimalLookBehavior() {
        return 
            Pair.of(5, new RunOne<>(ImmutableList.of(
            Pair.of(new SetEntityLookTarget(EntityType.VILLAGER, 8.0F), 2),
            Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), 2),
            Pair.of(new DoNothing(30, 60), 8))));
    }

//    @Nullable
//    @Override
//    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
//        var result = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
//        if (pReason == MobSpawnType.BREEDING) {
//            this.setVillagerData(this.getVillagerData().setProfession(TFCVillagerProfessions.PRIMITIVE.get()));
//        }
//        return result;
//    }

    @Override
    public void setVillagerData(VillagerData data) {
        super.setVillagerData(
            data
        );
    }

    public void randomizeData() {
        VillagerProfession profession;
        var list = TFCVillagerProfessions.PROFESSIONS.getEntries().stream().toList();
        profession = list.get((int) (Math.random() * list.size())).get();
        this.setVillagerData(getVillagerData().setProfession(profession));
        this.getOffers().clear();
        this.updateTrades();
    }

    @Override
    protected void updateTrades() {
        if (getOffers().size() >= 7) return;
        for(int i = 0 ; i < 7; i++) {
            HomesteadTrades.getRandomTrade(this.getVillagerData().getProfession()).ifPresent(offer -> {
                this.getOffers().add(offer);
            });
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Offers", 10)) {
            this.offers = new MerchantOffers(pCompound.getCompound("Offers"));
        }

//        this.inventory.fromTag(pCompound.getList("Inventory", 10));
    }

    @Override
    public Villager getBreedOffspring(ServerLevel level, AgeableMob mob) {
        Villager villager = new TFCHomesteadVillager(HomesteadEntities.VILLAGER.get(), level);
        villager.finalizeSpawn(level, level.getCurrentDifficultyAt(villager.blockPosition()), MobSpawnType.BREEDING, (SpawnGroupData) null, (CompoundTag) null);
        villager.setAge(-24000 * 8 * 12);
        return villager;
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> pDynamic) {
        Brain<Villager> brain = this.brainProvider().makeBrain(pDynamic);
        this.registerBrainGoals(brain);
        return brain;
    }

    @Override
    public void refreshBrain(ServerLevel pServerLevel) {
        Brain<Villager> brain = this.getBrain();
        brain.stopAll(pServerLevel, this);
        this.brain = brain.copyWithoutBehaviors();
        this.registerBrainGoals(this.getBrain());
    }

    private void registerBrainGoals(Brain<Villager> pVillagerBrain) {
        VillagerProfession villagerprofession = this.getVillagerData().getProfession();
        if (this.isBaby()) {
            pVillagerBrain.setSchedule(Schedule.VILLAGER_BABY);
            pVillagerBrain.addActivity(Activity.PLAY, getPlayPackage(0.5F));
        } else {
            pVillagerBrain.setSchedule(Schedule.VILLAGER_DEFAULT);
            pVillagerBrain.addActivityWithConditions(Activity.WORK, getWorkPackage(villagerprofession, 0.5F), ImmutableSet.of(
Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT)));
        }

        pVillagerBrain.addActivity(Activity.CORE, getCorePackage(villagerprofession, 0.5F));
        pVillagerBrain.addActivityWithConditions(Activity.MEET, getMeetPackage(villagerprofession, 0.5F), ImmutableSet.of(
Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT)));
        pVillagerBrain.addActivity(Activity.REST, getRestPackage(villagerprofession, 0.5F));
        pVillagerBrain.addActivity(Activity.IDLE, getIdlePackage(villagerprofession, 0.5F));
        pVillagerBrain.addActivity(Activity.PANIC, getPanicPackage(villagerprofession, 0.5F));
        pVillagerBrain.addActivity(Activity.PRE_RAID, getPreRaidPackage(villagerprofession, 0.5F));
        pVillagerBrain.addActivity(Activity.RAID, getRaidPackage(villagerprofession, 0.5F));
        pVillagerBrain.addActivity(Activity.HIDE, getHidePackage(villagerprofession, 0.5F));
        pVillagerBrain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        pVillagerBrain.setDefaultActivity(Activity.IDLE);
        pVillagerBrain.setActiveActivityIfPossible(Activity.IDLE);
        pVillagerBrain.updateActivityFromSchedule(this.level.getDayTime(), this.level.getGameTime());
    }
    
}
