package com.EmGiuPa.androidgames.ProjectRunner;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

import java.util.List;

public class PRHelpScreen extends Screen {
    PRHelpScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        //touch events
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        //sound settings
        if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound")){
            Assets.tutorial.play();
            Assets.tutorial.setLooping(true);
        }
        if(!PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound")){
            Assets.tutorial.pause();
        }
        //screen touch button interaction
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x < 100 && event.y > 416 ) {
                    game.setScreen(new PRMainMenuScreen(game));
                    if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound"))
                        Assets.menuClick.play(1);
                    return;
                }
                if(event.x >240 && event.y > 416 ) {
                    game.setScreen(new PRHelpScreen2(game));
                    if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound"))
                        Assets.menuClick.play(1);
                    return;
                }
            }
        }
    }

    @Override//screen draw
    public void present(float deltaTime) {

        Graphics g = game.getGraphics();      
        g.drawPixmap(Assets.tutorialBackground, 0, 0);
        //messages
        g.drawPixmapResized(Assets.help1, 300, 250, 10,10);
        //bard sprite
        g.drawPixmapResized(Assets.bardSinglePlayn, 50,50, 90, 340);
        //back arrow
        g.drawPixmapResized(Assets.backToMain, 100, 40, 0, 416);
        //next arrow
        g.drawPixmapResized(Assets.nextArrow, 100, 40,230,416);
    }

    @Override
    public void pause() {
        //stop music on pause
        Assets.tutorial.stop();
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {

    }
}