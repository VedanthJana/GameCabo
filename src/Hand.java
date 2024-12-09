import java.util.ArrayList;
public class Hand extends Deck {
    private final int HAND_SIZE = 4; // Maximum number of cards in the hand

    // Constructor to create a new empty hand
    public Hand() {
        super(new ArrayList<BaseCard>()); // Call the constructor of Deck
    }

    // Override addCard to enforce HAND_SIZE limit
    @Override
    public void addCard(BaseCard card) {
        if (size() >= HAND_SIZE) {
            throw new IllegalStateException("Cannot add more cards: hand is full.");
        }
        super.addCard(card); // Call the addCard method from Deck
    }

    // Calculate the total value of the hand
    public int calcHand() {
        int totalValue = 0;
        for (int i = 0; i < size(); i++) {
            totalValue += getRankAtIndex(i); // Assuming getRankAtIndex returns the rank value
        }
        return totalValue;
    }

    // Draw the hand at a given y-coordinate
    public void draw(int y) {
        for (int i = 0; i < size(); i++) {
            int x = 50 + 60 * i; // Calculate x-coordinate
            // Assuming there's a method in Deck to draw a card at (x, y)
            cardList.get(i).draw(x, y);
        }
    }

    // Access the rank of a card at a given index
    public int getRankAtIndex(int index) {
        BaseCard card = cardList.get(index); // Assuming cardList is a List<BaseCard>
        return card.getRank(); // Assuming BaseCard has a getRank() method
    }

    // Check if the mouse is over a card in the hand
    public int indexOfMouseOver() {
        // Implementation depends on the specific GUI framework used
        // This is a placeholder for actual mouse detection logic
    	for (int i = 0; i < size(); i++) {
            BaseCard card = cardList.get(i); // Get the card at the current index
    //        System.out.println("card position" + card.getX() + card.getY());
            if (card.isMouseOver()) {
                return i; // Return the index of the card that the mouse is over
            }
        }
        return -1; // Return -1 if no card is hovered over
    }

    // Change the face-up value of a card
    public void setFaceUp(int index, boolean faceUp) {
  //  	System.out.println("index" + cardList.size() + index);
    	BaseCard card = cardList.get(index); // Assuming cardList is a List<BaseCard>
        card.setFaceUp(faceUp); // Assuming BaseCard has a setFaceUp() method
    }

    // Swap a card at a given index with a new card
    public BaseCard swap(BaseCard newCard, int index) {
        if (index < 0 || index >= HAND_SIZE) {
            throw new IndexOutOfBoundsException("Index out of bounds.");
        }
        BaseCard oldCard = cardList.get(index);
        cardList.set(index, newCard); // Replace the card at the index
        return oldCard; // Return the old card
    }

    // Switch a card with another hand
    public void switchCards(int myIndex, Hand otherHand, int otherIndex) {
        BaseCard myCard = cardList.get(myIndex);
        BaseCard otherCard = otherHand.cardList.get(otherIndex);
        cardList.set(myIndex, otherCard); // Replace my card with the other card
        otherHand.cardList.set(otherIndex, myCard); // Replace the other card with my card
    }
}