package com.EmGiuPa.androidgames.ProjectRunner;

import android.content.SharedPreferences;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Pixmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class LevelManager {

    private Game mGame;
    private List<GameObject> enemyList;
    private List<GameObject> bonusList;
    private List<GameObject> colleList;
    private List<GameObject> bulletList;
    private Dictionary<String, Pixmap> pixDic;
    private GameObject boss;
    private SharedPreferences sh;
    private SharedPreferences.Editor editor;
    private int totalLevelCollectibles;

    /**
     * level manager constructor
     * @param game the current game instance
     */
    public LevelManager(Game game) {

        enemyList = new LinkedList<GameObject>();
        bonusList = new LinkedList<GameObject>();
        colleList = new LinkedList<GameObject>();
        bulletList = new LinkedList<GameObject>();

        mGame = game;

        sh = mGame.getSharedPreferences();
        editor = mGame.getEditor();
    }

    /**
     * loads the level of id "level" from file
     * @param level level id
     */
    void loadLevel(int level) {
        String json = null;
        //open file
        try {
            InputStream is = mGame.getFileIO().readAsset("Test_level" + level + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try { JSONObject obj = new JSONObject(json);

            String mapType = obj.getString("MapType");
            pixDic = pixmapDictionary(mapType);

            //enemies
            JSONArray enemiesArray = obj.getJSONArray("Enemies");
            for (int i = 0; i < enemiesArray.length(); i++) {
                JSONObject jInsideObj = enemiesArray.getJSONObject(i);
                String type = jInsideObj.getString("Type");
                int lane = jInsideObj.getInt("Lane");
                int row = jInsideObj.getInt("Row");

                GameObject e = PRFactory.makeEnemy(lane, -row, pixDic.get(type), getCharClassfromType(type), pixDic.get(type), Assets.skeleDeath,3,4);
                enemyList.add(e);
            }

            //Bonus
            JSONArray bonusArray = obj.getJSONArray("Bonus");
            for (int i = 0; i < bonusArray.length(); i++) {
                JSONObject jInsideObj = bonusArray.getJSONObject(i);
                String type = jInsideObj.getString("Type");
                int lane = jInsideObj.getInt("Lane");
                int row = jInsideObj.getInt("Row");

                GameObject b = PRFactory.makeBonus(lane, -row, pixDic.get(type), getCharClassfromType(type), pixDic.get(type), pixDic.get(type),8);
                bonusList.add(b);
            }

            //Collectibles
            JSONArray collectiblesArray = obj.getJSONArray("Collectibles");
            totalLevelCollectibles = collectiblesArray.length();
            for (int i = 0; i < collectiblesArray.length(); i++) {
                JSONObject jInsideObj = collectiblesArray.getJSONObject(i);
                String type = jInsideObj.getString("Type");
                int lane = jInsideObj.getInt("Lane");
                int row = jInsideObj.getInt("Row");

                GameObject c = PRFactory.makeCollectibles(lane, -row, pixDic.get(type), getCharClassfromType(type), pixDic.get(type), pixDic.get(type),6);
                colleList.add(c);
            }

            //Boss
            JSONObject jInsideObj = obj.getJSONObject("Boss");
            int row = jInsideObj.getInt("Row");
            int health = jInsideObj.getInt("Health");
            JSONArray spawnsList = jInsideObj.getJSONArray("Spawns");
            for (int j = 0; j < spawnsList.length(); j++) {
                JSONObject spawn = spawnsList.getJSONObject(j);
                String type = spawn.getString("Type");
                int lane = spawn.getInt("Lane");
                int rowB = spawn.getInt("Row");

                GameObject e = PRFactory.makeBullet(lane, -rowB, getCharClassfromType(type), pixDic.get("Bullet-"+getCharClassfromType(type).toString()),3);
                bulletList.add(e);
            }
            boss = PRFactory.makeBoss(-row, pixDic.get("Boss-idle"), pixDic.get("Boss-attack"), pixDic.get("Boss"), health,6,8);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * debug method used to check the saved preferences for a certain level
     * @param level leven id
     */
    public void checkLevelPreferences(int level) {

        String levelRef = "level" + level;

        String clear = sh.getString(levelRef + "_cleared", "Unclear");
        String allCollectibles = sh.getString(levelRef + "_all_collectibles", "NotAll");
        String noHit = sh.getString(levelRef + "_no_hit", "Hit");

        Log.d("SHARED_PREF_RESULTS", "\n" + clear + "\n" + allCollectibles + "\n" + noHit);
    }

    /**
     * checks shared preference to get the "cleared" state for the level of id level
     * @param level level id
     * @return cleared boolean
     */
    boolean checkLevelCleared(int level) {
        String levelRef = "level" + level;
        String clear = sh.getString(levelRef + "_cleared", "Unclear");

        if(clear.equals("Cleared")) {
            return true;
        }
        return false;
    }

    /**
     * checks shared preference to get the "all collectibles taken" state for the level of id level
     * @param level level id
     * @return cleared boolean
     */
    boolean checkAllCollectibles(int level) {
        String levelRef = "level" + level;
        String allCollectibles = sh.getString(levelRef + "_all_collectibles", "NotAll");

        if(allCollectibles.equals("Complete")) {
            return true;
        }
        return false;
    }

    /**
     * checks shared preference to get the "noHit" state for the level of id level
     * @param level level id
     * @return cleared boolean
     */
    boolean checkNoHit(int level) {
        String levelRef = "level" + level;
        String noHit = sh.getString(levelRef + "_no_hit", "Hit");

        if(noHit.equals("NoHit")) {
            return true;
        }
        return false;
    }

    /**
     * sets shared preference data for a certain level
     * @param level level id
     * @param cleared cleared state
     * @param allCollectibles all collectibles taken state
     * @param noHit no hit state
     */
    void setLevelPreferences(int level, boolean cleared, int allCollectibles, boolean noHit) {

        if(cleared) {
            setLevelClearPreferences(level, "Cleared");
        }
        if(allCollectibles == 0) {
            setAllCollectiblesPreferences(level, "Complete");
        }
        if(noHit) {
            setNoHitPreferences(level, "NoHit");
        }
    }

    private void setLevelClearPreferences(int level, String cleared) {
        String levelRef = "level" + level;

        editor.putString(levelRef + "_cleared", cleared);
        editor.apply();
    }

    private void setAllCollectiblesPreferences(int level, String allCollectibles) {
        String levelRef = "level" + level;

        editor.putString(levelRef + "_all_collectibles", allCollectibles);
        editor.apply();
    }

    private void setNoHitPreferences(int level, String noHit) {
        String levelRef = "level" + level;

        editor.putString(levelRef + "_no_hit", noHit);
        editor.apply();
    }

    /**
    * clears shared preferences
    */
    void clearSharedPreferences(){
        sh.edit().clear().commit();
    }

    /**
     * associates a class fo the enumeration ClassTypeEnum to a string
     * @param type passed string readed from file
     * @return enumeration class associated to type
     */
    private ClassTypeEnum getCharClassfromType(String type) {
        if (type.compareTo("A") == 0) {
            return ClassTypeEnum.A;
        } else if (type.compareTo("B") == 0) {
            return ClassTypeEnum.B;
        } else if (type.compareTo("C") == 0) {
            return ClassTypeEnum.C;
        }
        else if(type.compareTo("Life") == 0) {
            return ClassTypeEnum.Life;
        }
        else if(type.compareTo("RED GEM") == 0) {
            return ClassTypeEnum.RED_GEM;
        }
        else return ClassTypeEnum.D;
    }

    List<GameObject> getEnemyList() {
        return enemyList;
    }

    public int getTotalLevelCollectibles(){return this.totalLevelCollectibles;}


    List<GameObject> getBonusList() {
        return bonusList;
    }

    List<GameObject> getCollectibles() {
        return colleList;
    }

    List<GameObject> getBulletList() {
        return bulletList;
    }

    GameObject getBoss() {
        return boss;
    }

    void clear() {
        enemyList.clear();
        bonusList.clear();
        colleList.clear();
        bulletList.clear();
    }

    /**
     * debug method
     */
    public void printObjectsInLog() {
        int i = 0;
        for (GameObject go : this.enemyList) {
            PositionComponent pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            Log.d(String.format("Enemy %d", i), String.valueOf(pc.getLane()));
            Log.d(String.format("Enemy %d", i), String.valueOf(pc.getRow()));
            i++;
        }
        i = 0;

        for (GameObject go : this.bonusList) {
            PositionComponent pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            Log.d(String.format("Bonus %d", i), String.valueOf(pc.getLane()));
            Log.d(String.format("Bonus %d", i), String.valueOf(pc.getRow()));
            i++;
        }
        i = 0;

        for (GameObject go : this.colleList) {
            PositionComponent pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            Log.d(String.format("Colle %d", i), String.valueOf(pc.getLane()));
            Log.d(String.format("Colle %d", i), String.valueOf(pc.getRow()));
            i++;
        }
        for (GameObject go : this.bonusList) {
            PositionComponent pc = (PositionComponent) go.getComponent(ComponentTypeEnum.POSITION);
            Log.d(String.format("Colle %d", i), String.valueOf(pc.getLane()));
            Log.d(String.format("Colle %d", i), String.valueOf(pc.getRow()));
            i++;
        }
        Log.d("Boss", boss.toString());
    }

    /**
     * method used to set pixmap to key string
     * @param map not used
     * @return the builded dictionary
     */
    private Dictionary<String, Pixmap> pixmapDictionary(String map) {
        Dictionary<String, Pixmap> p = new Hashtable<String, Pixmap>();

        //Enemy assets
        p.put("A", Assets.skeleGreenIdle);
        p.put("B", Assets.skeleGrayIdle);
        p.put("C", Assets.skeleYellowIdle);

        //Bonus assets
        p.put("Life", Assets.potion);

        //Collectibles assets
        p.put("RED GEM", Assets.coinColl);

        //Boss assets
        p.put("Boss-idle", Assets.bossIdle);
        p.put("Boss-attack", Assets.bossAttack);

        //Bullet assets
        p.put("Bullet-A",Assets.bulletGreen);
        p.put("Bullet-B",Assets.bulletGray);
        p.put("Bullet-C",Assets.bulletYellow);

        return p;
    }
}
