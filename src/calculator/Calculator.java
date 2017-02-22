/*
 *
 *  OVERVIEW OF LOGIC
 *  -- use ArrayList to store input numbers.
 *  -- use ArrayList to store input calculations
 *  -- use local ArrayList in calculate method to prevent manipulation of user input
 *  -- use recursion to execute order of operation ** only when equal is pressed ** 
 *      --- BONUS FOR LATER: refactor logic to allow calculations when + or - are pressed
 *         --- identify when there are both multi/divide && add/subtract in calcAL
 *         --- if both multi/divide && add/subtract are present, identify when add/subtract are the last operands in calcAL
 *  -- have a boolean value to reset input numbers and calculations after equals is pressed
 *      -- if a number is pressed after equals reset both ArrayLists and start fresh
 *      -- if an operand is pressed after equals, continue adding to values already input
 *  -- have a boolean value to know when to reset number display vs when to keep concating numbers 
 *      
 * PROBLEMS
 **  test the order of operation functions
 *    -x divide by zero exception caught?
 *    -- correct calculations?
 **  number added after operator (error)
 *    -x elimiate calculations in-between the clicking of operands. (only calculate after equals is pressed)
 *    -- BONUS: refactor logic to allow calculations when + or - are pressed
 *      --- identify when there are both multi/divide && add/subtract in calcAL
 *      --- if both multi/divide && add/subtract are present, identify when add/subtract are the last operands in calcAL
 */

package calculator;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Jess
 */
public class Calculator extends javax.swing.JFrame {

    //using arrayList so user can input as many values as they want
    private final ArrayList<Short> calcAL = new ArrayList<>(); //reference never changes so final is allowed. :) 
    private final ArrayList<Double> numAL = new ArrayList<>(); 
    private int index1, index2; // open scope needed because you can't return two values at once. 
        // after all testing is finished, re-configure code to make these indexes local. They are only used in two methods    
    private boolean numberSaved; // flag to know when it's safe/necessary to clear jTextField1
    private boolean resetCalculations; // flag to know when it's necessary to clear ArrayLists
    private final DecimalFormat df = new DecimalFormat("##.########");
    
    /**
     * Creates new form calculator_frame
     */
    public Calculator() {
        initComponents();
    }
    /**
     * The calculateKey short will relate to a math operator.
     * 1 --> +
     * 2 --> -
     * 3 --> *
     * 4 --> /
     */

    private String compute() {//, double input){
        
        // set boolean field for continue computing
        //   if operator is pressed after the equals button
        if(resetCalculations){
            numAL.clear();  
            calcAL.clear();
            resetCalculations = false; // flip boolean.
        }
        
        ArrayList<Double> tempAL = numAL;        
        
        System.out.println("numAL: "+numAL);
        System.out.println("calcAL: "+calcAL);
        // avoid computing when user hasn't input two numbers        
        // compute if calc ends in equal
        // avoid computing when 
        if(numAL.size()>1 ){
            
            try{
                // loop inside this method until there are no longer ** 3 or 4 ** values in tempCalc
                recursiveCalc((short)3,(short)4, tempAL);
            } catch (ArithmeticException e){
                return "ERROR";
            }
            // loop inside this method until there are no longer ** 1 or 2 ** values in tempCalc
            recursiveCalc((short)1,(short)2, tempAL);
        } 
        
        numberSaved = false; //set to false to indicate clearing out textField is necessary
        // "print" answer to screen 
        System.out.println("tempAL size: "+tempAL.size());
        System.out.println("tempAL.get(0): "+tempAL.get(0));
        return df.format(tempAL.get(0)); //return answer in string form.
    }
 
    private void recursiveCalc( short searchIndex1, short searchIndex2, ArrayList<Double> tempAL) throws ArithmeticException{
        // CREATE LOGIC TO THROW METHOD EXCEPTION 
        // input indexes cannot be equal and should be in groups of 1&2 OR 3&4
        ArrayList<Short> tempCalc = calcAL;
        SortnSaveIndex(searchIndex1, tempCalc);
        SortnSaveIndex(searchIndex2, tempCalc);
        boolean indexFound = true;
        do{
            // it is possible for multiple occurances of index, loop through this logic until both indexes are -1
            if (index1 != -1) {
                //we know odd has been entered
                if(index2 != -1){
                      //we know BOTH odd and even has been entered
                      if(index1 < index2){ // which index occurs first?
                        // we know odd was entered first, calculate it, then re-enter outside loop 
                        addOrMultiply(tempAL, tempCalc, searchIndex1);
                      } else {
                        //we know even was entered first, calculate it, then re-enter outside loop 
                        subtractOrDivide(tempAL, tempCalc, searchIndex2);
                      }
                  } else {
                    //we know odd has been entered, calculate it, then re-enter outside loop 
                    addOrMultiply(tempAL, tempCalc, searchIndex1);
                  }
            } else {
                // we know odd has NOT been entered
                if(index2 != -1){
                  //we know even was entered, calculate it, then re-enter outside loop 
                  subtractOrDivide(tempAL, tempCalc, searchIndex2);
                } else {
                  // neither index input exist in calcAL, flag to end loop
                  indexFound = false;
                }
            }
            // search for additional operators after additional calculations have been made.
            SortnSaveIndex(searchIndex1, tempCalc);
            SortnSaveIndex(searchIndex2, tempCalc);
        } while(indexFound);
        
    }
    
    private void SortnSaveIndex(short searchIndex, ArrayList<Short> tempCalc){
        // logic in recursiveCalc calls multiplication or division based off even or odd indexes
        if(searchIndex % 2 == 1){ 
            index1 = tempCalc.indexOf(searchIndex); // multiply / addition
        } else {
            index2 = tempCalc.indexOf(searchIndex); // subtraction / division   
        }   
    }
    
    private void addOrMultiply(ArrayList<Double> tempAL, ArrayList<Short> tempCalc, short searchIndex1){
        System.out.println("addOrMultiply");
        System.out.println("before: "+tempAL.toString());
        //ArrayList<Double> tempAL = new ArrayList<>();
        if(searchIndex1 == 3){
            // change value through reference, which changes original value
             multiplication(index1, tempAL, tempCalc);
        } else {
            if(searchIndex1 == 1){
                addition(index1, tempAL, tempCalc);
            }
        }                
        System.out.println("after: "+tempAL.toString());
        
        //return tempAL;
    }
    private void subtractOrDivide(ArrayList<Double> tempAL, ArrayList<Short> tempCalc, short searchIndex2){
        System.out.println("subtractOrDivide");
        System.out.println("before: "+tempAL.toString());

        if(searchIndex2 == 4){
            division(index2, tempAL, tempCalc);
        } else {
            if(searchIndex2 == 2){
                subtraction(index2, tempAL, tempCalc);
            }
        }
        System.out.println("after: "+tempAL.toString());   
     //   return tempAL;
    }
    private void multiplication(int indx, ArrayList<Double> tempAL, ArrayList<Short> tempCalc){ //pass reference

        //in order to use values in calculations, ArrayList must be converted to Array
        Double[] numArr = tempAL.toArray(new Double[tempAL.size()]);  //local varible used only for math
        numArr[indx] = numArr[indx] * numArr[indx+1];
        
        tempCalc.remove(new Integer(3));//removes first occurance in calc
        tempAL.remove(indx+1); //delete at index + 1
        tempAL.set(indx, numArr[indx]); //replace value in numAl with result         

    }
    private void addition(int indx, ArrayList<Double> tempAL, ArrayList<Short> tempCalc){ //pass reference
        
        //in order to use values in calculations, ArrayList must be converted to Array
        Double[] numArr = tempAL.toArray(new Double[tempAL.size()]);  //local varible used only for math
        numArr[indx] = numArr[indx] + numArr[indx+1];
        
        tempCalc.remove(new Integer(1));//removes first occurance in calc
        tempAL.remove(indx+1); //delete at index + 1
        tempAL.set(indx, numArr[indx]); //replace value in numAl with result 
        
    }
    private void division(int indx, ArrayList<Double> tempAL, ArrayList<Short> tempCalc) throws ArithmeticException{ //pass reference

         //in order to use values in calculations, ArrayList must be converted to Array
        Double[] numArr = tempAL.toArray(new Double[tempAL.size()]);  //local varible used only for math
        if(numArr[indx+1] == 0){
            throw new ArithmeticException();
        }else{
            numArr[indx] = numArr[indx] / numArr[indx+1];
        }
        
        
        tempCalc.remove(new Integer(4));//removes first occurance in calc
        tempAL.remove(indx+1); //delete at index + 1
        tempAL.set(indx, numArr[indx]); //replace value in numAl with result 
        
    }
    private void subtraction(int indx, ArrayList<Double> tempAL, ArrayList<Short> tempCalc){ //pass reference

        //in order to use values in calculations, ArrayList must be converted to Array
        Double[] numArr = tempAL.toArray(new Double[tempAL.size()]);  //local varible used only for math
        numArr[indx] = numArr[indx] - numArr[indx+1];
        
        tempCalc.remove(new Integer(2));//removes first occurance in calc
        tempAL.remove(indx+1); //delete at index + 1
        tempAL.set(indx, numArr[indx]); //replace value in numAl with result
        
    }
    
    
 
/**
 * concatInput removes leading zeros and puts together the values of buttons pressed from the user display
 * @param String input, button clicked from user
 */
    private void concatInput(String input){
        // set boolean field for continue computing
        //   if number is pressed after the equals button, reset calculations
        if(resetCalculations){ //reset board?
            numAL.clear();
            calcAL.clear();
            numberDisplay.setText(input);
            resetCalculations = false;
        }else{
            if(numberSaved){  
               // numberSaved is true,  conact rather than replace
                // Don't allow leading zeros
                if(numberDisplay.getText().equals("0")){
                    numberDisplay.setText(input);
                } else {
                    numberDisplay.setText(numberDisplay.getText()+input);
                }
            } else{ 
                 // replace textField with new input after calculations
                numberDisplay.setText(input);
                numberSaved = true; // value has been replaced, set flag to true
            }        
        }        
    }
    
    private String saveInput(String input){

        numAL.add(Double.parseDouble(input));
        numberSaved = false;
        
        return input;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        numberDisplay = new javax.swing.JTextField();
        buttonClear = new java.awt.Button();
        buttonClearAll = new java.awt.Button();
        buttonDivision = new java.awt.Button();
        button7 = new java.awt.Button();
        button9 = new java.awt.Button();
        button8 = new java.awt.Button();
        buttonMultiplication = new java.awt.Button();
        button4 = new java.awt.Button();
        button6 = new java.awt.Button();
        button5 = new java.awt.Button();
        buttonSubtraction = new java.awt.Button();
        button1 = new java.awt.Button();
        button3 = new java.awt.Button();
        button2 = new java.awt.Button();
        buttonAddition = new java.awt.Button();
        button0 = new java.awt.Button();
        buttonPeriod = new java.awt.Button();
        buttonEquals = new java.awt.Button();
        buttonPositiveNegative = new java.awt.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calculator");
        setBackground(new java.awt.Color(223, 218, 194));
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        numberDisplay.setEditable(false);
        numberDisplay.setBackground(new java.awt.Color(204, 204, 204));
        numberDisplay.setFont(new java.awt.Font("Silom", 1, 24)); // NOI18N
        numberDisplay.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        numberDisplay.setText("0");

        buttonClear.setActionCommand("C");
        buttonClear.setBackground(new java.awt.Color(195, 8, 14));
        buttonClear.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        buttonClear.setLabel("C");
        buttonClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearPressed(evt);
            }
        });

        buttonClearAll.setBackground(new java.awt.Color(195, 8, 14));
        buttonClearAll.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        buttonClearAll.setLabel("AC");
        buttonClearAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonClearAllPressed(evt);
            }
        });

        buttonDivision.setBackground(new java.awt.Color(153, 153, 153));
        buttonDivision.setLabel("÷");
        buttonDivision.setPreferredSize(new java.awt.Dimension(45, 24));
        buttonDivision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDivisionPressed(evt);
            }
        });

        button7.setBackground(new java.awt.Color(153, 153, 153));
        button7.setLabel("7");
        button7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button7Pressed(evt);
            }
        });

        button9.setBackground(new java.awt.Color(153, 153, 153));
        button9.setLabel("9");
        button9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button9Pressed(evt);
            }
        });

        button8.setBackground(new java.awt.Color(153, 153, 153));
        button8.setLabel("8");
        button8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button8Pressed(evt);
            }
        });

        buttonMultiplication.setBackground(new java.awt.Color(153, 153, 153));
        buttonMultiplication.setLabel("x");
        buttonMultiplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMultiplicationPressed(evt);
            }
        });

        button4.setBackground(new java.awt.Color(153, 153, 153));
        button4.setLabel("4");
        button4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button4Pressed(evt);
            }
        });

        button6.setBackground(new java.awt.Color(153, 153, 153));
        button6.setLabel("6");
        button6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button6Pressed(evt);
            }
        });

        button5.setBackground(new java.awt.Color(153, 153, 153));
        button5.setLabel("5");
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5Pressed(evt);
            }
        });

        buttonSubtraction.setBackground(new java.awt.Color(153, 153, 153));
        buttonSubtraction.setLabel("-");
        buttonSubtraction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSubtractionPressed(evt);
            }
        });

        button1.setBackground(new java.awt.Color(153, 153, 153));
        button1.setLabel("1");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1pressed(evt);
            }
        });

        button3.setBackground(new java.awt.Color(153, 153, 153));
        button3.setLabel("3");
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3Pressed(evt);
            }
        });

        button2.setBackground(new java.awt.Color(153, 153, 153));
        button2.setLabel("2");
        button2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button2Pressed(evt);
            }
        });

        buttonAddition.setBackground(new java.awt.Color(153, 153, 153));
        buttonAddition.setLabel("+");
        buttonAddition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAdditionPressed(evt);
            }
        });

        button0.setBackground(new java.awt.Color(153, 153, 153));
        button0.setLabel("0");
        button0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button0Pressed(evt);
            }
        });

        buttonPeriod.setBackground(new java.awt.Color(153, 153, 153));
        buttonPeriod.setLabel(".");
        buttonPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPeriodPressed(evt);
            }
        });

        buttonEquals.setBackground(new java.awt.Color(153, 153, 153));
        buttonEquals.setLabel("=");
        buttonEquals.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonEqualsPressed(evt);
            }
        });

        buttonPositiveNegative.setActionCommand("+/-");
        buttonPositiveNegative.setBackground(new java.awt.Color(153, 153, 153));
        buttonPositiveNegative.setFont(new java.awt.Font("Lucida Grande", 1, 10)); // NOI18N
        buttonPositiveNegative.setLabel("+/-");
        buttonPositiveNegative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPositiveNegativePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(numberDisplay)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(button0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(buttonClearAll, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                        .addComponent(button7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(button4, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                        .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(3, 3, 3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(button8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(buttonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(button5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(buttonPositiveNegative, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                            .addComponent(button9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonPeriod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(buttonDivision, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonMultiplication, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonSubtraction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(buttonAddition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(buttonEquals, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(numberDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonDivision, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(buttonClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonClearAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonPositiveNegative, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(button9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonMultiplication, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(button6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonSubtraction, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(button3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonAddition, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(buttonPeriod, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button0, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buttonEquals, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    private void buttonDivisionPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDivisionPressed
        calcAL.add((short)4);
        // when an operator is clicked, also push the value of jTextField1 into numAL
        numberDisplay.setText(saveInput(numberDisplay.getText()));
        // having this logic here prevents adding multiple operators without having numerals to execute the operand on. 
        
    }//GEN-LAST:event_buttonDivisionPressed

    private void buttonMultiplicationPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMultiplicationPressed
        calcAL.add((short)3);
        // when an operator is clicked, also push the value of jTextField1 into numAL
        numberDisplay.setText(saveInput(numberDisplay.getText()));
        // having this logic here prevents adding multiple operators without having numerals to execute the operand on. 
        
    }//GEN-LAST:event_buttonMultiplicationPressed

    private void buttonSubtractionPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSubtractionPressed
        calcAL.add((short)2);
        // when an operator is clicked, also push the value of jTextField1 into numAL
        numberDisplay.setText(saveInput(numberDisplay.getText()));
        // having this logic here prevents adding multiple operators without having numerals to execute the operand on. 
       
    }//GEN-LAST:event_buttonSubtractionPressed

    private void buttonAdditionPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAdditionPressed
        calcAL.add((short)1);
        // when an operator is clicked, also push the value of jTextField1 into numAL
        numberDisplay.setText(saveInput(numberDisplay.getText()));
        // having this logic here prevents adding multiple operators without having numerals to execute the operand on. 

    }//GEN-LAST:event_buttonAdditionPressed

    private void buttonEqualsPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonEqualsPressed
        calcAL.add((short)0);
        saveInput(numberDisplay.getText());
        String response = compute();
        
        if(response.equals("ERROR")){
            // do not allow further calculations, clear values now
            numAL.clear();
            calcAL.clear();
        }
        numberDisplay.setText(response); //autobox to string format

        resetCalculations = true;
        // set boolean field for continue computing
        //   if number is pressed next, reset calculations
        //   if operator is pressed next, continue computing
        
    }//GEN-LAST:event_buttonEqualsPressed

    private void button1pressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1pressed
        // TODO add your handling code here:
        concatInput("1");
    }//GEN-LAST:event_button1pressed

    private void button2Pressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button2Pressed
        // TODO add your handling code here:
        concatInput("2");
    }//GEN-LAST:event_button2Pressed

    private void button3Pressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3Pressed
        // TODO add your handling code here:
        concatInput("3");
    }//GEN-LAST:event_button3Pressed

    private void button4Pressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button4Pressed
        // TODO add your handling code here:
        concatInput("4");
    }//GEN-LAST:event_button4Pressed

    private void button5Pressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5Pressed
        // TODO add your handling code here:
        concatInput("5");
    }//GEN-LAST:event_button5Pressed

    private void button6Pressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button6Pressed
        // TODO add your handling code here:
        concatInput("6");
    }//GEN-LAST:event_button6Pressed

    private void button7Pressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button7Pressed
        // TODO add your handling code here:        
        concatInput("7");
    }//GEN-LAST:event_button7Pressed

    private void button8Pressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button8Pressed
        // TODO add your handling code here:
        concatInput("8");
    }//GEN-LAST:event_button8Pressed

    private void button9Pressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button9Pressed
        // TODO add your handling code here:
        concatInput("9");
    }//GEN-LAST:event_button9Pressed

    private void button0Pressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button0Pressed
        //add a zero to the string, only if the String is not equal to 0
        if(!numberDisplay.getText().equals("0")) {
            concatInput("0");    
        }
    }//GEN-LAST:event_button0Pressed

    private void buttonPeriodPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPeriodPressed
        // TODO add your handling code here:
        numberDisplay.setText(numberDisplay.getText()+".");
    }//GEN-LAST:event_buttonPeriodPressed

    private void buttonClearPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearPressed
        // TODO add your handling code here:
        // clear last entry 
        numberDisplay.setText("");
        numberSaved = false;//set flag to replace input with next button pressed
    }//GEN-LAST:event_buttonClearPressed

    private void buttonClearAllPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonClearAllPressed
        // TODO add your handling code here:
        numberDisplay.setText("");
        numberSaved = false; //set flag to replace input with next button pressed
        resetCalculations = true; //set flag to clear numAL & calcAL
    }//GEN-LAST:event_buttonClearAllPressed

    private void buttonPositiveNegativePressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPositiveNegativePressed
        // +/- button
        // simply multiply input by -1 to change from positive to negative
        // don't save the number just yet, let an operator button do that.
        double temp = Double.parseDouble(numberDisplay.getText());
        temp *= -1;
        numberDisplay.setText(df.format(temp));  

    }//GEN-LAST:event_buttonPositiveNegativePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Calculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Calculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Calculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Calculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Calculator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button0;
    private java.awt.Button button1;
    private java.awt.Button button2;
    private java.awt.Button button3;
    private java.awt.Button button4;
    private java.awt.Button button5;
    private java.awt.Button button6;
    private java.awt.Button button7;
    private java.awt.Button button8;
    private java.awt.Button button9;
    private java.awt.Button buttonAddition;
    private java.awt.Button buttonClear;
    private java.awt.Button buttonClearAll;
    private java.awt.Button buttonDivision;
    private java.awt.Button buttonEquals;
    private java.awt.Button buttonMultiplication;
    private java.awt.Button buttonPeriod;
    private java.awt.Button buttonPositiveNegative;
    private java.awt.Button buttonSubtraction;
    private javax.swing.JTextField numberDisplay;
    // End of variables declaration//GEN-END:variables
}
