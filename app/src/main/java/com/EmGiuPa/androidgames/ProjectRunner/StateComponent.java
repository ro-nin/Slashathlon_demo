package com.EmGiuPa.androidgames.ProjectRunner;


import java.util.HashSet;
import java.util.Set;

public class StateComponent extends Component {
    private Set<ObjectStateEnum> currentStates;

    StateComponent() {
        currentStates = new HashSet<ObjectStateEnum>();
    }

    @Override
    public ComponentTypeEnum type() {
        return ComponentTypeEnum.STATE;
    }

    void addState(ObjectStateEnum newState) {
        this.currentStates.add(newState);
    }
    void removeState(ObjectStateEnum state){
        currentStates.remove(state);
    }
    boolean contains(ObjectStateEnum state){
        return currentStates.contains(state);
    }
}
