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
		this.facingDirection = FacingDirection.UP;
	}

	public FacingDirection getFacingDirection() {
		return facingDirection;
	}
	public void setFacingDirection(FacingDirection facingDirection) {
		this.facingDirection = facingDirection;
	}

	public TeamColor getTeamColor() {
		return teamColor;
	}
	public void setTeamColor(TeamColor teamColor) {
		this.teamColor = teamColor;
	}

    @Override
    public void doEvent(EventType event) {
		switch (event){
			case WALK_UP:
				facingDirection = FacingDirection.UP;
				break;
			case WALK_UP_LEFT:
				facingDirection = FacingDirection.UP_LEFT;
				break;
			case WALK_LEFT:
				facingDirection = FacingDirection.LEFT;
				break;
			case WALK_DOWN_LEFT:
				facingDirection = FacingDirection.DOWN_LEFT;
				break;
			case WALK_DOWN:
				facingDirection = FacingDirection.DOWN;
				break;
			case WALK_DOWN_RIGHT:
				facingDirection = FacingDirection.DOWN_RIGHT;
				break;
			case WALK_RIGHT:
				facingDirection = FacingDirection.RIGHT;
				break;
			case WALK_UP_RIGHT:
				facingDirection = FacingDirection.UP_RIGHT;
				break;

		}



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
