package net.raumzeitfalle.registration.examples;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class DemoTest {

	@Test
	void runDemos() {
		List<Runnable> examples = new ArrayList<>();
		
		examples.add(()->DemoFourpointsOnlyWithMissingMeas.main());
		examples.add(()->DemoFourpointsScanner.main());
		examples.add(()->DemoFourpointsStandard.main());
		examples.add(()->DemoMultipoint.main());
		examples.add(()->DemoMultipointMagnification.main());
		examples.add(()->DemoMultipointOneDimensional.main());
		examples.add(()->DemoMultipointResidual.main());
		examples.add(()->DemoUnaligned.main());
		
		
		assertDoesNotThrow(()->examples.forEach(Runnable::run));
		
	}
	
}