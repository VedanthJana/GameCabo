public class ActionCard extends BaseCard {
    // Field
    private String actionType; // The type of action associated with this card

    // Constructor
    public ActionCard(int rank, String suit, String actionType) {
        super(rank, suit); // Call the constructor of the BaseCard
        
        if (actionType == null || actionType.isEmpty()) {
            throw new IllegalStateException("Action type must be set before constructing an ActionCard.");
        }
        this.actionType = actionType; // Set the action type
    }

    // Method to get the action type
    public String getActionType() {
        return actionType; // Return the action type
    }

    // Override toString method to include action type
    @Override
    public String toString() {
        return super.toString() + " (Action: " + actionType + ")"; // Format: "Suit Rank (Action: actionType)"
    }
}