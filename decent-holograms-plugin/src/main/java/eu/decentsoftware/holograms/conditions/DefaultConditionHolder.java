package eu.decentsoftware.holograms.conditions;

import eu.decentsoftware.holograms.api.conditions.Condition;
import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import eu.decentsoftware.holograms.api.utils.collection.DecentList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DefaultConditionHolder implements ConditionHolder {

    private final @NotNull List<Condition> conditions;

    public DefaultConditionHolder() {
        this(new DecentList<>());
    }

    public DefaultConditionHolder(@NotNull List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public void addCondition(@NotNull Condition condition) {
        this.conditions.add(condition);
    }

    @Override
    public void removeCondition(@NotNull Condition condition) {
        this.conditions.remove(condition);
    }

    @Override
    public void removeCondition(int index) {
        this.conditions.remove(index);
    }

    @Override
    public void clearConditions() {
        this.conditions.clear();
    }

    @NotNull
    @Override
    public List<Condition> getConditions() {
        return conditions;
    }

}
