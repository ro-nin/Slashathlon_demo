package com.badlogic.androidgames.framework;

import com.EmGiuPa.androidgames.ProjectRunner.AnimatedComponent;
import com.EmGiuPa.androidgames.ProjectRunner.ObjectStateEnum;


public interface Animator {

    void animate(Graphics g, AnimatedComponent ac, ObjectStateEnum animation, int x, int y, int width, int height);
}
