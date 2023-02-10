/*
 * DecentHolograms
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.conditions;

public class ConditionTypeRegistry {

//    private final @NotNull Set<ConditionType> types;
//
//    /**
//     * Create a new instance of {@link DefaultConditionTypeRegistry}. This constructor
//     * also registers all default condition types.
//     */
//    public DefaultConditionTypeRegistry() {
//        this.types = new HashSet<>();
//
//        // Register default condition types
//        register(PERMISSION);
//        register(HAS_MONEY);
//        register(REGEX);
//    }
//
//    @Override
//    public void register(@NotNull ConditionType type) {
//        this.types.add(type);
//    }
//
//    @Override
//    public void remove(@NotNull ConditionType type) {
//        this.types.remove(type);
//    }
//
//    @Override
//    public void remove(@NotNull String name) {
//        for (ConditionType type : new HashSet<>(this.types)) {
//            if (type.isAlias(name)) {
//                this.types.remove(type);
//                return;
//            }
//        }
//    }
//
//    @Nullable
//    @Override
//    public ConditionType get(@NotNull String name) {
//        for (ConditionType type : this.types) {
//            if (type.isAlias(name)) {
//                return type;
//            }
//        }
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public Set<ConditionType> getTypes() {
//        return this.types;
//    }
//
//    // =========================================================== //
//
//    public static final ConditionType PERMISSION = new ConditionType("PERMISSION", "PERM") {
//        @Nullable
//        @Override
//        public Condition createCondition(String... data) {
//            if (data.length > 0) {
//                return new PermissionCondition(data[0]);
//            }
//            return null;
//        }
//
//        @Nullable
//        @Override
//        public Condition createCondition(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            if (object.has("permission")) {
//                return new PermissionCondition(object.get("permission").getAsString());
//            }
//            return null;
//        }
//    };
//
//    public static final ConditionType HAS_MONEY = new ConditionType("HAS_MONEY", "MONEY") {
//        @Nullable
//        @Override
//        public Condition createCondition(String... data) {
//            if (data.length > 0) {
//                double amount = Double.parseDouble(data[0]);
//                return new MoneyCondition(amount);
//            }
//            return null;
//        }
//
//        @Nullable
//        @Override
//        public Condition createCondition(@NotNull JsonElement json) {
//            JsonObject object = json.getAsJsonObject();
//            double amount = object.get("amount").getAsDouble();
//            return new MoneyCondition(amount);
//        }
//    };
//
//    public static final ConditionType REGEX = new ConditionType() {
//        @Nullable
//        @Override
//        public Condition createCondition(String... data) {
//            if (data.length < 2) {
//                return null;
//            }
//            String patternString = data[0];
//            String input = data[1];
//            try {
//                Pattern pattern = Pattern.compile(patternString);
//                return new RegexCondition(pattern, input);
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//        @Nullable
//        @Override
//        public Condition createCondition(@NotNull JsonElement json) {
//            String patternString = json.getAsJsonObject().get("pattern").getAsString();
//            String input = json.getAsJsonObject().get("input").getAsString();
//            if (patternString == null || input == null) {
//                return null;
//            }
//            try {
//                Pattern pattern = Pattern.compile(patternString);
//                return new RegexCondition(pattern, input);
//            } catch (Exception e) {
//                return null;
//            }
//        }
//    };

}
