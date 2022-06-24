package eu.decentsoftware.holograms.conditions;

import eu.decentsoftware.holograms.api.conditions.ConditionHolder;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class DefaultConditionHolder extends ConditionHolder {

    @Override
    public void load(@NotNull ConfigurationSection config) {
        for (String key : config.getKeys(false)) {
            add(DefaultCondition.load(config.getConfigurationSection(key)));
        }
    }

}
