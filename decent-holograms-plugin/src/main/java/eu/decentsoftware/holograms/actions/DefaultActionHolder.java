package eu.decentsoftware.holograms.actions;

import eu.decentsoftware.holograms.api.actions.ActionHolder;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class DefaultActionHolder extends ActionHolder {

    @Override
    public void load(@NotNull ConfigurationSection config) {
        for (String key : config.getKeys(false)) {
            add(DefaultAction.load(config.getConfigurationSection(key)));
        }
    }

}
