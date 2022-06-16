package eu.decentsoftware.holograms.api.component.common;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents a component that can be named.
 *
 * @author d0by
 */
public interface INameable {

    /**
     * Get the name of this component.
     *
     * @return The name of this component.
     */
    @NotNull
    String getName();

}
