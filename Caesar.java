/**
* Name: Alan Wu
* Pennkey: wualan
* Execution: java Caesar
*
* Program Description: Contains various functions to handle Caesar ciphers
*/

public class Caesar {
    
    /*
    * Description: converts a string to a symbol array,
    *              where each element of the array is an
    *              integer encoding of the corresponding
    *              element of the string.
    * Input:  the message text to be converted
    * Output: integer encoding of the message
    */
    public static int[] stringToSymbolArray(String str) {
        int[] val = new int[str.length()]; // Returned array of int values
        str = str.toUpperCase(); // Input string converted to uppercase
        
        // Initializes array 
        for (int i = 0; i < val.length; i++) {
            val[i] = (int)str.charAt(i) - 'A';
        }
        
        return val;
    }
    
    /*
    * Description: converts an array of symbols to a string,
    *              where each element of the array is an
    *              integer encoding of the corresponding
    *              element of the string.
    * Input:  integer encoding of the message
    * Output: the message text
    */
    public static String symbolArrayToString(int[] symbols) {
        String phrase = ""; // Returned string
        
        // Adds each character to the string
        for (int i = 0; i < symbols.length; i++) {
            phrase += (char)(symbols[i] + 'A');
        }
        
        return phrase;
    }
    
    /**
    * Description: Shifts a given int value by the offset value. If the shift goes 
    *              beyond the alphabet, the value wraps around i.e. 25 shifted by
    *              2 is 1.          
    * Input: Given int value corresponding to a character and int value that is the 
    *        number of positions the function shifts by.number
    * Output: If the input corresponds to a character is A through Z, then it 
    *         outputs the letter shifted by the given shift value. Otherwise, it
    *.        outputs the same value entered.
    */
    public static int shift(int symbol, int offset) {
        /* Check if the given symbol is a valid character, then returns 
         * then value shifted
         */
        if (symbol > 25 || symbol < 0) {
            return symbol;
        } else { 
            if (symbol + offset <= 25) {
                return symbol + offset;
            } else {
                return (symbol + offset) % 26;
            }
        }
    }
    
    /**
    * Description: Shifts a given int value, that corresponds to a letter, 
    *.             backwards by a given number of characters.
    * Input: A int value that corresponds to a character value and the number of 
    *        characters to shift the character value.
    * Output: If the character value corresponds to a letter, then outputs the
    *         character shifted by the given value. Otherwise returns the input 
    *         value.
    */

    public static int unshift(int symbol, int offset) {
        /* Check if the given symbol is a valid character. If it is, it shifts it 
         * back by the given offset. If it isn't, it returns it as it.
         */ 
        if (symbol > 25 || symbol < 0) {
            return symbol;
        } else {
            if (symbol - offset >= 0) {
                return symbol - offset;
            } else {
                return symbol + 26 - offset;    
            }
        }    
    }
    
    /**
    * Description: Encrypts a message by shifting all the letters by a given value
    * Input: The message to be encoded and the value to shift each letter by
    * Output: The shifted message
    */
    public static String encrypt(String message, int key) {
        int[] input = stringToSymbolArray(message); /* Message converted to a 
                                                     * symbol array
                                                     */
        int[] output = new int[input.length]; // Array of shifted symbols
        // Initializes array of shifted symbols
        for (int i = 0; i < output.length; i++) {
            output[i] = shift(input[i], key);
        }

        return symbolArrayToString(output);
    }
    
    /**
    * Description: Shifts all characters in the given string backwards by a given 
    *              amount
    * Input: The encrypted message to be decrypted and the value shift each character
    *        back by
    * Output: The decoded message
    */
    public static String decrypt(String cipher, int key) {
        int[] input = stringToSymbolArray(cipher); // Array of shifted symbols
        int[] output = new int[input.length]; // Array of unshifted symbols
        // Initializes array of unshifted symbols
        for (int i = 0; i < output.length; i++) {
            output[i] = unshift(input[i], key);
        }

        return symbolArrayToString(output);
    }
    
    /**
    * Description: Returns an array containing the frequencies of letter in the 
    *              English alphabet
    * Input: Name of a file containing the frequencies of English letters
    * Output: Array of dictionary frequencies
    */
    public static double[] getDictionaryFrequencies(String filename) {
        In inStream = new In(filename); // Sets up file reader
        double[] frequencies = new double[26]; // Array of frequencies
        
        // Initializes frequencies from document
        for (int i = 0; i < frequencies.length; i++) {
            frequencies[i] = inStream.readDouble();
        }
        
        return frequencies;
    }
    
    /**
    * Description: Finds the frequencies of letters in the symbol array and returns
    *              the frequencies in a double array.
    * Input: Message shifted into a symbol array
    * Output: Array of frequencies of letters appearing in the symbol array
    */
    public static double[] findFrequencies(int[] symbols) {
        double[] frequencies = new double[26]; // Array of symbol frequencies
        int numLetter = 0; // Number of letters in the symbol array
        
        // Counts the number of letters in the symbol array
        for(int i = 0; i < symbols.length; i++) {
            if (symbols[i] >= 0 && symbols[i] <= 25) {
                numLetter++;
            }
        }
        
        /* Counts number of a certain letter within the symbol 
         * array and initializes the frequency array
         */ 
        for (int i = 0; i < 26; i++) {
            double count = 0; // Number of a certain letter within the symbol array
            for (int j = 0; j < symbols.length; j++) {
                if (symbols[j] == i){
                    count++;
                }
            }
            frequencies[i] = count / numLetter;
        }
        return frequencies;
    }
    
    /**
    * Description: Scores the similarity of English frequencies and the frequencies 
    *              of the symbol array
    * Input: double[] of English letter frequencies and and a double[] of the letters
    *        in the symbol array
    * Output: Score of the similarities
    */
    public static double scoreFrequencies(double[] english, double[] currentFreqs) {
        double total = 0.0; // Score, with lower values meaning closer similarity
        for (int i = 0; i < 26; i++) {
            total += Math.abs(english[i] - currentFreqs[i]);
        }
        return total;
    }
    
    /* Description: Cracks a Caesar Cipher. Prints the English version of the input
     *              message.
     * Input: A string containing the name of the file with the message in it and a 
     *        string containing the name of the file with the frequencies of letters
     *        within the English language.
     * Output: The decoded message in string form.
     */ 
    public static String crack(String fileName, String textEnglish) {
        In inStream = new In(fileName); // File reader
        String message = inStream.readAll(); // File in String form
        int[] symbols = stringToSymbolArray(message); // File in num symbol form
        double[] currentFreqs = findFrequencies(symbols); // Freq of symbol array
        double[] english = getDictionaryFrequencies(textEnglish); // Freq of Enlgish
        double lowScore = Double.MAX_VALUE; // Lowest value from scoreFrequencies
        int key = 0; // Shift needed for decrypt
        
        /* Checks the similarities between the all shifts of the letter frequency
         * of the input message and the letter frequency of the Enlgish language
         */
        for (int i = 0; i < 26; i++) {
            if (lowScore > scoreFrequencies(english, currentFreqs)) {
                lowScore = scoreFrequencies(english, currentFreqs);
                key = i;
            }
            
            String newMessage = decrypt(message, i + 1); // Original message, shifted
            symbols = stringToSymbolArray(newMessage); // Shifted version of symbols
            currentFreqs = findFrequencies(symbols); // New frequency of symbols
        }
        return decrypt(message, key);
    }
    
    public static void main(String[] args) {

        // Tests stringToSymbolArray   
//         int[] phrase = stringToSymbolArray("zyxwvutsrqponmlkjihgfedcba");
//         for (int i = 0; i < phrase.length; i++) {
//             System.out.print(phrase[i] + " ");
//         }
//         System.out.println();
        
//         // Tests symbolArrayToString
//         System.out.println(symbolArrayToString(phrase));
//         System.out.println();
        
//         // Tests shift
//         for (int i = 0; i <= 25; i++) {
//             for (int j = 0; j <= 25; j++){
//                 System.out.print(shift(i, j) + " ");
//             }
//             System.out.println();
//         }
//         System.out.println();
        
//         // Tests unshift
//         System.out.println("Tests unshift");
//         for (int i = 0; i <= 25; i++) {
//             for (int j = 0; j <= 25; j++) {
//                 System.out.print(unshift(shift(i,j), j));    
//             }
//             System.out.println();
//         }
//         System.out.println();
        
//         // Tests encrypt
//         for (int i = 0; i <= 25; i++) {
//             System.out.println(encrypt("abcdefghijklmnopqrstuvwxyz", i));
//         }
        
//         // Tests decrypt
//         for (int i = 0; i <= 26; i++){
//            System.out.println(decrypt(encrypt("abcdefghijklmnopqrstuvwxyz", i), i));
//         }
        
//         // Tests getDictionaryFrequencies
//         double[] vals = getDictionaryFrequencies("english.txt");
//         for (int i = 0; i < 26; i++) {
//             System.out.println(vals[i]);
//         }
   
//         // Tests findFrequencies()
//         int[] bet = stringToSymbolArray("asdfghjkl");
//         double[] frequencies = findFrequencies(bet);
//         for (int i = 0; i < 26; i++) {
//             System.out.println(frequencies[i]);
//         }
        
        if (args[0].equals("crack")) {
            System.out.println(crack(args[1], args[2]));    
        } else if (args[0].equals("encrypt")) {
            In inStream = new In(args[1]);
            String message = inStream.readAll();
            int key = args[2].charAt(0) - 'A';
            System.out.println(encrypt(message, key));
        } else if (args[0].equals("decrypt")) {
            In inStream = new In(args[1]);
            String message = inStream.readAll();
            int key = args[2].charAt(0) - 'A';
            System.out.println(decrypt(message, key));
        }
    }    
}

