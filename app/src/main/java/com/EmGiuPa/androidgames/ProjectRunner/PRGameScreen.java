package com.EmGiuPa.androidgames.ProjectRunner;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.RegularSpritesheetAnimator;

import java.util.ArrayList;
import java.util.List;

public class PRGameScreen extends Screen {

    private static final int SWIPE_START_BOUND_TOLERANCE = 60;
    private static final int SWIPE_END_THRESHOLD = 5;
    public static final int BACKGROUND_SLIDE_TIME_OFFSET = 20;

    //Game states enumeration
    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver,
        Win,
        Quit
    }

    private RegularSpritesheetAnimator rg;
    private LevelManager lm;

    private GameState state;
    private PRWorld world;
    private TouchEvent startingSwipeTouchDown;

    private int startingX = -1;
    private int startingY = -1;
    static int frameBufferHeight;
    static int frameBufferWidth;

    private boolean swipingRight;
    private boolean swipingLeft;


    //background draw data
    private List<Pixmap> backgroudTiles;
    private Pixmap currentBackgroundTile;
    private Pixmap nextBackgroundTile;

    private int mLevel;

    /**
     * game screen constructor
     * @param game the passed game instance
     * @param level the selected level
     */
    PRGameScreen(Game game, int level) {
        super(game);
        frameBufferHeight = game.getGraphics().getHeight();
        frameBufferWidth = game.getGraphics().getWidth();

        //init level manager
        lm = game.getLevelManager();
        lm.loadLevel(level);
        //init game world
        world = new PRWorld(game);
        PRWorld.backgroundOffset = 0;
        PRWorld.screenRow = 0;
        //init class var

        //load background tiles
        backgroudTiles = new ArrayList<Pixmap>();
        if (level == 0) {
            backgroudTiles.add(Assets.grass0);
            backgroudTiles.add(Assets.grass1);
            backgroudTiles.add(Assets.terrain1);
        }
        if (level == 1) {
            backgroudTiles.add(Assets.darkTile0);
            backgroudTiles.add(Assets.darkTile1);
            backgroudTiles.add(Assets.darkTile2);
        }
        currentBackgroundTile = backgroudTiles.get(0);
        findNextBackgroundTile();
        rg = new RegularSpritesheetAnimator();

        mLevel = level;
        state = GameState.Ready;
        swipingRight = false;
        swipingLeft = false;
    }

    /**
     * find the next background tile to draw selected randomly from the tile list, except the current
     */
    private void findNextBackgroundTile() {
        Pixmap candidate;
        boolean found = false;
        while (!found) {
            candidate = backgroudTiles.get((int) Math.floor(Math.random() * (backgroudTiles.size())));
            if (candidate != this.currentBackgroundTile)
                found = true;
            this.nextBackgroundTile = candidate;
        }
    }

    /**
     * Draw the sliding background
     * @param currentBackgroundTile current tile to draw
     * @param nextBackgroundTile    next tile to draw contiguously
     * @param g                     graphic manager
     */
    private void drawBackground(Pixmap currentBackgroundTile, Pixmap nextBackgroundTile, Graphics g) {
        if (currentBackgroundTile.getHeight() - PRWorld.backgroundOffset <= frameBufferHeight) {
            int currentImagePixelsHeightToDraw = currentBackgroundTile.getHeight() - PRWorld.backgroundOffset;
            int nextImagePixelsHeightToDraw = frameBufferHeight - currentImagePixelsHeightToDraw;
            int nextImageStartHeightPixel = nextBackgroundTile.getHeight() - nextImagePixelsHeightToDraw;

            g.drawPixmap(nextBackgroundTile, 0, 0, frameBufferWidth, nextImagePixelsHeightToDraw, 0, nextImageStartHeightPixel, frameBufferWidth, nextImagePixelsHeightToDraw);
            g.drawPixmap(currentBackgroundTile,0,nextImagePixelsHeightToDraw,frameBufferWidth,currentImagePixelsHeightToDraw,0,0,frameBufferWidth,currentImagePixelsHeightToDraw);

        } else
            g.drawPixmap(currentBackgroundTile, 0, 0,frameBufferWidth,frameBufferHeight,0, currentBackgroundTile.getHeight() - PRWorld.backgroundOffset - frameBufferHeight, frameBufferWidth, frameBufferHeight);

        if (PRWorld.backgroundOffset >= currentBackgroundTile.getHeight()) {
            PRWorld.backgroundOffset = PRWorld.backgroundOffset % (currentBackgroundTile.getHeight());
            this.currentBackgroundTile = this.nextBackgroundTile;
            findNextBackgroundTile();
        }
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        if (state == GameState.Ready)
            updateReady(touchEvents);
        if (state == GameState.Running) {
            //sound management
            if (PRSettings.getBooleanSetting(game.getSharedPreferences(), "sound")) {
                Assets.gameLoop.play();
                if (!Assets.gameLoop.isLooping())
                    Assets.gameLoop.setLooping(true);
            }
            updateRunning(touchEvents, deltaTime);
        }
        if (state == GameState.Paused) {
            updatePaused(touchEvents);
            Assets.gameLoop.pause();
        }
        if (state == GameState.GameOver) {
            Assets.gameLoop.stop();
            updateGameOver(touchEvents);
        }
        if (state == GameState.Win) {
            Assets.gameLoop.stop();
            updateWin(touchEvents);
        }
        if (state == GameState.Quit) {
            Assets.gameLoop.stop();
            updateQuit();
        }
    }

    /**
     * updates game state according to touch events in the ready state
     * @param touchEvents list of captured touch events
     */
    private void updateReady(List<TouchEvent> touchEvents) {
        if (touchEvents.size() > 0)
            state = GameState.Running;
    }


    /**
     * updates game state according to touch events in the running state
     * @param touchEvents list of captured touch events
     * @param deltaTime passed tipe steps
     */
    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
        int len = touchEvents.size();

        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x < 64 && event.y > 430) {
                    if (PRSettings.getBooleanSetting(game.getSharedPreferences(), "sound"))
                        Assets.menuClick.play(1);
                    state = GameState.Paused;
                    return;
                }
            }

            if (event.type == TouchEvent.TOUCH_DRAGGED && (swipingRight || swipingLeft)) {
                if (event.x > startingX && swipingRight) {
                    startingX = event.x;
                    startingY = event.y;

                    if (event.x > startingSwipeTouchDown.x + SWIPE_END_THRESHOLD) {

                        makeSwipe(startingSwipeTouchDown.x, ObjectStateEnum.SWIPING_RIGHT);
                        swipingLeft = false;
                        swipingRight = false;

                    }
                } else if (event.x < startingX && swipingLeft) {
                    startingX = event.x;
                    startingY = event.y;

                    if (event.x + SWIPE_END_THRESHOLD < startingSwipeTouchDown.x) {
                        //swipeleft

                        makeSwipe(startingSwipeTouchDown.x, ObjectStateEnum.SWIPING_LEFT);
                        swipingLeft = false;
                        swipingRight = false;
                    }
                } else {
                    swipingLeft = false;
                    swipingRight = false;
                }

            }
            //Swipe gesture detection
            if ((event.type == TouchEvent.TOUCH_DOWN)||(event.type == TouchEvent.TOUCH_DRAGGED) && (!swipingRight && !swipingLeft)) {

                int touchedLane = getLaneFromScreenCord(event.x);
                GameObject touchedCharacter = world.getCharacterFromLane(touchedLane);
                if(touchedCharacter!=null) {
                    PositionComponent touchedCharacterPosComp = (PositionComponent) touchedCharacter.getComponent(ComponentTypeEnum.POSITION);

                    //bY useful only if you wanna constrain the vertical axis in swiping gesture
                    //int bY = (int) (touchedCharacterPosComp.getRow() * frameBufferHeight / PRWorld.WORLD_HEIGHT) - frameBufferHeight / PRWorld.WORLD_HEIGHT / 2;
                    int bX = (int) (touchedCharacterPosComp.getLane() * frameBufferWidth / PRWorld.WORLD_WIDTH) - frameBufferWidth / PRWorld.WORLD_WIDTH / 2;

                    if (event.x >= bX - SWIPE_START_BOUND_TOLERANCE && event.x <= bX + world.CHARACTER_WIDTH + SWIPE_START_BOUND_TOLERANCE) {
                        startingSwipeTouchDown = event;
                        startingX = event.x;
                        startingY = event.y;
                        swipingRight = true;
                        swipingLeft = true;
                    }
                }
            }
        }

        //if the player lost
        if (world.getLifeTotal() <= 0) {
            world.gameOver = true;
        }

        //updates game loop
        world.update(deltaTime);
        if (world.gameOver) {//lost
            if (PRSettings.getBooleanSetting(game.getSharedPreferences(), "sound"))
                Assets.gameOverSound.play(1);
            state = GameState.GameOver;
        }
        if (world.levelCleared) {//win
            if (PRSettings.getBooleanSetting(game.getSharedPreferences(), "sound"))
                Assets.winSound.play(1);
            state = GameState.Win;
        }
    }

    /**
     * swap the position of a character and the next on the passed direction
     * @param lastX character position
     * @param direction swipe direction
     */
    private void makeSwipe(int lastX, ObjectStateEnum direction) {
        int startLane;
        int endLane;
        startLane = getLaneFromScreenCord(lastX);
        if (direction == ObjectStateEnum.SWIPING_LEFT)
            endLane = startLane - 2;
        else
            endLane = startLane + 2;

        GameObject charStart = world.getCharacterFromLane(startLane);
        GameObject charEnd = world.getCharacterFromLane(endLane);
        if (charStart != null && charEnd != null) {
            StateComponent stateComponentCharStart = (StateComponent) charStart.getComponent(ComponentTypeEnum.STATE);
            StateComponent stateComponentCharEnd = (StateComponent) charEnd.getComponent(ComponentTypeEnum.STATE);
            if (canSwipe(stateComponentCharStart) && canSwipe(stateComponentCharEnd)) {
                if (PRSettings.getBooleanSetting(game.getSharedPreferences(), "sound"))
                    Assets.swipeSound.play(2);
                //set the swiping state for both characters and update their state time
                stateComponentCharStart.addState(direction);
                stateComponentCharEnd.addState(getOppositeDirection(direction));
                if (startLane > endLane) {
                    world.leftCharLane = endLane;
                    world.rightCharLane = startLane;
                } else {
                    world.leftCharLane = startLane;
                    world.rightCharLane = endLane;
                }
            }
        }
    }

    /**
     * Returns the opposite direction to the one passed
     * @param direction passed direction
     * @return opposite direction
     */
    private ObjectStateEnum getOppositeDirection(ObjectStateEnum direction) {
        if (direction == ObjectStateEnum.SWIPING_RIGHT)
            return ObjectStateEnum.SWIPING_LEFT;
        else
            return ObjectStateEnum.SWIPING_RIGHT;
    }

    /**
     * checks if the state of a game object is compatible with a swiping animation
     * @param objState set of current states for a game object
     * @return boolean
     */
    private boolean canSwipe(StateComponent objState) {
        boolean b = !objState.contains(ObjectStateEnum.SWIPING_LEFT) && !objState.contains(ObjectStateEnum.SWIPING_RIGHT) &&
                !objState.contains(ObjectStateEnum.ATTACKING);
        return b;
    }

    /**
     * updates game state according to touch events in the paused state
     * @param touchEvents list of captured touch events
     */
    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x > 80 && event.x <= 240) {
                    if (event.y > 100 && event.y <= 148) {
                        if (PRSettings.getBooleanSetting(game.getSharedPreferences(), "sound"))
                            Assets.menuClick.play(1);
                        state = GameState.Running;
                        return;
                    }
                    if (event.y > 170 && event.y < 230) {
                        if (PRSettings.getBooleanSetting(game.getSharedPreferences(), "sound"))
                            Assets.menuClick.play(1);
                        state = GameState.Quit;
                        return;
                    }
                }
            }
        }
    }

    /**
     * updates game state according to touch events in the game over state
     * @param touchEvents list of captured touch events
     */
    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x >= 128 && event.x <= 192 &&
                        event.y >= 400 && event.y <= 464) {
                    if (PRSettings.getBooleanSetting(game.getSharedPreferences(), "sound"))
                        Assets.menuClick.play(1);
                    state = GameState.Quit;
                    return;
                }
            }
        }
    }

    /**
     * updates game state according to touch events in the win state
     * @param touchEvents list of captured touch events
     */
    private void updateWin(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for (int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if (event.type == TouchEvent.TOUCH_UP) {
                if (event.x >= 128 && event.x <= 192 &&
                        event.y >= 200 && event.y <= 264) {
                    if (PRSettings.getBooleanSetting(game.getSharedPreferences(), "sound"))
                        Assets.menuClick.play(1);
                    lm.setLevelPreferences(mLevel, true, world.totalCollectiblesNumber, world.noHit);
                    state = GameState.Quit;
                    return;
                }
            }
        }
    }

    /**
     * Updates game date in the quit state
     */
    private void updateQuit() {
        lm.clear();
        game.setScreen(new PRMainMenuScreen(game));
    }

    /**
     * Call the appropriate draw method
     *
     * @param deltaTime
     */
    @Override
    public void present(float deltaTime) {

        Graphics g = game.getGraphics();
        g.clear(1);
        if (PRWorld.screenRow == 0) {
            g.drawPixmap(Assets.testBig, 0, 0);
        }
        drawWorld(world);
        if (state == GameState.Ready)
            drawReadyUI();
        if (state == GameState.Running)
            drawRunningUI();
        if (state == GameState.Paused)
            drawPausedUI();
        if (state == GameState.GameOver)
            drawGameOverUI();
        if (state == GameState.Win)
            drawWinUI();
    }

    /**
     * Draws state indipendent game elements
     * @param world game world data
     */
    private void drawWorld(PRWorld world) {
        Graphics g = game.getGraphics();
        if (PRWorld.backgroundTimer == 0) {
            PRWorld.backgroundTimer = System.currentTimeMillis();
        }

        //Draw sliding background screen
        drawBackground(currentBackgroundTile, nextBackgroundTile, g);

        for (GameObject go : world.enemyList) {
            finalizeNonCharacterAnimation(go);
            animateNonCharacterGameObject(go, world.CHARACTER_WIDTH, world.CHARACTER_WIDTH);
        }

        //Game Objects Animations
        for (GameObject go : world.characterList) {
            animateCharacterGameObject(go);
            finalizeCharacterAnimation(go);
        }

        animateNonCharacterGameObject(world.boss, 256, 128);

        //Draws
        //animate bonuses
        for (GameObject go : world.bonusList) {
            finalizeNonCharacterAnimation(go);
            animateNonCharacterGameObject(go, world.CHARACTER_WIDTH, world.CHARACTER_WIDTH);
        }
        //animate collectibles
        for (GameObject go : world.collectiblesList) {
            finalizeNonCharacterAnimation(go);
            animateNonCharacterGameObject(go, world.CHARACTER_WIDTH, world.CHARACTER_WIDTH);
        }
        //animate bullets
        for (GameObject go : world.bulletList) {
            PositionComponent pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            if (pc.getRow() > 1) {
                finalizeNonCharacterAnimation(go);
                animateNonCharacterGameObject(go, world.CHARACTER_WIDTH, world.CHARACTER_WIDTH);
            }
        }

        //bottom screen wood background
        g.drawPixmapResized(Assets.bottomScreenBackground, 320, 70, 0, 410);
        drawLifeTotal(g);
    }

    /**
     * Draws player life on the bottom of the screen
     * @param g graphic draw manager
     */
    private void drawLifeTotal(Graphics g) {
        int x1 = 140;
        int x2 = 190;
        int x3 = 240;
        int y = 410;
        if (world.getLifeTotal() >= 3) {
            g.drawPixmapResized(Assets.lifeSprite, 70, 70, x1, y);
            g.drawPixmapResized(Assets.lifeSprite, 70, 70, x2, y);
            g.drawPixmapResized(Assets.lifeSprite, 70, 70, x3, y);
        } else if (world.getLifeTotal() == 2) {
            g.drawPixmapResized(Assets.lifeSprite, 70, 70, x1, y);
            g.drawPixmapResized(Assets.lifeSprite, 70, 70, x2, y);
            g.drawPixmapResized(Assets.lifeSpriteEmpty, 70, 70, x3, y);
        } else if (world.getLifeTotal() == 1) {
            g.drawPixmapResized(Assets.lifeSprite, 70, 70, x1, y);
            g.drawPixmapResized(Assets.lifeSpriteEmpty, 70, 70, x2, y);
            g.drawPixmapResized(Assets.lifeSpriteEmpty, 70, 70, x3, y);
        } else if (world.getLifeTotal() <= 0) {
            g.drawPixmapResized(Assets.lifeSpriteEmpty, 70, 70, x1, 420);
            g.drawPixmapResized(Assets.lifeSpriteEmpty, 70, 70, x2, 420);
            g.drawPixmapResized(Assets.lifeSpriteEmpty, 70, 70, x3, 420);
        }
    }

    /**
     * Draws UI elements in the ready state
     */
    private void drawReadyUI() {
        Graphics g = game.getGraphics();

        g.drawPixmapResized(Assets.ready, 300, 100, 10, 50);
    }

    /**
     * Draws UI elements in the running state
     */
    private void drawRunningUI() {
        Graphics g = game.getGraphics();
        //increments background offset every certain time
        if (PRWorld.backgroundTimer + BACKGROUND_SLIDE_TIME_OFFSET < System.currentTimeMillis()) {
            PRWorld.backgroundOffset = PRWorld.backgroundOffset + PRWorld.backgroundSpeed;
            PRWorld.backgroundTimer = 0;
        }
        //draw pause button
        g.drawPixmapResized(Assets.buttonPause, 40, 40, 20, 425);
    }

    /**
     * Draws UI elements in the running state
     */
    private void drawPausedUI() {
        Graphics g = game.getGraphics();

        g.drawPixmapResized(Assets.pause, 150, 120, 80, 100);
    }

    /**
     * Draws UI elements in the game over state
     */
    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        g.clear(0);
        g.drawPixmapResized(Assets.gameOver, 300, 70, 10, 100);
        g.drawPixmapResized(Assets.bardGameOver, 100, 100, 120, 250);
        g.drawPixmapResized(Assets.backToMain, 120, 65, 100, 400);
    }

    /**
     * Draws UI elements in the win state
     */
    private void drawWinUI() {
        Graphics g = game.getGraphics();
        g.drawPixmapResized(Assets.gameWin, 300, 100, 10, 50);
        g.drawPixmapResized(Assets.backToMain, 120, 65, 90, 200);
    }

    /**
     * draw a static drawable component from a gameObject using the graphics g
     * @param g  graphic draw manager
     * @param go a single game object
     */
    private void drawGameObject(Graphics g, GameObject go) {
        DrawableComponent drawableComponent = (DrawableComponent) go.getComponent(ComponentTypeEnum.DRAWABLE);
        PositionComponent positionComponent = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
        BoxColliderComponent boxColliderComponent = (BoxColliderComponent) go.getComponent(ComponentTypeEnum.BOXCOLLIDER);

        if (positionComponent.getRow() >= 0 && positionComponent.getRow() < PRWorld.WORLD_HEIGHT) {

            float laneAdjusted =  PRWorld.getCoordinateAdjusted(positionComponent.getLane(),frameBufferWidth,PRWorld.WORLD_WIDTH);

            float rowAdjusted =  PRWorld.getCoordinateAdjusted(positionComponent.getRow(),frameBufferHeight,PRWorld.WORLD_HEIGHT);
            //draw characters
            drawableComponent.drawResized(g, laneAdjusted, rowAdjusted,
                    frameBufferWidth / PRWorld.WORLD_WIDTH,
                    frameBufferHeight / PRWorld.WORLD_HEIGHT);

            CharClassComponent charClassComponent = (CharClassComponent) go.getComponent(ComponentTypeEnum.CLASS);
            int boxColor = getBoxColorFromClass(charClassComponent);
            g.drawRect(boxColliderComponent.getTopLeftX(), boxColliderComponent.getTopLeftY(), boxColliderComponent.getBottomRightX() - boxColliderComponent.getTopLeftX(),
                    boxColliderComponent.getBottomRightY() - boxColliderComponent.getTopLeftY(), boxColor);
        }
    }

    /**
     * animates a gameObject using the graphics g and the animatedComponent of that object
     * @param go a single game object
     */
    private void animateCharacterGameObject(GameObject go) {
        AnimatedComponent animatedComponent = (AnimatedComponent) go.getComponent(ComponentTypeEnum.ANIMATED);
        PositionComponent positionComponent = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
        StateComponent stateComponent = (StateComponent) go.getComponent(ComponentTypeEnum.STATE);

        ObjectStateEnum stateToDraw = ObjectStateEnum.RUNNING;

        //sets animations to draw according to a certain priority
        if (stateComponent.contains(ObjectStateEnum.RUNNING))
            stateToDraw = ObjectStateEnum.RUNNING;
        if (stateComponent.contains(ObjectStateEnum.HIT))
            stateToDraw = ObjectStateEnum.HIT;
        if (stateComponent.contains(ObjectStateEnum.ATTACKING))
            stateToDraw = ObjectStateEnum.ATTACKING;
        //calls animator
        rg.animate(game.getGraphics(),
                animatedComponent,
                stateToDraw,
                (int)  PRWorld.getCoordinateAdjusted(positionComponent.getLane(),frameBufferWidth,PRWorld.WORLD_WIDTH),
                (int) PRWorld.getCoordinateAdjusted(positionComponent.getRow(),frameBufferHeight,PRWorld.WORLD_HEIGHT),
                world.CHARACTER_WIDTH,world.CHARACTER_WIDTH);
    }

    /**
     * draw a gameObject using the graphics g
     * @param go a single game object
     */
    private void animateNonCharacterGameObject(GameObject go, int widthToDraw, int lengthToDraw) {
        AnimatedComponent animatedComponent = (AnimatedComponent) go.getComponent(ComponentTypeEnum.ANIMATED);
        PositionComponent positionComponent = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
        StateComponent stateComponent = (StateComponent) go.getComponent(ComponentTypeEnum.STATE);

        ObjectStateEnum stateToDraw = ObjectStateEnum.IDLE;

        //sets animations to draw according to a certain priority
        if (stateComponent.contains(ObjectStateEnum.IDLE))
            stateToDraw = ObjectStateEnum.IDLE;
        if (stateComponent.contains(ObjectStateEnum.DESTROYED))
            stateToDraw = ObjectStateEnum.DESTROYED;

        rg.animate(game.getGraphics(),
                animatedComponent,
                stateToDraw, (int) PRWorld.getCoordinateAdjusted(positionComponent.getLane(), frameBufferWidth, PRWorld.WORLD_WIDTH),
                (int) PRWorld.getCoordinateAdjusted(positionComponent.getRow(), frameBufferHeight, PRWorld.WORLD_HEIGHT),
                widthToDraw,
                lengthToDraw);
    }

    /**
     * get the color assigned to a specific character class (DEBUG purpose)
     * @param charClassComponent component containint a game object compatibility class
     * @return integer associated to the class color
     */
    private int getBoxColorFromClass(CharClassComponent charClassComponent) {
        int boxColor = Color.WHITE;

        switch (charClassComponent.getCharClass()) {
            case A:
                boxColor = Color.RED;
                break;
            case B:
                boxColor = Color.GREEN;
                break;
            case C:
                boxColor = Color.BLUE;
                break;
            case Life:
                boxColor = Color.CYAN;
                break;
            case RED_GEM:
                boxColor = Color.MAGENTA;
                break;
        }
        return boxColor;
    }

    /**
     * manages animations and state changes for character game-objects, according to currently active states
     * @param go game object whose animation must be finalized
     */
    private void finalizeCharacterAnimation(GameObject go) {
        PositionComponent positionComponent = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
        StateComponent stateComponent = ((StateComponent) go.getComponent(ComponentTypeEnum.STATE));
        AnimatedComponent animatedComponent = (AnimatedComponent) go.getComponent(ComponentTypeEnum.ANIMATED);
        if (stateComponent.contains(ObjectStateEnum.HIT)) {
            if (System.currentTimeMillis() > animatedComponent.getAnimationTimeCounter(ObjectStateEnum.HIT) + animatedComponent.getDuration(ObjectStateEnum.HIT)) {
                //remove state from currents states map
                stateComponent.removeState(ObjectStateEnum.HIT);
                animatedComponent.setAnimationsTimeCounter(ObjectStateEnum.HIT, 0);
            }
        }
        if (stateComponent.contains(ObjectStateEnum.ATTACKING)) {
            if (System.currentTimeMillis() > animatedComponent.getAnimationTimeCounter(ObjectStateEnum.ATTACKING) + animatedComponent.getDuration(ObjectStateEnum.ATTACKING)) {
                //remove state from currents states map
                stateComponent.removeState(ObjectStateEnum.ATTACKING);
                animatedComponent.setAnimationsTimeCounter(ObjectStateEnum.ATTACKING, 0);
            }
        }
    }

    /**
     * manages animations and state changes for character game-objects, according to currently active states
     * @param go game object whose animation must be finalized
     */
    private void finalizeNonCharacterAnimation(GameObject go) {
        StateComponent stateComponent = ((StateComponent) go.getComponent(ComponentTypeEnum.STATE));
        AnimatedComponent animatedComponent = (AnimatedComponent) go.getComponent(ComponentTypeEnum.ANIMATED);

        if (stateComponent.contains(ObjectStateEnum.DESTROYED)) {
            if (System.currentTimeMillis() > animatedComponent.getAnimationTimeCounter(ObjectStateEnum.DESTROYED) + animatedComponent.getDuration(ObjectStateEnum.DESTROYED)) {
                //remove state from currents states map
                animatedComponent.setAnimationsTimeCounter(ObjectStateEnum.DESTROYED, 0);
                go.setToDestroy(true);
            }
        }
    }

    @Override
    public void pause() {
        Assets.gameLoop.stop();
        if (state == GameState.Running)
            state = GameState.Paused;
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    /**
     * Conversion to discrete coordinate between 1 and 5, used for lanes
     * @param x float that must be converted in and integer between 1 and 5
     * @return discrete x lane coordinate (nearest integer to x between 1 and 5)
     */
    public int continueToDiscrete(float x) {
        if (x < 1)
            return 1;
        else if (x > 5)
            return 5;
        else
            return Math.round(x);
    }

    /**
     * returns the index of the logical grid screen lane in which is contained a value
     * @param x value we want to locate in a lane
     * @return lane index
     */
    private int getLaneFromScreenCord(int x) {
        float aXstart = 28;
        float aXend = 81;
        float bXstart = 143;
        float bXend = 187;
        float cXstart = 245;
        float cXend = 290;
        if (x > aXstart && x < aXend)
            return 1;
        if (x > bXstart && x < bXend)
            return 3;
        if (x > cXstart && x < cXend)
            return 5;
        return 0;
    }

}