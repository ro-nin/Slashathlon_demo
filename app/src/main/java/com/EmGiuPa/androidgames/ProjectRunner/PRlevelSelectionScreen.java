package com.EmGiuPa.androidgames.ProjectRunner;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

import java.util.List;

public class PRlevelSelectionScreen extends Screen {

    private LevelManager lm;

    PRlevelSelectionScreen(Game game) {
        super(game);
        lm = game.getLevelManager();
    }   

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                //lv1
                if(inBounds(event, 60, 260, 130, 60)) {
                    game.setScreen(new PRGameScreen(game,0));
                    if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound")) {//select lv
                        Assets.menuClick.play(1);
                    }
                    Assets.intro.stop();
                    return;
                }
                //lv2
                if(inBounds(event, 60, 310, 130, 60)) {
                    game.setScreen(new PRGameScreen(game,1));
                    if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound")) {//select lv
                        Assets.menuClick.play(1);
                    }
                    Assets.intro.stop();
                    return;
                }
                if(event.x < 100 && event.y > 416 ) {
                    game.setScreen(new PRMainMenuScreen(game));
                    if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound"))
                        Assets.menuClick.play(1);
                    return;
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

    @Override
    public void present(float deltaTime) {

        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.testBig, 0, 0);
        g.drawPixmapResized(Assets.logo,290,250, 15, 0);
        g.drawPixmapResized(Assets.backToMain, 100, 40, 0, 416);
        g.drawPixmapResized(Assets.lv1lv2, 130, 120,60,260 );

        //star drawing based on shared preferences
        if(lm.checkLevelCleared(0)) {
            g.drawPixmapResized(Assets.star,30,30,190,265);
        }
        if(lm.checkLevelCleared(1)) {
            g.drawPixmapResized(Assets.star,30,30,190,340);
        }
        if(lm.checkAllCollectibles(0)) {
            g.drawPixmapResized(Assets.star,30,30,220,265);
        }
        if(lm.checkAllCollectibles(1)) {
            g.drawPixmapResized(Assets.star,30,30,220,340);
        }
        if(lm.checkNoHit(0)) {
            g.drawPixmapResized(Assets.star,30,30,250,265);
        }
        if(lm.checkNoHit(1)) {
            g.drawPixmapResized(Assets.star,30,30,250,340);
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
