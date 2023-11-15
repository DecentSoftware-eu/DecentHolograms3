package eu.decentsoftware.holograms.internal;

import eu.decentsoftware.holograms.actions.ClickActionHolder;
import eu.decentsoftware.holograms.conditions.ClickConditionHolder;

/**
 * Represents an object that has click actions and click conditions.
 *
 * @author d0by
 * @since 3.0.0
 */
public interface PluginClickable {

    ClickActionHolder getClickActions();

    ClickConditionHolder getClickConditions();

}
