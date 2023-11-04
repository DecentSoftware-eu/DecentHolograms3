package eu.decentsoftware.holograms.internal;

import eu.decentsoftware.holograms.actions.ClickActionHolder;
import eu.decentsoftware.holograms.conditions.ClickConditionHolder;

public interface IHasClickActions {

    ClickActionHolder getClickActions();

    ClickConditionHolder getClickConditions();

}
