package de.hochschuletrier.gdw.ws1314.entity.player;

import com.badlogic.gdx.graphics.Texture;

import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.sound.LocalSound;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author ElFapo
 *
 */

public class ClientPlayer extends ClientEntity
{
    private static final Logger logger = LoggerFactory.getLogger(ClientPlayer.class);

    private int 			eggCount;
    private float 			currentHealth;
    private float			currentArmor;
    private FacingDirection facingDirection;
    private PlayerKit		playerKit;
    private PlayerData		playerData;
    private TeamColor		teamColor;
    
    public int 				getEggCount() 		{ return eggCount; }
    public float 			getCurrentHealth() 	{ return currentHealth; }
    public float 			getCurrentArmor() 	{ return currentArmor; }
    public FacingDirection getFacingDirection() { return facingDirection; }
    public PlayerKit 		getPlayerKit() 		{ return playerKit; }
    public PlayerData 		getPlayerInfo() 	{ return playerData; }
    public EntityType getEntityType() 			{ return playerKit.getEntityType(); }
    public TeamColor		getTeamColor()		{ return teamColor; }
    
    Texture testTexture;
    
    public void setCurrentHealth(float currentHealth) 				{ this.currentHealth = currentHealth; }
    public void setEggCount(int eggCount) 							{ this.eggCount = eggCount; }
    public void setCurrentArmor(float currentArmor) 				{ this.currentArmor = currentArmor; }
    public void setFacingDirection(FacingDirection facingDirection) { this.facingDirection = facingDirection; }
    public void setPlayerKit(PlayerKit playerKit) 					{ this.playerKit = playerKit; }
    public void setPlayerInfo(PlayerData playerData)				{ this.playerData = playerData; }
    public void setTeamColor(TeamColor teamColor)					{ this.teamColor = teamColor; }

    @Override
    public void doEvent(EventType event) {
    	if (Main.getInstance().getCurrentState().equals(GameStates.CLIENTGAMEPLAY))
    		LocalSound.getInstance().playSoundByAction(event, this);

    }
    
    public void enable() {}
    public void disable() {}
    public void dispose() {}
    
    public ClientPlayer() {
    	
    }
    
    public void update(float delta) {
    }
    
    @Override
    public void render() {
    }

}
