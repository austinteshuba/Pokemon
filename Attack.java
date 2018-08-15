//Austin Teshuba
//Attack.java
//This is the class that will be used to create attack objects. It contains information such as the name, energy cost, damage, etc.

import java.util.*;//import utilities

public class Attack {
	private String name;//string for the name
	private int energyCost;//energy cost of the attack
	private int damage;//the damage the attack does
	private final String[] SPECIALS = {" ", "STUN", "WILD CARD", "WILD STORM", "DISABLE", "RECHARGE"};//this is all the possibilities of specials by index
	private boolean stun = false;//BOOLEAN FOR STUN
	private boolean wildCard = false;//boolean for the wildcard
	private boolean wildStorm = false;//boolean for the wildstorm
	private boolean disable = false;//boolean for disable
	private boolean recharge = false;//boolean for recharge
	
	public Attack (String n, int e, int d, String s) {//initializer for the attack
		name = n;//set the name to n
		energyCost = e;//set energy to e
		damage = d;//set damage to d
		int index = Arrays.asList(SPECIALS).indexOf(s.toUpperCase());//get the index of the special from the specials array
		if (index==1) {//if the index is 1, stun
			stun=true;
		} else if (index==2) { //if the index is 2, wildcard
			wildCard=true;
		} else if (index==3) {//if the index is 3, wildstorm
			wildStorm = true;
		} else if (index==4) {//if the index is 4, disable
			disable = true;
		} else if (index==5) {//if the index is 5, recharge
			recharge = true;
		}
		//stun = s;
	}
	public int getEnergy() {//this method gets the energy
		return energyCost;
	}
	public int getDamage() {
		return damage;
	}
	public boolean getStun() {
		return stun;
	}
	public boolean getWildCard() {
		return wildCard;
	}
	public boolean getWildStorm() {
		return wildStorm;
	}
	public boolean getDisable() {
		return disable;
	}
	public boolean getRecharge() {
		return recharge;
	}
	public String toString() {//this method returns a string describing the attack
		String specials = "";
		
		if (stun) {
			specials += "STUN,";
		}
		if (wildCard) {
			specials += "WILDCARD,";
		}
		if (wildStorm) {
			specials += "WILDSTORM,";
		}
		if (disable) {
			specials += "DISABLE,";
		}
		if (recharge) {
			specials += "RECHARGE,";
		}
		return String.format("Name: %s, Energy Cost: %d, Damage: %d, Specials: %s", name, energyCost, damage, specials);
	}
	//Hey! This was a bad implementation. Just ignore it, unless if my other method is wrong, then pls look here for pity marks
//	public boolean attackPokemon(Pokemon target, Pokemon from, boolean playerAttacks) {//this method actually performs the attack. target = target pokemon, from = attacking pokemon, playerAttacks if true means that the player is launching the attack
//		from.subtractEnergy(energyCost);//subtract the energy cost from the attacking pokemon
//		//normal damage
//		int damageCopy = damage;//set the damage copy to have the normal damage
//		//resistance
//		if (target.getResistanceInt() == from.getType()) {//if the target is resistant to the  attacks
//			damageCopy/=2;//halve the damage
//			if (playerAttacks) {//announce the resistance
//				System.out.printf("The enemy is resistant to your attacks. Damage is now halved to: %d\n", damageCopy);
//			} else {
//				System.out.printf("You are resistant to the ememy's attacks. Damage is now halved to: %d\n", damageCopy);
//			}
//		}
//		//weakness
//		if (target.getWeaknessInt() == from.getType()) {//if the target is weak to the attacks
//			damageCopy*=2;//double the damage
//			if (playerAttacks) {//announce the weakness
//				System.out.printf("The enemy is weak to your attacks. Damage is now doubled to: %d\n", damageCopy);
//			} else {
//				System.out.printf("You are weak to the ememy's attacks. Damage is now doubled to: %d\n", damageCopy);
//			}
//		}
//		if (from.getDisable()) {//if the attacking pokemon is disabled
//			if (playerAttacks) {//announce disability
//				System.out.println("You are disabled, and your attack has weakened by 10 HP");
//			} else {
//				System.out.println("The enemy is disabled, and its attack has weakened by 10 HP");
//			}
//			damageCopy-=10;//reduce damage by 10 to a minimum of 0
//			if (damageCopy<0) {
//				damageCopy = 0;
//			}
//		}
//		System.out.printf("Initial damage before specials: %d\n", damageCopy);//announce initial damage
//		if(stun) {//if stun is a special
//			if (from.flip()) {//50% chance of happening
//				if (target.getStun()) {//if the target is already stunned you cant stun again
//					System.out.println("Target is already stunned.");
//				} else {//otherwise, stun them!
//					target.setStun(true);//stun!
//					System.out.print("Stunned!\n");
//				}
//			} else {//if the chance fails, announce
//				System.out.println("Stun failed");
//			}
//		} if (wildCard) {//if the attack is a special
//			if (from.flip()) {//50% chance of failing
//				System.out.println("The wildcard didn't hit! Damage = 0");
//				damageCopy=0;//if fail, reduce damage to 0
//			} else {//if it didnt fail, do nothing and announce
//				System.out.println("Wildcard was successful!");
//			}
//		} if (wildStorm) {//if the wildstorm is a special
//			int counter=0;//counter for amount of times the attack worked
//			int adjustedDamage = damageCopy;//this is for the resistance and weakness. store the damage copy
//			damageCopy=0;//set the damage copy to 0
//			while (true) {
//				if (from.flip()) {//50% chance of failure
//					break;//leave the loop
//				} else {
//					damageCopy+=adjustedDamage;//add the adjusted initial damage
//					counter+=1;//add to the counter
//				}
//			}
//			System.out.printf("The attack was successful %d time(s), causing a total damage of %d\n", counter, damageCopy);
//		} if (disable) {//if the disable is a special
//			target.setDisable(true);//make the target disabled
//			System.out.println("Successfully Disabled!");
//			
//		} if (recharge) {//if the recharge is a special
//			from.addEnergy(20);//add 20 energy units to the attacking pokemon, to a max of 50
//			if (from.getEnergy()>50) {
//				from.setEnergy(50);
//			}
//			System.out.printf("Successfully recharged! Energy increased to %d!\n", from.getEnergy());//announce
//		} 
//		target.subtractHP(damageCopy);//subtract the damage from the target's HP
//		if (target.getHP() <=0) {//if the target pokemon has died
//			System.out.println("The target pokemon has been killed.");//announce the death
//			return true;//return true (this means something died)
//		} else {
//			if (playerAttacks) {
//				System.out.printf("Remaining enemy pokemon's HP: %d \n", target.getHP());//announce updated stats
//			} else {
//				System.out.printf("Your pokemon's HP: %d \n", target.getHP());
//			}
//			return false;//return false (this means no one died)
//			
//		}
//		
//	}
	
}
