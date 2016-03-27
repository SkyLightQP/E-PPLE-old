package me.efe.fortest;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.util.ParticleEffect;

import org.bukkit.Location;

public class EfeCylinderEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public ParticleEffect particle = ParticleEffect.FLAME;

    /**
     * Amount of particles
     */
    public int particles = 50;

    /**
     * Radius of the cylinder
     */
    public float radius = 0.7F;
    
    /**
     * Height of the cylinder
     */
    public float height = 2.0F;

    public EfeCylinderEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 10;
        iterations = 20;
    }

    @Override
    public void onRun() {
    	Location location = getLocation().clone();
        
    	for (int j = 0; j < (height / 0.3F); j ++) {
    		location.add(0, 0.3F, 0);
    		
    		for (int i = 0; i < particles; i++) {
                double angle, x, z;
                angle = 2 * Math.PI * i / particles;
                x = Math.cos(angle) * radius;
                z = Math.sin(angle) * radius;
                location.add(x, 0, z);
                display(particle, location);
                location.subtract(x, 0, z);
            }
    	}
    }
}