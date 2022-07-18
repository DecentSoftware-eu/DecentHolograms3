package eu.decentsoftware.holograms.conditions;

import eu.decentsoftware.holograms.api.actions.ActionHolder;
import eu.decentsoftware.holograms.api.conditions.Condition;
import eu.decentsoftware.holograms.conditions.impl.ComparingCondition;
import eu.decentsoftware.holograms.conditions.impl.PermissionCondition;
import eu.decentsoftware.holograms.conditions.impl.RegexCondition;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

/**
 * This class represents a Condition. We can then check whether the Condition is met.
 */
@Getter
@Setter
public abstract class DefaultCondition implements Condition {

    protected boolean inverted;
    protected boolean required;
    protected ActionHolder metActions;
    protected ActionHolder notMetActions;

    /**
     * Create a new instance of {@link DefaultCondition}.
     */
    public DefaultCondition() {
        this(false);
    }

    /**
     * Create a new instance of {@link DefaultCondition}.
     *
     * @param inverted true if the condition should be inverted when checked, false otherwise.
     */
    public DefaultCondition(boolean inverted) {
        this.inverted = inverted;
        this.required = true;
    }

    /**
     * Load a condition from a configuration section.
     *
     * @param config The configuration section.
     * @return The condition.
     */
    @Nullable
    public static Condition load(@NotNull ConfigurationSection config) {
        if (!config.isString("type")) {
            return null;
        }

        // -- Get type of the condition
        String typeName = config.getString("type", "");
        String typeNameLowerTrimmed = typeName.trim().toLowerCase();
        boolean inverted = typeNameLowerTrimmed.startsWith("!") || typeNameLowerTrimmed.startsWith("not");
        ConditionType type = ConditionType.fromString(typeName);
        if (type == null) {
            return null;
        }

        // -- Create the condition if possible
        Condition condition = null;
        switch (type) {
            case STRING_EQUAL:
            case STRING_EQUAL_IGNORECASE:
            case STRING_CONTAINS:
            case EQUAL:
            case GREATER_EQUAL:
            case LESS_EQUAL:
            case LESS:
            case GREATER:
                if (config.isString("compare") && config.isString("input")) {
                    String compare = config.getString("compare", "");
                    String input = config.getString("input", "");
                    condition = new ComparingCondition(inverted, type, compare, input);
                }
                break;
            case PERMISSION:
                if (config.isString("permission")) {
                    String permission = config.getString("permission", "");
                    condition = new PermissionCondition(inverted, permission);
                }
                break;
            case REGEX:
                if (config.isString("regex") && config.isString("input")) {
                    String regex = config.getString("regex", "");
                    String input = config.getString("input", "");
                    condition = new RegexCondition(inverted, Pattern.compile(regex), input);
                }
                break;
            // TODO finish
        }

        // -- Load actions if possible
        if (condition != null) {
            // TODO: met & not met actions
//            // Met actions
//            ConfigurationSection metActionsSection = config.getConfigurationSection("met_actions");
//            if (metActionsSection != null) {
//                ActionHolder actionHolder = new DefaultActionHolder();
//                actionHolder.load(metActionsSection);
//                condition.setMetActions(actionHolder);
//            }
//            // Not Met actions
//            ConfigurationSection notMetActionsSection = config.getConfigurationSection("not_met_actions");
//            if (notMetActionsSection != null) {
//                ActionHolder actionHolder = new DefaultActionHolder();
//                actionHolder.load(notMetActionsSection);
//                condition.setNotMetActions(actionHolder);
//            }
        }
        return condition;
    }


}
