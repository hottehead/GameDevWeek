package de.hochschuletrier.gdw.ws1314.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.input.InputInterceptor;
import de.hochschuletrier.gdw.commons.gdx.state.transition.SplitHorizontalTransition;
import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import de.hochschuletrier.gdw.ws1314.network.NetworkManager;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

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

        switch(keycode)
        {
            case(Input.Keys.A):{
                netManager.sendAction(PlayerIntention.MOVE_TOGGLE_LEFT);
                break;
            }
            case(Input.Keys.S):{
                netManager.sendAction(PlayerIntention.MOVE_TOGGLE_DOWN);
                break;
            }
            case(Input.Keys.D):{
                netManager.sendAction(PlayerIntention.MOVE_TOGGLE_RIGHT);
                break;
            }
            case(Input.Keys.W):{
                netManager.sendAction(PlayerIntention.MOVE_TOGGLE_UP);
                break;
            }
            case(Input.Keys.SPACE):{
                netManager.sendAction(PlayerIntention.ATTACK_1);
                break;
            }
            case(Input.Keys.E):{
                netManager.sendAction(PlayerIntention.DROP_EGG);
                break;
            }
        }

        return false;
    }

    public boolean keyUp(int keycode) {
        switch(keycode)
        {
            case(Input.Keys.A):{
                netManager.sendAction(PlayerIntention.MOVE_TOGGLE_LEFT);
                break;
            }
            case(Input.Keys.S):{
                netManager.sendAction(PlayerIntention.MOVE_TOGGLE_DOWN);
                break;
            }
            case(Input.Keys.D):{
                netManager.sendAction(PlayerIntention.MOVE_TOGGLE_RIGHT);
                break;
            }
            case(Input.Keys.W):{
                netManager.sendAction(PlayerIntention.MOVE_TOGGLE_UP);
                break;
            }
        }
        return false;
    }

}
