package com.badlogic.androidgames.framework.impl;


import com.EmGiuPa.androidgames.ProjectRunner.AnimatedComponent;
import com.EmGiuPa.androidgames.ProjectRunner.ObjectStateEnum;
import com.badlogic.androidgames.framework.Animator;
import com.badlogic.androidgames.framework.Graphics;

public class RegularSpritesheetAnimator implements Animator {

    private static int bias = 0;


    public void animate(Graphics g, AnimatedComponent ac, ObjectStateEnum animation, int x, int y, int width, int height) {
        RegularSpritesheet rs = ac.getSpritesheet(animation);
        int totalDuration = ac.getDuration(animation);
        int rows = rs.getSpritesheet().getWidth()/(int)rs.getSingleFrameWidth();
        int cols = rs.getSpritesheet().getHeight()/(int)rs.getSingleFrameHeight();
        int numFrames = rows*cols;
        int frameDuration = totalDuration/numFrames;
        int frame = (int) ((System.currentTimeMillis() / frameDuration) % (numFrames));
        frame = (frame + bias) %numFrames;
        if (rs != null  && numFrames > 0) {
            int col = frame;
            int row = 0;
            while (col > rows){
                col -= cols;
                row++;
            }
            g.drawPixmap(rs.getSpritesheet(), x, y, width, height,
                    col * (int)rs.getSingleFrameWidth(),
                    row * (int)rs.getSingleFrameHeight(),
                    (int)rs.getSingleFrameWidth(),
                    (int)rs.getSingleFrameHeight());
        }
    }
    /*
    public static void restartFrom(AnimatedComponent ac, ObjectStateEnum animation, int numFrames, int numFrame) {
        int frame = (int) ((System.currentTimeMillis() / (ac.getDuration(animation)/numFrames) % (numFrames)));
        bias = (numFrame - frame);
        if(bias < 0)
            bias += numFrames;
    }
    */
}
