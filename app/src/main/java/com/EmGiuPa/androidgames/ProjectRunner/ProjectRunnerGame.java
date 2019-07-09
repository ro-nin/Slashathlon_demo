package com.EmGiuPa.androidgames.ProjectRunner;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class ProjectRunnerGame extends AndroidGame {
    @Override
    //Entry point of the game ?
    public Screen getStartScreen() {
        return new PRLoadingScreen(this);
    }
}