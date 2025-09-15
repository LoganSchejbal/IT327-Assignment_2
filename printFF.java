
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class printFF {
    public static void main(String[] args){
        //Input to select a file
        //File must be in same folder or a path given
        Scanner input = new Scanner(System.in);
        String fileName = input.nextLine();
        input.close();


        //Creates the File instance
        File grammarFile = new File(fileName);
        Scanner reader = null;
        try {
            reader = new Scanner(grammarFile);
        } catch (java.io.FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            return;
        }

        //Skip first line - It is an unnecessary header
        reader.nextLine();

        //2D array for storing file's contents
        ArrayList<ArrayList<String>> fileContents = new ArrayList<>();
        
        //Puts file into array
        while (reader.hasNextLine()){
            
            //Uses delimiter to seperate, group, and remove characters
            String line = reader.nextLine();
            StringTokenizer tempLine = new StringTokenizer(line, " :=", false);

            //Adds line to tempArray
            ArrayList<String> tempArray = new ArrayList<>();
            while (tempLine.hasMoreTokens()){
                tempArray.add(tempLine.nextToken());
            }
            
            //Adds the tempArray to the full array
            fileContents.add(tempArray);
        }
        
        //Outputs fileContents's contents
        //UPDATE THIS ARRAY TO FOLLOW ASSIGNMENT
        for (int i = 0; i < fileContents.size(); ++i) {
            System.out.print(fileContents.get(i).get(0) + " ::= ");
            for(int j = 1; j < fileContents.get(i).size(); ++j){
                System.out.print(fileContents.get(i).get(j)+ " ");
            }
            System.out.print("\n");
        }

        reader.close();

        //Creates arrays that store the terminal and nonterminal symbols
        ArrayList<String> nonterminal = new ArrayList<>();
        ArrayList<String> terminal = new ArrayList<>();

        //Adds first element in each row to nonterminal
        //Also checks for duplicates
        for(int i = 0; i < fileContents.size(); ++i){
            String tempString = fileContents.get(i).get(0);
            boolean duplicate = false;
            for (int k = 0; k < nonterminal.size(); ++k){
                if (tempString.equals(nonterminal.get(k))){
                    duplicate = true;
                    break;
                }
                else{

                }
            }
            if(!duplicate){
                nonterminal.add(fileContents.get(i).get(0));
            }
        }

        //Finds all terminal Symbols
        for (int i = 0; i < fileContents.size(); ++i){
            for(int j = 1; j < fileContents.get(i).size(); ++j){
                String tempString = fileContents.get(i).get(j);
                boolean duplicate = false;

                //Lambda character is skipped here because it is not an actual symbol
                if(tempString.equals("lambda")){
               
                }
                else{

                    //Checks to see if its a nonterminal symbol
                    for(int k = 0; k < nonterminal.size(); ++k){
                        if (tempString.equals(nonterminal.get(k))){
                            duplicate = true;
                            break;
                        }
                        else{

                        }
                    }

                    //checks to see if it a duplicate
                    //Skipped if we already determined if its a nonterminal symbol
                    if(!duplicate){
                        for(int l = 0; l < terminal.size(); ++l){
                            if(tempString.equals(terminal.get(l))){
                                duplicate = true;
                                break;
                            }
                            else{

                            }
                        }
                    }
                    else{

                    }

                    //Adds to list of terminal symbols if:
                    // -not lambda
                    // -not nonterminal
                    // -not a duplicate
                    if (!duplicate){
                        terminal.add(tempString);
                    }
                    else{

                    }
                }

                
            }
        }


        //Simply prints out nonterminal sybols
        System.out.print("\nNon-terminal Symbols: ");
        for(int i = 0; i < nonterminal.size(); ++i){
            System.out.print(nonterminal.get(i) + " ");
        }

        //Copy and pasted, but for terminal symbols
        System.out.print("\nterminal Symbols: ");
        for(int i = 0; i < terminal.size(); ++i){
            System.out.print(terminal.get(i) + " ");
        }

        //Adds start symbol to end of fileContents for every rule with the start symbol
        String startSymb = nonterminal.get(0);
        for (int i = 0; i < fileContents.size(); ++i){
            if(startSymb.equals(fileContents.get(i).get(0))){
                fileContents.get(0).add("$");
            }
            else{
                break;
            }
        }

        //FirstSets find the initial first characters for each nonterminal symbol and gives consideration to the lambda character
        ArrayList<ArrayList<String>> firstSets = firstSet(fileContents, nonterminal);

        //refineFirstSets will then recurse, turning A -> first(A)
        //will continue to recurse until the function no longer changes anything
        firstSets = refineFirstSets(nonterminal, firstSets);

        //Prints out the final version of firstSets
        System.out.print("\n\nFirst Sets:\n===");
        for(int i = 0; i < firstSets.size(); ++i){
            System.out.print("\n" + nonterminal.get(i) + ": ");
            for(int j = 1; j < firstSets.get(i).size(); ++j){
                System.out.print(firstSets.get(i).get(j) + " ");
            }
        }

    }

    private static ArrayList<ArrayList<String>> firstSet(ArrayList<ArrayList<String>> fileContents, ArrayList<String> nonterminal){
        ArrayList<ArrayList<String>> firstSet = new ArrayList<>();
        
        //Just adds the nonterminal symbols into the vector so each line can be identified
        for (int i = 0; i < nonterminal.size(); ++i){
            ArrayList<String> tempList = new ArrayList<>();
            tempList.add(nonterminal.get(i));
            firstSet.add(tempList);
        }

        //Adds the first element of each rule to the set
        //position is identified as the nonterminal symbol at pos(0)
        //position is passed to addtoset which just adds the first to each set. 
        //Checks for lambda - if lambda is the last in the set -> addtoset. If something follows it, add the next element to the right to the set
        for (int i = 0; i < fileContents.size(); ++i){
            String position = new String();
            for (int j = 0; j < fileContents.get(i).size(); ++j){
                if( j == 0 ){
                    position = fileContents.get(i).get(j);
                }
                else{
                    if (fileContents.get(i).get(j).equals("lambda")){
                        if(j != fileContents.get(i).size()-1){
                            addToSet(position, firstSet, fileContents.get(i).get(j + 1));
                            break;
                        }
                        else{
                            addToSet(position, firstSet, fileContents.get(i).get(j));
                            break;
                        }
                    }
                    else{
                        addToSet(position, firstSet, fileContents.get(i).get(j));
                        break;
                    }
                }

            }
        }

        cleanupSets(firstSet);

        return firstSet;
    }

    private static ArrayList<ArrayList<String>> refineFirstSets(ArrayList<String> nonterminal, ArrayList<ArrayList<String>> firstSets){
        boolean isChanged = false;
        for (int i = 0; i < firstSets.size(); ++i){
            for(int j = 1; j < firstSets.get(i).size(); ++j){

                //Here we check if an item in the first set is a nonterminal symbol
                for(int k = 0; k < nonterminal.size(); ++k){    
                    //if it is we have to find the location of the nonterminal symbol in the list, then place the first set into a temporary list
                    if (firstSets.get(i).get(j).equals(nonterminal.get(k))){
                        ArrayList<String> tempList = new ArrayList<>();
                        int location = -1;
                        for (int l = 0; l < firstSets.size(); ++l){
                            if (firstSets.get(l).get(0).equals(nonterminal.get(k).trim())){
                                location = l;
                                break;
                            }
                            else{

                            }
                        }       

                        //here we add the first set to the tempList
                        if (location != -1){
                            for (int l = 1; l < firstSets.get(location).size(); ++l){
                                tempList.add(firstSets.get(location).get(l));
                            }
                        }
                        else{
                            //Just Debug Info
                            System.out.println("Unable to find symbol: |" + firstSets.get(i).get(j) + "|");
                            System.out.println("nonterminals in set: ");
                            for(int m = 0; m < nonterminal.size(); ++m){
                                System.out.println("|" + nonterminal.get(m) +"|");
                            }
                            System.out.println("nonterminals in Set: ");
                            for (int m = 0; m < firstSets.size(); ++m){
                                System.out.println("|" + firstSets.get(m).get(0)+ "|");
                            }
                        }

                        //Now we add the tempList in place of the nonterminal symbol
                        firstSets.get(i).remove(j);
                        firstSets.get(i).addAll(j, tempList);
                        isChanged = true;


                    }
                    else{

                    }
                }
            }
        }

        //Remove duplicates
        cleanupSets(firstSets);

        //If its changed at all, recurse to see if it needs further adjustments
        if (isChanged){
            return refineFirstSets(nonterminal, firstSets);
        }
        else{
            return firstSets;
        }
    }

    private static ArrayList<ArrayList<String>> cleanupSets(ArrayList<ArrayList<String>> set){
        //This Function just removes duplicate entries
        for (int i = 0 ; i < set.size(); ++i){
            for (int j = set.get(i).size() - 1; j > 0; --j){
                for (int k = 0; k < set.get(i).size(); ++k){
                    if (j != k){
                        if (set.get(i).get(j).equals(set.get(i).get(k))){
                            set.get(i).remove(j);
                            break;
                        }
                        else{

                        }
                    }
                }
            }
        }
        return set;
    }

    private static ArrayList<ArrayList<String>> addToSet(String position, ArrayList<ArrayList<String>> firstSet, String Symbol){
        for(int i = 0; i < firstSet.size(); ++i){
            if(firstSet.get(i).get(0).equals(position)){
                firstSet.get(i).add(Symbol);
                return firstSet;
            }
        }
        return firstSet;
    }

    private static ArrayList<String> followSet(){
        ArrayList<String> followSet = new ArrayList<>();

        return followSet();
    }

}   