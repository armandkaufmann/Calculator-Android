package com.example.mobilemathcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final private int MAX_NUM_DIG = 9;

    //number & operator input
    private String[] nums = {"", ""};
    private int currNum = 0; //keep track of current num 0 = left, 1 = right
    private Operator op = Operator.NONE;
    //initializing math business class
    private UtilMath mathCalc = new UtilMath(MAX_NUM_DIG);
    //keeping track of when the equals is pressed in certain conditions
    private boolean equalsPressed = false;

    //declaring views (from tableInput)
    //row1
    private TextView calcScreenTextView;
    private Button buttonClear;
    private Button buttonRemoveDig;
    private Button buttonNegPos;
    private Button buttonDivide;
    //row2
    private Button buttonNum7;
    private Button buttonNum8;
    private Button buttonNum9;
    private Button buttonMultiply;
    //row3
    private Button buttonNum4;
    private Button buttonNum5;
    private Button buttonNum6;
    private Button buttonMinus;
    //row4
    private Button buttonNum1;
    private Button buttonNum2;
    private Button buttonNum3;
    private Button buttonPlus;
    //row5
    private Button buttonNum0;
    private Button buttonDecimal;
    private Button buttonEquals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connecting declared widgets with activity_main.xml and creating listeners
        calcScreenTextView = (TextView) findViewById(R.id.CalcScreenTextView);

        buttonClear = (Button) findViewById(R.id.buttonClear);
        buttonClear.setOnClickListener(buttonClearListener);

        buttonRemoveDig = (Button) findViewById(R.id.buttonRemoveDig);
        buttonRemoveDig.setOnClickListener(buttonRemoveDigListener);

        buttonNegPos = (Button) findViewById(R.id.buttonNegPos);
        buttonNegPos.setOnClickListener(buttonNegPosListener);

        buttonDivide = (Button) findViewById(R.id.buttonDivide);
        buttonDivide.setOnClickListener(buttonDivideListener);

        buttonNum7 = (Button) findViewById(R.id.buttonNum7);
        buttonNum7.setOnClickListener(buttonNum7Listener);

        buttonNum8 = (Button) findViewById(R.id.buttonNum8);
        buttonNum8.setOnClickListener(buttonNum8Listener);

        buttonNum9 = (Button) findViewById(R.id.buttonNum9);
        buttonNum9.setOnClickListener(buttonNum9Listener);

        buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
        buttonMultiply.setOnClickListener(buttonMultiplyListener);

        buttonNum4 = (Button) findViewById(R.id.buttonNum4);
        buttonNum4.setOnClickListener(buttonNum4Listener);

        buttonNum5 = (Button) findViewById(R.id.buttonNum5);
        buttonNum5.setOnClickListener(buttonNum5Listener);

        buttonNum6 = (Button) findViewById(R.id.buttonNum6);
        buttonNum6.setOnClickListener(buttonNum6Listener);

        buttonMinus = (Button) findViewById(R.id.buttonMinus);
        buttonMinus.setOnClickListener(buttonMinusListener);

        buttonNum1 = (Button) findViewById(R.id.buttonNum1);
        buttonNum1.setOnClickListener(buttonNum1Listener);

        buttonNum2 = (Button) findViewById(R.id.buttonNum2);
        buttonNum2.setOnClickListener(buttonNum2Listener);

        buttonNum3 = (Button) findViewById(R.id.buttonNum3);
        buttonNum3.setOnClickListener(buttonNum3Listener);

        buttonPlus = (Button) findViewById(R.id.buttonPlus);
        buttonPlus.setOnClickListener(buttonPlusListener);

        buttonNum0 = (Button) findViewById(R.id.buttonNum0);
        buttonNum0.setOnClickListener(buttonNum0Listener);

        buttonDecimal = (Button) findViewById(R.id.buttonDecimal);
        buttonDecimal.setOnClickListener(buttonDecimalListener);

        buttonEquals = (Button) findViewById(R.id.buttonEquals);
        buttonEquals.setOnClickListener(buttonEqualsListener);

    }

    //methods for UI -------------------------------------------------------------------------------
    private void outputToTextView() { //outputs the current number to the screen
        String tempNum = "";
        if (nums[currNum].contains(".")) { //is a decimal
            if (nums[currNum].substring(0,2).equals("-0")){ //is a negative integer of -0.###
                //was having issues with long.parseLong because -0 = 0, was not printing out negative
                tempNum = nums[currNum];
            }else{
                tempNum = String.format("%,d", Long.parseLong(nums[currNum].substring(0, nums[currNum].indexOf("."))));
                tempNum += nums[currNum].substring(nums[currNum].indexOf("."));
            }
        } else { //is not a decimal
            tempNum = String.format("%,d", Long.parseLong(nums[currNum]));
        }
        calcScreenTextView.setText(tempNum);
    } //end outputToTextView method

    private void clearAll(){
        nums[0] = ""; //clearing left num
        nums[1] = ""; //clearing right num
        currNum = 0; //sets current num to input as left
        op = Operator.NONE; //setting operator as none
        setSelectedButtonFalse(); //unselecting any operator
    } //end clearAll Method

    private void saveNum(String num) { //saves the number into the program as a string
        if (!(nums[currNum] + num).equals("00")){
            //if the current number and num to add aren't both zero. User can't overload with zeros
            if (equalsPressed && op == Operator.NONE){ //if the equals is pressed and no op
                //user has clicked a number after hitting equals, meaning needing to reset
                clearAll();
                nums[currNum] += num;
                equalsPressed = false;
            } else{
                if (nums[currNum].length() <= MAX_NUM_DIG + 2 && (nums[currNum].contains(".") && nums[currNum].contains("-"))){
                    nums[currNum] += num;
                } else if (nums[currNum].length() <= MAX_NUM_DIG + 1 && (nums[currNum].contains(".") || nums[currNum].contains("-"))){
                    nums[currNum] += num;
                } else if (nums[currNum].length() <= MAX_NUM_DIG) { //if the length of the num doesn't exceed max
                    nums[currNum] += num;
                }
            }
            outputToTextView();
            setSelectedButtonFalse();
        }
    } //end saveNum method

    private void saveDecimal(){
        if (equalsPressed && op == Operator.NONE){ //if equals just pressed and there's no operator
            clearAll();
            nums[currNum] = "0.";
            equalsPressed = false;
        }else{
            if (nums[currNum].length() <= MAX_NUM_DIG && !nums[currNum].contains("-")){
                if (nums[currNum].length() == 0){ //if the current num is empty = "0." when adding decimal
                    nums[currNum] = "0.";
                }
                else{
                    if (!nums[currNum].contains(".")){ //only add one decimal to num string
                        nums[currNum] += ".";
                    }
                }
            } else if (nums[currNum].length() <= MAX_NUM_DIG + 1 && nums[currNum].contains("-")){
                nums[currNum] += ".";
            }
        }
        outputToTextView();
    } //end saveDecimal method

    private void saveNegPos(){
        if (!nums[currNum].equals("")){ //if there is a number
            if (!nums[currNum].equals("0") && !nums[currNum].equals("0.")){ //if it is not zero
                if (nums[currNum].contains("-")){ //if it is already a negative number, make positive
                    nums[currNum] = nums[currNum].substring(1);
                }else{
                    nums[currNum] = "-" + nums[currNum];
                }
                outputToTextView();
            }
        }
    }//end saveNegPos method

    private void setNextNum(){
        if (currNum == 0){
            currNum = 1; //set currnum to 1 = right
        }else{
            currNum = 0; //set currnum to 0 = left
        }
    } //end setNextNum method

    private int getNextNum(){
        if (currNum == 0){
            return 1;
        }else{
            return 0;
        }
    } //end getNextNum method

    private void setSelectedButtonTrue(Button btnOp){
        btnOp.setSelected(true);
    }//end setSelectedButtonTrue method

    private void setSelectedButtonFalse(){
        //sets all selectable buttons to false
        buttonDivide.setSelected(false);
        buttonMultiply.setSelected(false);
        buttonMinus.setSelected(false);
        buttonPlus.setSelected(false);
    }//end setSelectedButtonFalse method

    private boolean hasSelectedBtn(){
        if (buttonDivide.isSelected()){
            return true;
        }else if (buttonMultiply.isSelected()){
            return true;
        }else if (buttonMinus.isSelected()){
            return true;
        }else if (buttonPlus.isSelected()){
            return true;
        }
        return false;
    } //end hasSelectedBtn method

    private void operatorButtonSave(Button btn, Operator pOp){
        if (nums[0].equals("") && currNum == 0){
            //user didn't enter the first number but hit an operator
            //set the first number to 0
            nums[currNum] = "0";
        } else if ((!nums[0].equals("") && nums[1].equals("")) && op != Operator.NONE){
            //if user is still selecting operator after the input of the left num
            setSelectedButtonFalse(); //unselect any operator selected previously
        } else if ((!nums[0].equals("") && !nums[1].equals("")) && op != Operator.NONE){
            //user hits an operator when there are already two numbers and another operator selected
            calculateAnswer();
        }
        setSelectedButtonTrue(btn); //select current operator
        if (nums[getNextNum()].equals("")){
            //only go to the next number if it is empty
            //made this because of bug when entering a operator, was switching between left & right
            setNextNum(); //start inputting the next num
        }
        op = pOp;
    } //end operatorButtonSave Method

    private void calculateAnswer(){ //overloaded method
        String tempAnswer = mathCalc.toCalculation(op, nums[0], nums[1]);
        if (tempAnswer.equals("NaN")){ //if the answer is not a number error
            clearAll();
            calcScreenTextView.setText(R.string.NotANumber);
        } else if (tempAnswer.equals("MAX")){ //if the number is a max number
            clearAll();
            calcScreenTextView.setText(R.string.maxNum);
        }
        else{ //if the number is valid
            clearAll();
            nums[0] = tempAnswer; //setting the answer to the left num
            outputToTextView();
        }
    } //end calculateAnswer method

    private void calculateAnswer(Operator pOp, String numLeft, String numRight){ //overloaded method
        String tempAnswer = mathCalc.toCalculation(pOp, numLeft, numRight);
        if (tempAnswer.equals("NaN")){ //if answer is NaN -> not a number
            clearAll();
            calcScreenTextView.setText(R.string.NotANumber);
        } else if (tempAnswer.equals("MAX")){ //if the answer is a max number
            clearAll();
            calcScreenTextView.setText(R.string.maxNum);
        }
        else{ //if answer is valid
            clearAll();
            nums[0] = tempAnswer; //setting the answer to the left num
            outputToTextView();
        }
    } //end calculateAnswer method

    private void toEquals(){ //equals is pressed
        if ((!nums[0].equals("") && nums[1].equals("")) && op != Operator.NONE){
            //if only given left number and operator when equals is hit
            calculateAnswer(op, nums[0], nums[0]);
        } else if ((!nums[0].equals("") && !nums[1].equals("")) && op != Operator.NONE){
            //if both numbers are provided with an operator
            equalsPressed = true;
            calculateAnswer();

        }
    } //end toEquals method

    //listeners ------------------------------------------------------------------------------------
    private View.OnClickListener buttonPlusListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            operatorButtonSave(buttonPlus, Operator.ADDITION);
        }
    }; //end buttonPlusListener

    private View.OnClickListener buttonMinusListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            operatorButtonSave(buttonMinus, Operator.SUBTRACTION);
        }
    }; //end buttonMinusListener

    private View.OnClickListener buttonMultiplyListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            operatorButtonSave(buttonMultiply, Operator.MULTIPLICATION);
        }
    }; //end buttonMultiplyListener

    private View.OnClickListener buttonDivideListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            operatorButtonSave(buttonDivide, Operator.DIVISION);
        }
    }; //end buttonDivideListener

    private View.OnClickListener buttonEqualsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            toEquals();
        }
    }; //end buttonPlusListener

    private View.OnClickListener buttonNegPosListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNegPos();
        }
    }; //end buttonClearListener

    private View.OnClickListener buttonClearListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            calcScreenTextView.setText("0");
            clearAll();
        }
    }; //end buttonClearListener

    private View.OnClickListener buttonRemoveDigListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!hasSelectedBtn()){
                if (nums[currNum].length() <= 1) { //if the number is not set or is on the last digit
                    nums[currNum] = "0"; //set the current number to 0
                    calcScreenTextView.setText("0");
                } else if (nums[currNum].length() == 2 && nums[currNum].contains("-")){
                    nums[currNum] = "";
                    calcScreenTextView.setText("0");
                } else {
                    nums[currNum] = nums[currNum].substring(0, nums[currNum].length() - 1);
                    outputToTextView();
                }
            }

        }
    }; //end buttonRemoveDigListener

    private View.OnClickListener buttonDecimalListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveDecimal();
        }
    }; //end buttonNum1Listener

    private View.OnClickListener buttonNum1Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNum(buttonNum1.getText().toString());
        }
    }; //end buttonNum1Listener

    private View.OnClickListener buttonNum2Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNum(buttonNum2.getText().toString());
        }
    }; //end buttonNum2Listener

    private View.OnClickListener buttonNum3Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNum(buttonNum3.getText().toString());
        }
    }; //end buttonNum3Listener

    private View.OnClickListener buttonNum4Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNum(buttonNum4.getText().toString());
        }
    }; //end buttonNum4Listener

    private View.OnClickListener buttonNum5Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNum(buttonNum5.getText().toString());
        }
    }; //end buttonNum5Listener

    private View.OnClickListener buttonNum6Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNum(buttonNum6.getText().toString());
        }
    }; //end buttonNum6Listener

    private View.OnClickListener buttonNum7Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNum(buttonNum7.getText().toString());
        }
    }; //end buttonNum7Listener

    private View.OnClickListener buttonNum8Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNum(buttonNum8.getText().toString());
        }
    }; //end buttonNum8Listener

    private View.OnClickListener buttonNum9Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNum(buttonNum9.getText().toString());
        }
    }; //end buttonNum9Listener

    private View.OnClickListener buttonNum0Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            saveNum(buttonNum0.getText().toString());
        }
    }; //end buttonNum0Listener
}