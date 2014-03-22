package de.hochschuletrier.gdw.ws1314.render.materials;

import de.hochschuletrier.gdw.ws1314.entity.EntityStates;
import de.hochschuletrier.gdw.ws1314.input.FacingDirection;
import de.hochschuletrier.gdw.ws1314.render.MaterialInfo;
import de.hochschuletrier.gdw.ws1314.render.RenderState;

public class HunterMaterials {

	public static MaterialInfo materials[];

	public static MaterialInfo[] build() {
		materials = new MaterialInfo[4];
		materials[0] = new MaterialInfo("hunterWhiteIdleDown", new RenderState(
				EntityStates.IDLE, FacingDirection.DOWN), 110, 74, 1, true);
		materials[1] = new MaterialInfo("hunterWhiteWalkLeft", new RenderState(
				EntityStates.WALKING, FacingDirection.LEFT), 110, 74, 1, true);
		materials[2] = new MaterialInfo("hunterWhiteWalkDown", new RenderState(
				EntityStates.WALKING, FacingDirection.DOWN), 110, 74, 1, true);
		materials[3] = new MaterialInfo("hunterWhiteAttackDown", new RenderState(
				EntityStates.ATTACK, FacingDirection.DOWN), 110, 74, 1, true);

		return materials;

	}

}
