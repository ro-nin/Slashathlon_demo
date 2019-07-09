package com.EmGiuPa.androidgames.ProjectRunner;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.impl.AndroidGame;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.EmGiuPa.androidgames.ProjectRunner.PRGameScreen.frameBufferHeight;
import static com.EmGiuPa.androidgames.ProjectRunner.PRGameScreen.frameBufferWidth;

public class PRWorld {
    //GAME WORLD SETTINGS DATA
    static final int WORLD_WIDTH = 6;
    static final int WORLD_HEIGHT = 10;

    private static final float TICK_INITIAL = 0.1f;
    private static final float LANE_SWIPING_DELTA = .5f;
    static final int SWIPE_TRANSFORM_DURATION = 400;
    static final int HIT_ANIM_DURATION = 900;
    static final int ATTACK_ANIM_DURATION = 400;
    static final int RUN_ANIM_DURATION = 500;
    static final int DESTROYED_ANIM_DURATION = 300;
    static final int IDLE_BOSS_ANIM_DURATION = 1500;
    private static final float VISIBILITY_THRESHOLD = 0;
    static final int BULLET_IDLE_ANIM_DURATION = 300;
    private static final int CHARACTERS_ROW = 8;
    int CHARACTER_WIDTH;

    //background sliding settings
    static long backgroundTimer = 0;
    static int backgroundOffset = 0;
    static int backgroundSpeed = 5;
    static int screenRow = 0;

    private static int PlayerTotalLife = 3;
    //debug flag
    private static boolean debug = false;

    //world elements lists
    List<GameObject> characterList;
    List<GameObject> enemyList;
    List<GameObject> bonusList;
    List<GameObject> collectiblesList;
    List<GameObject> bulletList;
    //level manager
    private LevelManager lm;
    private GameObject characterB;
    private GameObject characterC;

    boolean gameOver = false;
    boolean levelCleared = false;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private float tickTime = 0;
    private static float descendingrate;
    private static float ascendingrate;
    private static float tick = TICK_INITIAL;

    protected int totalCollectiblesNumber;
    protected boolean noHit;

    protected GameObject boss;

    float swipeCounterLeft = 0;
    float swipeCounterRight = 0;
    public float leftCharLane = 1;
    public float rightCharLane = 3;


    /**
     * World constructor
     *
     * @param game the game instance we need to build the game world
     */
    PRWorld(Game game) {
        //create characters game objects
        createPlayingCharacters();

        //set player total life
        setLifeTotal(3);
        //level is loaded from a jSon file using the level manager class
        lm = game.getLevelManager();
        enemyList = lm.getEnemyList();
        bonusList = lm.getBonusList();
        collectiblesList = lm.getCollectibles();
        boss = lm.getBoss();
        bulletList = lm.getBulletList();

        sharedPreferences = game.getSharedPreferences();
        editor = game.getEditor();
        //flags used to store additional level clear condition met by the player
        totalCollectiblesNumber = collectiblesList.size();
        noHit = true;

        AndroidGame Ag = (AndroidGame) game;
        //resources manager
        Resources res = Ag.getResources();
        CHARACTER_WIDTH = res.getInteger(R.integer.cellSize);

        //enemy descending speed
        descendingrate = 0.4f; //enemies and bullets
        ascendingrate = 0.6f; //for bullets bouncing back
    }

    private void createPlayingCharacters() {
        //characters
        GameObject characterA = PRFactory.makeCharacter(1, CHARACTERS_ROW, ClassTypeEnum.A, Assets.bardRunning, Assets.bardAttack, Assets.bardHit, 3, 3, 3, 3);
        characterB = PRFactory.makeCharacter(3, CHARACTERS_ROW, ClassTypeEnum.B, Assets.knightRunning, Assets.knightAttack, Assets.knightHit, 3, 3, 3, 3);
        characterC = PRFactory.makeCharacter(5, CHARACTERS_ROW, ClassTypeEnum.C, Assets.mageRunning, Assets.mageAttack, Assets.mageHit, 3, 3, 3, 3);
        characterList = new LinkedList<GameObject>();
        characterList.add(characterA);
        characterList.add(characterB);
        characterList.add(characterC);
    }

    //Main Loop

    /**
     * main game loop
     *
     * @param deltaTime time between two consecutive update calls
     */
    public void update(float deltaTime) {
        if (gameOver)
            return;
        tickTime += deltaTime;
        while (tickTime > tick) {
            tickTime -= tick;
            //updates game objects state
            updateCharactersGameObjectsState(characterList);

            //updates the boss state
            updateBoss(boss);

            //updates boss's bullets state
            updateBulletGameObjects(bulletList);

            //updates game objects collision box
            updateNonPlayableGameObjects(enemyList);
            updateNonPlayableGameObjects(bonusList);
            updateNonPlayableGameObjects(collectiblesList);
        }
    }

    /**
     * Updates a character game object state, and it's collision box, according to game events
     *
     * @param golist character game objects list
     */


    private void updateCharactersGameObjectsState(List<GameObject> golist) {
        StateComponent stateComponent;
        PositionComponent pc;
        BoxColliderComponent boxColliderComponent;
        swipeCounterLeft += LANE_SWIPING_DELTA;
        swipeCounterRight += LANE_SWIPING_DELTA;
        for (GameObject go : golist) {
            pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            stateComponent = (StateComponent) go.getComponent(ComponentTypeEnum.STATE);
            boxColliderComponent = (BoxColliderComponent) go.getComponent(ComponentTypeEnum.BOXCOLLIDER);
            AnimatedComponent animatedComponent = (AnimatedComponent) go.getComponent(ComponentTypeEnum.ANIMATED);


            if (stateComponent.contains(ObjectStateEnum.SWIPING_LEFT)) {
                if (pc.getLane() <= leftCharLane) {
                    stateComponent.removeState(ObjectStateEnum.SWIPING_LEFT);
                    animatedComponent.setAnimationsTimeCounter(ObjectStateEnum.SWIPING_LEFT, 0);
                    pc.setLane(leftCharLane);
                    swipeCounterLeft = 0;
                } else {
                    float temp = pc.getLane();
                    pc.setLane(temp - LANE_SWIPING_DELTA);
                }
            }

            if (stateComponent.contains(ObjectStateEnum.SWIPING_RIGHT)) {
                if (pc.getLane() >= rightCharLane) {
                    stateComponent.removeState(ObjectStateEnum.SWIPING_RIGHT);
                    animatedComponent.setAnimationsTimeCounter(ObjectStateEnum.SWIPING_RIGHT, 0);
                    pc.setLane(rightCharLane);
                    swipeCounterRight = 0;
                } else {
                    float temp1 = pc.getLane();
                    pc.setLane(temp1 + LANE_SWIPING_DELTA);
                }
            }


            updateCollider(go, boxColliderComponent);
        }

    }

    /**
     * Updates a non character game object state, and it's collision box, according to game events
     *
     * @param golist non character game objects list
     */
    private void updateNonPlayableGameObjects(List<GameObject> golist) {
        PositionComponent pc;
        StateComponent sc;
        CharClassComponent charClassComponent;
        Iterator<GameObject> it = golist.iterator();
        while (it.hasNext()) {
            GameObject go = it.next();
            pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            sc = (StateComponent) go.getComponent(ComponentTypeEnum.STATE);
            charClassComponent = (CharClassComponent) go.getComponent(ComponentTypeEnum.CLASS);
            if (!sc.contains(ObjectStateEnum.DESTROYED)) {
                pc.setRow(pc.getRow() + descendingrate);
                go.addComponent(pc);
            }

            BoxColliderComponent bc = (BoxColliderComponent) go.getComponent(ComponentTypeEnum.BOXCOLLIDER);
            //update collisions box
            updateCollider(go, bc);
            ClassTypeEnum currentGoClass = charClassComponent.getCharClass();

            if (currentGoClass == ClassTypeEnum.A || currentGoClass == ClassTypeEnum.B || currentGoClass == ClassTypeEnum.C) {
                enemyCollisionDetection(bc, go, it);//Enemies
            } else if (currentGoClass == ClassTypeEnum.Life/* o altri bonus*/) {
                bonusCollisionDetection(bc, go, it);
            } else if (currentGoClass == ClassTypeEnum.RED_GEM/*o altri collezionabili*/) {
                collectiblesCollisionDetection(bc, go, it);
            }
            //if below screen visibility, destroy
            if (pc.getRow() > WORLD_HEIGHT) {
                it.remove();
            }
        }
    }

    /**
     * bullet hit the boss
     */
    private void bossHit() {
        if (PRSettings.getBooleanSetting(sharedPreferences, "sound"))
            Assets.meleeWeaponHit.play(1);
        HealthComponent bossHealth = (HealthComponent) boss.getComponent(ComponentTypeEnum.HEALTH);
        if (!debug) {
            int bossLifePoints = bossHealth.decreaseLife();
            if (bossLifePoints <= 0) {
                levelCleared = true;
            }
        }
    }

    /**
     * Updates a bullet game object state, its collision box and its moving direction according to game events
     *
     * @param golist bullet list
     */
    private void updateBulletGameObjects(List<GameObject> golist) {
        PositionComponent pc;
        StateComponent sc;
        Iterator<GameObject> it = golist.iterator();
        while (it.hasNext()) {
            GameObject go = it.next();
            pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            sc = (StateComponent) go.getComponent(ComponentTypeEnum.STATE);
            if (!sc.contains(ObjectStateEnum.HIT)) {
                pc.setRow(pc.getRow() + descendingrate);
                go.addComponent(pc);
            } else {
                pc.setRow(pc.getRow() - ascendingrate);
                go.addComponent(pc);
            }

            BoxColliderComponent bc = (BoxColliderComponent) go.getComponent(ComponentTypeEnum.BOXCOLLIDER);

            //update collisions box
            updateCollider(go, bc);
            bulletCollisionDetection(bc, go, it);

            //bullet bounced on player and hit the boss
            if (pc.getRow() <= 1 && pc.getRow() >= 0 && sc.contains(ObjectStateEnum.HIT)) {
                bossHit();
                it.remove();
            }
            //bullet below screen visibility or to be destroyed
            if (pc.getRow() > WORLD_HEIGHT || (pc.getRow() < 0 && sc.contains(ObjectStateEnum.HIT))) {
                it.remove();
            }
        }
    }

    /**
     * Updates boss game data according to game events
     *
     * @param go boss
     */
    private void updateBoss(GameObject go) {
        PositionComponent pc;
        pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);

        //move the boss only if it hasn't reached "the screen"
        if (pc.getRow() < VISIBILITY_THRESHOLD) {
            pc.setRow(pc.getRow() + descendingrate);
            go.addComponent(pc);
        }
    }

    /**
     * Manages collisions between characters and bullets game objects
     *
     * @param bc bullet collision box
     * @param go bullet game object
     * @param it current iterator in the bullet list
     */
    private void bulletCollisionDetection(BoxColliderComponent bc, GameObject go, Iterator<GameObject> it) {
        for (GameObject character : characterList) {
            PositionComponent pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            if (pc.getRow() >= 0 && pc.getRow() < WORLD_HEIGHT) {
                boolean collision = bc.checkCollision((BoxColliderComponent) character.getComponent(ComponentTypeEnum.BOXCOLLIDER));
                if (collision) {
                    CharClassComponent goClass = (CharClassComponent) go.getComponent(ComponentTypeEnum.CLASS);
                    CharClassComponent characterClass = (CharClassComponent) character.getComponent(ComponentTypeEnum.CLASS);
                    if (goClass.getCharClass() == characterClass.getCharClass()) {
                        //Correct Match
                        GameObject hitBullet = (GameObject) bc.getOwner();
                        StateComponent bulletStateComponent = (StateComponent) hitBullet.getComponent(ComponentTypeEnum.STATE);
                        bulletStateComponent.addState(ObjectStateEnum.HIT);
                    } else {
                        playerCollideWithEnemy(character, false);
                        it.remove();
                    }
                }
            }
        }
    }

    /**
     * Manages collisions between characters and bonus game objects
     *
     * @param bc bonus collision box
     * @param go bonus game object
     * @param it current iterator in the bonus list
     */
    private void bonusCollisionDetection(BoxColliderComponent bc, GameObject go, Iterator<GameObject> it) {
        for (GameObject character : characterList) {
            //broadphase collision
            PositionComponent pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            if (pc.getRow() >= 0 && pc.getRow() < WORLD_HEIGHT) {
                boolean collision = bc.checkCollision((BoxColliderComponent) character.getComponent(ComponentTypeEnum.BOXCOLLIDER));
                if (collision) {
                    it.remove();
                    if (PRSettings.getBooleanSetting(sharedPreferences, "sound"))
                        Assets.getCoin.play(1);
                    increaseLifeTotal();
                    return;
                }
            }
        }
    }

    /**
     * Manages collisions between characters and collectibles game objects
     *
     * @param bc collectible collision box
     * @param go collectible game object
     * @param it current iterator in the collectible list
     */
    private void collectiblesCollisionDetection(BoxColliderComponent bc, GameObject go, Iterator<GameObject> it) {
        for (GameObject character : characterList) {
            PositionComponent pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            if (pc.getRow() >= 0 && pc.getRow() < WORLD_HEIGHT) {
                boolean collision = bc.checkCollision((BoxColliderComponent) character.getComponent(ComponentTypeEnum.BOXCOLLIDER));
                if (collision) {
                    it.remove();
                    if (PRSettings.getBooleanSetting(sharedPreferences, "sound"))
                        Assets.getCoin.play(1);
                    totalCollectiblesNumber--;
                    return;
                }
            }
        }
    }

    /**
     * Manages collisions between enemy and collectibles game objects
     *
     * @param bc enemy collision box
     * @param go enemy game object
     * @param it current iterator in the enemy list
     */
    private void enemyCollisionDetection(BoxColliderComponent bc, GameObject go, Iterator<GameObject> it) {
        for (GameObject character : characterList) {
            //broadphase collision
            PositionComponent pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            if (pc.getRow() >= 0 && pc.getRow() < WORLD_HEIGHT) {
                boolean collision = bc.checkCollision((BoxColliderComponent) character.getComponent(ComponentTypeEnum.BOXCOLLIDER));
                if (collision) {

                    CharClassComponent goClass = (CharClassComponent) go.getComponent(ComponentTypeEnum.CLASS);
                    CharClassComponent characterClass = (CharClassComponent) character.getComponent(ComponentTypeEnum.CLASS);
                    StateComponent stateComponent = (StateComponent) character.getComponent(ComponentTypeEnum.STATE);

                    StateComponent toRemoveStateComponent = (StateComponent) go.getComponent(ComponentTypeEnum.STATE);

                    if (toRemoveStateComponent.contains(ObjectStateEnum.DESTROYED) && go.getToDestroy()) {
                        it.remove();
                    } else if (goClass.getCharClass() == characterClass.getCharClass()) {
                        //Correct Match
                        if (!toRemoveStateComponent.contains(ObjectStateEnum.DESTROYED) && !go.getToDestroy()) {
                            playerCollideWithEnemy(character, true);
                            AnimatedComponent toRemoveAnimatedComponent = (AnimatedComponent) go.getComponent(ComponentTypeEnum.ANIMATED);
                            toRemoveStateComponent.addState(ObjectStateEnum.DESTROYED);
                            toRemoveAnimatedComponent.setAnimationsTimeCounter(ObjectStateEnum.DESTROYED, System.currentTimeMillis());
                        }
                    } else if (!stateComponent.contains(ObjectStateEnum.HIT)) {//avoid re-hit for the same character
                        playerCollideWithEnemy(character, false);
                    }
                }
            }
        }
    }

    /**
     * @param go           character game object
     * @param correctMatch flag used to memorize the win or lost match
     */
    private void playerCollideWithEnemy(GameObject go, boolean correctMatch) {
        StateComponent stateComponent = (StateComponent) go.getComponent(ComponentTypeEnum.STATE);
        AnimatedComponent animatedComponent = (AnimatedComponent) go.getComponent(ComponentTypeEnum.ANIMATED);

        if (!correctMatch) {
            stateComponent.addState(ObjectStateEnum.HIT);
            if (PRSettings.getBooleanSetting(sharedPreferences, "sound"))
                Assets.takingDamage.play(1);
            decreaseLifeTotal();
            noHit = false;
            animatedComponent.setAnimationsTimeCounter(ObjectStateEnum.HIT, System.currentTimeMillis());
        } else {
            stateComponent.addState(ObjectStateEnum.ATTACKING);
            if (PRSettings.getBooleanSetting(sharedPreferences, "sound"))
                Assets.meleeWeaponHit.play(1);
            animatedComponent.setAnimationsTimeCounter(ObjectStateEnum.ATTACKING, System.currentTimeMillis());
        }
    }

    /**
     * Updates a game object box
     *
     * @param go game object
     * @param bc go collider box
     */
    private void updateCollider(GameObject go, BoxColliderComponent bc) {
        //if on screen
        PositionComponent pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
        if (pc.getRow() > 0 && pc.getRow() <= WORLD_HEIGHT) {
            float laneAdjusted = getCoordinateAdjusted(pc.getLane(), frameBufferWidth, PRWorld.WORLD_WIDTH);

            float rowAdjusted = getCoordinateAdjusted(pc.getRow(), frameBufferHeight, PRWorld.WORLD_HEIGHT);
            //update collisions box
            //+-1 to make the collider smaller than the logical grid cell
            bc.updateBoxCoordinates((int) laneAdjusted + 1, (int) rowAdjusted + 1, (int) laneAdjusted - 1 + frameBufferWidth / PRWorld.WORLD_WIDTH, (int) rowAdjusted - 1 + frameBufferHeight / PRWorld.WORLD_HEIGHT);
            //detect collisions with characters
        }
    }

    /**
     * converts logical grid coordinates to screen draw coordinates
     *
     * @param dimension            dimension axis (lane or row)
     * @param bufferTotalDimension dimension max length (frameBufferheight or frameBufferwidth according to dimension)
     * @param worldDimension       world height or width according to dimension
     * @return screen converted coordinate
     */
    static float getCoordinateAdjusted(float dimension, int bufferTotalDimension, int worldDimension) {
        float coord;
        coord = dimension * bufferTotalDimension / worldDimension;
        float ret = coord - bufferTotalDimension / worldDimension / 2;
        return ret;
    }

    /**
     * Get the character contained in a single lane
     *
     * @param lane passed lane
     * @return the detected character
     */
    GameObject getCharacterFromLane(int lane) {
        for (GameObject go : characterList) {
            if (lane == ((PositionComponent) go.getComponent(ComponentTypeEnum.POSITION)).getLane())
                return go;
        }
        return null;
    }


    private void increaseLifeTotal() {
        PlayerTotalLife++;
        if (PlayerTotalLife > 3)
            setLifeTotal(3);
    }

    private void decreaseLifeTotal() {
        if (!debug)
            PlayerTotalLife--;
    }

    private void setLifeTotal(int newLifeTotal) {
        PlayerTotalLife = newLifeTotal;
    }

    protected int getLifeTotal() {
        return PlayerTotalLife;
    }
}
