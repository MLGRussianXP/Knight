package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Personage {
    public Texture texture;
    public Rectangle rectangle;
    public int step;

    public Personage(Texture texture, int width, int height, int step) {
        this.texture = texture;
        this.rectangle = new Rectangle(0, 0, width, height);
        this.step = step;
    }

    public Personage(Texture texture, int x, int y, int width, int height, int step) {
        this.texture = texture;
        this.rectangle = new Rectangle(x, y, width, height);
        this.step = step;
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

    public void down() {
        this.rectangle.y -= this.step * Gdx.graphics.getDeltaTime();
    }

    public void left() {
        this.rectangle.x -= this.step * Gdx.graphics.getDeltaTime();
    }

    public void right() {
        this.rectangle.x += this.step * Gdx.graphics.getDeltaTime();
    }

    public boolean overlaps(Personage other) {
        return this.rectangle.overlaps(other.rectangle);
    }
}
