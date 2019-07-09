package com.EmGiuPa.androidgames.ProjectRunner;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class DrawableComponent extends Component {

    private Pixmap pixmap;

    DrawableComponent(Pixmap pixmap) {
        this.pixmap= pixmap;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public void setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
    }

    void draw(Graphics g, float lane, float row){
        g.drawPixmap(pixmap,lane,row);
    }

    /**
     * draws a pixmap with a certain width and height
     * @param g draw manager
     * @param lane lane where to draw (x)
     * @param row row where to draw(y)
     * @param width draw width
     * @param height draw height
     */
    void drawResized(Graphics g, float lane, float row, int width, int height){
        g.drawPixmapResized(pixmap,width,height,(int)lane,(int)row);
    }

    @Override
    public ComponentTypeEnum type() {
        return ComponentTypeEnum.DRAWABLE;
    }
}
