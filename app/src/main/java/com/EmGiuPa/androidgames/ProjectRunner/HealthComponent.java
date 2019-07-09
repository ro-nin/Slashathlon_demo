package com.EmGiuPa.androidgames.ProjectRunner;

public class HealthComponent extends Component {
    private int lifePoints;

    public int getLifePoints() {
        return lifePoints;
    }

    public void setLifePoints(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    HealthComponent(int lifePoints) {
        this.lifePoints = lifePoints;
    }

    @Override
    public ComponentTypeEnum type() {
        return ComponentTypeEnum.HEALTH;
    }

    int decreaseLife(){
        lifePoints--;
        return lifePoints;
    }
    public int increaseLife(){
        lifePoints++;
        return lifePoints;
    }
}
