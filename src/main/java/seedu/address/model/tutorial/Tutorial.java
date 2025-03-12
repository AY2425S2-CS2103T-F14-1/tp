package seedu.address.model.tutorial;

/**
 * Represents a Tutorial in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTutorialName(String)}
 */
public class Tutorial {
    public static final String MESSAGE_CONSTRAINTS = "Tutorial names should be alphanumeric";
    public final String tutorialName;

    public Tutorial(String tutorialName) {
        this.tutorialName = tutorialName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tutorial)) {
            return false;
        }

        Tutorial otherTut = (Tutorial) other;
        return tutorialName.equals(otherTut.tutorialName);
    }

    @Override
    public int hashCode() {
        return tutorialName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tutorialName + ']';
    }
}
