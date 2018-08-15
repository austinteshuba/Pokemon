//Austin Teshuba
//Arena.java
//This is where all of the fun stuff happens! This is where all of the battles actually take place. 

import java.io.File;//import the file IO package
import java.io.FileNotFoundException;//import the error
import java.util.*;//import all java utilities
public class Arena {
	
	private static ArrayList<Pokemon> ourPokemon= new ArrayList<Pokemon>();//array list of all good pokemon
	private static ArrayList<Pokemon> enemyPokemon = new ArrayList<Pokemon>();//array list of all enemies
	private static Pokemon currentPokemon = null;//this will be the pokemon currently being used. init as null.
	private static Pokemon currentEnemyPokemon = null;// this is the enemy pokemon currently being used. init as null.
	private static boolean playerFirst;
	public static void main(String[] args) {
		setup();//call the setup method that will setup the game
		int round = 0;//the round starts at 0.
		int battle = 0;//the battle number starts at 0. Iterates immediately as enemy inits
		System.out.println("WELCOME to the Pokemon Battlefield! To be champion and be Pokemon Trainer Supreme, you must defeat all of the enemy pokemon!");
		while(true) {//will iterate every round
			//handle winnning and losing
			if (ourPokemon.size()==0 && currentPokemon==null) {//if you have no pokemon left in the reserves and the current pokemon is null, you've lost
				System.out.println("You have lost. Restart the program to play a new game and fight for a chance to be Trainer Supreme!");//announce player loses
				break;//break out of the while loop
			} else if (enemyPokemon.size()==0 && currentEnemyPokemon==null) {//if the enemy has no pokemon left in the reserves and its current pokemon is null. you've won!
				System.out.println("Congratulations! You won. I hearby pronounce you the Trainer Supreme");//announce player wins
				break;//break out of the while loop
			}
			round++;//iterate the round counter
			if (currentEnemyPokemon==null) {//if there is no enemy pokemon
				currentEnemyPokemon = selectEnemy();//select the enemy. 
				battle+=1;//add one to the battle. A new battle seems to be defined as the time an enemy dies
				boolean playerFirst = flip();// check who goes first. 50/50 chance for either enemy or player
				if (currentEnemyPokemon!=null) {
					System.out.printf("The enemy selected the following pokemon: %s\n", currentEnemyPokemon.toString());//let the user know who they're up against
				}
				System.out.printf("Battle %d!\n", battle);//announce battle number
				if (currentPokemon!=null) {//check if the current pokemon is null. If so, dont add it or you get null pointer reference. Oh no!
					ourPokemon.add(currentPokemon);
				}
				currentPokemon = select(false);//in this case, the false indicates the user may not cancel.
			}
			if(currentPokemon==null) {//if there is no current pokemon
				currentPokemon = select(false);//select a new pokemon. the false means the current pokemon is not alive.
				System.out.printf("Player: %s, I choose you!\n", currentPokemon.getName());//announce the chosen one.
			}
			System.out.printf("Remaining Enemy Pokemon: %d, Remaining Good Pokemon: %d\n", enemyPokemon.size()+1, ourPokemon.size()+1);//Just give a little update on how many pokemon are left
			System.out.printf("Round %d! Everyone gear up for another round of attack!\n",round);//announce the round number
			if (playerFirst) {//if the playerFirst boolean is true, the player will go first
				System.out.println("Player, you will go first!");//announce that the player will go first
				if (currentPokemon!=null) {
					playerMove();//player move
				}
				if (currentEnemyPokemon!=null) {//if the enemy hasnt been killed, the enemy will have a move
					enemyMove();//enemy move
				} 
			} else {//if the playerFirst boolean is false, the enemy will go first
				System.out.println("Enemy will go first");
				if (currentEnemyPokemon!=null) {
					enemyMove();//enemy move
				}
				if (currentPokemon!=null) {//if the player hasnt been killed
					playerMove();//player move 
				}
			}
			
			System.out.println("");//just add a blank line for pretty formatting
		}
	}
	private static void enemyMove() {//this method is for the enemy's move. 
		System.out.println("It is the enemy's turn");//announce it is the enemy's turn
		System.out.printf("Enemy Pokemon's Stats:\n %s\n", currentEnemyPokemon.toString());//update on the enemy pokemon's stats
		if (currentEnemyPokemon.getStun()) {//if  the enemy is stunned, it cant do anything. however, it gets unstunned
			System.out.println("Stunned! Enemy Can't do anything");
			currentEnemyPokemon.setStun(false);
		} else {//the enemy isnt stunned and can do something
			 ArrayList<Attack> possibleAttacks = new ArrayList<Attack>();//make an arraylist of possible attacks
			 for (int t = 0; t<currentEnemyPokemon.getAttacks().length; t++) {//iterate through the pokemon's available attacks
				 //System.out.printf("Energy: %d\n", currentEnemyPokemon.getEnergy());
					if (currentEnemyPokemon.getAttacks()[t].getEnergy() <= currentEnemyPokemon.getEnergy()) {//if the enemy pokemon can afford the attack, add it to the array list
						possibleAttacks.add(currentEnemyPokemon.getAttacks()[t]);
						//System.out.printf("Energy Cost: %d\n", currentEnemyPokemon.getAttacks()[t].getEnergy());
					}
				}
			 if (possibleAttacks.size()!=0) {//if there are possible attacks
				 int index = randint(0,possibleAttacks.size()-1);//randomly choose an attack
				 Attack currentAttack = possibleAttacks.get(index);//get the attack
				 System.out.printf("Enemy Attack: %s\n", currentAttack.toString());//print out the attack details
				 boolean killed = currentEnemyPokemon.attackPokemon(currentPokemon, currentAttack, true);
				// boolean killed = currentAttack.attackPokemon(currentPokemon, currentEnemyPokemon, false);//actually do the attack and store the killed bool
				 
				 if (killed) {//if the killed boolean is true, the attack resulted in a death. In this case, the death of the player's pokemon
					 System.out.println("Your pokemon has been killed!");//announce the player's pokemon is dead
					 currentPokemon = null;//kill the pokemon
				 }
			 } else {//if there are no possible attacks, do nothing and say that the enemy cannot afford to attack.
				 System.out.println("Enemy cannot afford to attack");
			 }
			//energy
			 if (currentEnemyPokemon.getEnergy()>40) {//add 10 energy to the current pokemon, to a maximum of 50.
					currentEnemyPokemon.setEnergy(50);
				} else {
					currentEnemyPokemon.addEnergy(10);
				}
			 System.out.printf("%s's new Energy: %d\n", currentEnemyPokemon.getName(), currentEnemyPokemon.getEnergy());//print out current pokemon's new energy
			 for (Pokemon poke:enemyPokemon) {//iterate through the enemy pokemon's arraylist and then add 10 energy, to a max of 50, to each pokemon.
				 if (poke.getEnergy()>40) {
					 poke.setEnergy(50);
				 } else {
					 poke.addEnergy(10);
				 }
				 //System.out.printf("%s's new Energy: %d\n", poke.getName(), poke.getEnergy());
			 }
		}
		System.out.println("");//print blank line for formatting
	}
	private static void playerMove() {//this is for the player's move
		System.out.println("Player! It is your turn");//announce it is the player's turn
		System.out.printf("Your Pokemon's Stats:\n %s\n", currentPokemon.toString());//update on pokemon's stats
		if (currentPokemon.getStun()) {//if the player is stunned, it cant do anything. it does get unstunned, however.
			System.out.println("Player cannot do anything. It is stunned!");
			currentPokemon.setStun(false);
		} else {//if the player is not stunned
			Scanner kb = new Scanner(System.in);//make a new scanner
			boolean again = false;//this is a flag variable that will allow the player to repeat the turn. Will be used for cancelling things
			int select = -1;//create select int out of range
			while (true) {
				System.out.println("What would you like to do? Enter 0 for attack, 1 for retreat, 2 for pass.");//ask what it would like to do
				select = kb.nextInt();//select is equal to the next int in the input
				if (select>=0 && select<=2) {
					break;//if the select is valid, break
				}
				System.out.println("That was an invalid input. Please try again");//if the input is invalid, do it again
			}
			if (select==0) {//if select is 0, attack
				//attack
				ArrayList<Attack> playerAttacks = new ArrayList<Attack>();//make array list of possible attacks
				//Attack[] playerAttacks = currentPokemon.getAttacks();
				for (int t = 0; t<currentPokemon.getAttacks().length; t++) {//iterate through the pokemon's available attacks
					if (currentPokemon.getAttacks()[t].getEnergy() <= currentPokemon.getEnergy()) {//if the pokemon can afford the attack, add to arraylist
						playerAttacks.add(currentPokemon.getAttacks()[t]);
					}
					//System.out.printf("%d", currentPokemon.getEnergy());
				}
				if (playerAttacks.size()==0) {//if there are no available attacks, the player cant afford to attack. repeat the turn
					System.out.println("You can't attack as you have too little energy");//announce
					again=true;//repeat the turn
				}
				if (again==false) {//if again is false, get the user to select from available attacks
					System.out.println("Please select from the following attacks");
					int c = 0;//counter
					for (Attack t: playerAttacks) {//iterate through the attacks
						c++;//iterate counter
						System.out.printf("%d: %s\n",c,t.toString());//printout the attack description and the attack number
					}
					Attack currentAttack = null;//create current attack variable
					while (true) {
						Scanner tt = new Scanner(System.in);//make a new scanner object
						int h = tt.nextInt()-1;//the index h is the next int -1
						if (h<0 || h>playerAttacks.size()) {//if the index is out of bounds, it is an invalid input. restart loop
							System.out.println("Invalid input. Please try again");
						} else {
							currentAttack = playerAttacks.get(h);//current attack is not equal to the attack at the inputted index.
							break;//leave loop
						}
					}
					System.out.println("Wise choice!");
					//make the attack work
					boolean killed = currentPokemon.attackPokemon(currentEnemyPokemon, currentAttack, true);
					//boolean killed = currentAttack.attackPokemon(currentEnemyPokemon, currentPokemon, true);//perform the attack and store the killed boolean
					
					if (killed) {//if killed is true, the attack resulted in a death. in this case, the death of the enemy.
						currentEnemyPokemon = null;//kill enemy pokemon
						currentPokemon.addHP(20);//add 20 HP to every pokemon
						System.out.printf("%s has healed! HP: %d\n", currentPokemon.getName(), currentPokemon.getHP());
						for (Pokemon t: ourPokemon) {
							t.addHP(20);
							System.out.printf("%s has healed! HP: %d\n", t.getName(), t.getHP());
						}
					}
				}
				
			} else if (select==1) {//if the player wants to retreat
				Pokemon pastPokemon = currentPokemon;//store the past pokemon
				currentPokemon = select(true);//currentpokemon is going to be the returned pokemon from the select method. The true means the old pokemon is alive
				if (currentPokemon == null) {//if the current pokemon is null, the operation was cancelled
					System.out.println("Cancelled.");
					currentPokemon = pastPokemon;//current pokemon is the same as the past pokemon
					again=true;//restart turn
				} else {//if the current pokemon is not null, confirm the change and announce it.
					System.out.println("Your pokemon has been replaced! Please wait until your turn to do an attack");
				}
			} else {//if the select was 2, the player wants to do nothing.
				System.out.println("That's fine! Just do nothing for now.");
			}
			if (again==false) {//if the turn isnt being redone
				if (currentPokemon.getEnergy()>40) {//add 10 energy, to a max of 50, to the current pokemon
					currentPokemon.setEnergy(50);
					System.out.println("yes this happened");
				} else {
					currentPokemon.addEnergy(10);
				}
				System.out.printf("10 Energy Units added to %s. New energy: %d\n", currentPokemon.getName(), currentPokemon.getEnergy());
				for (Pokemon poke:ourPokemon) {//add 10 energy, to a max of 50, to each pokemon in the reserves
					if (poke.getEnergy()>40) {
						poke.setEnergy(50);
					} else {
						poke.addEnergy(10);
					}
					//System.out.printf("10 Energy Units added to %s. New energy: %d\n", poke.getName(), poke.getEnergy());
				}
				System.out.println("");//print empty line for formatting
			} else {//if again is true
				again=false;//make again false
				playerMove();//restart the turn
			}
		}
	}
	
	
	
	private static Pokemon select(boolean alive) {//this is the select method. It selects pokemon and returns them
		Scanner kb = new Scanner(System.in);//make a scanner
		if (alive) {//if the pokemon is alive, allow for cancelling
			System.out.println("Please Select a New Pokemon from your Pokemon. Please choose by entering the list number. Enter 0 to cancel.");
		} else {//otherwise, dont.
			System.out.println("Please Select a New Pokemon from your Pokemon. Please choose by entering the list number.");
		}
		System.out.println("My Pokemon:");
		for (int t=0; t<ourPokemon.size(); t++) {//iterate through and display the other pokemon
			System.out.printf("%d: %s \n", t+1, ourPokemon.get(t).toString());
		}
		
		while (true) {
			int input = kb.nextInt()-1;
			if (input<ourPokemon.size() && input>=0) {
				Pokemon returning = ourPokemon.remove(input);//remove the specified pokemon and store in a variable
				if (alive) {//if the existing pokemon is alive, add it back to the array list.
					ourPokemon.add(currentPokemon);
				}
				return returning;//return the selected pokemon
			} else if (input==-1 && alive==true) {//if the player cancelled, return null
				return null;
			}
			System.out.println("It appears that was invalid. Please try again.");//if the input was out of range, try again.
		}
		
		
	}
	private static boolean flip() {//this is a flip method that just returns true half the time and false half the time
		return Math.random() < 0.5;
	}
	
	private static int randint(int low,int high) {//this method returns a random integer between the bounds of low and high
		return (int)(Math.random()*(high-low+1)+low);
	}
	
	
	private static Pokemon selectEnemy() {//this method selects an enemy. The enemy is always dead when this happens.
		
		int ind = randint(0,enemyPokemon.size()-1);//index is a random number in the bounds of the array list
		System.out.println(ind);
		if (enemyPokemon.size()>0) {
			return enemyPokemon.remove(ind);//return the random pokemon
		} else {
			return null;
		}
	}
	private static Pokemon[] load(){//this method loads from the data file
		try {//try doing all of this
			Scanner kb = new Scanner(new File("pokemon.txt"));//scanner for the data file
			int total = Integer.parseInt(kb.nextLine());//total is how many pokemon there are
			Pokemon[] pokes = new Pokemon[total];//make an array with that many pokemon spots
			for (int t=0; t<total; t++){
				pokes[t] = new Pokemon(kb.nextLine());//make new pokemon object from the next line. add this to the array
			}
			return pokes;//return the array
			
		} catch (FileNotFoundException e) {//if there is a file not found exception, trace it and return null.
			e.printStackTrace();
			return null;
		}
	}
	
	private static void setup() {//this is a setup method that sets up the battles before fighting
		Pokemon[] totalPokemon = load();//total pokemon available comes from the load method.
		System.out.println("Please select your four pokemon from the following list:");//ask the user to select 4 pokemon from the available pokemon
		for (int x=0; x<totalPokemon.length; x++) {
			System.out.printf("%d: %s\n",x+1,totalPokemon[x].toString());//print out each pokemon's details
			System.out.println("Attacks:");//list out each pokemon's attacks
			for (int t=0; t<totalPokemon[x].getAttacks().length; t++) {
				System.out.printf("     %d: %s\n",t+1, totalPokemon[x].getAttacks()[t].toString());
			}
			System.out.println("");//print a blank line for formatting
		}
		Scanner check = new Scanner(System.in);//make a new scanner
		while (true) {//repeat loop until scope is broken out of
			int[] indexes = {check.nextInt()-1, check.nextInt()-1, check.nextInt()-1, check.nextInt()-1};// make int array of the indexes inputted
			boolean invalid = false;//make a boolean for validity
			//inefficient solution, but it works because its a length of 4
			for (int t = 0; t<4; t++) {
				for (int x = 0; x<4; x++) {
					if (indexes[t]==indexes[x] && x!=t) {//if there are duplicates, then invalid is true
						invalid=true;
					}
					
				}
				if (indexes[t]>totalPokemon.length-1 || indexes[t]<0) {//if one of the indexes is out of bounds, set invalid to true. 
					invalid=true;
				}
			}
			if (invalid==false) {//if the indexes are valid
				ourPokemon = new ArrayList<Pokemon>();//set the global array list of our pokemon to a blank array list
				ourPokemon.add(totalPokemon[indexes[0]]);//add the pokemon from the following indexes from the total pokemon array
				ourPokemon.add(totalPokemon[indexes[1]]);
				ourPokemon.add(totalPokemon[indexes[2]]);
				ourPokemon.add(totalPokemon[indexes[3]]);
				enemyPokemon = new ArrayList <Pokemon>();//set the global array list of enemy pokemon to a blank array list
				enemyPokemon.addAll(Arrays.asList(totalPokemon));//add all of the pokemon in the total pokemon array
				enemyPokemon.remove(totalPokemon[indexes[0]]);//remove the pokemon that have been added to the player's pokemon
				enemyPokemon.remove(totalPokemon[indexes[1]]);
				enemyPokemon.remove(totalPokemon[indexes[2]]);
				enemyPokemon.remove(totalPokemon[indexes[3]]);
				break;//leave scope
			} else {//if indexes are invalid, restart the loop and tell the user the input was invalid.
				System.out.println("Invalid input. Please try again");
			}
		}
		
		
	}
	
	

}
