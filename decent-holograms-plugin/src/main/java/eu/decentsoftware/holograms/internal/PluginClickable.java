package eu.decentsoftware.holograms.internal;

import eu.decentsoftware.holograms.actions.ClickActionHolder;
import eu.decentsoftware.holograms.conditions.ClickConditionHolder;

public interface PluginClickable {

    ClickActionHolder getClickActions();

    ClickConditionHolder getClickConditions();

}
