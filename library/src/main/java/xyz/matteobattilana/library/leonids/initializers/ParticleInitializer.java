package xyz.matteobattilana.library.leonids.initializers;

import java.util.Random;

import xyz.matteobattilana.library.leonids.Particle;

public interface ParticleInitializer {

	void initParticle(Particle p, Random r);

}
