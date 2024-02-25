package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Ufo extends Personage {
    public Animation<TextureRegion> explosionAnimation;
    Texture explosionSheet;
    public boolean exploded = false;
    public float stateTimeExplosion = 0;

    public Ufo(Texture texture, int width, int height, int step) {
        super(texture, width, height, step);

        explosionSheet = new Texture("explosion.png");
        TextureRegion[] explosionSheets = TextureRegion.split(explosionSheet,
                explosionSheet.getWidth() / 40, explosionSheet.getHeight())[0];
        explosionAnimation = new Animation<>(0.05f, explosionSheets);
    }

    public void explode() {
        exploded = true;
    }

    public void down() {
        if (exploded)
            stateTimeExplosion += Gdx.graphics.getDeltaTime();
        else
            super.down();
    }
}
