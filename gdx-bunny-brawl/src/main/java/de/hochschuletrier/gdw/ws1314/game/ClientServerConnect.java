package de.hochschuletrier.gdw.ws1314.game;

import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.ws1314.entity.*;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerUpdateDatagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Jerry on 18.03.14.
 */
public class ClientServerConnect {

    private static final Logger logger = LoggerFactory.getLogger(ClientServerConnect.class);

    private static ClientServerConnect csc;

    private ServerEntityManager sem;
    private ClientEntityManager cem;

    private long playerID = -1;

    private ClientServerConnect(){
        logger.warn("Replace ClientServerConnect");

        sem = ServerEntityManager.getInstance(null);
        cem = ClientEntityManager.getInstance();
    }

    public static ClientServerConnect getInstance() {
        if(csc == null) {

            csc = new ClientServerConnect();
        }

        return csc;
    }

    public void update(){
        for(int i = 0; i < sem.getListSize(); i++){
            long id = sem.getListEntity(i).getID();
            ServerEntity senty = sem.getEntityById(id);
            ClientEntity centy = cem.getEntityById(id);
            if(centy == null)            {
                cem.createEntity(id,senty.getPosition(),senty.getEntityType());
                switch(senty.getEntityType()){
                    case Noob:
                    case Hunter:
                    case Tank:
                    case Knight:
                        playerID = id;
                        break;
                }

            }
            else {
                centy.setPosition(senty.getPosition());
                if(senty instanceof ServerPlayer){
                    ServerPlayer sp = (ServerPlayer)senty;
                    ClientPlayer cp = (ClientPlayer)cem.getEntityById(sp.getID());

                    cp.setCurrentHealth(sp.getCurrentHealth());
                    cp.setFacingDirection(sp.getFacingDirection());
                    cp.setCurrentArmor(sp.getCurrentArmor());
                }
            }
        }

    }

    public void sendEntityEvent(long id, PlayerIntention eventPlayerIntention){

    }

    public void sendAction(PlayerIntention eventPlayerIntention){

        ServerPlayer sp = (ServerPlayer)sem.getEntityById(playerID);
        if(sp != null)
        sp.doAction(eventPlayerIntention);
    }

    public void despawnEntity(long id){
        sem.removeEntity(sem.getEntityById(id));
        cem.removeEntity(cem.getEntityById(id));
    }

    public void sendPlayerUpdate(String playerName, EntityType type, byte team, boolean accept){

    }

}
