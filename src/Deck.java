import java.util.ArrayList;
import java.util.Collections;
import processing.core.PApplet;

/**
 * The Deck class represents a deck of playing cards for the game Cabo. It manages a collection of
 * cards, including shuffling, drawing, and adding cards.
 */
public class Deck {
  
  // TODO: add everything else, added
	
	// Fields
    protected ArrayList<BaseCard> cardList; // The list of cards in the deck
    protected static PApplet processing; // The Processing environment used for drawing the deck

    // Constructor
    public Deck(ArrayList<BaseCard> deck) {
        if (processing == null) {
            throw new IllegalStateException("Processing environment must be set before creating a deck.");
        }
        this.cardList = deck; // Initialize the deck with the provided cards
    }

    // Static method to set the Processing environment
    public static void setProcessing(PApplet processing) {
        Deck.processing = processing;
    }

    // Draw a card from the top of the deck
    public BaseCard drawCard() {
        if (!cardList.isEmpty()) {
            return cardList.remove(cardList.size() - 1); // Remove and return the top card
        }
        return null; // Return null if the deck is empty
    }

    // Add a card to the top (end) of the deck
    public void addCard(BaseCard card) {
        cardList.add(card); // Add the card to the deck
    }

    // Get the current number of cards in the Deck
    public int size() {
    	if ( ! this.isEmpty() )
    	{
          return cardList.size(); // Return the size of the deck
    	}
    	else
    	{
    		return 0;
    	}
    }

    // Check if the deck is empty
    public boolean isEmpty() {
        return cardList.isEmpty(); // Return true if the deck is empty
    }
  
  /**
   * Sets up the deck with CABO cards, including action cards. Initializes the deck with all
   * necessary cards and shuffles them.
   *
   * @return the completed ArrayList of CABO cards
   */
  public static ArrayList<BaseCard> createDeck() {
    ArrayList<BaseCard> cardList = new ArrayList<>();

    // Define the suits
    String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};

    // Cards from 1 (Ace) to 13 (King)
    for (int rank = 1; rank <= 13; ++rank) {
      // Loop through each suit
      for (String suit : suits) {
        if (rank >= 7 && rank <= 12) {
          // Special action cards
          String actionType = "";
          if (rank == 7 || rank == 8) {
            actionType = "peek";
          } else if (rank == 9 || rank == 10) {
            actionType = "spy";
          } else {
            actionType = "switch";
          }
          cardList.add(new ActionCard(rank, suit, actionType));  // Add ActionCard to deck
        } else {
          cardList.add(new BaseCard(rank, suit));  // Add NumberCard to deck
        }
      }
    }
    Collections.shuffle(cardList);
    return cardList;
  }
  
//Draw the top card of the deck onto the Processing canvas
  public void draw(int x, int y, boolean isDiscard) {
      if (!isEmpty()) {
          BaseCard topCard = cardList.get(cardList.size() - 1); // Get the top card
          if (isDiscard) {
              topCard.setFaceUp(true); // Draw face-up if it's a discard pile
          }
          topCard.draw(x, y); // Draw the top card at the specified position
      } else {
          // Draw a placeholder if the deck is empty
    	  processing.stroke(0);
          processing.fill(0); // Black color for the placeholder
          processing.rect(x, y, 50, 70); // Draw a rectangle for the placeholder
          processing.fill(255); // White color for the text
          processing.textSize(12); 
        //  processing.textAlign(processing.CENTER, processing.CENTER); 
          processing.text("Empty", x + 25, y + 35); // Indicate that the deck is empty
      }
  }

}
