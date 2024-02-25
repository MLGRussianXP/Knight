package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class MyButton {
    private Texture texture;
    private Texture textureClicked;
    private Rectangle rectangle;
    private boolean clickedState = false;

    public MyButton(Texture texture, Texture textureClicked, int x, int y) {
        this.texture = texture;
        this.textureClicked = textureClicked;
        this.rectangle = new Rectangle(x, y, 200, 200);
    }

    public void setState(boolean newState) {
        this.clickedState = newState;
    }

    public int getWidth() {
        return (int) this.rectangle.getWidth();
    }

    public int getHeight() {
        return (int) this.rectangle.getHeight();
    }

    public int getX() {
        return (int) this.rectangle.getX();
    }

    public int getY() {
        return (int) this.rectangle.getY();
    }

    public void setX(int x) {
        this.rectangle.x = x;
    }

    public void setY(int y) {
        this.rectangle.y = y;
    }

    public boolean checkClick(int xx, int yy) {
        return this.rectangle.contains(xx, yy);
    }

    public Texture getCurrentTexture() {
        return this.clickedState ? textureClicked : texture;
    }

}
