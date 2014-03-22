package de.hochschuletrier.gdw.ws1314.game;

import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntity;
import de.hochschuletrier.gdw.ws1314.entity.ServerEntityManager;
import de.hochschuletrier.gdw.ws1314.entity.player.ClientPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ClientProjectile;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerProjectile;
import de.hochschuletrier.gdw.ws1314.input.PlayerIntention;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Jerry on 18.03.14.
 */
public class ClientServerConnect {

    private static final Logger logger = LoggerFactory.getLogger(ClientServerConnect.class);

    private static ClientServerConnect csc;



    private long playerID = -1;

    private ClientServerConnect(){
        logger.warn("Replace ClientServerConnect");
    }

    public static ClientServerConnect getInstance() {
        if(csc == null) {

            csc = new ClientServerConnect();
        }

        return csc;
    }

    public void update(){
        ServerEntityManager sem = ServerEntityManager.getInstance();
        ClientEntityManager cem = ClientEntityManager.getInstance();
        //logger.info("server entity Listen size: {}",sem.getListSize());
        for(int i = 0; i < sem.getListSize(); i++){
            long id = sem.getListEntity(i).getID();
            ServerEntity senty = sem.getEntityById(id);
            ClientEntity centy = cem.getEntityById(id);
            if(centy == null)            {
                centy = cem.createEntity(id,senty.getPosition(),senty.getEntityType());
                switch(senty.getEntityType()){
                    case Noob:
                    case Hunter:
                    case Tank:
                    case Knight:
                        playerID = id;
                        cem.setPlayerEntityID(id);
                        ServerPlayer sp = (ServerPlayer)senty;
                        ClientPlayer cp = (ClientPlayer)centy;

                        cp.setCurrentHealth(sp.getCurrentHealth());
                        cp.setFacingDirection(sp.getFacingDirection());
                        cp.setCurrentArmor(sp.getCurrentArmor());
                        cp.setTeamColor(sp.getTeamColor());
                        cp.setEggCount(sp.getCurrentEggCount());
                        cp.setFacingDirection(sp.getFacingDirection());
						cp.setCurrentPlayerState(sp.getCurrentPlayerState());
                        break;
					case Projectil:
						ServerProjectile serverProjectile = (ServerProjectile)senty;
						ClientProjectile clientProjectile = (ClientProjectile)centy;

						clientProjectile.setFacingDirection(serverProjectile.getFacingDirection());
						clientProjectile.setTeamColor(serverProjectile.getTeamColor());

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
                    cp.setTeamColor(sp.getTeamColor());
                    cp.setEggCount(sp.getCurrentEggCount());
                    cp.setFacingDirection(sp.getFacingDirection());
					cp.setCurrentPlayerState(sp.getCurrentPlayerState());
                } else if(senty instanceof ServerProjectile) {
					ServerProjectile serverProjectile = (ServerProjectile)senty;
					ClientProjectile clientProjectile = (ClientProjectile)centy;

					clientProjectile.setFacingDirection(serverProjectile.getFacingDirection());
					clientProjectile.setTeamColor(serverProjectile.getTeamColor());
				}
            }
        }

    }

    public void sendEntityEvent(long id, EventType event){
        ClientEntityManager cem = ClientEntityManager.getInstance();
        ClientEntity e = cem.getEntityById(id);
        if(e != null){
            e.doEvent(event);
        }
    }

    public void sendAction(PlayerIntention eventPlayerIntention){
        ServerEntityManager sem = ServerEntityManager.getInstance();
        ServerPlayer sp = (ServerPlayer)sem.getEntityById(playerID);
        if(sp != null)
        	sp.doAction(eventPlayerIntention);
    }

    public void despawnEntity(long id){
        ClientEntityManager cem = ClientEntityManager.getInstance();
        cem.removeEntity(cem.getEntityById(id));
    }

    public void sendPlayerUpdate(String playerName, EntityType type, byte team, boolean accept){

    }

}
