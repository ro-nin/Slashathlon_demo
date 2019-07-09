package com.EmGiuPa.androidgames.ProjectRunner;

import com.badlogic.androidgames.framework.impl.RegularSpritesheet;

import java.util.EnumMap;
import java.util.Map;

public class AnimatedComponent extends Component{
    //maps to store animations duration, time counters, and spritesheets
    private final Map<ObjectStateEnum, RegularSpritesheet> spritesheets;
    private final Map<ObjectStateEnum, Integer> durations;
    private final Map<ObjectStateEnum, Long> animationsTimeCounters;


    public AnimatedComponent(){
        this.durations = new EnumMap<ObjectStateEnum, Integer>(ObjectStateEnum.class);
        this.spritesheets = new EnumMap<ObjectStateEnum, RegularSpritesheet>(ObjectStateEnum.class);
        this.animationsTimeCounters = new EnumMap<ObjectStateEnum, Long>(ObjectStateEnum.class);
    }

    public Map<ObjectStateEnum, RegularSpritesheet> getSpritesheet() {
        return spritesheets;
    }

    @Override
    public ComponentTypeEnum type() {
        return ComponentTypeEnum.ANIMATED;
    }

    public RegularSpritesheet getSpritesheet(ObjectStateEnum objectStateEnum){
        return spritesheets.get(objectStateEnum);
    }

    public Integer getDuration(ObjectStateEnum objectStateEnum){
        return durations.get(objectStateEnum);
    }

    Long getAnimationTimeCounter(ObjectStateEnum objectStateEnum){
        return animationsTimeCounters.get(objectStateEnum);
    }

    void setAnimationsTimeCounter(ObjectStateEnum objectStateEnum, long timeCounter){
        this.animationsTimeCounters.put(objectStateEnum,timeCounter);
    }

    void addAnimation(ObjectStateEnum objectStateEnum, RegularSpritesheet spritesheet, Integer duration){
        this.spritesheets.put(objectStateEnum,spritesheet);
        this.durations.put(objectStateEnum,duration);
        this.animationsTimeCounters.put(objectStateEnum, 0L);
    }
}
