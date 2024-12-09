
import processing.core.PApplet;
import processing.core.PImage;

public class BaseCard {
    // Fields
    private static PImage cardBack; // Image for the back of the card
    private PImage cardImage; // Image for the front of the card
    protected boolean faceUp; // Face-up status
    private final int HEIGHT = 70; // Height of the card
    protected final int WIDTH = 50; // Width of the card
    protected static PApplet processing; // Processing environment
    protected int rank; // Rank of the card (1 for Ace, 13 for King)
    protected String suit; // Suit of the card (e.g., "Hearts", "Diamonds")
    private int x; // X coordinate for drawing
    private int y; // Y coordinate for drawing

    // Constructor
    public BaseCard(int rank, String suit) {
    	
    	if (processing == null) {
            throw new IllegalStateException("Processing environment must be set before creating a BaseCard.");
        }
        this.rank = rank;
        this.suit = suit;
        this.faceUp = false; // Default to face down
        // Initialize cardImage based on rank and suit
        // (Assuming card images are named according to their rank and suit)
        this.cardImage = processing.loadImage("images/" + rank + "_of_" + suit.toLowerCase() + ".png");
        if (cardBack == null) {
            cardBack = processing.loadImage("images/back.png"); // Load card back image
        }
    }

    public int getX()
    {
    	return x;
    }
    
    public int getY()
    {
    	return y;
    }
    // Static method to set the Processing environment
    public static void setProcessing(PApplet processing) {
        BaseCard.processing = processing;
    }

    // Method to get the rank of the card
    public int getRank() {
        return (rank == 13) ? -1 : rank; // Return -1 for King of Diamonds
    }

    // Method to set the face-up status of the card
    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    // Method to draw the card
    public void draw(int xPosition, int yPosition) {
        this.x = xPosition; // Set X coordinate
        this.y = yPosition; // Set Y coordinate
        processing.fill(255); // Draw white rectangle
        processing.rect(xPosition, yPosition, WIDTH, HEIGHT);
        if (faceUp) {
            processing.image(cardImage, xPosition, yPosition, WIDTH, HEIGHT); // Draw front image
        } else {
            processing.image(cardBack, xPosition, yPosition, WIDTH, HEIGHT); // Draw back image
        }
    }

    // Method to check if the mouse is over the card
    public boolean isMouseOver() {
    //	System.out.println("mouse position" + processing.mouseX + " " + processing.mouseY + " " + x + " " + y);
        return processing.mouseX >= x && processing.mouseX <= x + WIDTH &&
               processing.mouseY >= y && processing.mouseY <= y + HEIGHT;
    }

    // Method to return string representation of the card
    @Override
    public String toString() {
        return suit + " " + rank; // Format: "Suit Rank"
    }
}