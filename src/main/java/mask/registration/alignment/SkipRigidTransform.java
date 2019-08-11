package mask.registration.alignment;

import java.util.Collection;

import mask.registration.Displacement;

public class SkipRigidTransform implements RigidTransform {
	
	@Override
	public Collection<Displacement> apply(Collection<Displacement> d) {
		return d;
	}

	@Override
	public double getTranslationX() { return 0.0; }

	@Override
	public double getTranslationY() { return 0.0; }

	@Override
	public double getRotation() { return 0.0; }

}
