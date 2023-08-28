package com.monstrous.lightning;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;


// See: https://gamedevelopment.tutsplus.com/how-to-generate-shockingly-good-2d-lightning-effects--gamedev-2681t


public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private BranchedLightning lightning;
    private static Sound thunder;
    private Color bgColor;

    @Override
    public void create() {
        batch = new SpriteBatch();
        lightning = new BranchedLightning(10, 100, 500, 200);
        thunder = Gdx.audio.newSound(Gdx.files.internal("sound/thunder-25689.mp3"));
        thunder.play();
        bgColor = new Color();
    }

    @Override
    public void render() {
        bgColor.set(0.02f, 0, .02f, 1f);
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            lightning.create(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight(), Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            thunder.play();
            bgColor.set(1f, 1, 1f, 1f);
        }

        lightning.update(Gdx.graphics.getDeltaTime());

        ScreenUtils.clear(bgColor, true);

        batch.enableBlending();
        batch.setBlendFunction(Gdx.gl20.GL_SRC_ALPHA, Gdx.gl20.GL_ONE);
        //batch.setBlendFunction(Gdx.gl20.GL_ONE, Gdx.gl20.GL_ONE);
        batch.begin();
        lightning.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        batch.getProjectionMatrix().setToOrtho2D(0,0,width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        lightning.dispose();
        thunder.dispose();
    }
}
