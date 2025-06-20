package martian.minefactorial.foundation.client;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Generic helper functions for particles.
 */
public final class ParticleHelper
{
	private ParticleHelper()
	{
	}

	/**
	 * Adds a dust particle at the given location.
	 *
	 * @param options Particle options to use.
	 * @param level   The level to spawn the particle in.
	 * @param pos     Where to spawn the particle.
	 * @param vel     Velocity for the particle.
	 */
	public static void addDust(DustParticleOptions options, Level level, Vec3 pos, Vec3 vel)
	{
		level.addParticle(options, pos.x, pos.y, pos.z, vel.x, vel.y, vel.z);
	}

	/**
	 * Adds a dust particle at the given location.
	 *
	 * @param options Particle options to use.
	 * @param level   The level to spawn the particle in.
	 * @param pos     Where to spawn the particle.
	 */
	public static void addDust(DustParticleOptions options, Level level, Vec3 pos)
	{
		addDust(options, level, pos, Vec3.ZERO);
	}

	/**
	 * Adds many dust particles inside the given block pos.
	 *
	 * @param options Particle options to use.
	 * @param level   The level to spawn particles in.
	 * @param pos     Where to spawn the particles.
	 */
	public static void addDustInBlock(DustParticleOptions options, Level level, BlockPos pos)
	{
		addDustInBlock(options, level, pos, 1, 3f);
	}

	/**
	 * Adds many dust particles inside the given block pos or around the position.
	 * The total amount of particles spawned is {@code (spread*2)^3}.
	 *
	 * @param options Particle options to use.
	 * @param level   The level to spawn particles in.
	 * @param pos     Where to spawn the particles.
	 * @param spread  A range to spawn particles in. A value of 1 will only spawn
	 *                particles inside the provided block pos.
	 * @param density How close together particles should spawn.
	 */
	public static void addDustInBlock(DustParticleOptions options, Level level, BlockPos pos, int spread, float density)
	{
		var centre = pos.getCenter();
		// insane loop nesting, go!
		for (int y = -spread ; y < spread + 1 ; y++)
			for (int x = -spread ; x < spread + 1 ; x++)
				for (int z = -spread ; z < spread + 1 ; z++)
					addDust(options, level, centre.add(x / density, y / density, z / density));
	}
}
