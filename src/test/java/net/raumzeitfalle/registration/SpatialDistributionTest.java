package net.raumzeitfalle.registration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.raumzeitfalle.registration.displacement.Displacement;

class SpatialDistributionTest {
	
	private SpatialDistribution classUnderTest;

	@BeforeEach
	void prepare() {
		 classUnderTest = new SpatialDistribution();
	}
	
	@Test
	void area_2D_distribution() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0,      0,      0,      0,         0));
		points.add(Displacement.at(1, 1,      0, 140000,      0,    140000));
		points.add(Displacement.at(2, 2, 150000, 140000, 150000,    140000));
		points.add(Displacement.at(3, 3, 150000,      0, 150000,         0));
		points.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.AREA, distribution);
		
	}
	
	@Test
	void horizontal_1D_distribution() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0,      0,  70000,      0,     70000));
		points.add(Displacement.at(1, 1,      0,  70000,      0,     70000));
		points.add(Displacement.at(2, 2, 150000,  70000, 150000,     70000));
		points.add(Displacement.at(3, 3, 150000,  70000, 150000,     70000));
		points.add(Displacement.at(4, 4,  75000,  70000, Double.NaN, Double.NaN));
		
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.HORIZONTAL, distribution);
		
	}
	
	@Test
	void vertical_1D_distribution() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0, 0,      0,     0,          0));
		points.add(Displacement.at(1, 1, 0, 140000,     0,     140000));
		points.add(Displacement.at(2, 2, 0, 140000,     0,     140000));
		points.add(Displacement.at(3, 3, 0,      0,     0,          0));
		points.add(Displacement.at(4, 4, 0,  70000, Double.NaN, Double.NaN));
			
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.VERTICAL, distribution);
		
	}
	
	@Test
	void singularity_XY() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0, 0, 140000,     0,      140000));
		points.add(Displacement.at(1, 1, 0, 140000,     0,      140000));
		points.add(Displacement.at(2, 2, 0, 140000,     0,      140000));
		points.add(Displacement.at(3, 3, 0, 140000,     0,      140000));
		points.add(Displacement.at(4, 4, 0,  70000, Double.NaN, Double.NaN));
			
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.SINGULARITY, distribution);
		
	}
	
	@Test
	void singularity_X() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0, 0, Double.NaN, 0,      Double.NaN));
			
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.SINGULARITY, distribution);
		
	}
	
	@Test
	void singularity_Y() {
		
		List<Displacement> points = new ArrayList<>(5);
		points.add(Displacement.at(0, 0, Double.NaN, 14000, Double.NaN, 14000));
			
		points.forEach(classUnderTest);
		
		Distribution distribution = classUnderTest.getDistribution();
		
		assertEquals(Distribution.SINGULARITY, distribution);
		
	}
	
	@Test
	void undefined() {
		
		List<Displacement> points = Collections.emptyList();
			
		points.forEach(classUnderTest);
		
		Throwable t = assertThrows(IllegalArgumentException.class,
				()->classUnderTest.getDistribution());
		
		assertEquals("Could not determine data distribution as no valid displacements have been processed yet.", t.getMessage());
		
	}

}
