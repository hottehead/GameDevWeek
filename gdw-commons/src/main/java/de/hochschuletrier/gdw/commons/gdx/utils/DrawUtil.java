package de.hochschuletrier.gdw.commons.gdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

import java.util.LinkedList;

/**
 *
 * @author Santo Pfingsten
 */
public class DrawUtil {

    private static final Color currentColor = Color.WHITE.cpy();
    private static Mode currentMode = Mode.NORMAL;
    public static SpriteBatch batch;
	private static Texture white;
    private static LinkedList<Matrix4> matrixStack = new LinkedList<Matrix4>();
	private static OrthographicCamera camera;
    public enum Mode {

        NORMAL,
        ALPHA_MAP,
        ALPHA_BLEND,
        COLOR_MULTIPLY,
        ADD,
        SCREEN
    }

	public static void init() {
		camera = new OrthographicCamera();
        // create a white image for filling rects
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		white = new Texture(pixmap);
        batch = new SpriteBatch();
    }

	public static OrthographicCamera getCamera() {
		return camera;
	}

	public static void update() {
		camera.update(true);
		batch.setProjectionMatrix(camera.combined);
	}

	public static void setCamera(OrthographicCamera camera) {
		DrawUtil.camera = camera;
	}

	public static void setViewport(float width, float height) {
		camera.setToOrtho(true, width, height);
		camera.position.set(width / 2, height / 2, 0);
		camera.update(true);
	}

    public static void setClip(int x, int y, int width, int height) {
        batch.flush();
        Gdx.gl20.glEnable(GL20.GL_SCISSOR_TEST);
		Gdx.gl20.glScissor(x, (int) (camera.viewportHeight - y - height), width, height);
    }

    public static void clearClip() {
        batch.flush();
        Gdx.gl20.glDisable(GL20.GL_SCISSOR_TEST);
    }

    public static void copyArea(Texture target, int x, int y, int format) {
        target.bind();
        Gdx.gl20.glCopyTexImage2D(GL20.GL_TEXTURE_2D, 0, format,
 x,
				(int) (camera.viewportHeight - (y + target.getHeight())),
                target.getWidth(), target.getHeight(), 0);
    }

    public static void setDrawMode(Mode mode) {

        currentMode = mode;
        switch (currentMode) {
            case NORMAL:
                batch.enableBlending();
                batch.flush();
                Gdx.gl20.glColorMask(true, true, true, true);
                batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                break;
            case ALPHA_MAP:
                batch.disableBlending();
                batch.flush();
                Gdx.gl20.glColorMask(false, false, false, true);
                break;
            case ALPHA_BLEND:
                batch.enableBlending();
                batch.flush();
                Gdx.gl20.glColorMask(true, true, true, false);
                batch.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);
                break;
            case COLOR_MULTIPLY:
                batch.enableBlending();
                batch.flush();
                Gdx.gl20.glColorMask(true, true, true, true);
                batch.setBlendFunction(GL20.GL_ONE_MINUS_SRC_COLOR, GL20.GL_SRC_COLOR);
                break;
            case ADD:
                batch.enableBlending();
                batch.flush();
                Gdx.gl20.glColorMask(true, true, true, true);
                batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
                break;
            case SCREEN:
                batch.enableBlending();
                batch.flush();
                Gdx.gl20.glColorMask(true, true, true, true);
                batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_COLOR);
                break;
            default:
                throw new AssertionError(currentMode.name());
        }
    }

    public static void clearColor(Color color) {
        Gdx.gl20.glClearColor(color.r, color.g, color.b, color.a);
    }

    public static void clearColor(float r, float g, float b, float a) {
        Gdx.gl20.glClearColor(r, g, b, a);
    }

    public static void clear() {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public static void resetColor() {
        setColor(Color.WHITE);
    }

    public static void setColor(Color color) {
        currentColor.set(color);
        batch.setColor(currentColor);
    }

    public static Color getColor() {
        return currentColor;
    }

    public static void fillRect(float x, float y, float width, float height) {
		DrawUtil.batch.draw(white, x, y, width, height);
    }

    public static void fillRect(float x, float y, float width, float height, Color color) {
        batch.setColor(color);
		DrawUtil.batch.draw(white, x, y, width, height);
        batch.setColor(currentColor);
    }

    public static void drawRect(float x, float y, float width, float height) {
        fillRect(x, y, width, 1);
        fillRect(x, y + height - 1, width, 1);
        fillRect(x, y, 1, height);
        fillRect(x + width - 1, y, 1, height);
    }

    public static void drawRect(float x, float y, float width, float height, Color color) {
        batch.setColor(color);
        fillRect(x, y, width, 1);
        fillRect(x, y + height - 1, width, 1);
        fillRect(x, y, 1, height);
        fillRect(x + width - 1, y, 1, height);
        batch.setColor(currentColor);
    }

    public static void setLineWidth(float width) {
        Gdx.gl20.glLineWidth(width);
    }

    public static void pushTransform() {
        matrixStack.push(batch.getTransformMatrix().cpy());
    }

    public static void popTransform() {
        batch.setTransformMatrix(matrixStack.pop());
    }

    public static void translate(float x, float y) {
        Matrix4 m = batch.getTransformMatrix();
        m.translate(x, y, 0);
        batch.setTransformMatrix(m);
    }

    public static void scale(float sx, float sy) {
        Matrix4 m = batch.getTransformMatrix();
        m.scale(sx, sy, 0);
        batch.setTransformMatrix(m);
    }

    public static void rotate(float x, float y, float degrees) {
        Matrix4 m = batch.getTransformMatrix();
        m.translate(x, y, 0);
        m.rotate(0, 0, 1, degrees);
        m.translate(-x, -y, 0);
        batch.setTransformMatrix(m);
    }
}
