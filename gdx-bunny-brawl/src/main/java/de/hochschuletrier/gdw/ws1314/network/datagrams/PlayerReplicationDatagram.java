package de.hochschuletrier.gdw.ws1314.network.datagrams;

import de.hochschuletrier.gdw.commons.netcode.NetConnection;
import de.hochschuletrier.gdw.commons.netcode.datagram.INetDatagram;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageIn;
import de.hochschuletrier.gdw.commons.netcode.message.INetMessageOut;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.player.ServerPlayer;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.entity.player.kit.PlayerKit;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.network.DatagramHandler;

/**
 * Created by albsi on 17.03.14.
 */
public class PlayerReplicationDatagram extends BaseDatagram {
    public static final byte PLAYER_REPLICATION_DATAGRAM = INetDatagram.Type.FIRST_CUSTOM + 0x20;
    private long id;
    private float xposition;
    private float yposition;
    private PlayerKit playerKit;
    private float eggs;
    private float health;
    private float armor;
    private FacingDirection facingDirection;
    private TeamColor teamColor;

    public PlayerReplicationDatagram (byte type, short id, short param1, short param2) {
        super (MessageType.DELTA, type, id, param1, param2);
    }

    public PlayerReplicationDatagram(long id, float xposition,
			float yposition, PlayerKit playerKit, float eggs, float health,
			float armor, FacingDirection facingDirection, TeamColor teamColor) {
    	super (MessageType.DELTA, PLAYER_REPLICATION_DATAGRAM, (short) 0, (short) 0, (short) 0);
        this.id=id;
    	this.xposition = xposition;
		this.yposition = yposition;
		this.playerKit = playerKit;
		this.eggs = eggs;
		this.health = health;
		this.armor = armor;
		this.facingDirection = facingDirection;
		this.teamColor = teamColor;
	}

	public PlayerReplicationDatagram(ServerPlayer entity) {
		this(entity.getID(),entity.getPosition().x,entity.getPosition().y,
				entity.getPlayerKit(),entity.getCurrentEggCount(),
				entity.getCurrentHealth(),entity.getCurrentArmor(),
				entity.getFacingDirection(),entity.getTeamColor());
	}

	@Override
    public void handle (DatagramHandler handler, NetConnection connection) {
        handler.handle (this, connection);
    }

    @Override
    public void writeToMessage (INetMessageOut message) {
        message.putLong (id);
        message.putFloat (xposition);
        message.putFloat (yposition);
        message.putEnum (playerKit);
        message.putFloat(eggs);
        message.putFloat(health);
        message.putFloat(armor);
        message.putEnum(facingDirection);
        message.putEnum(teamColor);
    }

    @Override
    public void readFromMessage (INetMessageIn message) {
        id = message.getInt ();
        xposition = message.getFloat ();
        yposition = message.getFloat ();
        playerKit = message.getEnum (PlayerKit.class);
        eggs = message.getFloat();
        health = message.getFloat();
        armor = message.getFloat();
        facingDirection=message.getEnum(FacingDirection.class);
        teamColor=message.getEnum(TeamColor.class);
    }

	public long getId() {
		return id;
	}

	public float getXposition() {
		return xposition;
	}

	public float getYposition() {
		return yposition;
	}

	public PlayerKit getPlayerKit() {
		return playerKit;
	}

	public float getEggs() {
		return eggs;
	}

	public float getHealth() {
		return health;
	}

	public float getArmor() {
		return armor;
	}

	public FacingDirection getFacingDirection() {
		return facingDirection;
	}

	public TeamColor getTeamColor() {
		return teamColor;
	}


}
