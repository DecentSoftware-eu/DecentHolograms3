package eu.decent.holograms.animations;

import eu.decent.holograms.api.animations.Animation;
import eu.decent.holograms.api.utils.collection.Registry;

public class DefaultAnimationRegistry extends Registry<String, Animation> {

    public DefaultAnimationRegistry() {
        this.reload();
    }

    @Override
    public void reload() {
        
    }

}
