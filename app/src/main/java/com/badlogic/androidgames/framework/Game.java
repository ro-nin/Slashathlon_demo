package com.badlogic.androidgames.framework;

import android.content.SharedPreferences;

import com.EmGiuPa.androidgames.ProjectRunner.LevelManager;

public interface Game {

    public SharedPreferences getSharedPreferences();

    public SharedPreferences.Editor getEditor();

    public LevelManager getLevelManager();

    public Input getInput();

    public FileIO getFileIO();

    public Graphics getGraphics();

    public Audio getAudio();

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();
}