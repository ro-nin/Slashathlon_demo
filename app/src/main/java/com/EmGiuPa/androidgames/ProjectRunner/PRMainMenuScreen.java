package com.EmGiuPa.androidgames.ProjectRunner;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

import java.util.List;

public class PRMainMenuScreen extends Screen {
    public PRMainMenuScreen(Game game) {
        super(game);               
    }   

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        //touch events
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        //intro music
        if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound")) {
            if(!Assets.intro.isPlaying()) {
                Assets.intro.play();
                Assets.intro.setLooping(true);
            }
        }
        if(!PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound")) {
            Assets.intro.pause();
        }
        //screen touch button interaction
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(inBounds(event, 0, g.getHeight() - 90, 64, 64)) {
                    PRSettings.setBooleandSetting(game.getEditor(),"sound",!(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound")));
                    return;
                }
                if(inBounds(event, 70, 270, 140, 42) ) {
                    game.setScreen(new PRlevelSelectionScreen(game));
                    if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound"))
                        Assets.menuClick.play(1);
                    return;
                }
                if(inBounds(event, 95, 350, 120, 50) ) {
                    game.setScreen(new PRHelpScreen(game));
                    if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound"))
                        Assets.menuClick.play(1);
                    return;
                }
                if(event.x>260 && event.y>410){
                    g.drawPixmapResized(Assets.clearPref,50,50,270,200);
                    LevelManager lm = game.getLevelManager();
                    lm.clearSharedPreferences();
                }
            }
        }
    }
    
    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if(event.x > x && event.x < x + width - 1 && 
           event.y > y && event.y < y + height - 1) 
            return true;
        else
            return false;
    }

    @Override //screen draw
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        //background
        g.drawPixmap(Assets.testBig, 0, 0);
        //Game logo
        g.drawPixmapResized(Assets.logo,290,250, 15, 0);
        //Menu
        g.drawPixmapResized(Assets.mainMenu, 130, 150,80,260 );
        //clear preference buttons
        g.drawPixmapResized(Assets.clearPref,50,50,270,420);

        if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound"))
            g.drawPixmapResized(Assets.buttonVolumeOn,50,50,20,410);
        else
            g.drawPixmapResized(Assets.buttonVolumeOff,50,50,20,410);
    }

    @Override
    public void pause() {
        //stop music on pause
        Assets.intro.stop();
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
    }
}
