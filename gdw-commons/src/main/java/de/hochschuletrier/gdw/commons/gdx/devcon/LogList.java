package de.hochschuletrier.gdw.commons.gdx.devcon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

/**
 * A list that shows a number of actors depending on their visibility.
 * Based on libgdx Stack.
 *
 * @author Santo Pfingsten
 */
public class LogList extends WidgetGroup {

    private float prefWidth, prefHeight, minWidth, minHeight, maxWidth, maxHeight;
    private boolean sizeInvalid = true;

    public LogList() {
        setTransform(false);
        setWidth(150);
        setHeight(150);
        setTouchable(Touchable.childrenOnly);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        sizeInvalid = true;
    }

    private void computeSize() {
        sizeInvalid = false;
        maxWidth = minWidth = prefWidth = getStage().getWidth() - 5;
        maxHeight = minHeight = prefHeight = 0;
        SnapshotArray<Actor> children = getChildren();
        for (int i = 0, n = children.size; i < n; i++) {
            Actor child = children.get(i);
            if(!child.isVisible())
                continue;
            if (child instanceof Layout) {
                Layout layout = (Layout) child;
                prefHeight += layout.getPrefHeight();
                minHeight += layout.getMinHeight();
            } else {
                prefHeight += child.getHeight();
                minHeight += child.getHeight();
            }
        }
    }

    public void add(Actor actor) {
        addActor(actor);
    }

    @Override
    public void layout() {
        if (sizeInvalid) {
            computeSize();
        }
        Array<Actor> children = getChildren();
        float y = minHeight;
        for (int i = 0, n = children.size; i < n; i++) {
            Actor child = children.get(i);
            if(!child.isVisible())
                continue;
            float height;
            if (child instanceof Layout) {
                Layout layout = (Layout) child;
                height = Math.max(layout.getPrefHeight(), layout.getMinHeight());
            } else {
                height = child.getHeight();
            }

            y -= height;
            child.setBounds(0, y, prefWidth, height);
            if (child instanceof Layout) {
                ((Layout) child).validate();
            }
        }
    }

    @Override
    public float getPrefWidth() {
        if (sizeInvalid) {
            computeSize();
        }
        return prefWidth;
    }

    @Override
    public float getPrefHeight() {
        if (sizeInvalid) {
            computeSize();
        }
        return prefHeight;
    }

    @Override
    public float getMinWidth() {
        if (sizeInvalid) {
            computeSize();
        }
        return minWidth;
    }

    @Override
    public float getMinHeight() {
        if (sizeInvalid) {
            computeSize();
        }
        return minHeight;
    }

    @Override
    public float getMaxWidth() {
        if (sizeInvalid) {
            computeSize();
        }
        return maxWidth;
    }

    @Override
    public float getMaxHeight() {
        if (sizeInvalid) {
            computeSize();
        }
        return maxHeight;
    }
}
