package arc.challenge;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Algorithm {

	public List<Integer> solve(Pandora pandora, List<Demon> demons) {
		List<Integer> indexes = new LinkedList<>();
		List<DemonExtended> aliveDemons = demons.stream().map(DemonExtended::new).collect(Collectors.toList());
		Demon[] koDemons = new Demon[pandora.totalTurns];
		for(int t = 0; t < pandora.totalTurns && !aliveDemons.isEmpty(); t++) {
			// Reload stamina
			for(int k = 0; k < t; k++) {
				if(koDemons[k] != null && koDemons[k].staminaCooldown + k <= t) {
					pandora.currentStamina += Math.min(koDemons[k].staminaRestored, pandora.totalStamina);
					koDemons[k] = null;
				}
			}
			// Choose next demon
			Optional<? extends Demon> demonOpt = chooseNextDemon(aliveDemons, pandora, t);
			if(demonOpt.isPresent()) {
				Demon choosenOne = demonOpt.get();
				// Defeat demon
				pandora.currentStamina -= choosenOne.staminaCost;
				koDemons[t] = choosenOne;
				aliveDemons.remove(choosenOne);
				indexes.add(choosenOne.index);
			} else {
				// Rest 1 turn
			}
			// TODO? count points
		}
		return indexes;
	}
	
	public Optional<? extends Demon> chooseNextDemon(List<DemonExtended> aliveDemons, Pandora pandora, int t) {
		aliveDemons.forEach(d -> d.init(t, pandora.totalTurns));
		Optional<? extends Demon> demonOpt = aliveDemons.stream().filter(d -> pandora.currentStamina >= d.staminaCost).sorted(new DemonComparator()).findFirst();
		return demonOpt;
	}
	
	class DemonExtended extends Demon {
		
		public DemonExtended(Demon d) {
			this.index = d.index;
			this.staminaRestored = d.staminaRestored;
			this.staminaCooldown = d.staminaCooldown;
			this.staminaCost = d.staminaCost;
			this.totalTurns = d.totalTurns;
			this.points = new ArrayList<>(d.points);
		}
		
		public int totalPoints = 0;
		public float pointRatio;
		
		public void init(int tAtt, int tMax) {
			for(int t = 0; (t < tMax-tAtt) && t < totalTurns; t++) {
				totalPoints += points.get(t);
			}
			if(staminaCost > 0) {
				pointRatio = ((float) totalPoints) / ((float) staminaCost);
			} else {
				pointRatio = totalPoints + 1;
			}
		}
		
		@Override
		public boolean equals(Object o) {
			if(o instanceof Demon) {
				Demon d = (Demon)o;
				return this.index == d.index;
			}
			return false;
		}
	}
	
	class DemonComparator implements Comparator<DemonExtended> {

		@Override
		public int compare(DemonExtended o1, DemonExtended o2) {
			return Float.compare(o1.pointRatio, o2.pointRatio) * (-1);
		}
	}
}
