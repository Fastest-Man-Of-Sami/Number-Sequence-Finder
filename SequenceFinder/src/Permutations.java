import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Permutations {

    public static void main(String[] args) {
        List<String> totalPermutation = new ArrayList<>();
        String digits = "12345678";
        int digitSize = 9;
        generatePermutations("", digits, totalPermutation);
        allowedSet = new ArrayList<>(totalPermutation);
        Random r = new Random();
        // Preset an answer
        answer = totalPermutation.get(r.nextInt(0, totalPermutation.size() - 1));
        // Print all generated sequences
        boolean isInitialGuess = true;
        while (true) {
            if (allowedSet.size() == 1) {
                System.out.println("Current Guess:");
                System.out.println(allowedSet.getFirst());
                System.out.println("And the Answer is correct.");
                System.out.println("By the time the Answer is correct, the size of AllowedSet is " + allowedSet.size());
                break;
            }
            double maximumInformation = 0;
            String maximumInformationString = "";
            //for all possible guesses, i.e. the entire group, calculate their information, find the maximum
            //locate that guess, and utilize it.
            if (!isInitialGuess) {
                for (String guess : totalPermutation) {
                    //System.out.println("Analysing the guess of " + guess + " and calculating the entropy:");
                    double information = 0;
                    for (String assumedAnswer : allowedSet) {
                        int newAllowedCount = 0;
                        int supposedNumber = getSimilarity(guess, assumedAnswer);
                        for (String i : allowedSet) {
                            int candidateNumber = getSimilarity(i, guess);
                            newAllowedCount += supposedNumber == candidateNumber ? 1 : 0;
                        }
                        information += -(double) 1 / allowedSet.size() * Math.log((double) newAllowedCount / allowedSet.size());
                    }
                    //System.out.println("It's Information is:" + information);
                    if (information > maximumInformation) {
                        maximumInformation = information;
                        maximumInformationString = guess;
                    }
                }
            } else {
                double information = 0;
                for (String assumedAnswer : allowedSet) {
                    int newAllowedCount = 0;
                    int supposedNumber = getSimilarity(allowedSet.getFirst(), assumedAnswer);
                    for (String i : allowedSet) {
                        int candidateNumber = getSimilarity(i, allowedSet.getFirst());
                        newAllowedCount += supposedNumber == candidateNumber ? 1 : 0;
                    }
                    information += -(double) 1 / allowedSet.size() * Math.log((double) newAllowedCount / allowedSet.size());
                }
                //System.out.println("It's Information is:" + information);
                if (information > maximumInformation) {
                    maximumInformation = information;
                }
            }
            isInitialGuess = false;
            maximumInformationString = allowedSet.get(r.nextInt(0, allowedSet.size() - 1));
            if (maximumInformationString.equals(answer)) {
                System.out.println("Current Guess:");
                System.out.println(maximumInformationString);
                System.out.println("And the Answer is correct.");
                System.out.println("By the time the Answer is correct, the size of AllowedSet is " + allowedSet.size());
                break;
            }
            List<String> readyForRemoval = new ArrayList<>();
            int allowedSetOriginalSize = allowedSet.size();
            int returnedNumber = getSimilarity(maximumInformationString, answer);
            for (String i : allowedSet) {
                int candidateNumber = getSimilarity(i, maximumInformationString);
                if (candidateNumber != returnedNumber && !readyForRemoval.contains(i)) {
                    readyForRemoval.add(i);
                }
            }

            for (String i : readyForRemoval) {
                allowedSet.remove(i);
            }
            int allowedSetCurrentSize = allowedSet.size();
            System.out.println("Current Guess:");
            System.out.println(maximumInformationString);
            System.out.println("This guess would decrease the total entropy by " + maximumInformation + " in expectation.");
            System.out.println("In fact, the total entropy is decreased by " + -Math.log((double) allowedSetCurrentSize / allowedSetOriginalSize));
        }
    }

    public static String answer;
    public static List<String> allowedSet;

    // Recursive method to generate permutations
    public static void generatePermutations(String prefix, String remaining, List<String> result) {
        if (remaining.isEmpty()) {
            result.add(prefix);
        } else {
            for (int i = 0; i < remaining.length(); i++) {
                // Choose a digit
                String newPrefix = prefix + remaining.charAt(i);
                // Remove chosen digit from remaining
                String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
                // Recursively generate permutations with the new prefix and remaining digits
                generatePermutations(newPrefix, newRemaining, result);
            }
        }
    }

    public static int getSimilarity(String string1, String string2) {
        int count = 0;
        for (int i = 0; i < string1.length(); i++) {
            count += string1.charAt(i) == string2.charAt(i) ? 1 : 0;
        }
        return count;
    }
}
