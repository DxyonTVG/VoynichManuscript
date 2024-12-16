import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;



/**
 * Project: Voynich Manuscript Project
 * Purpose Details: Figure out what this thing means!!!!
 * Course: IST 242
 * Author: Dayon McCray
 * Date Developed: 12/2/2024
 * Last Date Changed: 12/15/2024
 * Rev: Added code to read all the manuscript from the page. Also added translations and NOTESSSSS!!!
 */

/**
 * Resources:
 * String Splitting - https://www.geeksforgeeks.org/split-string-java-examples/#
 * Spliting Strings Into Tokens - https://stackoverflow.com/questions/18739190/splitting-a-string-into-tokens
 * HashMap in Java - https://www.geeksforgeeks.org/java-util-hashmap-in-java-with-examples/
 * Voynich Page Text - https://www.voynich.nu/q03/f022r_tr.txt
 * What we know about the Voynich Manuscipt - https://aclanthology.org/W11-1511.pdf
 * The Voynich Manuscript - https://collections.library.yale.edu/catalog/2002046
 * ChatGPT - Resources for Dictionary implementation, String Splitting  and Token HELP
 */



public class VoynichAnalysis {

    public static void main(String[] args) {
        // We are giving the code a directory to load our manuscript so it can be read!!!
        String manuscriptFilePath = "manuscript.txt";

        // Now we are loading the manuscript from the path!!
        String textToAnalyze = loadManuscript(manuscriptFilePath);
        if (textToAnalyze == null || textToAnalyze.isEmpty()) {
            System.err.println("Error: No Manuscript!!!!!!!!!!");
            return;
        }

        // We obviously have to have a path to read from our dictionaries!!!!
        String dictionariesPath = "dictionaries";

        // Now we are loading all of the dictionaries
        Map<String, Set<String>> dictionaries = loadDictionaries(dictionariesPath);
        if (dictionaries.isEmpty()) {
            System.err.println("Error: No Dictionaries!!!!!!!");
            return;
        }

        //No we perform a lookup from the manuscript to see if we get and word hits from our dictionary!!!
        analyzeText(textToAnalyze, dictionaries);
    }

    /**
     * Loads the manuscript content from the file path.
     *
     * @param filePath This goes to the manuscript text file!
     * @return The manuscript as a string!!!.
     */
    private static String loadManuscript(String filePath) {
        StringBuilder manuscriptContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                manuscriptContent.append(line).append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error: I cant read the manuscript!!! " + e.getMessage());
            return null;
        }

        return manuscriptContent.toString();
    }

    /**
     * Loads the dictionaries resource path.
     *
     * @param resourcePath Path to the dictionaries folder in the resources directory.
     * @return the language and the words according to the dictionary!!
     * Needed help from ChatGPT to implement the right resources code so it can find the dictionaries, Everytime i tried i kept getting an error!
     */
    private static Map<String, Set<String>> loadDictionaries(String resourcePath) {
        Map<String, Set<String>> dictionaries = new HashMap<>();

        try {
            // Get the dictionaries directory as a resource
            URL resourceUrl = VoynichAnalysis.class.getClassLoader().getResource(resourcePath);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Cant find the resource!!" + resourcePath);
            }

            File dictionariesDir = new File(resourceUrl.toURI());
            File[] files = dictionariesDir.listFiles();

            if (files == null || files.length == 0) {
                throw new IllegalArgumentException("No dictionary in here!!! " + resourcePath);
            }

            for (File file : files) {
                String language = file.getName().replace(".txt", ""); // Extract language name
                Set<String> words = new HashSet<>(Files.readAllLines(file.toPath()));
                dictionaries.put(language, words);
                System.out.println("Loaded dictionary for: " + language + " (" + words.size() + " words)");
            }
        } catch (Exception e) {
            System.err.println("Error loading dictionaries: " + e.getMessage());
        }

        return dictionaries;
    }

    /**
     * Analyzes the manuscript text by comparing it to words in the language dictionary.
     *
     * @param text        Analyze the text from the manuscript!
     * @param dictionaries The dictionaries loaded from resources.
     *    Had to use ChatGPT to implement the tokens for the split text using "." delimiter!
     */
    private static void analyzeText(String text, Map<String, Set<String>> dictionaries) {
        System.out.println("Analyzing text: " + text);

        // Split the text using "." as the delimiter
        String[] tokens = text.split("\\.");

        for (String token : tokens) {
            boolean matched = false;

            for (Map.Entry<String, Set<String>> entry : dictionaries.entrySet()) {
                String language = entry.getKey();
                Set<String> words = entry.getValue();

                if (words.contains(token)) {
                    System.out.println("Found match: '" + token + "' in " + language + " dictionary.");
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                System.out.println("No match found for: '" + token + "'");
            }
        }
    }
}
