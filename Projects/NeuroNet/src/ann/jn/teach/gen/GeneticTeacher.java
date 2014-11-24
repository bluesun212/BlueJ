package ann.jn.teach.gen;

public class GeneticTeacher {
	
	/**
	 * Defines the interface of a class that wishes to use a {@link GeneticTeacher} to
	 * teach a {@link NeuralNet}.
	 * @author Nicholas Utz
	 */
	public interface IGeneticTeacherCallbacks {
		/**
		 * Called when a cycle of evolution is complete and the next generation
		 * of {@link WeightMap}s is ready.
		 */
		public void onGenerationReady();
		
		
	}

	/**
	 * Creates a new GeneticTeacher with the 
	 * @param initialTypes
	 */
	public GeneticTeacher(int initialIndividuals) {
		
	}
}
