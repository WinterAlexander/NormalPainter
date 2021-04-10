package net.jumpai.client.render;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * A screen that has a stage
 * <p>
 * Created on 2018-01-19.
 *
 * @author Alexander Winter
 */
public interface StageScreen
{
	/**
	 * @return stage of this StageScreen
	 */
	Stage getStage();
}
