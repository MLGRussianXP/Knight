package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Knight {
    Animation<TextureRegion> attackAnimation, runAnimation, idleAnimation, crouchAnimation, crouchWalkAnimation, jumpAnimation, rollAnimation, comboAnimation;
    Texture attackSheet, runSheet, idleSheet, crouchSheet, crouchWalkSheet, jumpSheet, rollSheet, comboSheet;
    private final Rectangle rectangle;
    public int step = 300;
    public float jumpSpeed;
    public static final int ground = 300;
    public boolean directionRight = true;
    public int state = STATE_IDLE;
    public static final int STATE_IDLE = 1;
    public static final int STATE_RUN = 2;
    public static final int STATE_ATTACK = 3;
    public boolean crouching = false, jumping = false, rolling = false, is_combo = false;
    private float stateTimeAttack = 0;

    public Knight() {
        // Load the sprite sheet as a Texture
        attackSheet = new Texture("Knight-anim/_Attack2NoMovement.png");
        runSheet = new Texture("Knight-anim/_Run.png");
        idleSheet = new Texture("Knight-anim/_Idle.png");
        crouchSheet = new Texture("Knight-anim/_CrouchAll.png");
        crouchWalkSheet = new Texture("Knight-anim/_CrouchWalk.png");
        jumpSheet = new Texture("Knight-anim/_Jump.png");
        rollSheet = new Texture("Knight-anim/_Roll.png");
        comboSheet = new Texture("Knight-anim/_AttackComboNoMovement.png");

        // Use the split utility method to create a 2D array of TextureRegions. This is
        // possible because this sprite sheet contains frames of equal size and they are
        // all aligned.
        TextureRegion[][] tmp = TextureRegion.split(attackSheet,
                attackSheet.getWidth() / 6, attackSheet.getHeight());

        // Place the regions into a 1D array in the correct order, starting from the top
        // left, going across first. The Animation constructor requires a 1D array.
        TextureRegion[] attackFrames = tmp[0];
        TextureRegion[] runFrames = TextureRegion.split(runSheet,
                runSheet.getWidth() / 10, runSheet.getHeight())[0];

        TextureRegion[] idleFrames = TextureRegion.split(idleSheet,
                idleSheet.getWidth() / 10, idleSheet.getHeight())[0];

        TextureRegion[] crouchFrames = TextureRegion.split(crouchSheet,
                crouchSheet.getWidth() / 3, crouchSheet.getHeight())[0];

        TextureRegion[] crouchWalkFrames = TextureRegion.split(crouchWalkSheet,
                crouchWalkSheet.getWidth() / 8, crouchWalkSheet.getHeight())[0];

        TextureRegion[] jumpFrames = TextureRegion.split(jumpSheet,
                jumpSheet.getWidth() / 3, jumpSheet.getHeight())[0];

        TextureRegion[] rollFrames = TextureRegion.split(rollSheet,
                rollSheet.getWidth() / 12, rollSheet.getHeight())[0];

        TextureRegion[] comboFrames = TextureRegion.split(comboSheet,
                comboSheet.getWidth() / 10, comboSheet.getHeight())[0];

        // Initialize the Animation with the frame interval and array of frames
        attackAnimation = new Animation<>(0.17f, attackFrames);
        runAnimation = new Animation<>(0.1f, runFrames);
        idleAnimation = new Animation<>(0.17f, idleFrames);
        crouchAnimation = new Animation<>(0.2f, crouchFrames);
        crouchWalkAnimation = new Animation<>(0.1f, crouchWalkFrames);
        jumpAnimation = new Animation<>(0.17f, jumpFrames);
        rollAnimation = new Animation<>(0.05f, rollFrames);
        comboAnimation = new Animation<>(0.17f, comboFrames);

        rectangle = new Rectangle(0, 500, 300, 300);
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


    public void left() {
        if (state == STATE_ATTACK || rolling)
            return;

        double step = this.step * Gdx.graphics.getDeltaTime();
        if (crouching)
            step *= 0.5;

        this.rectangle.x -= step;
        this.directionRight = false;
        this.state = STATE_RUN;
    }

    public void right() {
        if (state == STATE_ATTACK || rolling)
            return;

        double step = this.step * Gdx.graphics.getDeltaTime();
        if (crouching)
            step *= 0.5;

        this.rectangle.x += step;
        this.directionRight = true;
        this.state = STATE_RUN;
    }

    public void attack() {
        if (crouching || jumping || rolling)
            return;

        if (this.state != STATE_ATTACK) {
            this.stateTimeAttack = 0;
        }
        this.state = STATE_ATTACK;
    }

    public void crouch() {
        if (crouching || rolling || jumping || state == STATE_ATTACK)
            return;

        crouching = true;
    }

    public void jump() {
        if (jumping || rolling || state == STATE_ATTACK)
            return;

        crouching = false;
        state = STATE_IDLE;

        jumping = true;
        jumpSpeed = 500;
    }

    public void roll() {
        if (rolling || jumping || state == STATE_ATTACK)
            return;

        stateTimeAttack = 0;
        rolling = true;
        crouching = false;
        state = STATE_IDLE;
    }

    public void combo() {
        attack();
        if (state == STATE_ATTACK)
            is_combo = true;
    }

    public boolean overlaps(Personage other) {
        return this.rectangle.overlaps(other.rectangle);
    }

    private TextureRegion getCurrentTextureRegion(float stateTime) {
        // Get current frame of animation for the current stateTime
        TextureRegion currentAttackFrame = attackAnimation.getKeyFrame(stateTimeAttack, false);
        TextureRegion currentComboFrame = comboAnimation.getKeyFrame(stateTimeAttack, false);

        TextureRegion currentRunFrame = runAnimation.getKeyFrame(stateTime, true);
        TextureRegion currentIdleFrame = idleAnimation.getKeyFrame(stateTime, true);
        TextureRegion currentCrouchFrame = crouchAnimation.getKeyFrame(stateTime, true);
        TextureRegion currentCrouchWalkFrame = crouchWalkAnimation.getKeyFrame(stateTime, true);
        TextureRegion currentJumpFrame = jumpAnimation.getKeyFrame(stateTime, true);
        TextureRegion currentRollFrame = rollAnimation.getKeyFrame(stateTimeAttack, false);

        if (jumping)
            return currentJumpFrame;

        if (state == STATE_RUN) {
            state = STATE_IDLE;
            if (crouching)
                return currentCrouchWalkFrame;
            else
                return currentRunFrame;
        }

        if (crouching)
            return currentCrouchFrame;

        if (rolling) {
            if (rollAnimation.isAnimationFinished(stateTimeAttack))
                rolling = false;
            else
                return currentRollFrame;
        }

        if (state == STATE_ATTACK) {
            if (is_combo) {
                if (comboAnimation.isAnimationFinished(stateTimeAttack)) {
                    this.state = STATE_IDLE;
                    is_combo = false;
                }
                else
                    return currentComboFrame;
            }
            else {
                if (attackAnimation.isAnimationFinished(stateTimeAttack))
                    this.state = STATE_IDLE;
                else
                    return currentAttackFrame;
            }
        }

        return currentIdleFrame;
    }

    public void draw(SpriteBatch spriteBatch, float stateTime) {
        if (state == STATE_ATTACK || rolling)
            stateTimeAttack += Gdx.graphics.getDeltaTime();

        if (jumping) {
            jumpSpeed -= 1200 * Gdx.graphics.getDeltaTime();
            rectangle.y += jumpSpeed * Gdx.graphics.getDeltaTime();

            if (rectangle.y < ground) {
                rectangle.y = ground;
                jumping = false;
            }
        }

        else if (rolling)
            rectangle.x += 400 * (directionRight ? 1 : -1) * Gdx.graphics.getDeltaTime();

        if (rectangle.x < 0)
            rectangle.x = 0;
        else if (rectangle.x + getWidth() > Gdx.graphics.getWidth())
            rectangle.x = Gdx.graphics.getWidth() - getWidth();

        spriteBatch.draw(getCurrentTextureRegion(stateTime), rectangle.x, rectangle.y,
                rectangle.width / 2, 0,
                rectangle.width, rectangle.height,
                directionRight ? 1 : -1, 1, 0);
    }
}
