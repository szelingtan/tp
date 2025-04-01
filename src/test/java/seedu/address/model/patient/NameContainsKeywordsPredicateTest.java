package seedu.address.model.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PatientBuilder;

public class NameContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> keywordList1 = Collections.singletonList("first");
        List<String> keywordList2 = Arrays.asList("first", "second");

        NameContainsKeywordsPredicate predicate1 = new NameContainsKeywordsPredicate(keywordList1, false);
        NameContainsKeywordsPredicate predicate2 = new NameContainsKeywordsPredicate(keywordList2, false);
        NameContainsKeywordsPredicate predicate1Strict = new NameContainsKeywordsPredicate(keywordList1, true);

        // same object -> returns true
        assertTrue(predicate1.equals(predicate1));

        // same values -> returns true
        NameContainsKeywordsPredicate predicate1Copy = new NameContainsKeywordsPredicate(keywordList1, false);
        assertTrue(predicate1.equals(predicate1Copy));

        // different types -> returns false
        assertFalse(predicate1.equals(1));

        // null -> returns false
        assertFalse(predicate1.equals(null));

        // different keywords -> returns false
        assertFalse(predicate1.equals(predicate2));

        // same keywords, different isStrict -> returns false
        assertFalse(predicate1.equals(predicate1Strict));
    }

    @Test
    public void testNameContainsKeywordsPrefix() {
        // One keyword, prefix match
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.singletonList("Alex"));
        assertTrue(predicate.test(new PatientBuilder().withName("Alexa Tan").build()));

        // Multiple keywords, one matches
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("Al", "Tan"));
        assertTrue(predicate.test(new PatientBuilder().withName("Alexa Tan").build()));

        // Mixed-case prefix match
        predicate = new NameContainsKeywordsPredicate(Arrays.asList("aLeX"));
        assertTrue(predicate.test(new PatientBuilder().withName("Alexa Tan").build()));
    }

    @Test
    public void testNameDoesNotContainKeywords() {
        // No keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PatientBuilder().withName("Alexa Tan").build()));

        // Non-matching prefix
        predicate = new NameContainsKeywordsPredicate(List.of("Zan"));
        assertFalse(predicate.test(new PatientBuilder().withName("Alexa Tan").build()));
    }

    @Test
    public void test_nameMatchesStrict_returnsTrue() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("Alexa", "Tan"), true);
        assertTrue(predicate.test(new PatientBuilder().withName("Alexa Tan").build()));

        // Case-insensitive exact match
        predicate = new NameContainsKeywordsPredicate(List.of("aLeXa", "tAn"), true);
        assertTrue(predicate.test(new PatientBuilder().withName("Alexa Tan").build()));
    }

    @Test
    public void test_nameDoesNotMatchStrict_returnsFalse() {
        // Order mismatch
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("Tan", "Alexa"), true);
        assertFalse(predicate.test(new PatientBuilder().withName("Alexa Tan").build()));

        // Partial match
        predicate = new NameContainsKeywordsPredicate(List.of("Alex"), true);
        assertFalse(predicate.test(new PatientBuilder().withName("Alexa Tan").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");

        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords, false);
        String expected = predicate.getClass().getCanonicalName()
                + "{keywords=[keyword1, keyword2], isStrict=false}";
        assertEquals(expected, predicate.toString());

        NameContainsKeywordsPredicate strictPredicate = new NameContainsKeywordsPredicate(keywords, true);
        String expectedStrict = strictPredicate.getClass().getCanonicalName()
                + "{keywords=[keyword1, keyword2], isStrict=true}";
        assertEquals(expectedStrict, strictPredicate.toString());
    }
}
