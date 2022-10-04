package com.example.mobilemathcalculator;
import java.text.DecimalFormat;

enum Operator { //keeping track of the operator
    NONE,
    DIVISION,
    MULTIPLICATION,
    SUBTRACTION,
    ADDITION
}

public class UtilMath {
    // member variables/attributes
    private int MAX_NUM;

    //constructor to get the max num
    public UtilMath(int p_MAX_NUM){
        setMAX_NUM(p_MAX_NUM);
    }

    //class methods --------------------------------------------------------------------------------
    public String toCalculation(Operator pOp, String numLeft, String numRight){ //deciding which method operator to call
        String answer = "";
        switch (pOp){
            case ADDITION:
                answer = addition(numLeft, numRight);
                break;
            case SUBTRACTION:
                answer = subtraction(numLeft, numRight);
                break;
            case MULTIPLICATION:
                answer = multiplication(numLeft, numRight);
                break;
            case DIVISION:
                answer = division(numLeft, numRight);
                break;
        }//doesn't need a default case because it will never reach a default due to previous logic
        return answer;
    } //end toCalculation method

    private String addition(String numLeft, String numRight){
        double numLeftC = Double.parseDouble(numLeft);
        double numRightC = Double.parseDouble(numRight);
        double answer = numLeftC + numRightC;

        return numFormatter(answer);
    } //end addition method

    private String subtraction(String numLeft, String numRight){
        double numLeftC = Double.parseDouble(numLeft);
        double numRightC = Double.parseDouble(numRight);
        double answer = numLeftC - numRightC;

        return numFormatter(answer);
    } //end subtraction method

    private String multiplication(String numLeft, String numRight){
        double numLeftC = Double.parseDouble(numLeft);
        double numRightC = Double.parseDouble(numRight);
        double answer = numLeftC * numRightC;

        return numFormatter(answer);
    } //end multiplication method

    private String division(String numLeft, String numRight){
        double numRightC = Double.parseDouble(numRight); //had to convert right num to 0 first to find if it is 0
        if (numRightC == 0){ //divide by zero error
            return "NaN";
        }
        double numLeftC = Double.parseDouble(numLeft);
        double answer = numLeftC / numRightC;

        return numFormatter(answer);
    } //end division method

    private String numFormatter(double answer){ //to format the number
        String tempAnswer = String.valueOf(answer);
        if (answer % 1 == 0 && !tempAnswer.contains("E")){ //if the answer has no decimals (not a float), and isn't an exponent
            return String.valueOf((long) answer); //return the long form
        }else{ //has decimals
            //need to round the answer
            if (tempAnswer.contains("E")) { //if the number has an exponent, only round to first whole num
                int expVal = Integer.parseInt(tempAnswer.substring(tempAnswer.indexOf("E") + 1)); //to get the value of the exponent
                if (expVal <= 7){ //rounding the exponent to the max digits
                    int numToRound = getMAX_NUM() - (expVal + 1); //getting the max number to round to
                    if (numToRound < 0){
                        numToRound = 0;
                    }
                    StringBuilder pattern = new StringBuilder("#.");
                    for (int i = 1; i <= numToRound; i++){
                        pattern.append("#");
                    }
                    return new DecimalFormat(pattern.toString()).format(answer);
                }else if (expVal >= 18){ //if the number is 10^18 it is a max value
                    return "MAX"; //max num
                }
                else{
                    return new DecimalFormat("#").format(answer);
                }
            } else{ //if it does not contain an exponent
                if (tempAnswer.length() > getMAX_NUM()){
                    //rounding the answer if it is bigger than max length
                    int idxDecimal = tempAnswer.indexOf("."); //finding the index of the decimal
                    int numToRound = getMAX_NUM() - idxDecimal; //getting the max number to round to
                    if (numToRound < 0){
                        numToRound = 0;
                    }
                    StringBuilder pattern = new StringBuilder("#.");
                    for (int i = 1; i <= numToRound; i++){
                        pattern.append("#");
                    }
                    return new DecimalFormat(pattern.toString()).format(answer);
                }
            }
        }
        return String.valueOf(answer); //if it is a float, return it with decimals
    } //end numFormatter method

    //getters and setters --------------------------------------------------------------------------
    public int getMAX_NUM() {
        return MAX_NUM;
    } //end getMAX_NUM method

    public void setMAX_NUM(int MAX_NUM) {
        this.MAX_NUM = MAX_NUM;
    } //end setMAX_NUM method
}