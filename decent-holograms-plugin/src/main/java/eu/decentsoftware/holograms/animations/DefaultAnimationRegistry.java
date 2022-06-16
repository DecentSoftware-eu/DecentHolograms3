package eu.decentsoftware.holograms.animations;

import eu.decentsoftware.holograms.api.animations.Animation;
import eu.decentsoftware.holograms.api.utils.collection.Registry;

public class DefaultAnimationRegistry extends Registry<String, Animation> {

    public DefaultAnimationRegistry() {
        this.reload();
    }

    @Override
    public void reload() {
        
    }

}
