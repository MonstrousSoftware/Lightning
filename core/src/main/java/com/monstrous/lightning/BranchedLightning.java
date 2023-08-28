package com.monstrous.lightning;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class BranchedLightning {
    private Array<LightningBolt> bolts;
    private Array<LightningBolt> boltsToDelete;

    public BranchedLightning(int x1, int y1, int x2, int y2) {
        bolts = new Array<>();
        boltsToDelete = new Array<>();
        create(x1, y1, x2, y2);
    }

    // lightning bolts will be removed when faded out
    public void update(float deltaTime) {
        for(LightningBolt bolt : bolts) {
            bolt.update(deltaTime);
            if (bolt.isCompleted())
                boltsToDelete.add(bolt);
        }
        for(LightningBolt bolt : boltsToDelete) {
            bolt.dispose();
        }
        bolts.removeAll(boltsToDelete, true);
        boltsToDelete.clear();
    }

    public void draw(SpriteBatch batch) {
        for(LightningBolt bolt : bolts)
            bolt.draw(batch);
    }

    // add new lightning
    public void create(int x1, int y1, int x2, int y2) {
        LightningBolt main = new LightningBolt(x1, y1, x2, y2);
        bolts.add(main);
        Vector2 mainVec = new Vector2(x2-x1, y2-y1);

        int numBranches = MathUtils.random(3, 6);
        Array<Float> fractions = new Array<>();
        for(int i = 0; i < numBranches; i++){
            fractions.add( MathUtils.random());
        }
        fractions.sort();

        float angle = 30f;
        for(float fraction: fractions) {
            Vector2 branchStart = main.getPoint(fraction);
            Vector2 branchVec = new Vector2(mainVec).scl(1 - fraction);
            branchVec.rotateDeg(angle);
            angle = -angle;
            branchVec.add(branchStart);
            LightningBolt sub = new LightningBolt(branchStart, branchVec);
            bolts.add(sub);
        }
    }

    public void dispose() {
        for(LightningBolt bolt : bolts)
            bolt.dispose();
    }
}
