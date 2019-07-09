package com.EmGiuPa.androidgames.ProjectRunner;

abstract class Component {
    private ComponentTypeEnum componentType;
    private Entity owner;


    Entity getOwner() {
        return owner;
    }

    void setOwner(Entity owner) {
        this.owner = owner;
    }

    public abstract ComponentTypeEnum type();

    public ComponentTypeEnum getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentTypeEnum componentType) {
        this.componentType = componentType;
    }
}
