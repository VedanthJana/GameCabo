
public class Player {
    private String name;    // The identifier associated with this Player
    private int label;      // This player's label for running the game (0-3)
    private Hand hand;      // The collection of Cards that this Player is holding
    private boolean isComputer; // Indicator of whether this is a human or computer player

    // Constructor
    public Player(String name, int label, boolean isComputer) {
        this.name = name;
        this.label = label;
        this.isComputer = isComputer;
        this.hand = new Hand(); // Initialize the hand
    }

    // Accessor for the player's name
    public String getName() {
        return this.name;
    }

    // Accessor for the player's label
    public int getLabel() {
        return this.label;
    }

    // Accessor for the player's hand
    public Hand getHand() {
        return this.hand; // Returns a reference to this player's hand
    }

    // Reports whether this is a computer player
    public boolean isComputer() {
        return this.isComputer;
    }

    // Adds a card to this player's hand
    public void addCardToHand(BaseCard card) {
        this.hand.addCard(card); // Assuming Hand has an addCard method
    }
}