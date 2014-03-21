package de.hochschuletrier.gdw.ws1314.entity.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.basic.PlayerInfo;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.sound.LocalSound;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

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
    private PlayerInfo		playerInfo;
    private TeamColor		teamColor;
	private PlayerStates	playerState;
    
    public int 				getEggCount() 		{ return eggCount; }
    public float 			getCurrentHealth() 	{ return currentHealth; }
    public float 			getCurrentArmor() 	{ return currentArmor; }
    public FacingDirection getFacingDirection() { return facingDirection; }
    public PlayerKit 		getPlayerKit() 		{ return playerKit; }
    public PlayerInfo 		getPlayerInfo() 	{ return playerInfo; }
    public EntityType getEntityType() 			{ return playerKit.getEntityType(); }
    public TeamColor		getTeamColor()		{ return teamColor; }
	public PlayerStates 	getCurrentPlayerState() {return playerState;}
    
    
    public void setCurrentHealth(float currentHealth) 				{ this.currentHealth = currentHealth; }
    public void setEggCount(int eggCount) 							{ this.eggCount = eggCount; }
    public void setCurrentArmor(float currentArmor) 				{ this.currentArmor = currentArmor; }
    public void setFacingDirection(FacingDirection facingDirection) { this.facingDirection = facingDirection; }
    public void setPlayerKit(PlayerKit playerKit) 					{ this.playerKit = playerKit; }
    public void setPlayerInfo(PlayerInfo playerInfo)				{ this.playerInfo = playerInfo; }
    public void setTeamColor(TeamColor teamColor)					{ this.teamColor = teamColor; }
	public void setCurrentPlayerState(PlayerStates state) {this.playerState = state;}

    @Override
    public void doEvent(EventType event) {
    	if (Main.getInstance().getCurrentState().equals(GameStates.GAMEPLAY))
    		LocalSound.getInstance().playSoundByAction(event, this);
    	
   		this.activeAction = event; 

		/*if(event == EventType.WALK_LEFT){
			facingDirection = FacingDirection.LEFT;
			logger.info("angle: {}",facingDirection.getAngle());
		}*/

    }
    
    public void enable() {}
    public void disable() {}
    public void dispose() {}
    
    public ClientPlayer() {
    	
    }

	@Override
    public void update(float delta) {
		super.update(delta);
    }
    
    @Override
    public void render() {
    }

}
