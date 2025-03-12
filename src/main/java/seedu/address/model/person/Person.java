package seedu.address.model.person;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;
import seedu.address.model.tutorial.Tutorial;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Tutorial> tutorialClasses = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Set<Tag> tags, Set<Tutorial> tuts) {
        requireAllNonNull(name, phone, email, tags, tuts);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.tags.addAll(tags);
        this.tutorialClasses.addAll(tuts);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public Set<Tutorial> getTutorials() {
        return Collections.unmodifiableSet(tutorialClasses);
    }

    /**
     * Returns true if both persons have the same email.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && tags.equals(otherPerson.tags)
                && tutorialClasses.equals(otherPerson.tutorialClasses);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, tags, tutorialClasses);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("tags", tags)
                .add("tutorialClasses", tutorialClasses)
                .toString();
    }

}
