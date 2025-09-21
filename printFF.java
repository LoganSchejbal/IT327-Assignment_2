
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class printFF {
    public static void main(String[] args){
        //Input to select a file
        //File must be in same folder or a path given
        String fileName;
        if (args.length > 0 && args[0].length() != 0) {
            fileName = args[0];
        } 
        else {
            Scanner input = new Scanner(System.in);
            fileName = input.nextLine();
            input.close();
        }


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

        //FirstSets find the initial first characters for each nonterminal symbol and gives consideration to the lambda character
        ArrayList<ArrayList<String>> firstSets = firstSet(fileContents, nonterminal);

        //refineFirstSets will then recurse, turning A -> first(A)
        //will continue to recurse until the function no longer changes anything
        firstSets = refineFirstSets(nonterminal, firstSets);

        //Prints out the final version of firstSets
        System.out.print("\n\nFirst Sets:\n===");
        for(int i = 0; i < firstSets.size(); ++i){
            System.out.print("\n" + firstSets.get(i).get(0) + ": ");
            for(int j = 1; j < firstSets.get(i).size(); ++j){
                System.out.print(firstSets.get(i).get(j) + " ");
            }
        }

        //This function initializes followSet with what needs to be added.
        //Follows the rules of the followSets without handling any recursion
        ArrayList<ArrayList<String>> followSets = followSet(fileContents, firstSets, nonterminal);

        //Refines the follow sets so that if there is a nonterminal symbol, it replaces that with its followset
        followSets = refineFollowSets(followSets, nonterminal);

        //Prints out the final Version of the follow Sets
        System.out.print("\n\nFollow Sets:\n===");
        for(int i = 0; i < followSets.size(); ++i){
            System.out.print("\n" + followSets.get(i).get(0) + ": ");
            for(int j = 1; j < followSets.get(i).size(); ++j){
                System.out.print(followSets.get(i).get(j) + " ");
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

        cleanupSets(firstSet, false);

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
        cleanupSets(firstSets, false);

        //If its changed at all, recurse to see if it needs further adjustments
        if (isChanged){
            return refineFirstSets(nonterminal, firstSets);
        }
        else{
            return firstSets;
        }
    }

    private static ArrayList<ArrayList<String>> cleanupSets(ArrayList<ArrayList<String>> set, boolean removeLambda){
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

        //This part of the code is responsible for removing any lambda characters in follow Sets
        //It will only run if the second pass through variable is true
        if (removeLambda){
            for(int i = 0; i < set.size(); ++i){
                for (int j = set.get(i).size() - 1; j > 0; --j){
                    if (set.get(i).get(j).equals("lambda")){
                        set.get(i).remove(j);
                        break;
                    }
                }
            }
        }
        else{

        }
        return set;
    }

    private static ArrayList<ArrayList<String>> addToSet(String position, ArrayList<ArrayList<String>> set, String Symbol){
        //function is just a shorthand to add a particular thing to an ArrayList when you don't know which row to add to
        //Essentially just finds the row of the set that the position variable is passed as in row(i).get(0) and adds Symbol to that row
        for(int i = 0; i < set.size(); ++i){
            if(set.get(i).get(0).equals(position)){
                set.get(i).add(Symbol);
                return set;
            }
            else{

            }
        }
        return set;
    }

    private static ArrayList<ArrayList<String>> followSet(ArrayList<ArrayList<String>> fileContents, ArrayList<ArrayList<String>> firstSet, ArrayList<String> nonterminal){
        ArrayList<ArrayList<String>> followSet = new ArrayList<>();
        //Just adds the nonterminal symbols into the vector so each line can be identified
        for (int i = 0; i < nonterminal.size(); ++i){
            ArrayList<String> tempList = new ArrayList<>();
            tempList.add(nonterminal.get(i));
            followSet.add(tempList);
        }

        //Add $ to first
        followSet.get(0).add("$");

        for (int i = 0; i < fileContents.size(); ++i){
            //i corresponds to row of fileContents
            //j corresponds to element of a row in filecontents
            //k corresponds to list of nonterminal elements
            String position = fileContents.get(i).get(0);
            for (int j = 1; j < fileContents.get(i).size(); ++j){
                for (int k = 0; k < nonterminal.size(); ++k){
                    if (fileContents.get(i).get(j).equals(nonterminal.get(k))){
                        if (fileContents.get(i).size() - 1 == j){
                            //Add Follow set
                            addToSet(fileContents.get(i).get(j), followSet, position);
                        }
                        else{
                            boolean isNonTerminal = false;
                            //l corresponds to list of nonterminal elements
                            //m corresponds to row of the follow set
                            //n corresponds to row of first set
                            //o corresponds to element of the first set
                            for (int l = 0; l < nonterminal.size(); ++l){
                                if (fileContents.get(i).get(j+1).equals(nonterminal.get(l))){
                                    //Add First set
                                    isNonTerminal = true;
                                    for (int m = 0; m < followSet.size(); ++m){
                                        if (fileContents.get(i).get(j).equals(followSet.get(m).get(0))){
                                            for(int n = 0; n < firstSet.size(); ++n){
                                                if (fileContents.get(i).get(j+1).equals(firstSet.get(n).get(0))){
                                                    for (int o = 1; o < firstSet.get(n).size(); ++o){
                                                        if(firstSet.get(n).get(o).equals("lambda")){
                                                            followSet.get(m).add(fileContents.get(i).get(j+1));
                                                        }
                                                        else{
                                                            followSet.get(m).add(firstSet.get(n).get(o));
                                                        }
                                                    }
                                                }
                                                else{

                                                }
                                            }
                                        }
                                        else{

                                        }
                                    }
                                }
                                else{
                                }
                            }

                            if(!isNonTerminal){
                                //Add the terminal symbol
                                addToSet(fileContents.get(i).get(j), followSet, fileContents.get(i).get(j+1));
                            }
                        }
                    }
                    else{

                    }
                }
            }
        }
        cleanupSets(followSet, true);

        return followSet;
    }

    private static ArrayList<ArrayList<String>> refineFollowSets(ArrayList<ArrayList<String>> followSet, ArrayList<String> nonterminal){
        boolean isChanged = false;
        //This funtion recursively adds the followSets of nonterminal symbol
        //Essentialy it looks for a nonterminal in the followSet ArrayList
        //If one is found, 
        //  - IsChanged is becomes true
        //      - this signals that we need to check again for more possible nonterminal symbols after this call
        //  - the followSet of that nonterminal is added to the current symbol 
        for(int i = 0; i < followSet.size(); ++i){
            for(int j = 1; j < followSet.get(i).size(); ++j){
                for (int k = 0; k < nonterminal.size(); ++k){
                    if(followSet.get(i).get(j).equals(nonterminal.get(k))){
                        //Makes sure the function is not adding itself
                        if(followSet.get(i).get(j).equals(followSet.get(i).get(0))){
                            followSet.get(i).remove(j);
                            isChanged = true;
                            break;
                        }
                        else{
                            //Adds the follow set
                            String temp = followSet.get(i).get(j);
                            followSet.get(i).remove(j);
                            for (int l = 0; l < followSet.size(); ++l){
                                if (followSet.get(l).get(0).equals(temp)){
                                    for (int m = 1; m < followSet.get(l).size(); ++m){
                                        if (!followSet.get(i).get(0).equals(followSet.get(l).get(m))){
                                            followSet.get(i).add(followSet.get(l).get(m));
                                        }
                                    }
                                    break;
                                }
                            }
                            isChanged = true;
                            break;
                        }
                    }
                    else{

                    }
                }
            }
        }

        //Call cleanup function
        //If the follow set is edited, it will recurse and run again
        cleanupSets(followSet, true);
        if(isChanged){
            return refineFollowSets(followSet, nonterminal);
        }
        else{
            return followSet;
        }
    }

}   