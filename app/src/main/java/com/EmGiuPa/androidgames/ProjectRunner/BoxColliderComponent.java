package com.EmGiuPa.androidgames.ProjectRunner;

public class BoxColliderComponent extends Component {
    int getTopLeftX() {
        return topLeftX;
    }

    public void setTopLeftX(int topLeftX) {
        this.topLeftX = topLeftX;
    }

    int getTopLeftY() {
        return topLeftY;
    }

    public void setTopLeftY(int topLeftY) {
        this.topLeftY = topLeftY;
    }

    int getBottomRightX() {
        return bottomRightX;
    }

    public void setBottomRightX(int bottomRightX) {
        this.bottomRightX = bottomRightX;
    }

    int getBottomRightY() {
        return bottomRightY;
    }

    public void setBottomRightY(int bottomRightY) {
        this.bottomRightY = bottomRightY;
    }

    void updateBoxCoordinates(int topLeftX,int topLeftY,int bottomRightX,int bottomRightY){
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
    }

    private int topLeftX;
    private int topLeftY;
    private int bottomRightX;
    private int bottomRightY;

    BoxColliderComponent(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY) {
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
    }

    boolean checkCollision(BoxColliderComponent other){
        if(this.topLeftX <= other.bottomRightX &&
                this.bottomRightX >= other.topLeftX &&
                this.topLeftY <= other.bottomRightY &&
                this.bottomRightY >= other.topLeftY) {
            return true;
        }
        return false;
    }

    @Override
    public ComponentTypeEnum type() {
        return ComponentTypeEnum.BOXCOLLIDER;
    }
}
