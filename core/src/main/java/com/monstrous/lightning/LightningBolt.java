package com.monstrous.lightning;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class LightningBolt {

    private Texture image;
    private NinePatch ninePatch;
    private Vector2 lineVec;
    private Array<Vector2> points;
    private float alpha;        // to fade out

    public LightningBolt(int x1, int y1, int x2, int y2) {
        this(new Vector2(x1, y1), new Vector2(x2, y2));
    }

    public LightningBolt(Vector2 from, Vector2 to) {
        image = new Texture("LineSegAlpha3.png");
        ninePatch = new NinePatch(image, 31, 31, 31, 31);
        lineVec = new Vector2();
        points = new Array<>();
        makeBolt(from, to);
        alpha = 1f;
    }

    public void update( float deltaTime ){
        alpha -= deltaTime;
    }

    public boolean isCompleted() {
        return alpha <= 0;
    }


    public Vector2 getPoint( float fraction ){
        int index = (int) (fraction * points.size);
        return points.get(index);
    }

    public void draw( SpriteBatch batch ) {
        if(alpha <= 0)
            return;

        batch.setColor(0.8f, 0.8f, 1.0f, alpha);    // blueish white

        for(int i = 0; i < points.size-1; i++){
            Vector2 from = points.get(i);
            Vector2 to = points.get(i+1);
            segment(batch, from, to);
        }

    }

    private void makeBolt(Vector2 start, Vector2 end) {

        lineVec.set(end).sub(start);
        float len = lineVec.len();
        Vector2 normal = new Vector2(lineVec.y, -lineVec.x).nor();

        Array<Float> fractions = new Array<>();
        for(int i = 0; i < len/6; i++){
            fractions.add( MathUtils.random());
        }
        fractions.sort();


        points.clear();
        Vector2 source = start;
        Vector2 offset = new Vector2();
        float sway = 60;
        float jaggedness = 1f/sway;
        float prevDisplacement = 0;

        points.add(start);
        for(int i = 1; i < fractions.size; i++) {
            float fraction = fractions.get(i);

            float scale = (len * jaggedness)* (fraction - fractions.get(i-1));
            // defines an envelope. Points near the middle of the bolt can be further from the central line.
            float envelope = fraction > 0.9f ? 10 * (1 - fraction) : 1;

            float displacement = MathUtils.random(-sway, sway);
            displacement -= (displacement - prevDisplacement)*(1-scale);
            displacement *= envelope;
            prevDisplacement = displacement;

            Vector2 point = new Vector2();
            point.set(lineVec).scl(fraction);
            offset.set(normal).scl(displacement);
            point.add(offset);
            point.add(source);
            points.add(point);
        }
        points.add(end);

    }

    private void segment(SpriteBatch batch, Vector2 from, Vector2 to) {
        lineVec.set(to).sub(from);
        float len = lineVec.len();
        float angle = lineVec.angleDeg();

        ninePatch.draw(batch, from.x, from.y, 31, 31, 64+len-3, 64, 1, 1, angle);
    }


    public void dispose() {
        image.dispose();
    }
}
