package de.hochschuletrier.gdw.ws1314.entity.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hochschuletrier.gdw.ws1314.Main;
import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.network.datagrams.PlayerData;
import de.hochschuletrier.gdw.ws1314.sound.LocalSound;
import de.hochschuletrier.gdw.ws1314.states.GameStates;

/**
 * 
 * @author ElFapo
 * 
 */

public class ClientPlayer extends ClientEntity {
	private static final Logger logger = LoggerFactory.getLogger(ClientPlayer.class);

	private int eggCount;
	private float currentHealth;
	private float currentArmor;
	private FacingDirection facingDirection;
	private PlayerKit playerKit;
	private PlayerData playerData;
	private TeamColor teamColor;
	private EntityStates playerState;
	private String playerName;
	boolean isBuffCarrotActive=false, isBuffSpinachActive=false, didBuffCloverAppear=false;

	public int getEggCount() {
		return eggCount;
	}

	public float getCurrentHealth() {
		return currentHealth;
	}

	public float getCurrentArmor() {
		return currentArmor;
	}

	public FacingDirection getFacingDirection() {
		return facingDirection;
	}

	public PlayerKit getPlayerKit() {
		return playerKit;
	}

	public PlayerData getPlayerInfo() {
		return playerData;
	}

	public EntityType getEntityType() {
		return playerKit.getEntityType();
	}

	public TeamColor getTeamColor() {
		return teamColor;
	}

	public EntityStates getCurrentPlayerState() {
		return playerState;
	}

	public void setCurrentHealth(float currentHealth) {
		this.currentHealth = currentHealth;
	}

	public void setEggCount(int eggCount) {
		this.eggCount = eggCount;
	}

	public void setCurrentArmor(float currentArmor) {
		this.currentArmor = currentArmor;
	}

	public void setFacingDirection(FacingDirection facingDirection) {
		this.facingDirection = facingDirection;
	}

	public void setPlayerKit(PlayerKit playerKit) {
		this.playerKit = playerKit;
	}

	public void setPlayerName(String name) {
		this.playerName = name;
	}

	public void setTeamColor(TeamColor teamColor) {
		this.teamColor = teamColor;
	}

	public void setCurrentPlayerState(EntityStates state) {
		if (playerState != state)
			enterNewState();

		this.playerState = state;
	}

	@Override
	public void doEvent(EventType event) {
		// aaaaaif
		// (Main.getInstance().getCurrentState().equals(GameStates.DUALGAMEPLAY))
		// LocalSound.getInstance().playSoundByAction(event, this);
		this.getEntitySound().playSoundByAction(event, this);

		switch (event) {
		case BUFF_ACTIVATE_CARROT:
			isBuffCarrotActive = true;
			break;
		case BUFF_DEACTIVATE_CARROT:
			isBuffCarrotActive = false;
			break;
		case BUFF_ACTIVATE_SPINACH:
			isBuffSpinachActive=true;
			break;
		case BUFF_DEACTIVATE_SPINACH:
			isBuffSpinachActive=false;
			break;
		case BUFF_EAT_CLOVER:
			didBuffCloverAppear=true;
			break;
		default:
			break;
		}
		/*
		 * if(event == EventType.WALK_LEFT){ facingDirection =
		 * FacingDirection.LEFT;
		 * logger.info("angle: {}",facingDirection.getAngle()); }
		 */

	}

	public void enable() {
	}

	public void disable() {
	}

	public void dispose() {
	}

	public ClientPlayer() {

	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

	@Override
	public void render() {
	}

	public boolean isBuffCarrotActive() {
		return isBuffCarrotActive;
	}

	public boolean isBuffSpinachActive() {
		return isBuffSpinachActive;
	}

	public boolean DidBuffCloverAppear() {
		boolean tmp= didBuffCloverAppear;
		if (didBuffCloverAppear) {
			didBuffCloverAppear = false;
		}
		return tmp;
	}
	

}
