
//Austin Teshuba
//Pokemon.java
//This is the pokemon class. This is a class that will be used to make pokemon objects, containing their energy, hp, name, weaknesses, and other info
import java.util.*;//import utilities

public class Pokemon {
	private String name;// make fields for the name
	private int hp;// hp
	private int energy;// energy
	private final String[] TYPES = { "EARTH", "FIRE", "GRASS", "WATER", "FIGHTING", "ELECTRIC", "LEAF" };// This is a final array of the types																									// types.
	private int type;// index of the type in the above array
	private String resistance;// string of the resistance
	private String weakness;// string of the weakness
	private boolean disabled;// disabled boolean
	private boolean stun;// stun boolean
	private Attack[] attacks;// currently uninitialized
	private int resistanceInt;// index of the resistance in the TYPES array
	private int weaknessInt;// index of the weakness in the TYPES array

	public Pokemon(String inp) {// pokemon initializer from a string (parse string as per data file format)
		// System.out.println(inp);
		String[] inpArr = inp.split(",");// make a string array from the comma seperated information in the inputted
											// string
		name = inpArr[0];// name is the first string
		hp = Integer.parseInt(inpArr[1]);// the hp is the integer in the second string
		String typeText = inpArr[2].toUpperCase();// store the type text and make it uppercase
		// System.out.println(typeText);
		type = Arrays.asList(TYPES).indexOf(typeText);// set type to the index of the typeText in the TYPES array
		// System.out.println(type);
		resistance = inpArr[3];// resistance is the 4th string
		weakness = inpArr[4];// weakness is the 5th string
		resistanceInt = Arrays.asList(TYPES).indexOf(inpArr[3].toUpperCase());// set the resistance int to the index of
																				// the type in the TYPES array
		weaknessInt = Arrays.asList(TYPES).indexOf(inpArr[4].toUpperCase());// set the weakness int to the index of the
																			// type in the TYPES array
		int n = Integer.parseInt(inpArr[5]);// this is how many attacks there are

		attacks = new Attack[n];// make the attacks array have n amount of spots
		energy = 50;// set energy to 50 (default)

		for (int x = 0; x < n; x++) {// iterate through the n attacks
			attacks[x] = new Attack(inpArr[6 + x * 4], Integer.parseInt(inpArr[7 + x * 4]),
					Integer.parseInt(inpArr[8 + x * 4]), inpArr[9 + x * 4]);// set each spot in the attacks array to be
																			// a new attack object. Initialize attack
																			// object.
		}
	}

	public String getName() {// this method gets the name of the pokemon
		return name;
	}

	public void setStun(boolean x) {// this method sets the stun field to be true or false
		stun = x;
	}

	public boolean getStun() {// this method gets the stun state
		return stun;
	}

	public Attack[] getAttacks() {// this method gets the attacks array
		return attacks;
	}

	public int getEnergy() {// this method gets the pokemon's energy
		return energy;
	}

	public void addEnergy(int addEnergy) {// this method adds energy to the energy field
		energy += addEnergy;
	}

	public void setEnergy(int energy) {// this method sets the energy field
		this.energy = energy;
	}

	public void subtractEnergy(int energy) {// this method subtracts energy from the energy field
		this.energy -= energy;
	}

	public void setDisable(boolean x) {// this method sets the state of the disabled boolean
		disabled = x;
	}

	public boolean getDisable() {// this method gets the state of the disabled boolean
		return disabled;
	}

	public void setHP(int x) {// this method sets the HP of the pokemon
		hp = x;
	}

	public void addHP(int x) {// this method adds HP to the Hp field
		hp += x;
	}

	public int getHP() {// this method gets the pokemon's HP
		return hp;
	}

	public void subtractHP(int x) {// this method stubtracts units from the pokemon's HP
		hp -= x;
	}

	public int getResistanceInt() {// this method gets the resistance int field
		return resistanceInt;
	}

	public int getWeaknessInt() {// this method gets the weakness int field
		return weaknessInt;
	}

	public static boolean flip() {// this method returns true half the time and false half the time
		return Math.random() < 0.5;
	}

	public int getType() {// this method gets the type of the pokemon
		return type;
	}

	public String toString() {// this method returns a string describing the current state of the pokemon.
		return String.format("Name: %s, HP: %d, Type: %s, Energy: %d, Resistance: %s, Weakness: %s", name, hp,
				TYPES[type], energy, resistance, weakness);
	}
	
	public boolean attackPokemon(Pokemon target, Attack attack, boolean playerAttacks) {//this method actually performs the attack. target = target pokemon, from = attacking pokemon, playerAttacks if true means that the player is launching the attack
		energy -= attack.getEnergy();//subtract the energy cost from the attacking pokemon
		//normal damage
		int damageCopy = attack.getDamage();//set the damage copy to have the normal damage
		//resistance
		if (target.getResistanceInt() == type) {//if the target is resistant to the  attacks
			damageCopy/=2;//halve the damage
			if (playerAttacks) {//announce the resistance
				System.out.printf("The enemy is resistant to your attacks. Damage is now halved to: %d\n", damageCopy);
			} else {
				System.out.printf("You are resistant to the ememy's attacks. Damage is now halved to: %d\n", damageCopy);
			}
		}
		//weakness
		if (target.getWeaknessInt() == type) {//if the target is weak to the attacks
			damageCopy*=2;//double the damage
			if (playerAttacks) {//announce the weakness
				System.out.printf("The enemy is weak to your attacks. Damage is now doubled to: %d\n", damageCopy);
			} else {
				System.out.printf("You are weak to the ememy's attacks. Damage is now doubled to: %d\n", damageCopy);
			}
		}
		if (disabled) {//if the attacking pokemon is disabled
			if (playerAttacks) {//announce disability
				System.out.println("You are disabled, and your attack has weakened by 10 HP");
			} else {
				System.out.println("The enemy is disabled, and its attack has weakened by 10 HP");
			}
			damageCopy-=10;//reduce damage by 10 to a minimum of 0
			if (damageCopy<0) {
				damageCopy = 0;
			}
		}
		System.out.printf("Initial damage before specials: %d\n", damageCopy);//announce initial damage
		if(attack.getStun()) {//if stun is a special
			if (flip()) {//50% chance of happening
				if (target.getStun()) {//if the target is already stunned you cant stun again
					System.out.println("Target is already stunned.");
				} else {//otherwise, stun them!
					target.setStun(true);//stun!
					System.out.print("Stunned!\n");
				}
			} else {//if the chance fails, announce
				System.out.println("Stun failed");
			}
		} if (attack.getWildCard()) {//if the attack is a special
			if (flip()) {//50% chance of failing
				System.out.println("The wildcard didn't hit! Damage = 0");
				damageCopy=0;//if fail, reduce damage to 0
			} else {//if it didnt fail, do nothing and announce
				System.out.println("Wildcard was successful!");
			}
		} if (attack.getWildStorm()) {//if the wildstorm is a special
			int counter=0;//counter for amount of times the attack worked
			int adjustedDamage = damageCopy;//this is for the resistance and weakness. store the damage copy
			damageCopy=0;//set the damage copy to 0
			while (true) {
				if (flip()) {//50% chance of failure
					break;//leave the loop
				} else {
					damageCopy+=adjustedDamage;//add the adjusted initial damage
					counter+=1;//add to the counter
				}
			}
			System.out.printf("The attack was successful %d time(s), causing a total damage of %d\n", counter, damageCopy);
		} if (attack.getDisable()) {//if the disable is a special
			target.setDisable(true);//make the target disabled
			System.out.println("Successfully Disabled!");
			
		} if (attack.getRecharge()) {//if the recharge is a special
			energy+=20;//add 20 energy units to the attacking pokemon, to a max of 50
			if (energy>50) {
				energy=50;
			}
			System.out.printf("Successfully recharged! Energy increased to %d!\n", energy);//announce
		} 
		target.subtractHP(damageCopy);//subtract the damage from the target's HP
		if (target.getHP() <=0) {//if the target pokemon has died
			System.out.println("The target pokemon has been killed.");//announce the death
			return true;//return true (this means something died)
		} else {
			if (playerAttacks) {
				System.out.printf("Remaining enemy pokemon's HP: %d \n", target.getHP());//announce updated stats
			} else {
				System.out.printf("Your pokemon's HP: %d \n", target.getHP());
			}
			return false;//return false (this means no one died)
			
		}
		
	}

}
