/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * these naming standards are driving me crazy. jTextField1 doesn't mean anything!
 *
 *  NEW ORDER OF OPERATIONS LOGIC
 *  -- use ArrayList to store input values.
 *  -- use ArrayList to store input calculations
 *  -- use recursion? to execute order of operation ** only when equal is pressed ** 
 *  -- keep logic to display answer of two input values for addition and substraction
 *  -- logic for multiplication and division are separate
        
        //Multiply/Divide -->  Add/Subtract  * work recursively from left to right *
        
        //LOGIC GOAL
        // 0  1  2  3  4     numAL
        //     \ /
        //   +  *  -  /  =    calcAL
        // deleteCharAt(index1); deleteCharAt(index1+1); (calcAL should always?? end in 0 when entering this logic)
        // 0  2  3  4     numAL
        //        \ /
        //   +  -  /  =    calcAL
        // deleteCharAt(index2); deleteCharAt(index2+1); (calcAL should always?? end in 0 when entering this logic)
        // 0  2  .75     numAL
        //  \ /
        //   +  -   =    calcAL
        // deleteCharAt(index1); deleteCharAt(index1+1); (calcAL should always?? end in 0 when entering this logic)
        // 2  .75     numAL
        //  \ /
        //   -   =    calcAL
        // deleteCharAt(index2); deleteCharAt(index2+1); (calcAL should always?? end in 0 when entering this logic)
        //  1.25

 * PROBLEMS
 **  each time number is calculated, numAL is reduced to nothing. 
 *    -- need to preserve numbers input until either clear is pressed or a number is pressed after the enter button.
 *    -- refactor to do math by passing around local array rather than instance ArrayList??
 *    -- create local array for calcAL as well.
 **  clear up the need for num1 and num2. What purpose do they have in this new version?
 **  test the order of operation functions
 *    -- divide by zero exception caught?
 *    -- correct calculations?
 *    -- able to retreive answer from ArrayList?
 
        
 */
package calculator;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Jess
 */
public class Calculator extends javax.swing.JFrame {

    private ArrayList<Double> numAL = new ArrayList<>(); //using arrayList so user can input as many values as they want
    private int index1, index2; // open scope needed
    private ArrayList<Integer> calcAL = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("##.##");
    
    //old logic
    private double num1, num2; //auto initialized at 0 
    private boolean num2Set = false;
    private int calculateKey;    
    private boolean resetCalculations;
    
   // private int calculateKey;
    /**
     * Creates new form calculator_frame
     */
    public Calculator() {
        initComponents();
    }
    /**
     * The calculateKey integer will relate to a math operator.
     * 1 --> +
     * 2 --> -
     * 3 --> *
     * 4 --> /
     */
    private String compute() throws ArithmeticException{//, double input){
        // set boolean field for continue computing
        //   if operator is pressed after the equals button
        if(resetCalculations){
            num1 = num2; //save answer as num1
            numAL.clear();    //
            numAL.add(num2);
            num2Set = false; //set num2Set as false to avoid calculations
            resetCalculations = false; // flip boolean.
        }
        
        // save number entered
        numAL.add(num1); 
        //avoid computing when user hasn't input two numbers        
        if(numAL.size()>1){
            boolean indexFound = true; 
            try{
                // loop until there are no longer ** 3 or 4 ** values in calcAL    
                do{
                    // multiplyDivide() will search for indexes and return false to end loop
                    indexFound = multiplyDivide(indexFound,3,4);
                }while(indexFound);
            } catch (ArithmeticException e){
                return "ERROR";
            }

            // loop until there are no longer ** 1 or 2 ** values in calcAL
            indexFound = true;
            do{
                // addSubtract() will search for indexes and return false to end loop
                indexFound = multiplyDivide(indexFound,1,2);
            }while(indexFound);
        } 
        
        // "print" answer to screen 
        return num2+""; //return answer in string form.
    }
 
    private void SortnSaveIndex(int searchIndex){
        // logic in multiplyDivide calls multiplication or division based off even or odd indexes
        if(searchIndex % 2 == 1){ 
            index1 = calcAL.indexOf(searchIndex); // multiply / addition
        } else {
            index2 = calcAL.indexOf(searchIndex); // subtraction / division   
        }   
    }
    private boolean multiplyDivide(boolean indexFound, int searchIndex1, int searchIndex2) throws ArithmeticException{
        // CREATE LOGIC TO THROW METHOD EXCEPTION 
        // input indexes cannot be equal and should be in groups of 1&2 OR 3&4
        
        SortnSaveIndex(searchIndex1);
        SortnSaveIndex(searchIndex2);
        
        // it is possible for multiple occurances of index, loop through this logic until both indexes are -1
                //One would have to remove the values already computed for that to work
        if (index1 != -1) {
            //we know odd has been entered
            if(index2 != -1){
                  //we know BOTH odd and even has been entered
                  if(index1 < index2){ // which index occurs first?
                    // we know odd was entered first, calculate it, then re-enter outside loop 
                    addOrMultiply();
                  } else {
                    //we know even was entered first, calculate it, then re-enter outside loop 
                    subtractOrDivide();
                  }
              } else {
                //we know odd has been entered, calculate it, then re-enter outside loop 
                addOrMultiply();
              }
        } else {
            // we know odd has NOT been entered
            if(index2 != -1){
              //we know even was entered, calculate it, then re-enter outside loop 
              subtractOrDivide();
            } else {
              // neither index input exist in calcAL, flag to end loop
              indexFound = false;
            }
        }
        return indexFound;
    }
    
    private void addOrMultiply(){
        System.out.println(numAL.toString());
        if(index1 == 3){
            multiplication(numAL, index1);
        } else {
            if(index1 == 1){
                addition(numAL, index1);
            }
        }                
        System.out.println(numAL.toString());
    }
    
    private void subtractOrDivide(){
        System.out.println(numAL.toString());
        if(index1 == 4){
            division(numAL, index2);
        } else {
            if(index1 == 2){
                subtraction(numAL, index2);
            }
        }                
        System.out.println(numAL.toString());        
    }
    private void division(ArrayList<Double> numAL, int indx) throws ArithmeticException{ //pass reference
         //in order to use values in calculations, ArrayList must be converted to Array
        Double[] numArr = numAL.toArray(new Double[numAL.size()]);  //local varible used only for math
        
        numArr[indx] = numArr[indx] / numArr[indx+1];
        calcAL.remove(new Integer(4));//removes first occurance in calc
        numAL.remove(indx+1); //delete at index + 1
        numAL.set(indx, numArr[indx]); //replace value in numAl with result 

    }
    private void multiplication(ArrayList<Double> numAL, int indx){ //pass reference
        //in order to use values in calculations, ArrayList must be converted to Array
        Double[] numArr = numAL.toArray(new Double[numAL.size()]);  //local varible used only for math
        
        numArr[indx] = numArr[indx] * numArr[indx+1];
        calcAL.remove(new Integer(3));//removes first occurance in calc
        numAL.remove(indx+1); //delete at index + 1
        numAL.set(indx, numArr[indx]); //replace value in numAl with result         
    }
    private void subtraction(ArrayList<Double> numAL, int indx){ //pass reference
        //in order to use values in calculations, ArrayList must be converted to Array
        Double[] numArr = numAL.toArray(new Double[numAL.size()]);  //local varible used only for math
        numArr[indx] = numArr[indx] - numArr[indx+1];
        calcAL.remove(new Integer(3));//removes first occurance in calc
        numAL.remove(indx+1); //delete at index + 1
        numAL.set(indx, numArr[indx]); //replace value in numAl with result
    }
    private void addition(ArrayList<Double> numAL, int indx){ //pass reference
        //in order to use values in calculations, ArrayList must be converted to Array
        Double[] numArr = numAL.toArray(new Double[numAL.size()]);  //local varible used only for math
        numArr[indx] = numArr[indx] + numArr[indx+1];
        calcAL.remove(new Integer(3));//removes first occurance in calc
        numAL.remove(indx+1); //delete at index + 1
        numAL.set(indx, numArr[indx]); //replace value in numAl with result 
    }

         
////////////////////////////////////////////////////////////////////////////////
// OLD LOGIC  --- OLD LOGIC -- OLD LOGIC -- OLD LOGIC  --- OLD LOGIC -- OLD LOGIC -- 
////////////////////////////////////////////////////////////////////////////////
  

    private void updateNum1(String input){
        // set boolean field for continue computing
        //   if number is pressed after the equals button, reset calculations
        if(resetCalculations){ //reset board?
            num2 = 0;
            jTextField1.setText(input);
            resetCalculations = false;
        }else{
            if(num1 == 0){  
                // replace textField with new input after calculations
                jTextField1.setText(input);
            } else{ 
                // num1 has been set with a new number, conact rather than replace
                // Don't allow leading zeros
                if(jTextField1.getText().equals("0")){
                    jTextField1.setText(input);
                } else {
                    jTextField1.setText(jTextField1.getText()+input);
                }
            }        
        }
        
        // set num1 in preparation for next calculation
        num1 = Double.parseDouble(jTextField1.getText());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        button1 = new java.awt.Button();
        button3 = new java.awt.Button();
        button5 = new java.awt.Button();
        button6 = new java.awt.Button();
        button7 = new java.awt.Button();
        button8 = new java.awt.Button();
        button9 = new java.awt.Button();
        button10 = new java.awt.Button();
        button11 = new java.awt.Button();
        button12 = new java.awt.Button();
        button13 = new java.awt.Button();
        button14 = new java.awt.Button();
        button15 = new java.awt.Button();
        button16 = new java.awt.Button();
        button17 = new java.awt.Button();
        button18 = new java.awt.Button();
        button20 = new java.awt.Button();
        button21 = new java.awt.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calculator");
        setBackground(new java.awt.Color(223, 218, 194));
        setResizable(false);
        setType(java.awt.Window.Type.UTILITY);

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(204, 204, 204));
        jTextField1.setFont(new java.awt.Font("Silom", 1, 24)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextField1.setText("0");
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        button1.setBackground(new java.awt.Color(195, 8, 14));
        button1.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        button1.setLabel("CE");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        button3.setBackground(new java.awt.Color(195, 8, 14));
        button3.setFont(new java.awt.Font("Lucida Grande", 0, 12)); // NOI18N
        button3.setLabel("AC");
        button3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3ActionPerformed(evt);
            }
        });

        button5.setBackground(new java.awt.Color(153, 153, 153));
        button5.setLabel("/");
        button5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button5ActionPerformed(evt);
            }
        });

        button6.setBackground(new java.awt.Color(153, 153, 153));
        button6.setLabel("7");
        button6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button6ActionPerformed(evt);
            }
        });

        button7.setBackground(new java.awt.Color(153, 153, 153));
        button7.setLabel("9");
        button7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button7ActionPerformed(evt);
            }
        });

        button8.setBackground(new java.awt.Color(153, 153, 153));
        button8.setLabel("8");
        button8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button8ActionPerformed(evt);
            }
        });

        button9.setBackground(new java.awt.Color(153, 153, 153));
        button9.setLabel("x");
        button9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button9ActionPerformed(evt);
            }
        });

        button10.setBackground(new java.awt.Color(153, 153, 153));
        button10.setLabel("4");
        button10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button10ActionPerformed(evt);
            }
        });

        button11.setBackground(new java.awt.Color(153, 153, 153));
        button11.setLabel("6");
        button11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button11ActionPerformed(evt);
            }
        });

        button12.setBackground(new java.awt.Color(153, 153, 153));
        button12.setLabel("5");
        button12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button12ActionPerformed(evt);
            }
        });

        button13.setBackground(new java.awt.Color(153, 153, 153));
        button13.setLabel("-");
        button13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button13ActionPerformed(evt);
            }
        });

        button14.setBackground(new java.awt.Color(153, 153, 153));
        button14.setLabel("1");
        button14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button14ActionPerformed(evt);
            }
        });

        button15.setBackground(new java.awt.Color(153, 153, 153));
        button15.setLabel("3");
        button15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button15ActionPerformed(evt);
            }
        });

        button16.setBackground(new java.awt.Color(153, 153, 153));
        button16.setLabel("2");
        button16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button16ActionPerformed(evt);
            }
        });

        button17.setBackground(new java.awt.Color(153, 153, 153));
        button17.setLabel("+");
        button17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button17ActionPerformed(evt);
            }
        });

        button18.setBackground(new java.awt.Color(153, 153, 153));
        button18.setLabel("0");
        button18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button18ActionPerformed(evt);
            }
        });

        button20.setBackground(new java.awt.Color(153, 153, 153));
        button20.setLabel(".");
        button20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button20ActionPerformed(evt);
            }
        });

        button21.setBackground(new java.awt.Color(153, 153, 153));
        button21.setLabel("=");
        button21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button21ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(button10, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button12, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button11, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button17, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(button6, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button8, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button7, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button13, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(button3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button9, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(button18, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button20, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(button14, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button16, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button15, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button21, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(button5, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(button1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(button7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(button11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(button15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(button20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button18, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(button21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void button5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button5ActionPerformed
        //DIVISION
        calcAL.add(4);
        
        
        calculateKey = 4;
        compute();
        jTextField1.setText(df.format(num2)); // format out the decimals
    }//GEN-LAST:event_button5ActionPerformed

    private void button9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button9ActionPerformed
        //MULTIPLICATION 
        calcAL.add(3);
        
        calculateKey = 3;
        
        jTextField1.setText(df.format(compute())); //autobox to string format
    }//GEN-LAST:event_button9ActionPerformed

    private void button13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button13ActionPerformed
        //SUBTRACTION
        calcAL.add(2);
        
        
        calculateKey = 2;
        
        jTextField1.setText(df.format(compute())); //autobox to string format
    }//GEN-LAST:event_button13ActionPerformed

    private void button17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button17ActionPerformed
        //ADDITION
        calcAL.add(1);
        
        
        calculateKey = 1;

        jTextField1.setText(df.format(compute())); //autobox to string format
    }//GEN-LAST:event_button17ActionPerformed

    private void button21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button21ActionPerformed
        // EQUAL BUTTON
        calcAL.add(0);
        
        
        jTextField1.setText(df.format(compute())); //autobox to string format
        resetCalculations = true;
        num2Set = false;
        // set boolean field for continue computing
        //   if number is pressed next, reset calculations
        //   if operator is pressed next, continue computing
        
    }//GEN-LAST:event_button21ActionPerformed

    private void button14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button14ActionPerformed
        // TODO add your handling code here:
        updateNum1("1");
    }//GEN-LAST:event_button14ActionPerformed

    private void button16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button16ActionPerformed
        // TODO add your handling code here:
        updateNum1("2");
    }//GEN-LAST:event_button16ActionPerformed

    private void button15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button15ActionPerformed
        // TODO add your handling code here:
        updateNum1("3");
    }//GEN-LAST:event_button15ActionPerformed

    private void button10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button10ActionPerformed
        // TODO add your handling code here:
        updateNum1("4");
    }//GEN-LAST:event_button10ActionPerformed

    private void button12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button12ActionPerformed
        // TODO add your handling code here:
        updateNum1("5");
    }//GEN-LAST:event_button12ActionPerformed

    private void button11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button11ActionPerformed
        // TODO add your handling code here:
        updateNum1("6");
    }//GEN-LAST:event_button11ActionPerformed

    private void button6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button6ActionPerformed
        // TODO add your handling code here:        
        updateNum1("7");
    }//GEN-LAST:event_button6ActionPerformed

    private void button8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button8ActionPerformed
        // TODO add your handling code here:
        updateNum1("8");
    }//GEN-LAST:event_button8ActionPerformed

    private void button7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button7ActionPerformed
        // TODO add your handling code here:
        updateNum1("9");
    }//GEN-LAST:event_button7ActionPerformed

    private void button18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button18ActionPerformed
        //add a zero to the string, only if the String is not equal to 0
        if(!jTextField1.getText().equals("0")) {
            updateNum1("0");    
        }
    }//GEN-LAST:event_button18ActionPerformed

    private void button20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button20ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText(jTextField1.getText()+".");
    }//GEN-LAST:event_button20ActionPerformed

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        // clear last entry 
        jTextField1.setText("");
        num1 = 0;
    }//GEN-LAST:event_button1ActionPerformed

    private void button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3ActionPerformed
        // TODO add your handling code here:
        jTextField1.setText("");
        num1 = 0;
        num2 = 0;
        resetCalculations = false;
        num2Set = false;
    }//GEN-LAST:event_button3ActionPerformed

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
    private java.awt.Button button1;
    private java.awt.Button button10;
    private java.awt.Button button11;
    private java.awt.Button button12;
    private java.awt.Button button13;
    private java.awt.Button button14;
    private java.awt.Button button15;
    private java.awt.Button button16;
    private java.awt.Button button17;
    private java.awt.Button button18;
    private java.awt.Button button20;
    private java.awt.Button button21;
    private java.awt.Button button3;
    private java.awt.Button button5;
    private java.awt.Button button6;
    private java.awt.Button button7;
    private java.awt.Button button8;
    private java.awt.Button button9;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
