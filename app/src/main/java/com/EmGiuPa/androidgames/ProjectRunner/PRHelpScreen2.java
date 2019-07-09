package com.EmGiuPa.androidgames.ProjectRunner;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

import java.util.List;

public class PRHelpScreen2 extends Screen {

    PRHelpScreen2(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        Graphics g = game.getGraphics();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > g.getWidth() - 64 && event.y > g.getHeight() - 64 ) {
                    game.setScreen(new PRMainMenuScreen(game));
                    if(PRSettings.getBooleanSetting(game.getSharedPreferences(),"sound"))
                        Assets.menuClick.play(1);
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawPixmap(Assets.grass0, 0, 0);

        //messages
        g.drawPixmapResized(Assets.help2, 300, 250, 10,10);

        //char sprite
        g.drawPixmapResized(Assets.bardBack, 50,50, 40, 110);
        g.drawPixmapResized(Assets.knightBack, 50,50, 235, 115);
        g.drawPixmapResized(Assets.biArrow, 150,50, 85,110);

        //correct match
        g.drawPixmapResized(Assets.knightBack,50,50,130,430);
        g.drawPixmapResized(Assets.verticalArrow,50,100, 130, 340);
        g.drawPixmapResized(Assets.singleSkeleton,50,50, 130, 290);

        //next arrow
        g.drawPixmapResized(Assets.nextArrow, 100, 40,230,416);
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
