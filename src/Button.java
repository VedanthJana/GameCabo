import processing.core.PApplet;

public class Button {
    private boolean active;
    private int height;
    private String label;
    protected static PApplet processing;
    private int width;
    private int x;
    private int y;

    // Constructor
    public Button(String label, int x, int y, int width, int height) {
        if (processing == null) {
            throw new IllegalStateException("Processing environment not initialized.");
        }
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = false; // inactive by default
    }

    // Static method to set the Processing environment
    public static void setProcessing(PApplet processing) {
        Button.processing = processing;
    }

    // Returns the label of this button
    public String getLabel() {
        return this.label;
    }

    // Changes the label of this button
    public void setLabel(String label) {
        this.label = label;
    }

    // Returns whether the button is currently active
    public boolean isActive() {
        return this.active;
    }

    // Sets the active state of the button
    public void setActive(boolean active) {
        this.active = active;
    }

    // Renders the button on the Processing canvas
    public void draw() {
    	if (active) {
            if (isMouseOver()) {
                processing.fill(150); // Active button with mouse over
            } else {
                processing.fill(200); // Active button without mouse over
            }
        } else {
            processing.fill(255,51,51); // Inactive button
        }
        processing.rect(x, y, width, height, 5); // Draw the button with rounded corners

        processing.fill(0); // Set text color to black
        processing.textSize(14);
        processing.textAlign(processing.CENTER, processing.CENTER);
        processing.text(label, x + width / 2, y + height / 2); // Draw the button's label
    }

    // Checks if the mouse is currently over this button
    public boolean isMouseOver() {
        return processing.mouseX >= x && processing.mouseX <= x + width &&
               processing.mouseY >= y && processing.mouseY <= y + height;
    }
}