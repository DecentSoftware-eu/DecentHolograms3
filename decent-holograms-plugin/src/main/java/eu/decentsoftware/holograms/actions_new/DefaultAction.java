package eu.decentsoftware.holograms.actions_new;

import eu.decentsoftware.holograms.api.actions_new.Action;

public abstract class DefaultAction implements Action {

    protected final long delay;
    protected final double chance;

    public DefaultAction() {
        this(0, -1.0);
    }

    public DefaultAction(long delay, double chance) {
        this.delay = delay;
        this.chance = chance;
    }

    @Override
    public boolean checkChance() {
        if (chance < 0) {
            return true;
        }
        double rand = Math.random();
        return rand < chance;
    }

    @Override
    public long getDelay() {
        return delay;
    }

    @Override
    public double getChance() {
        return chance;
    }

}
