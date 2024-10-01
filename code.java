import org.json.JSONObject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PolynomialConstantTerm {

    // Method to decode the value given the base
    public static BigInteger decodeValue(int base, String value) {
        return new BigInteger(value, base);
    }

    // Method to compute the constant term 'c' from the decoded roots of the polynomial
    public static BigInteger constantTermFromRoots(List<BigInteger> decodedRoots) {
        BigInteger constantTerm = BigInteger.ONE;
        for (BigInteger root : decodedRoots) {
            constantTerm = constantTerm.multiply(root.negate());
        }
        return constantTerm;
    }

    // Main method to parse JSON input, decode roots, and calculate the constant term
    public static BigInteger findConstantTerm(String jsonInput) {
        // Parse JSON input
        JSONObject jsonObject = new JSONObject(jsonInput);

        // Get the number of roots (n) and required roots (k)
        int n = jsonObject.getJSONObject("keys").getInt("n");
        int k = jsonObject.getJSONObject("keys").getInt("k");

        // List to store the decoded roots
        List<BigInteger> decodedRoots = new ArrayList<>();

        // Iterate through the provided roots and decode them
        for (int i = 1; i <= n; i++) {
            String rootKey = String.valueOf(i);
            if (jsonObject.has(rootKey)) {
                JSONObject rootData = jsonObject.getJSONObject(rootKey);
                int base = rootData.getInt("base");
                String value = rootData.getString("value");
                BigInteger decodedRoot = decodeValue(base, value);
                decodedRoots.add(decodedRoot);
            }
        }

        // Ensure we have at least k roots for polynomial calculation
        if (decodedRoots.size() < k) {
            throw new IllegalArgumentException("Not enough roots provided to compute the polynomial");
        }

        // Find the constant term using the first k decoded roots
        return constantTermFromRoots(decodedRoots.subList(0, k));
    }

    public static void main(String[] args) {
        // Sample Test Case 1 (provided)
        String testCase1 = """
            {
                "keys": {
                    "n": 4,
                    "k": 3
                },
                "1": {
                    "base": "10",
                    "value": "4"
                },
                "2": {
                    "base": "2",
                    "value": "111"
                },
                "3": {
                    "base": "10",
                    "value": "12"
                },
                "6": {
                    "base": "4",
                    "value": "213"
                }
            }
            """;

        // Sample Test Case 2 (provided)
        String testCase2 = """
            {
                "keys": {
                    "n": 9,
                    "k": 6
                },
                "1": {
                    "base": "10",
                    "value": "28735619723837"
                },
                "2": {
                    "base": "16",
                    "value": "1A228867F0CA"
                },
                "3": {
                    "base": "12",
                    "value": "32811A4AA0B7B"
                },
                "4": {
                    "base": "11",
                    "value": "917978721331A"
                },
                "5": {
                    "base": "16",
                    "value": "1A22886782E1"
                },
                "6": {
                    "base": "10",
                    "value": "28735619654702"
                },
                "7": {
                    "base": "14",
                    "value": "71AB5070CC4B"
                },
                "8": {
                    "base": "9",
                    "value": "122662581541670"
                },
                "9": {
                    "base": "8",
                    "value": "642121030037605"
                }
            }
            """;

        // Running the test cases
        System.out.println("Constant term for test case 1: " + findConstantTerm(testCase1));
        System.out.println("Constant term for test case 2: " + findConstantTerm(testCase2));
    }
}
