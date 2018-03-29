public class VigenereCrack {

    /**
     * This code is written in order to try and crack the Vigenere Cipher
     * which has been applied to a message. First we determine the key period,
     * 'd' using the static methods defined in this class then using that we split
     * the ciphertext into 'd' subblocks. We arrange the cipher text row by row,
     * and then...
     *
     * @author Daniel Snider
     */
    public static void main(String[] args){

        String cipherText = "NXRUOYLIWAKWMXLIPFAMNYXRAPYTFQFXATLPYZSLJGNXRILLOCFIPNLXLSXLDPMAPEAAQBVDORLDQFAQNUYJIZNHFJAKRHOMPVUAPZUQYNXDFXGRLDPIYIKMRAGNXPYXNPOMAZFIQBIALOQAAIJFLPMRAHYPAFLWLPKQMFFLDQFAQNOPQIYJYXJSLYWXWMFEHQRUOLUMDOONUQRIKEHXLDFARXDTFLSQPUZRILLIKZRXLTLLHFJLPQCEMOIQFLPSZFOLJIKEAZAOOBIKETLFIJBIALOQAOKQIPROKJYLDBLMKPYNAZLXAKYMAOBSEYNAQAIQOPFORJDHLOTFOTROZMNPRRRATSYRFMUPNRXATFAAIRHFLGPGNTMOAUOOIPISMYGNDZLXAKPKIQFIKEMXQOKPYXLDPMFLPTEUIQFTEMRLSGEGNPRRRATFMNFLSXLIQYTFMNXLDQFAQKOPRIJNOORAKROCYLIWORRHCSLXATFTIQWGVK";

        //Calculate the IC of the ciphertext
        int[] cipherOccArr;
        cipherOccArr = countCharOccArr(cipherText);
        double indxOfC = calcIC(cipherOccArr, cipherText.length());

        //Some print statements to make the contents clearer
        System.out.println("Occurrences of each letter: ");
        for (int i=0; i < cipherOccArr.length; i++){
            System.out.println((char)(i + 65) + ": " + cipherOccArr[i]);
        }
        System.out.println("The length of the ciphertext is: " + cipherText.length());
        System.out.println("The estimated index of coincidence is: " + indxOfC);

        //The I.C. is closer to d = '2', but I tested that and d = '3' is the correct one.
        int d = 3;

        //Making 'd' subblocks stored in a 2D array.
        char[][] subBlocks = makeSubblocks(cipherText, d);

        //Print Statement for the 2D array
        for(int i=0; i<d; i++){
            for(int j=0; j < cipherText.length()/d; j++){
                System.out.print(subBlocks[i][j] + " ");
            }
            System.out.println("");
        }

        //These values are used to find the key
        double [] cipherFreqs;
        double [] plainFreqs = {0.078, 0.013, 0.029, 0.041, 0.131, 0.029, 0.014, 0.059, 0.068, 0.002, 0.004, 0.036,
                                0.026, 0.073, 0.082, 0.022, 0.001, 0.066, 0.065, 0.09, 0.028, 0.01, 0.015, 0.003, 0.015,
                                0.001};

        //This section uses the given method to find the key
        for(int i = 0; i < d; i++){
            cipherFreqs = calcSubCipherFreq(cipherText, d, subBlocks, i);
            double[] c = new double[26];
            for(int j = 0; j < 26; j++) {
                c[j] = eWiseProdSumShift(cipherFreqs, plainFreqs, j);
            }
            double max = c[0];
            int maxI = 0;
            for (int j = 1; j < c.length; j++)
            {
                if (c[j] > max)
                {
                    max = c[j];
                    maxI = j;
                }
            }
            System.out.print((char)(maxI + 65));
        }



    }

    //Calculates Index of Coincidence to find the value of 'd'.
    private static double calcIC(int[] cipherOccurIn, int numChars){
        double iC = 0.0;
        for(int i = 0; i < 26; i++){
            iC += cipherOccurIn[i]*(cipherOccurIn[i] - 1);
        }
        iC = iC/(double)(numChars * (numChars - 1));
        return iC;
    }

    //Reads a string, returns the number of occurrences of capital letters in the english alphabet.
    private static int[] countCharOccArr(String cipherIn){
        int[] charOccurOut = new int[26];
        for(int i = 0; i < charOccurOut.length; i++){
            charOccurOut[i] =  countCharOccur(cipherIn, (char)(i+65));
        }
        return charOccurOut;
    }

    //Counts the number of occurrences of a letter in a given string
    private static int countCharOccur(String cipherIn , char letter){
        int occurNum = 0;
        for (int i=0; i < cipherIn.length(); i++)
        {
            if (cipherIn.charAt(i) == letter)
            {
                occurNum++;
            }
        }
        return occurNum;
    }

    //Divides the cipher into d subblocks, and returns them as a 2D array
    private static char[][] makeSubblocks(String cipherIn, int d){
        int numCols = cipherIn.length()/d;
        char[][] subBlocks = new char[d][numCols];

        for(int i = 0; i < d; i++){
            for(int j = 0; j < numCols; j++){
                subBlocks[i][j] = cipherIn.charAt(j + (i*numCols));
            }
        }
        return subBlocks;
    }

    //Calculates the occurrence frequency of the letters for a given subblock (given by a column index)
    private static double[] calcSubCipherFreq(String cipherIn, int d, char[][] subBlocksIn, int rowIndex) {
        int cLength = cipherIn.length()/d;
        double[] cipherFreq = new double[26];
        String subBlockStr = "";
        for (int i = 0; i < cLength; i++){
            subBlockStr += subBlocksIn[rowIndex][i] + "";
        }
        int[] occArr = countCharOccArr(subBlockStr);
        for(int i = 0; i < occArr.length; i++){
            cipherFreq[i] = (double)occArr[i]/cLength;
        }
        return cipherFreq;
    }

    //Calculates the element-wise product of cipherFreqs + 'k' shifted plainFreqs
    private static double eWiseProdSumShift(double[] cipherFreqsIn, double[] plainFreqsIn, int k){
        double result = 0;
        for(int i = 0; i < 26; i++){
            result += cipherFreqsIn[i]*plainFreqsIn[(i + k) % 26];
        }
        return result;
    }


}
