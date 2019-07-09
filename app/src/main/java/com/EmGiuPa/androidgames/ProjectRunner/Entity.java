package com.EmGiuPa.androidgames.ProjectRunner;

import java.util.HashMap;
import java.util.Map;
 class Entity {
    private Map<ComponentTypeEnum,Component> componentsList;

    Entity() {
        this.componentsList = new HashMap<ComponentTypeEnum, Component>();
    }

    void addComponent(Component comp){
        comp.setOwner(this);
        componentsList.put(comp.type(),comp);
    }
    Component getComponent(ComponentTypeEnum type) {
        return componentsList.get(type);
    }

}
