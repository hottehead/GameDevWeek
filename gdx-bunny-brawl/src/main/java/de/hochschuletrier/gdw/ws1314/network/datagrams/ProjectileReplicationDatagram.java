package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.entity.projectile.ServerProjectile;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

/**
 * Created by albsi on 17.03.14.
 */
public class ProjectileReplicationDatagram extends BaseDatagram{
	public static final byte PROJETILE_REPLICATION_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x22;
	private long entityId;
	private float xposition;
	private float yposition;
	private FacingDirection direction;
	private TeamColor teamColor;

	public ProjectileReplicationDatagram(byte type, short id, short param1, short param2){
		super(MessageType.DELTA, type, id, param1, param2);
	}

	public ProjectileReplicationDatagram(long entityId, float xposition, float yposition, FacingDirection direction, TeamColor teamColor){
		super(MessageType.DELTA, PROJETILE_REPLICATION_DATAGRAM, (short) 0, (short) 0, (short) 0);
		this.entityId = entityId;
		this.xposition = xposition;
		this.yposition = yposition;
		this.direction = direction;
	}

	public ProjectileReplicationDatagram(ServerProjectile projectile){
		this(projectile.getID(), projectile.getPosition().x, projectile.getPosition().y, projectile.getFacingDirection(), projectile.getTeamColor());
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
		message.putEnum(direction);
		message.putEnum(teamColor);
	}

	@Override
	public void readFromMessage(INetMessageIn message){
		entityId = message.getLong();
		xposition = message.getFloat();
		yposition = message.getFloat();
		direction = message.getEnum(FacingDirection.class);
		teamColor = message.getEnum(TeamColor.class);
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

	public FacingDirection getDirection(){
		return direction;
	}

	public TeamColor getTeamColor(){
		return teamColor;
	}

}
