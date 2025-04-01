package seedu.address.model.patient;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Patient}'s {@code Name} matches either:
 * - Any of the keywords (case-insensitive prefix match), OR
 * - The full name exactly (case-insensitive), depending on strict mode.
 */
public class NameContainsKeywordsPredicate implements Predicate<Patient> {
    private final List<String> keywords;
    private final boolean isStrict;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this(keywords, false); // default to non-strict/partial match
    }

    public NameContainsKeywordsPredicate(List<String> keywords, boolean isStrict) {
        this.keywords = keywords;
        this.isStrict = isStrict;
    }

    @Override
    public boolean test(Patient patient) {
        String patientName = patient.getName().fullName.toLowerCase().trim();

        if (isStrict) {
            // Join all keywords into a full string and compare
            String searchName = String.join(" ", keywords).toLowerCase().trim();
            return patientName.equals(searchName);
        }

        // Prefix match: check if any keyword is a prefix of any word in the name
        String[] wordsInName = patientName.split("\\s+");
        return keywords.stream().anyMatch(keyword ->
                Arrays.stream(wordsInName)
                        .anyMatch(word -> word.startsWith(keyword.toLowerCase()))
        );
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords) && isStrict == otherPredicate.isStrict;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keywords", keywords)
                .add("isStrict", isStrict)
                .toString();
    }
}
