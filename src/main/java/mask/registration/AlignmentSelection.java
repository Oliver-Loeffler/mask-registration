package mask.registration;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

class AlignmentSelection implements Supplier<Predicate<Displacement>> {
	
	private final Predicate<Displacement> selection;

	AlignmentSelection(Predicate<Displacement> selection) {
		this.selection = Objects.requireNonNull(selection, "Predicate for alignment selection must not be null.");
	}
	
	CalculationSelection forCalculation(Predicate<Displacement> selection) {
		return new CalculationSelection(this, selection);
	}

	@Override
	public Predicate<Displacement> get() {
		return this.selection;
	}
}