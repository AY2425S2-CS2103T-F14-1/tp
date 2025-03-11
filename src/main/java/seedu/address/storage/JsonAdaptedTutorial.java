package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tutorial.Tutorial;

/**
 * Jackson-friendly version of {@link Tutorial}.
 */
class JsonAdaptedTutorial {

    private final String tutorialName;

    /**
     * Constructs a {@code JsonAdaptedTutorial} with the given {@code tutorialName}.
     */
    @JsonCreator
    public JsonAdaptedTutorial(@JsonProperty("tutorialName") String tutorialName) {
        this.tutorialName = tutorialName;
    }

    /**
     * Converts a given {@code Tutorial} into this class for Jackson use.
     */
    public JsonAdaptedTutorial(Tutorial source) {
        tutorialName = source.tutorialName;
    }

    /**
     * Converts this Jackson-friendly adapted tutorial object into the model's {@code Tutorial} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted tutorial.
     */
    public Tutorial toModelType() throws IllegalValueException {
        if (tutorialName == null) {
            throw new IllegalValueException(String.format("Tutorial's name field is missing!"));
        }
        return new Tutorial(tutorialName);
    }
}
