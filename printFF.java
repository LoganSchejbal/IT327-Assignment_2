
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
            for(int j = 0; j < fileContents.get(i).size(); ++j){
                System.out.print(fileContents.get(i).get(j)+ " | ");
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

        //Need to add $ to start
    }
}   
