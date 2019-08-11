package mask.registration;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import mask.registration.file.FileLoader;
import mask.registration.firstorder.Alignments;
import mask.registration.firstorder.Compensations;
import mask.registration.firstorder.FirstOrderCorrection;
import mask.registration.firstorder.FirstOrderSetup;

public class DemoFourpointsScanner {

	public static void main(String ...args) {
		
		DemoFourpointsScanner demo = new DemoFourpointsScanner();
		demo.run();

	}

	private void run() {
		
		System.out.println(System.lineSeparator() + "--- DEMO: Scanner-like model ------------------");
		
		List<Displacement> displacements = new FileLoader().load(Paths.get("Demo-4Point.csv"));
		
		SiteSelection selection = SiteSelection
					.forAlignment(d -> d.isOfType(SiteClass.ALIGN))
					.forCalculation(d->true)
					.build()
					.remove(d->d.isOfType(SiteClass.INFO_ONLY));
	
		FirstOrderCorrection correction = new FirstOrderCorrection();
		FirstOrderSetup setup = new FirstOrderSetup()
										.withAlignment(Alignments.SCANNER_SELECTED)
										.withCompensations(Compensations.SCALE, Compensations.ORTHO)
										.withSiteSelection(selection);
		
		Collection<Displacement> results = correction.apply(displacements, setup);
		
		DisplacementSummary uncorrectedSummary = Displacement.summarize(displacements, selection.getCalculation());
		System.out.println(System.lineSeparator()+ "--- unaligned --------------------------------" + uncorrectedSummary);
		
		DisplacementSummary correctedSummary = Displacement.summarize(results, selection.getCalculation());
		System.out.println(System.lineSeparator()+ "--- corrected --------------------------------" + correctedSummary);
		
	}
}
