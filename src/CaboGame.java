import java.util.ArrayList;
import processing.core.PApplet;

/**
 * The CaboGame class implements the main game logic for the card game CABO.
 * It manages the deck, discard pile, players, game state, and user interactions.
 */
public class CaboGame extends processing.core.PApplet  {
  
    private Button[] buttons = new Button[5]; // Array to hold buttons, Contains the UI buttons for game actions
    private int caboPlayer; // The index of the player who declared Cabo
    private int currentPlayer; // The index of the current player in the list of players
    private Deck deck; // The deck of cards used in the game
    private Deck discard; // The discard pile for cards that have been played or discarded
    private BaseCard drawnCard; // The card that has been drawn from the deck
    private boolean gameOver; // Flag indicating if the game is over
    private Player[] players; // The list of players in the game
    private int selectedCardFromCurrentPlayer; // The index of the first card selected for switching action
    private boolean oneDrawnCard = false;

    
  /**
   * Enum representing the different action states in the game
   * (e.g., swapping cards, peeking, spying, switching).
   * 
   * This allows us to easily restrict the possible values of a variable.
   */
  private enum ActionState {
    NONE, SWAPPING, PEEKING, SPYING, SWITCHING
  }
  private ActionState actionState = ActionState.NONE;
  
  // provided data fields for tracking the players' moves through the game
  private ArrayList<String> gameMessages = new ArrayList<>();
  
  /**
   * Launch the game window; PROVIDED. Note: the argument to PApplet.main() must match the name
   * of this class, or it won't run!
   * @param args unused
   */
  public static void main(String[] args) {
    PApplet.main("CaboGame");
  }
  
  /**
   * Sets up the initial window size for the game; PROVIDED.
   */
  @Override
  public void settings() {
    size(1000, 800);
  }
  
  /**
   * Sets up the game environment, including the font, game state, and game elements.
   */
  @Override
  public void setup() {
    textFont(createFont("Arial", 16));
    // TODO: setProcessing for the classes which require it
    
    deck.setProcessing(this);
    drawnCard.setProcessing(this);
    BaseCard.setProcessing(this);
    deckCheck();
    
    // TODO: set up deck and discard pile
    
    ArrayList<BaseCard> cardList = Deck.createDeck(); // Create a full deck of cards
    deck = new Deck(cardList); // Initialize the deck
    discard = new Deck(new ArrayList<>()); // Initialize an empty discard pile
    drawnCard = null;

    // TODO: set up players array and deal their cards
    
    // Set up players array and deal their cards
    players = new Player[4]; // Create an array for 4 players
    players[0] = new Player("Cyntra", 0, false); // Human player
    players[1] = new AIPlayer("Avalon", 1, true); // AI player
    players[2] = new AIPlayer("Balthor", 2, true); // AI player
    players[3] = new AIPlayer("Ophira", 3, true); // AI player
    
   
    
 // Deal 4 cards to each player
    for (Player player : players) {
        for (int i = 0; i < 4; i++) {
           BaseCard card = deck.drawCard(); // Draw a card from the deck
            player.getHand().addCard(card); // Add the card to the player's hand
        }
    }
    
    System.out.println("in setup before" + players[0].getHand().size());
	   
    
    // TODO: set up buttons and update their states for the beginning of the game
    
    Button.setProcessing(this);
    
    buttons[0] = new Button("Draw from Deck", 50, 700, 150, 40); 
    buttons[1] = new Button("Swap a Card", 220, 700, 150, 40); 
    buttons[2] = new Button("Declare Cabo", 390, 700, 150, 40); 
    buttons[3] = new Button("Use Action", 390 + 170, 700, 150, 40); 
    buttons[4] = new Button("End Turn", 390 + 170 + 170, 700, 150, 40);
     
    // Set the first two cards of the human player face-up

      players[0].getHand().setFaceUp(0,true);
      players[0].getHand().setFaceUp(1,true);
    
    System.out.println("in setup after" + players[0].getHand().size());
	   
    // TODO: update the gameMessages log: "Turn for "+currentPlayer.name
    
    currentPlayer = 0;
    caboPlayer = -1;
    
    setGameStatus("Turn for " + players[currentPlayer].getName());
    
    updateButtonStates();
    
    System.out.println("end of setup");
  }
  
  /**
   * Console-only output for verifying the setup of the card objects and the deck containing them
   */
  public void deckCheck() {
    System.out.println("TODO");
    // TODO: verify that there are 52 cards in the deck
    // TODO: verify that there are 8 of each type of ActionCard
    // TODO: verify that there are 13 of each suit
    // TODO: verify that the king of diamonds' getRank() returns -1
    
 // Step 1: Create a new set of cards with Deck’s static method
    ArrayList<BaseCard> cardList = Deck.createDeck(); // Assuming createDeck() is a static method in Deck class

    System.out.println("Deck Check:");
    System.out.println("Total cards in deck: " + cardList.size()); // Verify total cards

    // Verify that there are 52 cards in the deck
    if (cardList.size() == 52) {
        System.out.println("Deck contains 52 cards.");
    } else {
        System.out.println("Deck does NOT contain 52 cards.");
    }

    // Verify that there are 8 of each type of ActionCard
    int[] actionCardCounts = new int[3]; // Assuming 3 types: peek, spy, switch
    for (BaseCard card : cardList) {
        if (card instanceof ActionCard) {
            String actionType = ((ActionCard) card).getActionType();
            switch (actionType) {
                case "peek": actionCardCounts[0]++; break;
                case "spy": actionCardCounts[1]++; break;
                case "switch": actionCardCounts[2]++; break;
            }
        }
    }
    System.out.println("Peek cards: " + actionCardCounts[0]);
    System.out.println("Spy cards: " + actionCardCounts[1]);
    System.out.println("Switch cards: " + actionCardCounts[2]);

    boolean actionCardCheck = (actionCardCounts[0] == 8 && actionCardCounts[1] == 8 && actionCardCounts[2] == 8);
    System.out.println("Correct number of ActionCards: " + actionCardCheck);

    // Verify that there are 13 of each suit
    int[] suitCounts = new int[4]; // Assuming 4 suits: Hearts, Diamonds, Clubs, Spades
    for (BaseCard card : cardList) {
        String suit = card.suit; // Assuming getSuit() is a method in BaseCard
        switch (suit) {
            case "Hearts": suitCounts[0]++; break;
            case "Diamonds": suitCounts[1]++; break;
            case "Clubs": suitCounts[2]++; break;
            case "Spades": suitCounts[3]++; break;
        }
    }
    System.out.println("Hearts: " + suitCounts[0]);
    System.out.println("Diamonds: " + suitCounts[1]);
    System.out.println("Clubs: " + suitCounts[2]);
    System.out.println("Spades: " + suitCounts[3]);

    boolean suitCheck = (suitCounts[0] == 13 && suitCounts[1] == 13 && suitCounts[2] == 13 && suitCounts[3] == 13);
    System.out.println("Correct number of each suit: " + suitCheck);

    // Verify that the King of Diamonds' getRank() returns -1
    BaseCard kingOfDiamonds = new BaseCard(13, "Diamonds");
    if (kingOfDiamonds.getRank() == -1) {
        System.out.println("King of Diamonds rank is correctly set to -1.");
    } else {
        System.out.println("King of Diamonds rank is NOT correctly set.");
    }
  }
  
  
  /**
   * Updates the state of the action buttons based on the current game state.
   * Activates or deactivates buttons depending on whether it's the start of a player's turn, a card has been drawn, or the player is an AI.
   */
  public void updateButtonStates() {
	 
	  if (players[currentPlayer].isComputer()) {
          // If a computer player is active, set all buttons to inactive
          for (Button button : buttons) {
              button.setActive(false);
          }
      } else {
          if (drawnCard == null && oneDrawnCard == false) {
              // If no card has been drawn, only allow drawing a card or declaring CABO
              buttons[0].setActive(true); // Draw from Deck
              buttons[2].setActive(caboPlayer == -1); // Declare CABO only if no one declared
              buttons[1].setActive(false); // Swap a Card
              buttons[3].setActive(false); // Use Action
              buttons[3].setLabel("Use Action");
              buttons[4].setActive(false); // End Turn
          } else {
              // If a card has been drawn, allow swapping and ending turn
              buttons[0].setActive(false); // Draw from Deck
              buttons[2].setActive(false); // Declare CABO
              buttons[1].setActive(true); // Swap a Card
              buttons[4].setActive(true); // End Turn
              System.out.println("drawn card isnt null");
           // If the drawn card is an ActionCard, enable the Use Action button
              if (drawnCard instanceof ActionCard){
            	  ActionCard actionCard = (ActionCard) drawnCard;
                  buttons[3].setActive(true); // Use Action
                  buttons[3].setLabel(actionCard.getActionType().toUpperCase()); // Update label to action type
                  System.out.println("enable action button");
              } else {
                  buttons[3].setActive(false); // Disable Use Action if no ActionCard
                  buttons[3].setLabel("Use Action");
                  System.out.println("disable action button");
              }
          }
      }
  }
                  
  /**
   * Renders the graphical user interface; also handles some game logic for the computer players.
   */
  @Override
  public void draw() {
    background(0, 128, 0);
    
//    System.out.println("in draw");
    // TODO: draw the deck and discard pile
    
     deck.draw(500, 80, false);
     discard.draw(600,80, true);
     textSize(16);
     fill(255);
     text("Deck:", 520, 60);
     text("Discard Pile:", 644, 60);
    // TODO: draw the players' hands
     
     drawPlayers();
    // TODO: draw the buttons
     
  // Draw the buttons
     for (Button button : buttons) {
         button.draw(); // Call the draw method for each button
     }
     
     
    // TODO: show the drawn card, if there is one
     
     if (drawnCard != null)
     {
     
        drawnCard.setFaceUp(true);
        drawnCard.draw(500, 500);
     }
    
    // TODO: if the game is over, display the game over status
     
     
     if (gameOver) {
         fill(255, 0, 0); // Red color for game over message
         textSize(32);
         text("Game Over!", width / 2 - 80, height / 2); // Centered message
     }
     
    // TODO: handle the computer players' turns
     
  // Display game messages with different colors based on the content int y = 200; // Starting y-position for messages
     int y = 200;
     for (String message : gameMessages) {
     textSize(16);
     if (message.contains("CABO")) {
     fill(255, 128, 0);
     } else if (message.contains("switched")) {
     fill(255, 204, 153);
     } else if (message.contains("spied")) {
     fill(255, 229, 204); } else {
     fill(255); }
     text(message, width - 300, y); // Adjust x-position as needed
     y += 20; // Spacing between messages }
     }
     if (players[currentPlayer].isComputer())
     {
       performAITurn();
     }
  }
  
  /**
   * Draws each player's name and hand.
   */
  private void drawPlayers() {
      for (int i = 0; i < players.length; i++) {
          Player player = players[i];
          // Draw player name
          text(player.getName(), 50, 45 + 150 * i);
          // Draw player hand
          player.getHand().draw(60+150*i);
      }
  }
  
  /**
   * Handles mouse press events during the game. It manages user interactions with buttons (that is, 
   * drawing a card, declaring CABO, swapping cards, using action cards) and updates the game state 
   * accordingly.
   */
  @Override
  public void mousePressed() {
    // TODO: if game is over or it's the computer's turn, do nothing
    
    
	// Check if the game is over or it's the computer's turn
	    if ((gameOver) || (players[currentPlayer].isComputer())) {
	        return;
	    }
	 // TODO: handle button clicks
	    
	 // Check each button to see if it is active and clicked
	    for (Button button : buttons) {
	        if (button.isActive() && (button.isMouseOver()))
	        {   
	            // Call the action associated with the button
	        	System.out.println("mouse pressed" + button.getLabel());
	            handleButtonAction(button.getLabel());
	            break; // Exit the loop after handling the button click
	        }
	    }
	    
    // handle additional action states (TODO: complete these methods)
    switch (actionState) {
      case SWAPPING -> handleCardSwap();
      case PEEKING -> handlePeek();
      case SPYING -> handleSpy();
      case SWITCHING -> handleSwitch();
      default -> { /* No action to be taken */ }
    }
  }
  
  private void handleButtonAction(String buttonLabel) {
	    if (buttonLabel.equals("Draw from Deck")) {
	        drawFromDeck(); // Handle drawing a card
	    } else if (buttonLabel.equals("Swap a Card")) {
	        actionState = ActionState.SWAPPING; // Set action state for swapping
	        setGameStatus("Click a card in your hand to swap it with the drawn card.");
	    } else if (buttonLabel.equals("Declare Cabo")) {
	        declareCabo(); // Handle declaring CABO
	        nextTurn(); // End the turn after declaring CABO
	    } else if (buttonLabel.equals("End Turn")) {
	        nextTurn(); // Handle using an action card
	        
	    }
	    else
	    {
	    	// Handle action cards dynamically
            handleDynamicAction(buttonLabel);
	    }
	    
  }
  
  private void handleDynamicAction(String buttonLabel) {
	    if (drawnCard instanceof ActionCard) {
	        ActionCard actionCard = (ActionCard) drawnCard;
	        String actionType = actionCard.getActionType().toUpperCase();

	        if (buttonLabel.equals(actionType)) {
	            handleUseAction(); // Handle using an action card
	        }
	    }
  }
  
  private void handleUseAction() {
	    // Reset the label of the Use Action button
	    buttons[3].setLabel("Action Card"); // Assuming buttons[3] is the Use Action button

	    if (drawnCard instanceof ActionCard) {
	        ActionCard actionCard = (ActionCard) drawnCard; // Cast to ActionCard
	        selectedCardFromCurrentPlayer = -1; // Reset selected card

	        // Get the action type from the drawn card
	        String actionType = actionCard.getActionType();

	        // Set the action state based on the action type
	        switch (actionType) {
	            case "peek":
	                actionState = ActionState.PEEKING;
	                buttons[3].setLabel(actionCard.getActionType().toUpperCase());
	                setGameStatus("Click a card in your hand to peek at it.");
	                break;
	            case "spy":
	                actionState = ActionState.SPYING;
	                buttons[3].setLabel(actionCard.getActionType().toUpperCase());
	    	        setGameStatus("Click a card in another player's hand to spy on it.");
	                break;
	            case "switch":
	                actionState = ActionState.SWITCHING;
	                buttons[3].setLabel(actionCard.getActionType().toUpperCase());
	                setGameStatus("Click a card from your hand, then a card from another Kingdom's hand to switch.");
	                break;
	            default:
	                actionState = ActionState.NONE; // Default state if action type is unknown
	                buttons[3].setLabel(actionCard.getActionType());
	                setGameStatus("Unknown action type.");
	                break;
	        }
	    }
	}
  ///////////////////////////////////// BUTTON CLICK HANDLERS /////////////////////////////////////
  
  /**
   * Handles the action of drawing a card from the deck.
   * If the deck is empty, the game ends. Otherwise, the drawn card is added to the current player's hand.
   * The game status and button states are updated accordingly.
   */
  public void drawFromDeck() {
    // TODO: if the deck is empty, game over
	  
	// Check if the deck is empty
	    if (deck.isEmpty()) {
	        // End the game if the deck is empty
	        setGameStatus("The deck is empty. Game over!");
	        // Implement any additional game over logic here
	        return;
	    }
	    
    // TODO: otherwise, draw the next card from the deck
	    
	 // Draw the next card from the deck
	    drawnCard = deck.drawCard(); // Assuming drawCard() returns the top card

	 // Add the drawn card to the current player's hand
	    Player currentPlayer1 = players[currentPlayer]; // Get the current player
	    try {
	    	currentPlayer1.getHand().addCard(drawnCard); // Add card to player's hand
	    }
	    catch(Exception e)
	    {
	    	System.out.println("hand full");
	    }
	    

    // TODO: update the gameMessages log: player.name+" drew a card."
	    
	    // Update the gameMessages log
	    setGameStatus(currentPlayer1.getName() + " drew a card.");

    // TODO: update the button states
	    
	    // Update the button states
	    updateButtonStates();
  }
  
  /**
   * Handles the action of declaring CABO.
   * Updates the game status to show that the player has declared CABO.
   */
  public void declareCabo() {
    // TODO: update the gameMessages log: player.name+" declares CABO!"
    // TODO: set the caboPlayer to the current player's index
    // TODO: end this player's turn

	    // Update the gameMessages log
	    setGameStatus(players[currentPlayer].getName() + " declares CABO!");

	    // Set the caboPlayer to the current player's index
	    caboPlayer = currentPlayer;

	 // End this player's turn and advance to the next
	    nextTurn();
	    
	    // End this player's turn
	 //   gameOver = true;
	   // displayGameOver();
  }
  
  ///////////////////////////////////// ACTION STATE HANDLERS /////////////////////////////////////
  
  /**
   * This method runs when the human player has chosen to SWAP the drawn card with one from their
   * hand. Detect if the mouse is over a card from the currentPlayer's hand and, if it is, swap the
   * drawn card with that card.
   * 
   * If the mouse is not currently over a card from the currentPlayer's hand, this method does 
   * nothing.
   */
  public void handleCardSwap() {
    // TODO: find a card from the current player's hand that the mouse is currently over
      // TODO: swap that card with the drawnCard
      // TODO: add the swapped-out card from the player's hand to the discard pile
      // TODO: update the gameMessages log: "Swapped the drawn card with card "+(index+1)+" in the hand."
      // TODO: set the drawnCard to null and the actionState to NONE
      // TODO: set all buttons except End Turn to inactive
	  int cardIndex = players[currentPlayer].getHand().indexOfMouseOver();
	  System.out.println("mouse over" + cardIndex);
	  if (cardIndex != -1) { // If a card is found
	        // Swap the drawn card with the card at cardIndex
		  System.out.println("in swap card before" + players[currentPlayer].getHand().size());
	        BaseCard swappedCard = players[currentPlayer].getHand().swap(drawnCard,cardIndex );
	        System.out.println("in swap card after" + players[currentPlayer].getHand().size());
	        // Add the swapped-out card to the discard pile
	        discard.addCard(swappedCard);
	        
	        // Update the gameMessages log
	        setGameStatus("Swapped the drawn card with card " + (cardIndex + 1) + " in the hand.");
	        
	        // Set the drawnCard to null and the actionState to NONE
	        drawnCard = null;
	        oneDrawnCard = true;
	        actionState = ActionState.NONE;
	        
	     // Set all buttons except End Turn to inactive
	        updateButtonStates();
    
      // TODO: uncomment this code to erase all knowledge of the card at that index from the AI
      // (you may need to adjust its indentation and/or change some variables)
    
      AIPlayer AI;
      for (int j = 1; j < players.length; ++j) {
        AI = (AIPlayer) players[j];
        AI.setCardKnowledge(0, cardIndex, false);
      }
     }
	  
  }
  
  /**
   * Handles the action of peeking at one of your cards. The player selects a card from their own 
   * hand, which is then revealed (set face-up).
   * 
   * If the mouse is not currently over a card from the currentPlayer's hand, this method does 
   * nothing.
   */
  public void handlePeek() {
    // TODO: find a card from the current player's hand that the mouse is currently over
      // TODO: set that card to be face-up
      // TODO: update the gameMessages log: "Revealed card "+(index+1)+" in the hand."
      // TODO: add the drawnCard to the discard, set drawnCard to null and actionState to NONE
      // TODO: set all buttons except End Turn to inactive
	  
	  int cardIndex = players[currentPlayer].getHand().indexOfMouseOver();
	  if (cardIndex != -1) { // If a card is found
	        // Set the card to be face-up
		     players[currentPlayer].getHand().setFaceUp(cardIndex, true);
	        
	        // Update the gameMessages log
	        setGameStatus("Revealed card " + (cardIndex + 1) + " in the hand.");
	        
	        // Add the drawnCard to the discard pile, set drawnCard to null, and actionState to NONE
	        if (drawnCard != null) {
	            discard.addCard(drawnCard);
	            drawnCard = null;
	            oneDrawnCard = true;
	        }
	        actionState = ActionState.NONE;
	        
	        // Set all buttons except End Turn to inactive
	        updateButtonStates();
	        
	  }
  }
  
  /**
   * Handles the spy action, allowing the current player to reveal one of another player's cards.
   * The current player selects a card from another player's hand, which is temporarily revealed.
   * 
   * If the mouse is not currently over a card from another player's hand, this method does nothing.
   */
  public void handleSpy() {
    // TODO: find a card from any player's hand that the mouse is currently over
      // TODO: if it is not one of their own cards, set it to be face-up
      // TODO: update the gameMessages log: "Spied on "+player.name+"'s card.";
      // TODO: add the drawnCard to the discard, set drawnCard to null and actionState to NONE
      // TODO: set all buttons except End Turn to inactive
	// Iterate over all players to check their hands
	 for (int i = 0; i < players.length; i++) {
	        Player player = players[i];
	        int cardIndex = player.getHand().indexOfMouseOver();
	        
	     // If a card is found and it's not the current player's card
	        if (cardIndex != -1 && i != currentPlayer) {
	            // Set the card to be face-up
	            player.getHand().setFaceUp(cardIndex, true);
	            
	            // Update the gameMessages log
	            setGameStatus("Spied on " + player.getName() + "'s card.");
	            
	            // Add the drawnCard to the discard pile, set drawnCard to null, and actionState to NONE
	            if (drawnCard != null) {
	                discard.addCard(drawnCard);
	                drawnCard = null;
	                oneDrawnCard = true;
	            }
	            actionState = ActionState.NONE;
	            
	            // Set all buttons except End Turn to inactive
	            updateButtonStates();
	            break; // Exit after spying on one card
	        }
	 }
  }
  

  /**
   * Handles the switch action, allowing the current player to switch one of their cards with a 
   * card from another player's hand.
   * 
   * This action is performed in 2 steps, in this order:
   *   (1) select a card from the current player's hand
   *   (2) select a card from another player's hand
   * 
   * If the mouse is not currently over a card, this method does nothing.
   */
  public void handleSwitch() {
    // TODO: add CaboGame instance variable to store the index of the card from the currentPlayer's hand
    
	  Player player = players[currentPlayer];
	  int cardIndex = players[currentPlayer].getHand().indexOfMouseOver();
	  
    // TODO: check if the player has selected a card from their own hand yet
    // TODO: if they haven't: determine which card in their own hand the mouse is over & store it
    // and do nothing else
    
	// Step 1: Select a card from the current player's hand
	    if (selectedCardFromCurrentPlayer == -1) {
	        if (cardIndex != -1) {
	            selectedCardFromCurrentPlayer = cardIndex; // Store the selected card index
	            setGameStatus("Selected card " + (cardIndex + 1) + " from your hand. Now select a card from another player's hand.");
	        }
	        return; // Exit after selecting the first card
	    }
	    
    // TODO: if they have selected a card from their own hand already:
      // TODO: find a card from any OTHER player's hand that the mouse is currently over
      // TODO: swap the selected card with the card from the currentPlayer's hand
      // TODO: update the gameMessages log: "Switched a card with "+player.name
      // TODO: add the drawnCard to the discard, set drawnCard to null and actionState to NONE
      // TODO: set all buttons except End Turn to inactive
    
	 // Step 2: Select a card from another player's hand
	    for (int i = 0; i < players.length; i++) {
	        if (i != currentPlayer) {
	            Player otherPlayer = players[i];
	            int otherPlayerCardIndex = otherPlayer.getHand().indexOfMouseOver();
	            if (otherPlayerCardIndex != -1) {
	                // Swap the selected card with the card from the other player's hand
	            	  // Use switchCards to swap the cards
	                player.getHand().switchCards(selectedCardFromCurrentPlayer, otherPlayer.getHand(), otherPlayerCardIndex);

	                // Update the gameMessages log
	                setGameStatus("Switched a card with " + otherPlayer.getName());

	                // Add the drawnCard to the discard, set drawnCard to null, and actionState to NONE
	                if (drawnCard != null) {
	                    discard.addCard(drawnCard);
	                    drawnCard = null;
	                    oneDrawnCard = true;
	                }
	                actionState = ActionState.NONE;
	                
	             // Set all buttons except End Turn to inactive
	                updateButtonStates();

	                // Reset the selected card index
	                selectedCardFromCurrentPlayer = -1;
	                break; // Exit after completing the switch

	            }
	        }
	    }
      // TODO: uncomment this code to update the knowledge of the swapped card for the other player
      // (you may need to adjust its indentation and variables)
    
      /*boolean knowledge = ((AIPlayer)players[i]).getCardKnowledge(i, otherPlayerCardIndex);
      ((AIPlayer)players[i]).setCardKnowledge(i, otherPlayerCardIndex,
          ((AIPlayer)players[i]).getCardKnowledge(currentPlayer, currentPlayerCardIndex));
      ((AIPlayer)players[i]).setCardKnowledge(currentPlayer, currentPlayerCardIndex, knowledge);//*/

      // TODO: reset the selected card instance variable to -1
  }
  
  /////////////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * Advances the game to the next player's turn.
   * Hides all players' cards, updates the current player, checks for game-over conditions,
   * resets action states, and updates the UI button states for the new player's turn.
   */
  public void nextTurn() {
    // TODO: hide all players' cards
	  
	  for (Player player : players) {
		    for (int i=0; i<4; i++)
		    {
		 //   	System.out.println("in next turn" + player.getHand().size());
	           player.getHand().setFaceUp(i, false);; // making cards facedown
	        }
	     }
    // TODO: if there is still an active drawnCard, discard it and set drawnCard to null
	  
	  if (drawnCard != null) {
	        discard.addCard(drawnCard);
	        drawnCard = null; // Reset drawnCard to null
	    }
    // TODO: advance the current player to the next one in the list
	  
	  currentPlayer = (currentPlayer+1) % 4;
	  System.out.println("next turn"+ players[currentPlayer].getName());
  // TODO: check if the new player is the one who declared CABO (and end the game if so)
	  
	  if (currentPlayer == caboPlayer) { // Check if the current player declared Cabo
	        gameOver = true; // Set gameOver flag
	        displayGameOver(); // Handle end-game logic
	        // Optionally, you could handle end-game logic here
	        return; // Exit the method
	    }
	  
    // TODO: update the gameMessages log: "Turn for "+player.name
	  
	  setGameStatus("Turn for " + players[currentPlayer].getName());
    // TODO: reset the action state to NONE
	  
	  actionState = ActionState.NONE;
	  
    // TODO: update the button states
	  
	  updateButtonStates();
	  oneDrawnCard = false;
  }
  
  /**
   * Displays the game-over screen and reveals all players' cards.
   * The method calculates each player's score, identifies the winner, and displays
   * a message about the game's result, including cases where there is no winner.
   * 
   * We've provided the code for the GUI parts, but the logic behind this method is still TODO
   */
  public void displayGameOver() {
    // Create a dimmed background overlay
    fill(0, 0, 0, 200);
    rect(0, 0, width, height);
    fill(255);
    textSize(32);
    textAlign(CENTER, CENTER);
    text("Game Over!", (float) width / 2, (float) height / 2 - 150);

    // TODO: reveal all players' cards

    // TODO: calculate and display each player's score
    int yPosition = height / 2 - 100;
    textSize(24);
      // TODO: uncomment to Display a player's score
      //text(player.getName() + "'s score: " + score, (float) width / 2, yPosition);
      yPosition += 30;

    // TODO: check if there is a tie or a specific CABO winner (lowest score wins)
      int score=0;
      int minScore=1000;
      for(int i=0;i<4;i++) {
    	  for(int j=0;j<4;j++)
    	  {
    		 score= score+ players[i].getHand().getRankAtIndex(j);
    		 setGameStatus(players[i].getName() + "'s score" + score);
    		 
    	  }
    	  if(score<minScore) {
    		  minScore=score;
    		  
    	  }
      }
    String winner = null;

    // TODO: display this message if there is no winner
    text("No Winner. The war starts.", (float) width / 2, yPosition + 30);

    // TODO: display this message if there is a winner
    text("Winner: " + winner, (float) width / 2, yPosition + 30);
  }
  
  /**
   * PROVIDED: Sets the current game status message and updates the message log.
   * If the message log exceeds a maximum number of messages, the oldest message is removed.
   *
   * @param message the message to set as the current game status.
   */
  private void setGameStatus(String message) {
    gameMessages.add(message);
    int MAX_MESSAGES = 15;
    if (gameMessages.size() > MAX_MESSAGES) {
      gameMessages.remove(0); // Remove the oldest message
    }
  }
  
  /////////////////////////////////////////////////////////////////////////////////////////////////
  // The 2 methods below this line are PROVIDED in their entirety to run the AIPlayer interactions 
  // with the CABO game. Uncomment them once you are ready to add AIPlayer actions to your game!
  /////////////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * Performs the AI player's turn by drawing a card and deciding whether to swap, discard, or use 
   * an action card.
   * If the AI player draws a card that is better than their highest card, they swap it; otherwise, 
   * they discard it.
   * If the drawn card is an action card, the AI player performs the corresponding action.
   * If the AI player's hand value is low enough, they may declare CABO.
   */
    private void performAITurn() {
    AIPlayer aiPlayer = (AIPlayer) players[currentPlayer];
    String gameStatus = aiPlayer.getName() + " is taking their turn.";
    setGameStatus(gameStatus);

    // Draw a card from the deck
    drawnCard = deck.drawCard();
    if (drawnCard == null) {
      gameOver = true;
      return;
    }

    gameStatus = aiPlayer.getName() + " drew a card.";
    setGameStatus(gameStatus);

    // Determine if AI should swap or discard
    int drawnCardValue = drawnCard.getRank();
    int highestCardIndex = aiPlayer.getHighestIndex();
    if (highestCardIndex == -1) {
      highestCardIndex = 0;
    }
    int highestCardValue = aiPlayer.getHand().getRankAtIndex(highestCardIndex);

    // Swap if the drawn card has a lower value than the highest card in hand
    if (drawnCardValue < highestCardValue) {
      BaseCard cardInHand = aiPlayer.getHand().swap(drawnCard, highestCardIndex);
      aiPlayer.setCardKnowledge(aiPlayer.getLabel(), highestCardIndex, true);
      discard.addCard(cardInHand);
      gameStatus = aiPlayer.getName() + " swapped the drawn card with card " + (highestCardIndex + 1) + " in their hand.";
      setGameStatus(gameStatus);
    } else if (drawnCard instanceof ActionCard) {
      // Use the action card
      String actionType = ((ActionCard) drawnCard).getActionType();
      gameStatus = aiPlayer.getName() + " uses an action card: " + actionType;
      setGameStatus(gameStatus);
      performAIAction(aiPlayer, actionType);
      discard.addCard(drawnCard);
    } else {
      // Discard the drawn card
      discard.addCard(drawnCard);
      gameStatus = aiPlayer.getName() + " discarded the drawn card: " + drawnCard;
      setGameStatus(gameStatus);
    }

    // AI may declare Cabo if hand value is low enough
    int handValue = aiPlayer.calcHandBlind();
    if (handValue <= random(13, 21) && caboPlayer == -1) {
      declareCabo();
    }

    // Prepare for the next turn
    drawnCard = null;
    nextTurn();
  }//*/
  
  /**
   * Performs the specified action for the AI player based on the drawn action card.
   * Actions include peeking at their own cards, spying on another player's card, or switching cards with another player.
   *
   * @param aiPlayer   the AI player performing the action.
   * @param actionType the type of action to perform ("peek", "spy", or "switch").
   */
  private void performAIAction(AIPlayer aiPlayer, String actionType) {
    Player otherPlayer = players[0]; // Assuming Player 1 is the human player
    String gameStatus = "";
    switch (actionType) {
      case "peek" -> {
        // AI peeks at one of its own cards
        int unknownCardIndex = aiPlayer.getUnknownCardIndex();
        if (unknownCardIndex != -1) {
          aiPlayer.setCardKnowledge(aiPlayer.getLabel(), unknownCardIndex, true);
          gameStatus = aiPlayer.getName() + " peeked at their card " + (unknownCardIndex + 1);
          setGameStatus(gameStatus);
        }
      }
      case "spy" -> {
        // AI spies on one of the human player's cards
        int spyIndex = aiPlayer.getSpyIndex();
        if (spyIndex != -1) {
          aiPlayer.setCardKnowledge(0, spyIndex, true);
          gameStatus = aiPlayer.getName() + " spied on Player 1's card " + (spyIndex + 1);
          setGameStatus(gameStatus);
        }
      }
      case "switch" -> {
        // AI switches one of its cards with one of the human player's cards
        int aiCardIndex = aiPlayer.getHighestIndex();
        if (aiCardIndex == -1) {
          aiCardIndex = (int) random(aiPlayer.getHand().size());
        }
        int otherCardIndex = aiPlayer.getLowestIndex(otherPlayer);
        if (otherCardIndex == -1)
          otherCardIndex = (int) random(otherPlayer.getHand().size());

        // Swap the cards between AI and the human player
        aiPlayer.getHand().switchCards(aiCardIndex, otherPlayer.getHand(), otherCardIndex);
        boolean preCardKnowledge = aiPlayer.getCardKnowledge(aiPlayer.getLabel(), aiCardIndex);
        aiPlayer.setCardKnowledge(aiPlayer.getLabel(), aiCardIndex, aiPlayer.getCardKnowledge(0, otherCardIndex));
        aiPlayer.setCardKnowledge(0, otherCardIndex, preCardKnowledge);

        gameStatus = aiPlayer.getName() + " switched card " + (aiCardIndex + 1) + " with " + otherPlayer.getName() + "'s " + (otherCardIndex + 1) + ".";
        setGameStatus(gameStatus);
      }
    }
  }

}
