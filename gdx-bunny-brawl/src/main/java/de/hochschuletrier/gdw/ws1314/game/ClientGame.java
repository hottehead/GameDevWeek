package de.hochschuletrier.gdw.ws1314.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;

/**
 * Created by Jerry on 18.03.14.
 */
public class ClientGame {
    private ClientEntityManager entityManager;
    private NetworkManager netManager;
    private int Inputmask;

    public ClientGame() {
        entityManager = new ClientEntityManager();
        netManager = NetworkManager.getInstance();
    }

    public void init(AssetManagerX assets) {

    }

    public void render() {


    }

    public void update(float delta) {
        entityManager.update(delta);
    }

    public boolean keyDown(int keycode) {

        if(keycode == Input.Keys.A)
        {

        }

        return false;
    }

    public boolean keyUp(int keycode) {
        return false;
    }

}
