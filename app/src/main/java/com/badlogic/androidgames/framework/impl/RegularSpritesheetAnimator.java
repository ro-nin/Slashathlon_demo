package com.badlogic.androidgames.framework.impl;


import com.EmGiuPa.androidgames.ProjectRunner.AnimatedComponent;
import com.EmGiuPa.androidgames.ProjectRunner.ObjectStateEnum;
import com.badlogic.androidgames.framework.Animator;
import com.badlogic.androidgames.framework.Graphics;

public class RegularSpritesheetAnimator implements Animator {

    /**
     * Draw a looped animation from a spritesheet
     *
     * @param g
     * @param ac        Animated component of the object to draw
     * @param animation which animation  of the object to play, referenced via state
     * @param x         where to draw
     * @param y         where to draw
     * @param width
     * @param height
     */
    public void animate(Graphics g, AnimatedComponent ac, ObjectStateEnum animation, int x, int y, int width, int height) {
        RegularSpritesheet rs = ac.getSpritesheet(animation);
        int totalDuration = ac.getDuration(animation);
        int rows = rs.getSpritesheet().getWidth()/(int)rs.getSingleFrameWidth();
        int cols = rs.getSpritesheet().getHeight()/(int)rs.getSingleFrameHeight();
        int numFrames = rows*cols;
        int frameDuration = totalDuration/numFrames;
        int frame = (int) ((System.currentTimeMillis() / frameDuration) % (numFrames));
        frame = (frame ) %numFrames;
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

}
