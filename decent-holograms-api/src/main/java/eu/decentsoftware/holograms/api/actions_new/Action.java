package eu.decentsoftware.holograms.api.actions_new;

import eu.decentsoftware.holograms.api.profile.Profile;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface Action {

    void execute(@NotNull Profile profile);

    Set<String> getAliases();

    boolean checkChance();

    long getDelay();

    double getChance();

}
