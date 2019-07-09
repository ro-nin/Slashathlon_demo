package com.EmGiuPa.androidgames.ProjectRunner;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.Screen;

public class PRLoadingScreen extends Screen {
    PRLoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        //there is still some mr now asset
        Graphics g = game.getGraphics();

        Assets.testBig = g.newPixmap("images/testBg1.png", PixmapFormat.RGB565);

        Assets.logo = g.newPixmap("images/logo.png", PixmapFormat.ARGB4444);
        Assets.mainMenu = g.newPixmap("images/mainmenu.png", PixmapFormat.ARGB4444);

        Assets.help1 = g.newPixmap("images/help1.png", PixmapFormat.ARGB4444);
        Assets.help2 = g.newPixmap("images/help2.png", PixmapFormat.ARGB4444);

        Assets.ready = g.newPixmap("images/ready.png", PixmapFormat.ARGB4444);
        Assets.pause = g.newPixmap("images/pausemenu.png", PixmapFormat.ARGB4444);
        Assets.gameOver = g.newPixmap("images/gameover.png", PixmapFormat.ARGB4444);

        //buttons
        Assets.buttonPause = g.newPixmap("images/pause2.png",PixmapFormat.ARGB4444);
        Assets.buttonVolumeOn = g.newPixmap("images/volumeOn.png",PixmapFormat.ARGB4444);
        Assets.buttonVolumeOff = g.newPixmap("images/volumeOff.png",PixmapFormat.ARGB4444);
        Assets.clearPref = g.newPixmap("images/deletePref.png",PixmapFormat.ARGB4444);

        Assets.potion = g.newPixmap("images/potions.png", PixmapFormat.ARGB4444);
        Assets.coinColl = g.newPixmap("images/coinSpritesheet.png", PixmapFormat.ARGB4444);
        Assets.lifeSprite = g.newPixmap("images/lifeSprite.png",PixmapFormat.ARGB4444);
        Assets.lifeSpriteEmpty = g.newPixmap("images/lifeSpriteEmpty.png",PixmapFormat.ARGB4444);
        Assets.bossIdle = g.newPixmap("images/demon-idle.png",PixmapFormat.ARGB4444);
        Assets.bossAttack = g.newPixmap("images/demon-attack-no-breath.png",PixmapFormat.ARGB4444);

        //background level forest
        Assets.grass0 = g.newPixmap("images/grass0.png",PixmapFormat.ARGB4444);
        Assets.grass1 = g.newPixmap("images/grass1.png",PixmapFormat.ARGB4444);
        Assets.terrain1 = g.newPixmap("images/terrain0.png",PixmapFormat.ARGB4444);

        //background level dark
        Assets.darkTile0 =g.newPixmap("images/dark_tile0.png",PixmapFormat.ARGB4444);
        Assets.darkTile1 =g.newPixmap("images/dark_tile1.png",PixmapFormat.ARGB4444);
        Assets.darkTile2 =g.newPixmap("images/dark_tile2.png",PixmapFormat.ARGB4444);

        //bottom screen background
        Assets.bottomScreenBackground = g.newPixmap("images/bottomScreenBackground.png",PixmapFormat.ARGB4444);

        //tutorial  background
        Assets.tutorialBackground = g.newPixmap("images/tBG.png",PixmapFormat.ARGB4444);
        Assets.bardSinglePlayn = g.newPixmap("images/bardSinglePlayn.png",PixmapFormat.ARGB4444);
        Assets.bardBack = g.newPixmap("images/bardBack.png",PixmapFormat.ARGB4444);
        Assets.knightBack = g.newPixmap("images/knightBack.png",PixmapFormat.ARGB4444);

        Assets.biArrow = g.newPixmap("images/frecciaBid.png",PixmapFormat.ARGB4444);
        Assets.nextArrow = g.newPixmap("images/nextArrow.png",PixmapFormat.ARGB4444);
        Assets.backToMain = g.newPixmap("images/BackToMain.png",PixmapFormat.ARGB4444);
        Assets.verticalArrow =g.newPixmap("images/verticalArrow.png",PixmapFormat.ARGB4444);
        Assets.singleSkeleton =g.newPixmap("images/singleFrameSkeleton.png",PixmapFormat.ARGB4444);

        //win screen
        Assets.gameWin = g.newPixmap("images/levelCleared.png",PixmapFormat.ARGB4444);

        //characters
        Assets.bardGameOver =g.newPixmap("images/bardGameOver.png",PixmapFormat.ARGB4444);
        Assets.bardRunning = g.newPixmap("images/BARD.png",PixmapFormat.ARGB4444);
        Assets.bardHit = g.newPixmap("images/BARDHIT.png",PixmapFormat.ARGB4444);
        Assets.bardAttack = g.newPixmap("images/BARDATTACK.png",PixmapFormat.ARGB4444);

        Assets.mageRunning = g.newPixmap("images/MAGICIAN.png",PixmapFormat.ARGB4444);
        Assets.mageHit = g.newPixmap("images/MAGICIANHIT.png",PixmapFormat.ARGB4444);
        Assets.mageAttack = g.newPixmap("images/MAGICIANATTACK.png",PixmapFormat.ARGB4444);

        Assets.knightRunning = g.newPixmap("images/KNIGHT.png",PixmapFormat.ARGB4444);
        Assets.knightHit = g.newPixmap("images/KNIGHTHIT.png",PixmapFormat.ARGB4444);
        Assets.knightAttack = g.newPixmap("images/KNIGHTATTACK.png",PixmapFormat.ARGB4444);

        //level selection screen
        Assets.lv1lv2 =g.newPixmap("images/level1Level2.png",PixmapFormat.ARGB4444);
        Assets.star =g.newPixmap("images/star.png",PixmapFormat.ARGB4444);

        //enemy
        Assets.skeleGreenIdle = g.newPixmap("images/skeleGreen.png",PixmapFormat.ARGB4444);
        Assets.skeleGrayIdle = g.newPixmap("images/skeleGray.png",PixmapFormat.ARGB4444);
        Assets.skeleYellowIdle = g.newPixmap("images/skeleYellow.png",PixmapFormat.ARGB4444);
        Assets.skeleDeath = g.newPixmap("images/skeleDeath.png",PixmapFormat.ARGB4444);

        //bullets
        Assets.bulletGreen = g.newPixmap("images/bulletGreen.png",PixmapFormat.ARGB4444);
        Assets.bulletGray = g.newPixmap("images/bulletGrey.png",PixmapFormat.ARGB4444);
        Assets.bulletYellow = g.newPixmap("images/bulletYellow.png",PixmapFormat.ARGB4444);

        //Game sounds
        Assets.menuClick = game.getAudio().newSound("sounds/menuSound.wav");
        Assets.swipeSound = game.getAudio().newSound("sounds/switchSound.wav");
        Assets.meleeWeaponHit = game.getAudio().newSound("sounds/charHit.wav");
        Assets.skeletonDeath = game.getAudio().newSound("sounds/skeletonDeath.wav");
        Assets.takingDamage = game.getAudio().newSound("sounds/takingDamage.ogg");
        Assets.getCoin = game.getAudio().newSound("sounds/coin.wav");
        Assets.gameOverSound = game.getAudio().newSound("sounds/gameOver.ogg");
        Assets.winSound = game.getAudio().newSound("sounds/winSound.wav");
        //Game musics
        Assets.intro = game.getAudio().newMusic("music/intro.wav");
        Assets.tutorial = game.getAudio().newMusic("music/campfireMusic.ogg");
        Assets.gameLoop = game.getAudio().newMusic("music/gameLoop.ogg");

        game.setScreen(new PRMainMenuScreen(game));
    }

    @Override
    public void present(float deltaTime) {

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