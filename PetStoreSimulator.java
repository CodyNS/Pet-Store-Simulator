// Pet Store Simulator by Cody Stasyk | October 2023

// This is not a robust program by any means. Input checking is particularly weak.
// It's meant as a super simple demo of a Pet Store coded using OOP.
// This was thrown together quickly, so parts of it are messy: that's on purpose.
// This is meant to be used as a base for refactoring practice.
// All classes involved have been combined into this one file for convenience.

import java.util.*;


class PetStore
{
	private String name;
	private double money;
	private String address;
	private List<Pet> petInventory;
	private List<PetProduct> petProductInventory;
	private Scanner keyboard;
	
	public PetStore(String name, double money, String address, 
		            List<Pet> petInventory, List<PetProduct> petProductInventory) {
		this.name = name;
		this.money = money;
		this.address = address;
		this.petInventory = petInventory;
		this.petProductInventory = petProductInventory;
		keyboard = new Scanner(System.in);
	}
	public PetStore(String name, double money, String address) {
		this.name = name;
		this.money = money;
		this.address = address;
		this.petInventory = new ArrayList<>();
		this.petProductInventory = new ArrayList<>();
		keyboard = new Scanner(System.in);
	}
	public String getStoreName() {return name;}
	public void   setStoreName(String newName) {name = newName;}
	public double getStoreMoney() {return money;}
	public void   setStoreMoney(double money) {this.money = money;}
	public String getStoreAddress() {return address;}
	// no set method for address because ...that's not a thing: it would be a new store
	public List<Pet> getPetInventory() {return new ArrayList<Pet>(petInventory);}
	public List<PetProduct> getPetProductInventory() {return new ArrayList<PetProduct>(petProductInventory);}
	public void  addPetToInventory(Pet p) {petInventory.add(p);}
	public void  addPetProductToInventory(PetProduct p) {petProductInventory.add(p);}
	private void removeItemFromInventory(Pet p) {petInventory.remove(p);}
	private void removeItemFromInventory(PetProduct p) {petProductInventory.remove(p);} // overloaded
	
	public void sellPet(Pet p, Customer c) {
		if (c.hasEnoughMoneyToBuyThisItem(p)) {
			c.purchasePet(p);					     
			money += p.getPrice();
			return;
		}
		System.out.println("\nYou can't buy the " + p.getName() + ": you don't have enough money");
		// don't have to remove p from this.petProductInventory because for this program, that happens when customer adds it to cart
	}
	public void sellPetProduct(PetProduct p, Customer c){
		if (c.hasEnoughMoneyToBuyThisItem(p)) {     
			c.purchasePetProduct(p);			  				
			money += p.getPrice();
			return;
		}
		System.out.println("\nYou can't buy the " + p.getName() + ": you don't have enough money.");
		// don't have to remove p from this.petProductInventory because I've decided to do that when customer adds it to cart
	}

	public void goShopping(Customer c) {
		println("\nWelcome to " + this.name + ", " + c.getName() + "!");

		int menuSelection = 0;
		do {
			println();
			println("1) Shop for a pet or pet product");
			println("2) View cart + checkout");
			println("0) Exit store\n");
			print("Please make a selection: ");
			menuSelection = keyboard.nextInt();
			switch (menuSelection) {
				case 0:  println("\nThank you for shopping with us today!\n\n");
						 keyboard.close();  
						 return;
				case 1:  viewProductsMenu(c);  break;
				case 2:  viewCart(c);  break;
				default: println("\nInvalid selection: \"" + menuSelection + "\"\n");  continue;
			}
		} while(true);
	}

	private void viewProductsMenu(Customer c) {
		int menuSelection = 0;
		do {
			int i = 0;
			println("\n\n----- Pets for sale:  ----------------------------------\n");

			for (Pet pet : petInventory) {
				print(++i + ") Pet: " + pet.getType() + "   Price: $");
				System.out.printf("%,.2f", pet.getPrice());
				println();
			}

			println("\n----- Pet Products for sale:  --------------------------\n");

			for (PetProduct prod : petProductInventory) {
				print(++i + ")" + prod.getType() + ":  \"" + prod.getName() + "\"   Price: $");
				System.out.printf("%,.2f", prod.getPrice());
				println();
			}

			print("\nSelect item to view (0 to go back to main menu): ");
			menuSelection = keyboard.nextInt();
			if (0 == menuSelection)
				return;
			if ( invalidMenuInput(1, i, menuSelection) ) {
				println("\nInvalid selection \"" + menuSelection + "\"\n");
				continue;
			}
			println("\n\n");
			boolean petWasSelected = menuSelection <= petInventory.size();
			if (petWasSelected)  
				viewItem(petInventory.get(menuSelection-1), c);
			else  
				viewItem(petProductInventory.get(menuSelection - petInventory.size() - 1), c);
			// viewItem( (petWasSelected ? petInventory.get(i-1) : petProductInventory.get(i - petInventory.size() - 1)), c);  <-- compiler doesn't like this ternary
		} while(menuSelection != 0);
	}

	boolean invalidMenuInput(int min, int max, int menuSelection) {
		if (menuSelection < min || menuSelection > max)
			return true;
		return false;
	}

	private void viewItem(Pet p, Customer c) {
		print("Viewing:  ");
		print(p.getType() + ":  \"" + p.getName() + "\"   Price: $");
		System.out.printf("%,.2f", p.getPrice());

		println("\n\n    \"" + p.makeSound() + "\"");

		int menuSelection = 0;
		do {
			println("\n\nWhat would you like to do?\n");
			println("1) Add this to my shopping cart");
			println("2) Go back\n");
			print("Please make a selection: ");
			menuSelection = keyboard.nextInt();
			switch (menuSelection) {
				case 1:  customerAddsItemToCart(c, p);  return;
				case 2:  return;
				default: println("\nInvalid selection: \"" + menuSelection + "\"\n"); continue;
			}
		} while(true);
	}

	private void viewItem(PetProduct p, Customer c) {  // overloaded
		print("Viewing:  ");
		print(p.getType() + ":  \"" + p.getName() + "\"   Price: $");
		System.out.printf("%,.2f", p.getPrice());

		int menuSelection = 0;
		do {
			println("\n\n\nWhat would you like to do?\n");
			println("1) Add this to my shopping cart");
			println("2) Go back\n");
			print("Please make a selection: ");
			menuSelection = keyboard.nextInt();
			switch (menuSelection) {
				case 1:  customerAddsItemToCart(c, p);  return;
				case 2:  return;
				default: println("\nInvalid selection: \"" + menuSelection + "\"\n"); continue;
			}
		} while(true);
	}

	void customerAddsItemToCart(Customer c, Pet p) {
	// needs to add to Customer's cart and remove from Store's inventory (not the best way: should be difference between store's inventory and what's on the shelf, but I'm keeping it simple for this program)	
		c.addItemToCart(p);
		removeItemFromInventory(p); // again, a real store wouldn't really do it like this, but this simplifies my menu system here
	}
	void customerAddsItemToCart(Customer c, PetProduct p) { // overloaded
		c.addItemToCart(p);
		removeItemFromInventory(p);
	}

	private void viewCart(Customer c) {
		int menuSelection = 0;
		do {
			int i = 0;
			println("\n\n-----  " + c.getName() + "'s shopping cart:  ---------------------\n");

			for ( Pet pet : c.getShoppingCartPets() ) {
				print(++i + ") Pet: " + pet.getType() + "   Price: $");
				System.out.printf("%,.2f", pet.getPrice());
				println();
			}

			for ( PetProduct prod : c.getShoppingCartPetProducts() ) {
				print(++i + ") Item: " + prod.getType() + ":  \"" + prod.getName() + "\"   Price: $");
				System.out.printf("%,.2f", prod.getPrice());
				println();
			}

			print("\nCart total: $"); System.out.printf("%,.2f", c.getShoppingCartTotal());
			print("  (You have: $"); System.out.printf("%,.2f", c.getMoney()); println(")\n\n");

			println("Enter a number to remove that item from your cart,");
			print("enter 999 to purchase these,\nor enter 0 to go back to the main menu: ");
			menuSelection = keyboard.nextInt();
	
			if (0 == menuSelection)  return;
			else if (999 == menuSelection) {
				customerBuysItems(c);
				println("\n\nThank you very much!\n");
				return;
			}
			else if ( invalidMenuInput(1, i, menuSelection) )
				continue;
			else {
				boolean petWasSelected = menuSelection <= c.getShoppingCartPets().size();
				if (petWasSelected)
					customerPutsItemBack(c,  c.getShoppingCartPets().get(menuSelection-1));
				else
					customerPutsItemBack(c, c.getShoppingCartPetProducts().get(menuSelection - c.getShoppingCartPets().size() - 1));
			}
		} while(true);
	}

	void customerPutsItemBack(Customer c, Pet p) {
		c.removeItemFromCart(p);
		addPetToInventory(p);
	}
	void customerPutsItemBack(Customer c, PetProduct p) { // overloaded
		c.removeItemFromCart(p);
		addPetProductToInventory(p);
	}

	void customerBuysItems(Customer c) {
		for ( Pet p : c.getShoppingCartPets() )
			sellPet(p, c);
		for ( PetProduct p : c.getShoppingCartPetProducts() )
			sellPetProduct(p, c);
	}

	private void print(String s) {System.out.print(s);}
	private void println(String s) {System.out.println(s);}
	private void println() {System.out.println();}
}




class Customer
{
	private String name;
	private double money;
	private List<Pet> petsOwned;
	private List<PetProduct> petProductsOwned;
	private List<Pet> shoppingCart_Pets;
	private List<PetProduct> shoppingCart_PetProducts;

	public Customer(String name, double money) {
		this.name = name;
		this.money = money;
		this.petsOwned = new ArrayList<>();
		this.petProductsOwned = new ArrayList<>();
		this.shoppingCart_Pets = new ArrayList<>();
		this.shoppingCart_PetProducts = new ArrayList<>();
	}
	public Customer(String name, double money, List<Pet> petsOwned, List<PetProduct> petProductsOwned) {
		this.name = name;
		this.money = money;
		this.petsOwned = petsOwned;
		this.petProductsOwned = petProductsOwned;
		this.shoppingCart_Pets = new ArrayList<>();
		this.shoppingCart_PetProducts = new ArrayList<>();
	}
	public String getName() {return name;}
	public void   setName(String newName) {name = newName;}

	public double  getMoney() {return money;}
	public void    setMoney(double newAmount) {money = newAmount;}
	public boolean hasEnoughMoneyToBuyThisItem(Pet p) {return money >= p.getPrice();}
	public boolean hasEnoughMoneyToBuyThisItem(PetProduct p) {return money >= p.getPrice();}  // overloaded

	public List<Pet> getShoppingCartPets() {return new ArrayList<Pet>(shoppingCart_Pets);}
	public List<PetProduct> getShoppingCartPetProducts() {return new ArrayList<PetProduct>(shoppingCart_PetProducts);}

	public double getShoppingCartTotal() {
		double total = 0.0;
		for (int i = 0; i < shoppingCart_Pets.size(); i++)
			total += shoppingCart_Pets.get(i).getPrice();
		for (int i = 0; i < shoppingCart_PetProducts.size(); i++)
			total += shoppingCart_PetProducts.get(i).getPrice();
		return total;
	}
	public void addItemToCart(Pet p) {shoppingCart_Pets.add(p);}
	public void addItemToCart(PetProduct p) {shoppingCart_PetProducts.add(p);}
	public void removeItemFromCart(Pet p) {shoppingCart_Pets.remove(p);}
	public void removeItemFromCart(PetProduct p) {shoppingCart_PetProducts.remove(p);}

	public List<Pet> getPetsOwned() {return new ArrayList<Pet>(petsOwned);}
	public List<PetProduct> getPetProductsOwned()  {return new ArrayList<PetProduct>(petProductsOwned);}

	public void purchasePet(Pet p) {
		money -= p.getPrice();
		petsOwned.add(p);
		shoppingCart_Pets.remove(p);
	}
	public void purchasePetProduct(PetProduct p) {
		money -= p.getPrice();
		petProductsOwned.add(p);
		shoppingCart_PetProducts.remove(p);
	}
}


abstract class PetStoreItem 
{
	protected int type;
	protected String name;
	protected double price;

	public PetStoreItem(int type, String name, double price) {
		this.type = type;
		this.name = name;
		this.price = price;
	}

	public String getName() {return name;}
	public void   setName(String newName) {name = newName;}
	public double getPrice() {return price;}
	public void   setPrice(double newPrice) {price = newPrice;}

	public boolean equals(Object otherObj) {
		boolean isEqual = false;
		if ( isPossibleToCompare(otherObj) ){
			PetStoreItem other = (PetStoreItem) otherObj;
			isEqual = this.type == other.type &&
					  this.name == other.name &&
					  (Math.abs(this.price - other.price) < 0.001);
		}
		return isEqual;
	}
	abstract boolean isPossibleToCompare(Object otherObj);
}



class PetProduct extends PetStoreItem
{
	static final int FOOD = 1;
	static final int TOY  = 2;
	
	public PetProduct(int type, String name, double price) {
		super(type, name, price);
	if (type < 1 || type > 2)
		throw new RuntimeException("Invalid PetProduct type code"); 
	}
	protected boolean isPossibleToCompare(Object otherObj) {
		return ((otherObj != null)  && (otherObj instanceof PetProduct));
	}
	public String getType() {
		switch(type){
			case FOOD:  return "Food";
			case TOY:   return "Toy";
			default: throw new RuntimeException("Invalid PetProduct type code set for this pet!"); 
		}
	}
}

class Pet extends PetStoreItem
{
	static final int DOG  = 1;
	static final int CAT  = 2;
	static final int BIRD = 3;
	static final int FISH = 4;

	public Pet(int type, String name, double price) {
		super(type, name, price);
		if (type < 1 || type > 4)
			throw new RuntimeException("Invalid Pet type code"); 
	}
	protected boolean isPossibleToCompare(Object otherObj) {
		return ((otherObj != null)  && (otherObj instanceof Pet));
	}
	public String getType() {
		switch(type){
			case DOG:  return "Dog";
			case CAT:  return "Cat";
			case BIRD: return "Bird";
			case FISH: return "Fish";
			default: throw new RuntimeException("Invalid type code set for this pet!"); 
		}
	}
	public String makeSound() {
		switch(type){
			case DOG:  return "Bark! Bark!";
			case CAT:  return "Meowww";
			case BIRD: return "Chirp! Chirp!";
			case FISH: return "Blub... blub...";
			default: throw new RuntimeException("Invalid type code set for this pet!");
		}
	}
}



// ----------------------------------------------------------------------------
class SimRunner
{
	private PetStore store;
	private Customer customer;
    private Scanner keyboard;

    public SimRunner() {
    	store = new PetStore("Safari Bob's Pet Emporium", 1000.00, "118 Stewart Green SW");
    	keyboard = new Scanner(System.in);
    }
    public static void addSomeInventoryToStore(PetStore s) {
		s.addPetToInventory(new Pet(1, "Black Lab", 190.00));
		s.addPetToInventory(new Pet(2, "Calico", 150.00));
		s.addPetToInventory(new Pet(3, "Cockatoo", 220.00));
		s.addPetToInventory(new Pet(4, "Gold Fish", 10.00));
		s.addPetProductToInventory(new PetProduct(1, "Dog Food", 20.00));
		s.addPetProductToInventory(new PetProduct(1, "Cat Food", 17.00));
		s.addPetProductToInventory(new PetProduct(1, "Bird Food", 14.00));
		s.addPetProductToInventory(new PetProduct(1, "Fish Food", 9.00));
		s.addPetProductToInventory(new PetProduct(2, "Dog Chew Toy", 19.00));
		s.addPetProductToInventory(new PetProduct(2, "Cat Scratch Post", 25.00));
		s.addPetProductToInventory(new PetProduct(2, "Bird Toy", 14.00));
	}
	public void runSim() {
		System.out.print("\nEnter your name: ");
        String name = keyboard.nextLine();
        customer = new Customer(name, 400.00);
        addSomeInventoryToStore(store);
        store.goShopping(customer); // should probably reverse this. lol.
        keyboard.close();
	}
}


public class PetStoreSimulator
{
	public static void main(String[] args) {
        SimRunner s = new SimRunner();
        s.runSim();
    }
}