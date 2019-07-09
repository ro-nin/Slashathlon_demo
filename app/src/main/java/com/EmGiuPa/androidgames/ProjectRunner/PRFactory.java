package com.EmGiuPa.androidgames.ProjectRunner;

import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.impl.AndroidPixmap;
import com.badlogic.androidgames.framework.impl.RegularSpritesheet;

import static com.EmGiuPa.androidgames.ProjectRunner.PRGameScreen.frameBufferHeight;
import static com.EmGiuPa.androidgames.ProjectRunner.PRGameScreen.frameBufferWidth;

class PRFactory {
    /**
     * Factory method use to generate a character game object
     * @param lane lane of character creation
     * @param row row of character creation
     * @param charClass character class
     * @param running character running animation spritesheet
     * @param attacking character attacking animation spritesheet
     * @param hit character hit animation spritesheet
     * @param numOfFramesAttack character attack animation number of frame
     * @param numOfFramesSwipe character swipe animation number of frame
     * @param numOfFramesRun character run animation number of frame
     * @param numOfFramesHit character hit animation number of frame
     * @return the character game object created
     */
    static GameObject makeCharacter(int lane, int row, ClassTypeEnum charClass, Pixmap running, Pixmap attacking, Pixmap hit, int numOfFramesRun,
                                    int numOfFramesSwipe, int numOfFramesAttack, int numOfFramesHit) {
        GameObject go = new GameObject();
        go.addComponent(new PositionComponent(lane, row)); //row = 0 placeholder / don't use

        //Animator component
        AnimatedComponent animatedComponent = new AnimatedComponent();

        //spritesheets
        RegularSpritesheet runSprite = new RegularSpritesheet((AndroidPixmap) running, running.getWidth() / numOfFramesRun, running.getHeight());
        RegularSpritesheet swipeLeftSprite = new RegularSpritesheet((AndroidPixmap) running, running.getWidth() / numOfFramesSwipe, running.getHeight());
        RegularSpritesheet swipeRightSprite = new RegularSpritesheet((AndroidPixmap) running, running.getWidth() / numOfFramesSwipe, running.getHeight());
        RegularSpritesheet attackingSprite = new RegularSpritesheet((AndroidPixmap) attacking, attacking.getWidth() / numOfFramesAttack, attacking.getHeight());
        RegularSpritesheet hitSprite = new RegularSpritesheet((AndroidPixmap) hit, hit.getWidth() / numOfFramesHit, hit.getHeight());

        //Animations
        animatedComponent.addAnimation(ObjectStateEnum.RUNNING, runSprite, PRWorld.RUN_ANIM_DURATION);
        animatedComponent.addAnimation(ObjectStateEnum.SWIPING_LEFT, swipeLeftSprite, PRWorld.SWIPE_TRANSFORM_DURATION);
        animatedComponent.addAnimation(ObjectStateEnum.SWIPING_RIGHT, swipeRightSprite, PRWorld.SWIPE_TRANSFORM_DURATION);
        animatedComponent.addAnimation(ObjectStateEnum.ATTACKING, attackingSprite, PRWorld.ATTACK_ANIM_DURATION);
        animatedComponent.addAnimation(ObjectStateEnum.HIT, hitSprite, PRWorld.HIT_ANIM_DURATION);
        go.addComponent(animatedComponent);

        //State component
        StateComponent sc = new StateComponent();
        sc.addState(ObjectStateEnum.RUNNING);
        go.addComponent(sc);

        //create character bounding box
        addBoxCollider(go, lane, row);

        CharClassComponent charClassComponent = new CharClassComponent(charClass);
        go.addComponent(charClassComponent);
        return go;
    }

    /**
     * Factory method use to generate an enemy game object
     * @param lane the starting lane of the enemy
     * @param row the starting row of the enemy
     * @param pixmap the enemy drawable single image
     * @param charClass enemy compatibility class
     * @param idleEnemy enemy idle animation spritesheet
     * @param destroyEnemy enemy destroyed animation spritesheet
     * @param numOfFramesIdles idle animation frame duration
     * @param numofFramesDestroy destroyed animation frame duration
     * @return the created enemy game object
     */
     static GameObject makeEnemy(int lane, int row, Pixmap pixmap, ClassTypeEnum charClass, Pixmap idleEnemy, Pixmap destroyEnemy,int numOfFramesIdles,int numofFramesDestroy) {
        GameObject go = new GameObject(); // oppure prelevare da un pool
        go.addComponent(new PositionComponent(lane, row)); //row = 0 placeholder / don't use
        go.addComponent(new DrawableComponent(pixmap));
        //Animator component
        AnimatedComponent animatedComponent = new AnimatedComponent();

        AndroidPixmap idle = (AndroidPixmap) idleEnemy;
        AndroidPixmap destroy = (AndroidPixmap) destroyEnemy;

        //spritesheets
        RegularSpritesheet idleSprite = new RegularSpritesheet(idle,idle.getWidth()/numOfFramesIdles,idle.getHeight());
        RegularSpritesheet destroySprite = new RegularSpritesheet(destroy,destroy.getWidth()/numofFramesDestroy,destroy.getHeight());

        //Animations
        animatedComponent.addAnimation(ObjectStateEnum.IDLE, idleSprite, PRWorld.RUN_ANIM_DURATION);
        animatedComponent.addAnimation(ObjectStateEnum.DESTROYED, destroySprite, PRWorld.DESTROYED_ANIM_DURATION);
        go.addComponent(animatedComponent);

        //State component
        StateComponent sc = new StateComponent();
        sc.addState(ObjectStateEnum.IDLE);
        go.addComponent(sc);
        //if the enemy is visible calculate its box
        if (row >= 0) {
            addBoxCollider(go, lane, row);
        } else if (row < 0) {
            addBoxCollider(go);
        }
        CharClassComponent charClassComponent = new CharClassComponent(charClass);
        go.addComponent(charClassComponent);
        return go;
    }

    /**
     * Factory method use to generate a bullet game object
     * @param lane starting bullet lane
     * @param row starting bullet row
     * @param charClass bullet compatibility class
     * @param idleBullet idle bullet animation
     * @param numOfFrames idle bullet animation frame duration
     * @return the created bullet game object
     */
     static GameObject makeBullet(int lane, int row, ClassTypeEnum charClass, Pixmap idleBullet, int numOfFrames ) {
        GameObject go = new GameObject(); // oppure prelevare da un pool
        go.addComponent(new PositionComponent(lane, row)); //row = 0 placeholder / don't use

        //Animator component
        AnimatedComponent animatedComponent = new AnimatedComponent();

        AndroidPixmap idle = (AndroidPixmap) idleBullet;

        //spritesheets
        RegularSpritesheet idleSprite = new RegularSpritesheet(idle,idle.getWidth()/numOfFrames,idle.getHeight());

        //Animations
        animatedComponent.addAnimation(ObjectStateEnum.IDLE, idleSprite, PRWorld.BULLET_IDLE_ANIM_DURATION);
        go.addComponent(animatedComponent);
        animatedComponent.addAnimation(ObjectStateEnum.HIT, idleSprite, PRWorld.BULLET_IDLE_ANIM_DURATION);
        go.addComponent(animatedComponent);

        //State component
        StateComponent sc = new StateComponent();
        sc.addState(ObjectStateEnum.IDLE);/**/
        go.addComponent(sc);
        //if the enemy is visible calculate its box
        if (row >= 0) {
            addBoxCollider(go, lane, row);
        } else {
            addBoxCollider(go);
        }
        CharClassComponent charClassComponent = new CharClassComponent(charClass);
        go.addComponent(charClassComponent);
        return go;
    }


    /**
     * creates a boss game object
     * @param row starting boss row
     * @param idleEnemy idle boss animation spritesheet
     * @param attackSprite attacking boss animation spritesheet
     * @param deathSprite boss death animation spritesheet
     * @param healthNumber boss health
     * @param numOfFramesIdle idle animation frame duration
     * @param numOfFramesAttack attacking animation frame duration
     * @return the created boss game object
     */
     static GameObject makeBoss(int row, Pixmap idleEnemy, Pixmap attackSprite, Pixmap deathSprite, int healthNumber,int numOfFramesIdle,int numOfFramesAttack) {
        GameObject go = new GameObject(); // oppure prelevare da un pool
        go.addComponent(new PositionComponent(2, row));

        HealthComponent health = new HealthComponent(healthNumber);
        go.addComponent(health);

        //Animator component
        AnimatedComponent animatedComponent = new AnimatedComponent();

        //spritesheets
        RegularSpritesheet idleSprite = new RegularSpritesheet((AndroidPixmap) idleEnemy, idleEnemy.getWidth() / numOfFramesIdle, idleEnemy.getHeight());
        RegularSpritesheet bossAttack = new RegularSpritesheet((AndroidPixmap) attackSprite, attackSprite.getWidth() / numOfFramesAttack, attackSprite.getHeight());
        RegularSpritesheet bossDeath = new RegularSpritesheet((AndroidPixmap) deathSprite, idleEnemy.getWidth() / numOfFramesIdle, idleEnemy.getHeight());

        //Animations
        animatedComponent.addAnimation(ObjectStateEnum.IDLE, idleSprite, PRWorld.IDLE_BOSS_ANIM_DURATION);
        animatedComponent.addAnimation(ObjectStateEnum.ATTACKING, bossAttack, PRWorld.ATTACK_ANIM_DURATION);
        animatedComponent.addAnimation(ObjectStateEnum.DESTROYED, idleSprite, PRWorld.DESTROYED_ANIM_DURATION);
        go.addComponent(animatedComponent);

        //State component
        StateComponent sc = new StateComponent();
        sc.addState(ObjectStateEnum.IDLE);
        go.addComponent(sc);
        //if the enemy is visible calculate it's box
        if (row >= 0) {
            addBoxCollider(go, 3, row);
        } else {
            addBoxCollider(go);
        }
        return go;
    }

    /**
     * creates a bonus game object
     * @param lane starting bonus lane
     * @param row starting bonus row
     * @param pixmap bonus static image
     * @param charClass bonus compatibility class
     * @param idleBonus idle bonus spritesheet animation
     * @param tookBonus took bonus spritesheet animation
     * @param frameNumber bonus animation frame durations
     * @return the bonus created game object
     */
     static GameObject makeBonus(int lane, int row, Pixmap pixmap, ClassTypeEnum charClass, Pixmap idleBonus, Pixmap tookBonus, int frameNumber){
        GameObject go = new GameObject(); // oppure prelevare da un pool
        go.addComponent(new PositionComponent(lane, row)); //row = 0 placeholder / don't use
        go.addComponent(new DrawableComponent(pixmap));
        //Animator component
        AnimatedComponent animatedComponent = new AnimatedComponent();

        AndroidPixmap idle = (AndroidPixmap) idleBonus;
        AndroidPixmap took = (AndroidPixmap) tookBonus;

        //spritesheets
        RegularSpritesheet idleSprite = new RegularSpritesheet(idle,idle.getWidth()/frameNumber,idle.getHeight());
        RegularSpritesheet destroySprite = new RegularSpritesheet(took,took.getWidth()/frameNumber,took.getHeight());

        //Animations
        animatedComponent.addAnimation(ObjectStateEnum.IDLE, idleSprite, PRWorld.RUN_ANIM_DURATION);
        animatedComponent.addAnimation(ObjectStateEnum.DESTROYED, destroySprite, 100);
        go.addComponent(animatedComponent);

        //State component
        StateComponent sc = new StateComponent();
        sc.addState(ObjectStateEnum.IDLE);
        go.addComponent(sc);
        //if the enemy is visible calculate its box
        if (row >= 0) {
            addBoxCollider(go, lane, row);
        } else {
            addBoxCollider(go);
        }
        CharClassComponent charClassComponent = new CharClassComponent(charClass);
        go.addComponent(charClassComponent);
        return go;
    }

    /**
     * creates a collectible game object
     * @param lane starting collectible lane
     * @param row starting collectible row
     * @param pixmap static collectible pixmap
     * @param charClass collectible compatibility class
     * @param idleColl idle collectible animation spritesheet
     * @param tookColl took collectible animation spritesheet
     * @param numOfFrame collectible animation frame duration
     * @return the created collectible game object
     */
     static GameObject makeCollectibles(int lane, int row, Pixmap pixmap, ClassTypeEnum charClass, Pixmap idleColl, Pixmap tookColl, int numOfFrame){
        GameObject go = new GameObject(); // oppure prelevare da un pool
        go.addComponent(new PositionComponent(lane, row)); //row = 0 placeholder / don't use
        go.addComponent(new DrawableComponent(pixmap));
        //Animator component
        AnimatedComponent animatedComponent = new AnimatedComponent();

        AndroidPixmap idle = (AndroidPixmap) idleColl;
        AndroidPixmap took = (AndroidPixmap) tookColl;

        //spritesheets
        RegularSpritesheet idleSprite = new RegularSpritesheet(idle,idle.getWidth()/numOfFrame,idle.getHeight());
        RegularSpritesheet destroySprite = new RegularSpritesheet(took,took.getWidth()/numOfFrame,took.getHeight());

        //Animations
        animatedComponent.addAnimation(ObjectStateEnum.IDLE, idleSprite, PRWorld.RUN_ANIM_DURATION);
        animatedComponent.addAnimation(ObjectStateEnum.DESTROYED, destroySprite, 100);
        go.addComponent(animatedComponent);

        //State component
        StateComponent sc = new StateComponent();
        sc.addState(ObjectStateEnum.IDLE);
        go.addComponent(sc);
        //if the enemy is visible calculate its box
        if (row >= 0) {
            addBoxCollider(go, lane, row);
        } else {
            addBoxCollider(go);
        }
        CharClassComponent charClassComponent = new CharClassComponent(charClass);
        go.addComponent(charClassComponent);
        return go;
    }

    /**
     * creates the collision box for a game object given his lane/row position
     * @param go the given game object
     * @param lane go lane
     * @param row go row
     */
    private static void addBoxCollider(GameObject go, int lane, int row) {

        float laneAdjusted = PRWorld.getCoordinateAdjusted(lane,frameBufferWidth,PRWorld.WORLD_WIDTH);

        float rowAdjusted = PRWorld.getCoordinateAdjusted(row,frameBufferHeight,PRWorld.WORLD_HEIGHT);

        BoxColliderComponent bc = new BoxColliderComponent((int)laneAdjusted, (int) rowAdjusted,
                (int)laneAdjusted + frameBufferWidth / PRWorld.WORLD_WIDTH,
                (int) rowAdjusted + frameBufferHeight / PRWorld.WORLD_HEIGHT);
        go.addComponent(bc);
    }

    /**
     * creates an empty collision box for a game object
     * @param go the give game object
     */
    private static void addBoxCollider(GameObject go) {

        BoxColliderComponent bc = new BoxColliderComponent(
                0,
                0,
                0,
                0);
        go.addComponent(bc);
    }
}
