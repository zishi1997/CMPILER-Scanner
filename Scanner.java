import java.io.*;
import java.io.BufferedReader;

/**
 *
 * @author zishi
 */

public class Scanner {

    static class Token {
        public TokenType tokentype;
        public String value;

        Token(TokenType token, String value) {
            this.tokentype = token;
            this.value = value;
        }

        public void setTokentype(TokenType token){
            this.tokentype = token;
        }

        public TokenType getTokentype() {
            return tokentype;
        }
    }

    static enum TokenType {
        End_of_input, GPR, FPR, KEYWORD, ERROR, CHECKING
    }

    //Regex Used: (R|$)([1-2][0-9]|[3][0-1]|[0-9])
    public static Token GPR_Tokenizer(String str ){
        Token token =  new Token(TokenType.CHECKING, str);
        int pos = 0;

        if(token.value.length() <= 3 && token.value.length()>= 2){
            //Check route of GPR DFA of regex (R|$)
            if(token.value.charAt(pos) == 'R' || token.value.charAt(pos) == '$'){
                if(token.value.length() <=2){
                    //Check route of GPR DFA of regex [0-9]
                    pos = 1;
                    if(token.value.length() < 3){
                        if(token.value.charAt(pos) >= '0' && token.value.charAt(pos) <= '9'){
                            token.setTokentype(TokenType.GPR);
                            return token;
                        }
                    }
                }
                else{
                    //Check route of GPR DFA of regex  [1-2][0-9]
                    pos=1;
                    if( (token.value.charAt(pos) >= '1') && (token.value.charAt(pos) <= '2') ){
                        pos=2;
                        if(token.value.charAt(pos) >= '0' && token.value.charAt(pos) <= '9'){
                            token.setTokentype(TokenType.GPR);
                            return token;
                        }
                    }
                    //Check route of GPR DFA of regex [3][0-1]
                    pos = 1;
                    if(token.value.charAt(pos) == '3'){
                        pos = 2;
                        if( token.value.charAt(pos) >= '0' && token.value.charAt(pos) <= '1'){
                            token.setTokentype(TokenType.GPR);
                            return token;
                        }
                    }
                }
            }
        }
        return token;
    }

    //Regex Used: F([1-2][0-9]|[3][0-1]|[0-9])| [$]F([1-2][0-9]|[3][0-1]|[0-9])
    public static Token FPR_Tokenizer(String str ){
        Token token =  new Token(TokenType.CHECKING, str);
        int pos = 0;

        if(token.value.length() <= 5 && token.value.length()>=2){
            //For route with prefix 'F'
            if(token.value.charAt(pos) == 'F'){
                //Check route of GPR DFA of regex [0-9]
                pos = 1;
                if(token.value.length() < 3){
                    if(token.value.charAt(pos) >= '0' && token.value.charAt(pos) <= '9'){
                        token.setTokentype(TokenType.FPR);
                        return token;
                    }
                }
                else{
                    //Check route of FPR DFA of regex  [1-2][0-9]
                    pos = 1;
                    if(token.value.charAt(pos) >= '1' && token.value.charAt(pos) <= '2'){
                        pos++;
                        if(token.value.charAt(pos) >= '0' && token.value.charAt(pos) <= '9'){
                            token.setTokentype(TokenType.FPR);
                            return token;
                        }
                    }
                    //Check route of FPR DFA of regex [3][0-1]
                    pos = 1;
                    if(token.value.charAt(pos) == '3'){
                        pos++;
                        if(token.value.charAt(pos) >= '0' && token.value.charAt(pos) <= '1'){
                            token.setTokentype(TokenType.FPR);
                            return token;
                        }
                    }
                }
            }
            //For route with prefix [$]F
            else if(token.value.length() <= 5 && token.value.length()>2){
                if(token.value.charAt(pos) == '$'){ 
                    pos =1;
                    if(token.value.charAt(pos) == 'F'){
                        pos = 2;
                        if(token.value.length() < 4){
                            //Check route of FPR DFA of regex  [0-9]
                            if(token.value.charAt(pos) >= '0' && token.value.charAt(pos) <= '9'){
                                token.setTokentype(TokenType.FPR);
                                return token;
                            }
                        }
                        else{
                            //Check route of FPR DFA of regex  [(1-2)][0-9]
                            if(token.value.charAt(pos) >= '1' && token.value.charAt(pos) <= '2'){
                                pos++;
                                if(token.value.charAt(pos) >= '0' && token.value.charAt(pos) <= '9'){
                                    token.setTokentype(TokenType.FPR);
                                    return token;
                                }
                            }
                            //Check route of FPR DFA of regex [3][0-1]
                            if(token.value.charAt(pos) == '3'){
                                pos++;
                                if(token.value.charAt(pos) >= '0' && token.value.charAt(pos) <= '1'){
                                    token.setTokentype(TokenType.FPR);
                                    return token;
                                }
                            }
                        }
                    }
                }
            }
        }
        return token;
    }

    //Regex Used:  (D)((ADD)(I?U)|(MUL(TU?)))
    public static Token KEYWORD_Tokenizer(String str ){
        Token token =  new Token(TokenType.CHECKING, str);
        int pos = 0;

        if(token.value.length()==5 || token.value.length()==6){
            if(token.value.charAt(pos) == 'D'){
                pos++;
                if(token.value.charAt(pos) == 'A'){
                    pos++;
                    if(token.value.charAt(pos) == 'D'){
                        pos++;
                        if(token.value.charAt(pos) == 'D'){
                            pos++;
                            if(token.value.charAt(pos) == 'I'){
                                pos++;
                                if(token.value.charAt(pos) == 'U'){
                                    token.setTokentype(TokenType.KEYWORD);
                                }
                            }
                            else if(token.value.charAt(pos) == 'U'){
                                token.setTokentype(TokenType.KEYWORD);
                            }
                        }
                    }
                }
                else if(token.value.charAt(pos) == 'M'){
                        pos++;
                        if(token.value.charAt(pos) == 'U'){
                            pos++;
                            if(token.value.charAt(pos) == 'L'){
                                pos++;
                                if(token.value.charAt(pos) == 'T'){
                                    pos++;
                                    if(token.value.length() == 6){
                                        if(token.value.charAt(pos) == 'U'){
                                            token.setTokentype(TokenType.KEYWORD);
                                        }
                                    }
                                    else{
                                        token.setTokentype(TokenType.KEYWORD);
                                    }
                                }
                            }
                        }
                }
            }
        }
        return token;
    }
    
    public static void main(String[] args) throws Exception{
              
        String line = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader("Input.txt"));

            File outputFile = new File("Output.txt");
            outputFile.createNewFile();

            FileWriter myWriter = new FileWriter("Output.txt");

            System.out.println("--------------------READING THE FILE---------------------");
            System.out.println("---------------------CREATING TOKENS---------------------");
            System.out.println("-------CLASSIFYING TOKENS BASED ON THEIR PATTERNS--------");
            System.out.println("------------WRITING THE RESULT TO OUTPUT FILE------------");
            
            while ((line = br.readLine()) != null) {
                //Read line of the file 
                //Clean the line and separate them into tokens by delimeters
                String[] str = line.replace(","," ").split(" ");
                String outputStr = "";

                for (String element: str) {
                    if(element.length() > 0){
                        Token token = new Token(TokenType.CHECKING, element);

                        //Identify if the token is a GPR
                        token.setTokentype(GPR_Tokenizer(element).getTokentype());

                        //Identify if the token is a FPR
                        if(token.getTokentype()==TokenType.CHECKING)
                        token.setTokentype(FPR_Tokenizer(element).getTokentype());

                        //Identify if the token is a KEYWORD
                        if(token.getTokentype()==TokenType.CHECKING)
                        token.setTokentype(KEYWORD_Tokenizer(element).getTokentype());

                        //If all types are checked and not any of them, then it is an Error
                        if(token.getTokentype()==TokenType.CHECKING)
                        token.setTokentype(TokenType.ERROR);
                        
                        //System.out.println(element+ " : " + token.getTokentype());
                        outputStr += token.getTokentype().toString() + " ";
                    }
                }
                outputStr += "\n";
                myWriter.write(outputStr);
            }
            br.close();
            myWriter.close(); 
            System.out.println("\nOutput successfully generated!");      
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
