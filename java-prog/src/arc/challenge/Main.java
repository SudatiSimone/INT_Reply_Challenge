package arc.challenge;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
	
		List<String> inputFiles = Arrays.asList("00","01","02","03","04","05");
		String inputDirectory = "input";
		String outputDirectory = "output";
		
		
		for(String file : inputFiles) {
			Algorithm algo = new Algorithm();
			System.out.println("FILE: " + file);
			// Parse input
			InputParser inputParser = new InputParser(inputDirectory + "/" + file);
			Pandora pandora = inputParser.parsePandora();
			List<Demon> demons = inputParser.parseDemons(pandora);
			inputParser.close();
			// Solutioning
			List<Integer> indexes = algo.solve(pandora, demons);
			// Parse output
			OutputParser outputParser = new OutputParser(outputDirectory + "/" + file + "_out");
			outputParser.parseOutput(indexes);
			outputParser.close();
		}
	}
	
}
