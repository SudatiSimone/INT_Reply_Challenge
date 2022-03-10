package arc.challenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Algorithm {
	
	List<Integer> indexes = new LinkedList<>();
	Integer totalScore = 0;
	List<DemonExtended> aliveDemons;
	Demon[] koDemons;

	public List<Integer> solve(Pandora pandora, List<Demon> demons) {
		aliveDemons = demons.stream().map(DemonExtended::new).collect(Collectors.toList());
		koDemons = new Demon[pandora.totalTurns];
		for(int t = 0; t < pandora.totalTurns && !aliveDemons.isEmpty(); t++) {
			// Reload stamina
			for(int k = 0; k < t; k++) {
				if(koDemons[k] != null && koDemons[k].staminaCooldown + k <= t) {
					pandora.currentStamina = Math.min(koDemons[k].staminaRestored + pandora.currentStamina, pandora.totalStamina);
					koDemons[k] = null;
				}
			}
			// Choose next demon
			Optional<DemonExtended> demonOpt = chooseNextDemon(aliveDemons, pandora, t);
			if(demonOpt.isPresent()) {
				DemonExtended choosenOne = demonOpt.get();
				// Defeat demon
				pandora.currentStamina -= choosenOne.staminaCost;
				koDemons[t] = choosenOne;
				aliveDemons.remove(choosenOne);
				indexes.add(choosenOne.index);
				totalScore += choosenOne.totalPoints;
			} else {
				// Rest 1 turn
			}
			// TODO? count points
		}
		return indexes;
	}
	
	public Optional<DemonExtended> chooseNextDemon(List<DemonExtended> aliveDemons, Pandora pandora, int t) {
		aliveDemons.forEach(d -> d.init(t, pandora.totalTurns));
//		Optional<DemonExtended> demonOpt = aliveDemons.stream().filter(d -> pandora.currentStamina >= d.staminaCost).sorted(new DemonComparator()).findFirst();
		List<DemonExtended> sortedAliveDemons = aliveDemons.stream().filter(d -> pandora.currentStamina >= d.staminaCost).sorted(new DemonComparator()).collect(Collectors.toList());
		
		Optional<DemonExtended> demonOpt = Optional.ofNullable(sortedAliveDemons.size() > 0 ? sortedAliveDemons.get(0) : null);
		// Simulazione turno corrente
		int currentStaminaTmp = pandora.currentStamina;
		Demon[] koDemonsTmp = Arrays.copyOf(koDemons, pandora.totalTurns);
		List<DemonExtended> aliveDemonsTmp = new ArrayList<>(aliveDemons);
		List<Integer> indexesTmp = new LinkedList<>(indexes);
		Integer totalScoreTmp = totalScore;
		if(demonOpt.isPresent()) {
			DemonExtended choosenOne = demonOpt.get();
			// Defeat demon
			currentStaminaTmp -= choosenOne.staminaCost;
			koDemonsTmp[t] = choosenOne;
			aliveDemonsTmp.remove(choosenOne);
			indexesTmp.add(choosenOne.index);
			totalScoreTmp += choosenOne.totalPoints;
		}
		// Reload stamina
		for(int k = 0; k < t + 1; k++) {
			if(koDemonsTmp[k] != null && koDemonsTmp[k].staminaCooldown + k <= t + 1) {
				currentStaminaTmp = Math.min(koDemonsTmp[k].staminaRestored + currentStaminaTmp, pandora.totalStamina);
				koDemonsTmp[k] = null;
			}
		}
		// Simulazione turno successivo
		int finalCurrentStaminaTmp = currentStaminaTmp;
		aliveDemonsTmp.forEach(d -> d.init(t + 1, pandora.totalTurns));
		Optional<DemonExtended> demonOptTmp = aliveDemonsTmp.stream().filter(d -> finalCurrentStaminaTmp >= d.staminaCost).sorted(new DemonComparator()).findFirst();
		if(demonOptTmp.isPresent()) {
			DemonExtended choosenOne = demonOptTmp.get();
			// Defeat demon
			currentStaminaTmp -= choosenOne.staminaCost;
			koDemonsTmp[t] = choosenOne;
			aliveDemonsTmp.remove(choosenOne);
			indexesTmp.add(choosenOne.index);
			totalScoreTmp += choosenOne.totalPoints;
		}
		
		Optional<DemonExtended> demonOpt2 = Optional.ofNullable(sortedAliveDemons.size() > 1 ? sortedAliveDemons.get(1) : null);
		// Simulazione turno corrente
		int currentStaminaTmp2 = pandora.currentStamina;
		Demon[] koDemonsTmp2 = Arrays.copyOf(koDemons, pandora.totalTurns);
		List<DemonExtended> aliveDemonsTmp2 = new ArrayList<>(aliveDemons);
		List<Integer> indexesTmp2 = new LinkedList<>(indexes);
		Integer totalScoreTmp2 = totalScore;
		if(demonOpt2.isPresent()) {
			DemonExtended choosenOne = demonOpt2.get();
			// Defeat demon
			currentStaminaTmp2 -= choosenOne.staminaCost;
			koDemonsTmp2[t] = choosenOne;
			aliveDemonsTmp2.remove(choosenOne);
			indexesTmp2.add(choosenOne.index);
			totalScoreTmp2 += choosenOne.totalPoints;
		}
		// Reload stamina
		for(int k = 0; k < t + 1; k++) {
			if(koDemonsTmp2[k] != null && koDemonsTmp2[k].staminaCooldown + k <= t + 1) {
				currentStaminaTmp2 = Math.min(koDemonsTmp2[k].staminaRestored + currentStaminaTmp2, pandora.totalStamina);
				koDemonsTmp2[k] = null;
			}
		}
		// Simulazione turno successivo
		int finalCurrentStaminaTmp2 = currentStaminaTmp2;
		aliveDemonsTmp2.forEach(d -> d.init(t + 1, pandora.totalTurns));
		Optional<DemonExtended> demonOptTmp2 = aliveDemonsTmp2.stream().filter(d -> finalCurrentStaminaTmp2 >= d.staminaCost).sorted(new DemonComparator()).findFirst();
		if(demonOptTmp2.isPresent()) {
			DemonExtended choosenOne = demonOptTmp2.get();
			// Defeat demon
			currentStaminaTmp2 -= choosenOne.staminaCost;
			koDemonsTmp2[t] = choosenOne;
			aliveDemonsTmp2.remove(choosenOne);
			indexesTmp2.add(choosenOne.index);
			totalScoreTmp2 += choosenOne.totalPoints;
		}
		return totalScoreTmp > totalScoreTmp2 ? demonOpt : demonOpt2;
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
