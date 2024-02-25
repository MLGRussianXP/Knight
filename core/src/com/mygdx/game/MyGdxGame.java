package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {

    // Objects used


    SpriteBatch spriteBatch;

    // A variable for tracking elapsed time for the animation
    float stateTime;
    MyButton btnLeft, btnRight, btnAction, btnJump, btnCrouch, btnSpawnEnemy, btnRoll, btnCombo;
    int screenWidth, screenHeight;
    Knight knight;
    Array<Ufo> ufos = new Array<>();
    long lastDropTime = System.currentTimeMillis();
    Texture imgUFO;

    Music musicBackgroud;
    Sound explosionSound;

    @Override
    public void create() {
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();
        btnLeft = new MyButton(new Texture("PlayStation 5/Double/playstation_stick_l.png"),
                new Texture("PlayStation 5/Double/playstation_stick_l_left.png"),
                50, 20);
        btnRight = new MyButton(new Texture("PlayStation 5/Double/playstation_stick_r.png"),
                new Texture("PlayStation 5/Double/playstation_stick_r_right.png"), 250, 20);

        btnAction = new MyButton(new Texture("PlayStation 5/Double/playstation_button_square_outline.png"),
                new Texture("PlayStation 5/Double/playstation_button_square.png"),
                0, 20);
        btnAction.setX(screenWidth - btnAction.getWidth() - 70);
        btnCrouch = new MyButton(new Texture("PlayStation 5/Double/playstation_button_circle_outline.png"),
                new Texture("PlayStation 5/Double/playstation_button_circle.png"),
                btnAction.getX() - 200, btnAction.getY());
        btnJump = new MyButton(new Texture("PlayStation 5/Double/playstation_button_triangle_outline.png"),
                new Texture("PlayStation 5/Double/playstation_button_triangle.png"),
                btnCrouch.getX(), btnCrouch.getY() + 200);
        btnSpawnEnemy = new MyButton(new Texture("PlayStation 5/Double/playstation_touchpad_touch_outline.png"),
                new Texture("PlayStation 5/Double/playstation_touchpad_touch.png"),
                screenWidth / 2 - 128, 20);
        btnRoll = new MyButton(new Texture("PlayStation 5/Double/playstation_trigger_l2_outline.png"),
                new Texture("PlayStation 5/Double/playstation_trigger_l2.png"),
                btnCrouch.getX() - 200, 20);
        btnCombo = new MyButton(new Texture("PlayStation 5/Double/playstation_button_cross_outline.png"),
                new Texture("PlayStation 5/Double/playstation_button_cross.png"),
                btnAction.getX(), btnAction.getY() + 200);

        knight = new Knight();
        knight.setX(screenWidth / 2 - knight.getWidth() / 2);
        knight.setY(300);

        imgUFO = new Texture("ufo.png");

        // Instantiate a SpriteBatch for drawing and reset the elapsed animation
        // time to 0
        spriteBatch = new SpriteBatch();
        stateTime = 0f;

        // sound
        musicBackgroud = Gdx.audio.newMusic(Gdx.files.internal("musicBackground.mp3"));
        musicBackgroud.setLooping(true);
        musicBackgroud.play();

        explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.mp3"));
    }

    private void createEnemy() {
        if (System.currentTimeMillis() - lastDropTime > 1500) {
            Ufo ufo = new Ufo(imgUFO, 128, 128, 175);
            ufo.setY(screenHeight);
            Random rand = new Random();
            ufo.setX(rand.nextInt(screenWidth - ufo.getWidth()));
            ufos.add(ufo);
            lastDropTime = System.currentTimeMillis();
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear screen
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        boolean btnLeftClicked = false;
        boolean btnRightClicked = false;
        boolean btnActionClicked = false;
        boolean btnJumpClicked = false;
        boolean btnCrouchClicked = false;
        boolean btnSpawnEnemyClicked = false;
        boolean btnRollClicked = false;
        boolean btnComboClicked = false;

        for (int i = 0; i < 10; i++) {
            if (Gdx.input.isTouched(i)) {
                int touchY = screenHeight - Gdx.input.getY(i);
                btnLeftClicked = btnLeft.checkClick(Gdx.input.getX(i), touchY) || btnLeftClicked;
                btnRightClicked = btnRight.checkClick(Gdx.input.getX(i), touchY) || btnRightClicked;
                btnActionClicked = btnAction.checkClick(Gdx.input.getX(i), touchY) || btnActionClicked;
                btnJumpClicked = btnJump.checkClick(Gdx.input.getX(i), touchY) || btnJumpClicked;
                btnCrouchClicked = btnCrouch.checkClick(Gdx.input.getX(i), touchY) || btnCrouchClicked;
                btnSpawnEnemyClicked = btnSpawnEnemy.checkClick(Gdx.input.getX(i), touchY) || btnSpawnEnemyClicked;
                btnRollClicked = btnRoll.checkClick(Gdx.input.getX(i), touchY) || btnRollClicked;
                btnComboClicked = btnCombo.checkClick(Gdx.input.getX(i), touchY) || btnComboClicked;
            }
        }

        if (btnActionClicked) {
            knight.attack();
        }
        if (btnJumpClicked) {
            knight.jump();
        }
        if (btnLeftClicked) {
            knight.left();
        }
        if (btnRightClicked) {
            knight.right();
        }
        if (btnCrouchClicked) {
            knight.crouch();
        }
        if (btnRollClicked) {
            knight.roll();
        }
        if (btnComboClicked) {
            knight.combo();
        }

        if (btnSpawnEnemyClicked) {
            createEnemy();
        }

        btnLeft.setState(btnLeftClicked);
        btnRight.setState(btnRightClicked);
        btnAction.setState(btnActionClicked);
        btnJump.setState(btnJumpClicked);
        btnCrouch.setState(btnCrouchClicked);
        btnSpawnEnemy.setState(btnSpawnEnemyClicked);
        btnRoll.setState(btnRollClicked);
        btnCombo.setState(btnComboClicked);

        spriteBatch.begin();
        knight.draw(spriteBatch, stateTime);

        for (Ufo ufo : ufos) {
            if (ufo.exploded && ufo.explosionAnimation.isAnimationFinished(ufo.stateTimeExplosion))
                ufos.removeValue(ufo, true);

            ufo.down();
            if (ufo.getY() <= -ufo.getHeight()) {
                ufos.removeValue(ufo, true);
            }
            if (!ufo.exploded && knight.state == Knight.STATE_ATTACK) {
                Rectangle regularAttackRectangle = new Rectangle(0, knight.getY(), (float) knight.getWidth() / 2, knight.getHeight());
                if (knight.directionRight)
                    regularAttackRectangle.setX(knight.getX() + (float) knight.getWidth() / 2);
                else
                    regularAttackRectangle.setX(knight.getX());

                if (
                        (knight.is_combo && knight.overlaps(ufo)) ||
                                (!knight.is_combo && (ufo.rectangle.overlaps(regularAttackRectangle)))
                ) {
                    explosionSound.play();
                    ufo.explode();
                }
            }
        }
        for (Ufo ufo : ufos) {
            if (!ufo.exploded)
                spriteBatch.draw(ufo.texture, ufo.getX(), ufo.getY(),
                    ufo.getWidth(), ufo.getHeight());
            else
                spriteBatch.draw(ufo.explosionAnimation.getKeyFrame(ufo.stateTimeExplosion), ufo.getX(), ufo.getY(),
                        ufo.getWidth(), ufo.getHeight());
        }

        spriteBatch.draw(btnLeft.getCurrentTexture(), btnLeft.getX(), btnLeft.getY(), btnLeft.getWidth(), btnLeft.getHeight());
        spriteBatch.draw(btnRight.getCurrentTexture(), btnRight.getX(), btnRight.getY(), btnRight.getWidth(), btnRight.getHeight());
        spriteBatch.draw(btnAction.getCurrentTexture(), btnAction.getX(), btnAction.getY(), btnAction.getWidth(), btnAction.getHeight());
        spriteBatch.draw(btnJump.getCurrentTexture(), btnJump.getX(), btnJump.getY(), btnJump.getWidth(), btnJump.getHeight());
        spriteBatch.draw(btnCrouch.getCurrentTexture(), btnCrouch.getX(), btnCrouch.getY(), btnCrouch.getWidth(), btnCrouch.getHeight());
        spriteBatch.draw(btnSpawnEnemy.getCurrentTexture(), btnSpawnEnemy.getX(), btnSpawnEnemy.getY(), btnSpawnEnemy.getWidth(), btnSpawnEnemy.getHeight());
        spriteBatch.draw(btnRoll.getCurrentTexture(), btnRoll.getX(), btnRoll.getY(), btnRoll.getWidth(), btnRoll.getHeight());
        spriteBatch.draw(btnCombo.getCurrentTexture(), btnCombo.getX(), btnCombo.getY(), btnCombo.getWidth(), btnCombo.getHeight());

        spriteBatch.end();
    }

    @Override
    public void dispose() { // SpriteBatches and Textures must always be disposed
        spriteBatch.dispose();
    }
}
