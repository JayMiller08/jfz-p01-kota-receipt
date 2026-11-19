import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReceiptTest {

    private static final String EQUALS = "================================";
    private static final String DASHES = "--------------------------------";

    private static final String EXPECTED = String.join("\n",
            EQUALS,
            "        KASI KOTA CORNER",
            "     Your kota, your way.",
            EQUALS,
            "Item                 Qty   Total",
            DASHES,
            "Full house kota        2  120.00",
            "Russian and chips      1   45.00",
            "Coke 500ml             2   36.00",
            DASHES,
            "TOTAL                     201.00",
            EQUALS,
            "     Thank you, come again!",
            EQUALS) + "\n";

    private static final String HEADER = String.join("\n",
            EQUALS,
            "        KASI KOTA CORNER",
            "     Your kota, your way.",
            EQUALS) + "\n";

    private static String output() throws Exception {
        return ConsoleRunner.run(Receipt::main, "");
    }

    /** Line number `index` (counting from 0), with a helpful message if it's missing. */
    private static String line(int index) throws Exception {
        String printed = output();
        String[] lines = printed.isEmpty() ? new String[0] : printed.split("\n");
        assertTrue(lines.length > index,
                "Your receipt has " + lines.length + " line(s) so far, so line " + (index + 1)
                        + " is missing. Keep going: the brief shows all 14 lines.");
        return lines[index];
    }

    @Test
    @DisplayName("Starts with the shop header")
    void header() throws Exception {
        assertTrue(output().startsWith(HEADER),
                "The first four lines are the header from the starter code. Did you change them?");
    }

    @Test
    @DisplayName("Prints the column headings, lined up")
    void headings() throws Exception {
        assertEquals("Item                 Qty   Total", line(4),
                "Line 5 is the headings: printf(\"%-20s%4s%8s%n\", \"Item\", \"Qty\", \"Total\"). "
                        + "%-20s lines Item up on the left in 20 characters; %4s and %8s line Qty and "
                        + "Total up on the right.");
    }

    @Test
    @DisplayName("Has a line of 32 dashes above and below the items")
    void dashLines() throws Exception {
        assertEquals(DASHES, line(5), "Line 6 should be exactly 32 dashes (-), with no spaces.");
        assertEquals(DASHES, line(9), "Line 10 should be exactly 32 dashes (-), after the three items.");
    }

    @Test
    @DisplayName("Prints the full house kota line")
    void kotaLine() throws Exception {
        assertEquals("Full house kota        2  120.00", line(6),
                "Use printf(\"%-20s%4d%8.2f%n\", \"Full house kota\", 2, 120.00). %d takes the whole "
                        + "number 2; %8.2f prints 120.00 with two decimals in 8 characters.");
    }

    @Test
    @DisplayName("Prints the Russian and chips line")
    void russianLine() throws Exception {
        assertEquals("Russian and chips      1   45.00", line(7),
                "Same widths as the kota line: printf(\"%-20s%4d%8.2f%n\", \"Russian and chips\", 1, 45.00).");
    }

    @Test
    @DisplayName("Prints the Coke line")
    void cokeLine() throws Exception {
        assertEquals("Coke 500ml             2   36.00", line(8),
                "Same widths again: printf(\"%-20s%4d%8.2f%n\", \"Coke 500ml\", 2, 36.00).");
    }

    @Test
    @DisplayName("Prints every price with exactly two decimals")
    void twoDecimals() throws Exception {
        for (int i = 6; i <= 8; i++) {
            assertTrue(line(i).matches(".*\\d+\\.\\d\\d"),
                    "Line " + (i + 1) + " should end with a price like 45.00. %.2f always prints two "
                            + "decimals, even when they're zeros.");
        }
    }

    @Test
    @DisplayName("Prints the TOTAL line, lined up with the prices")
    void totalLine() throws Exception {
        assertEquals("TOTAL                     201.00", line(10),
                "Use printf(\"%-24s%8.2f%n\", \"TOTAL\", 201.00). 24 is the Item and Qty columns "
                        + "together (20 + 4), so the total sits under the prices.");
    }

    @Test
    @DisplayName("Ends with the thank-you footer")
    void footer() throws Exception {
        assertEquals(EQUALS, line(11), "Line 12 should be 32 equals signs (=).");
        assertEquals("     Thank you, come again!", line(12),
                "Line 13 is the thank-you message with 5 spaces in front of it.");
        assertEquals(EQUALS, line(13), "Line 14 should be 32 equals signs (=) again.");
    }

    @Test
    @DisplayName("Matches the whole receipt exactly, with no extra lines or spaces")
    void wholeReceipt() throws Exception {
        String actual = output();
        for (String printed : actual.split("\n")) {
            assertEquals(printed.stripTrailing(), printed,
                    "This line has spaces at the end: \"" + printed + "\". Remove them.");
        }
        assertEquals(EXPECTED, actual,
                "Compare your receipt with the example in the brief, line by line. The receipt should "
                        + "end with a new line, and nothing should come after the last line of = signs.");
    }
}
