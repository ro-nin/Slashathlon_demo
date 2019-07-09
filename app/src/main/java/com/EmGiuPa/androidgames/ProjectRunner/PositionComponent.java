package com.EmGiuPa.androidgames.ProjectRunner;

public class PositionComponent extends Component{
    private float lane; //1 to 5
    private float row;

    float getLane() {
        return lane;
    }

    void setLane(float lane) {
        this.lane = lane;
    }

    float getRow() {
        return row;
    }

    void setRow(float row) {
        this.row = row;
    }

    PositionComponent(float lane, float row) {
        this.lane = lane;
        this.row = row;
    }

    @Override
    public ComponentTypeEnum type() {
        return ComponentTypeEnum.POSITION;
    }
}
