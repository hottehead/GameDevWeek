package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.PlayerStates;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

public class PlayerReplicationDatagram extends BaseDatagram{
	public static final byte PLAYER_REPLICATION_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x20;
	private long entityId;
	private float xposition;
	private float yposition;
	private EntityType entityType;
	private int eggs;
	private float health;
	private float armor;
	private FacingDirection facingDirection;
	private TeamColor teamColor;
	private PlayerStates playerState;

	public PlayerReplicationDatagram(byte type, short id, short param1, short param2){
		super(MessageType.DELTA, type, id, param1, param2);
	}

	public PlayerReplicationDatagram(long entityId, float xposition, float yposition, EntityType entityType, int eggs, float health, float armor,
			FacingDirection facingDirection, TeamColor teamColor, PlayerStates playerState){
		super(MessageType.DELTA, PLAYER_REPLICATION_DATAGRAM, (short) entityId, (short) 0, (short) 0);
		this.entityId = entityId;
		this.xposition = xposition;
		this.yposition = yposition;
		this.entityType = entityType;
		this.eggs = eggs;
		this.health = health;
		this.armor = armor;
		this.facingDirection = facingDirection;
		this.teamColor = teamColor;
		this.playerState = playerState;
	}

	public PlayerReplicationDatagram(ServerPlayer entity){
		this(entity.getID(), entity.getPosition().x, entity.getPosition().y, entity.getEntityType(), entity.getCurrentEggCount(), entity.getCurrentHealth(),
				entity.getCurrentArmor(), entity.getFacingDirection(), entity.getTeamColor(), entity.getCurrentPlayerState());
	}

	@Override
	public void handle(DatagramHandler handler, NetConnection connection){
		handler.handle(this, connection);
	}

	@Override
	public void writeToMessage(INetMessageOut message){
		message.putLong(entityId);
		message.putFloat(xposition);
		message.putFloat(yposition);
		message.putEnum(entityType);
		message.putInt(eggs);
		message.putFloat(health);
		message.putFloat(armor);
		message.putEnum(facingDirection);
		message.putEnum(teamColor);
		message.putEnum(playerState);
	}

	@Override
	public void readFromMessage(INetMessageIn message){
		entityId = message.getLong();
		xposition = message.getFloat();
		yposition = message.getFloat();
		entityType = message.getEnum(EntityType.class);
		eggs = message.getInt();
		health = message.getFloat();
		armor = message.getFloat();
		facingDirection = message.getEnum(FacingDirection.class);
		teamColor = message.getEnum(TeamColor.class);
		playerState = message.getEnum(PlayerStates.class);
	}

	public long getEntityId(){
		return entityId;
	}

	public float getXposition(){
		return xposition;
	}

	public float getYposition(){
		return yposition;
	}

	public EntityType getEntityType(){
		return entityType;
	}

	public int getEggs(){
		return eggs;
	}

	public float getHealth(){
		return health;
	}

	public float getArmor(){
		return armor;
	}

	public FacingDirection getFacingDirection(){
		return facingDirection;
	}

	public TeamColor getTeamColor(){
		return teamColor;
	}

	public PlayerStates getPlayerState(){
		return playerState;
	}
}
