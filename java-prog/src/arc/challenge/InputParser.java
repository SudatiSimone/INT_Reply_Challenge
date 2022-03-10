package arc.challenge;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InputParser {

	public BufferedReader br;
	
	public InputParser(String file) throws FileNotFoundException {
		br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	}
	
	public Pandora parsePandora() throws IOException {
		String pandoraLine = br.readLine();
		String[] splits = pandoraLine.split(" ");
		Pandora pandora = new Pandora();
		pandora.currentStamina = Integer.parseInt(splits[0]);
		pandora.totalStamina = Integer.parseInt(splits[1]);
		pandora.totalTurns = Integer.parseInt(splits[2]);
		pandora.totalDemons = Integer.parseInt(splits[3]);
		return pandora;
	}
	
	public List<Demon> parseDemons(Pandora pandora) throws IOException {
		List<Demon> demons = new ArrayList<>();
		for(int i = 0; i < pandora.totalDemons; i++) {
			String demonLine = br.readLine();
			String[] splits = demonLine.split(" ");
			Demon demon = new Demon();
			demon.index = i;
			demon.staminaCost = Integer.parseInt(splits[0]);
			if(demon.staminaCost <= pandora.totalStamina) {
				demon.staminaCooldown = Integer.parseInt(splits[1]);
				demon.staminaRestored = Integer.parseInt(splits[2]);
				demon.totalTurns = Integer.parseInt(splits[3]);
				List<Integer> points = new ArrayList<>();
				for(int j = 0; j < demon.totalTurns; j++) {
					points.add(Integer.parseInt(splits[j + 4]));
				}
				demon.points = points;
				demons.add(demon);
			}
		}
		return demons;
	}
	
	public void close() throws IOException {
		br.close();
	}
}
