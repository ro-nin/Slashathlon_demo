package com.badlogic.androidgames.framework.impl;


public class RegularSpritesheet {

   private AndroidPixmap spritesheet;
   private float singleFrameWidth;
   private float singleFrameHeight;

    public RegularSpritesheet(AndroidPixmap spritesheet, float singleFrameWidth, float singleFrameHeight) {
        this.spritesheet = spritesheet;
        this.singleFrameWidth = singleFrameWidth;
        this.singleFrameHeight = singleFrameHeight;
    }

    public AndroidPixmap getSpritesheet() {
        return spritesheet;
    }

    public void setSpritesheet(AndroidPixmap spritesheet) {
        this.spritesheet = spritesheet;
    }

    public float getSingleFrameWidth() {
        return singleFrameWidth;
    }

    public void setSingleFrameWidth(float singleFrameWidth) {
        this.singleFrameWidth = singleFrameWidth;
    }

    public float getSingleFrameHeight() {
        return singleFrameHeight;
    }

    public void setSingleFrameHeight(float singleFrameHeight) {
        this.singleFrameHeight = singleFrameHeight;
    }
}
