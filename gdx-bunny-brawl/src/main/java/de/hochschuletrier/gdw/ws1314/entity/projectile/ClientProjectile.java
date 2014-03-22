package de.hochschuletrier.gdw.ws1314.entity.projectile;

import de.hochschuletrier.gdw.ws1314.entity.ClientEntity;
import de.hochschuletrier.gdw.ws1314.entity.EntityType;
import de.hochschuletrier.gdw.ws1314.entity.EventType;
import de.hochschuletrier.gdw.ws1314.entity.player.TeamColor;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;

/**
 * 
 * @author Sonic
 *
 */

public class ClientProjectile extends ClientEntity {
	protected FacingDirection facingDirection;
	protected TeamColor teamColor;
	
	public ClientProjectile() {
		super();
		this.direction = FacingDirection.UP;
	}

	public FacingDirection getFacingDirection() {
		return direction;
	}
	public void setFacingDirection(FacingDirection facingDirection) {
		this.direction = facingDirection;
	}

	public TeamColor getTeamColor() {
		return teamColor;
	}
	public void setTeamColor(TeamColor teamColor) {
		this.teamColor = teamColor;
	}

    @Override
    public void doEvent(EventType event) {
    }

	@Override
	public void enable() {
	}

	@Override
	public void disable() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void update(float delta) {
		super.update(delta);
	}

	@Override
	public void render() {
	}

	@Override
	public EntityType getEntityType()
	{
	    return EntityType.Projectil;
	}

}
