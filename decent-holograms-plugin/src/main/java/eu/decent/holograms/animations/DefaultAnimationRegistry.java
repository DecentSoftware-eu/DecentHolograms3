package eu.decent.holograms.animations;

import eu.decent.holograms.api.animations.Animation;
import eu.decent.holograms.api.utils.collection.DictRegistry;

public class DefaultAnimationRegistry extends DictRegistry<String, Animation> {

    public DefaultAnimationRegistry() {
        this.reload();
    }

    @Override
    public void reload() {
        
    }

}
