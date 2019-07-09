package com.EmGiuPa.androidgames.ProjectRunner;

class GameObject extends Entity{

    private boolean toDestroy;

    boolean getToDestroy() {
        return toDestroy;
    }

    void setToDestroy(boolean toDestroy) {
        this.toDestroy = toDestroy;
    }

    GameObject(){
        super();
        this.toDestroy = false;
    }
}
