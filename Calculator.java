/* 
 * Calculator.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */


import java.util.Vector;
import java.util.Stack;

/**
 * This program operates as a basic calculator. The main class defines two stacks,
 * One operator stack and one number stack. It also defines a string variable 'operators'
 * that contains the accepted operators. The main function calls the performCalculation
 * function that accepts an expression that is to be solved. The performCalculation
 * function defines a vector class of the name 'aline' that holds the characters
 * of the expression to be solved.. It then passes the aline vector to the calculate
 * function. The aline vector is stored in the inputLine vector. The function checks
 * whether the size of the inputLine vector is greater than or equal to 1. If the
 * condition is satisfied. It checks whether the first element is an operator
 * or a number. If it is an operator, it calls the performOperator function that
 * evaluates the precedence of the current operator with the operator already in the
 * stack. The precedence is checked by the index value of the operators in the operator
 * string. If the precedence is less than that of the operator in the stack then the
 * evaluate function is called, else the current operator is pushed in the stack.
 * The evaluate function uses the operator in the stack to perform the required
 * calculation. Once the calculation is done, the operator is removed from the stack.
 * Once the old operator is evaluated the current operator is pushed in the stack.
 * If the first element is a number the number is pushed in the stack. Then the
 * element at he [0] index position is removed from the InputLine vector.The
 * evaluate function does the required mathematical calculation based on the operator.
 * It pops the top most operator from the stack. It gets the top two elements from the
 * number stack. The numbers are evaluated according to the operator and the result
 * is pushed back in to the number stack. Once the operator stack is empty and only
 * one element remains in the number stack and output is returned.
 *
 */

public class Calculator {

    // See https://docs.oracle.com/javase/10/docs/api/java/util/Stack.html
    static Stack<Double> numberStack = new Stack<Double>();
    static Stack<String> operatorStack = new Stack<String>();
    // See https://docs.oracle.com/javase/10/docs/api/java/lang/String.html
    static String operators =  "+-%*/^" ;
    //static String operators =  "^/*%-+" ;
    static String parantheses =  "()[] {}" ;
    String closingParentheses="";

    public static void main (String args []) {

        performCalculation("1", "*", "{", "2", "+", "3", "-",
                "[", "1", "*", "(", "2", "-", "1", ")", "]", "+", "3", "}");
performCalculation("2", "+", "[", "(", "3", "-", "6", ")", "/", "5", "]");
performCalculation("1", "+", "(", "2", "+", "3", ")", "*", "3");
performCalculation("2", "^", "3","^", "4");
performCalculation("(", "2", "^", "3", ")", "^", "4");
performCalculation("1", "-", "(", "2", "+", "[", "+","2","-",")","-","3");
    	
/*    	
        performCalculation("1", "*", "{", "2", "+", "3", "-",
                "[", "1", "*", "(", "2", "-", "1", ")", "]", "+", "3", "}");    	
        performCalculation("1", "+", "(", "2", "+", "3", ")", "*", "3");
        performCalculation("(", "2", "^", "3", ")", "^", "4");
        performCalculation("1", "+", "(", "2", "+", "3", ")", "*", "3");
        performCalculation("2", "^", "3","^", "4");
        performCalculation("1", "*", "(", "2", "+", "3", "-",
                "(", "1", "*", "(", "2", "-", "1", ")", ")", "+", "3", ")");        
        performCalculation("2", "+", "3");
        performCalculation("2", "+", "3", "*", "3");
        performCalculation("2", "+", "3", "*", "4", "+", "5");
        performCalculation("2", "*", "3", "+", "3");
        performCalculation("2", "+", "3", "^", "4");
        performCalculation("2", "^", "3", "+", "4");
        performCalculation("2", "^", "3", "^", "4");
*/
    }

    // See https://docs.oracle.com/javase/8/docs/technotes/guides/language/varargs.html
    public static void performCalculation (String ... valuesAndOperators)	{
        Vector<String> aLine = new Vector<String>();
        for ( String valuesAndOperator: valuesAndOperators )	{
            aLine.add(valuesAndOperator);
            System.out.print(valuesAndOperator + " ");
        }
        System.out.println(" =  " + calculate(aLine) );
    }
    /** drives the calculation and returns the result
     */
    public static double calculate (Vector<String> inputLine) {
        String openingParentheses="";
        while ( inputLine.size() >= 1 )	{
            if ( operator( inputLine.firstElement() )	)
                performOperator(inputLine.firstElement());
            else if (inputLine.firstElement()=="(" || inputLine.firstElement()=="{" || inputLine.firstElement()=="[") 
            	performParantheses(inputLine.firstElement());
            else if (inputLine.firstElement()==")" || inputLine.firstElement()=="}" || inputLine.firstElement()=="]")
            {
            	if (inputLine.firstElement()==")")
            		openingParentheses="(";
            	if (inputLine.firstElement()=="}")
            		openingParentheses="{";
            	if (inputLine.firstElement()=="]")
            		openingParentheses="[";

            	do {
            		evaluate();
            	}
            	while(!  operatorStack.empty() && ! (operatorStack.peek()=="(" || operatorStack.peek()=="{" || operatorStack.peek()=="["));
            	if (operatorStack.peek()==openingParentheses) {
            		operatorStack.pop();
            	}
            	else
            	{
            		System.out.println("is an illegal expression");
            		System.exit(1);
            	}
            }
            else
                performNumber(inputLine.firstElement());

            inputLine.removeElementAt(0);
        }
        while ( !  operatorStack.empty() )	{
            if ( numberStack.size() > 1 )
                evaluate();
            else	{
                System.out.println("dangling operand ....");
                System.out.println(numberStack);
                System.exit(1);

            }
        }

        return numberStack.pop();
    }

    /** perform the required operation based on precedence of the operators on the stack
     */
    public static boolean operator (String op) {
        return ( operators.indexOf(op) >= 0 );
    }

    /** deteremence a precedence level for the operator
     */
    public static int precedence (String op) {
        return operators.indexOf(op);
    }

    /** perform the required operation based on precedence on the stack
     */
    public static void performOperator (String op) {
        while (! operatorStack.empty()  &&
                (  precedence(op) < precedence(operatorStack.peek() ) )
        )
            evaluate();
        operatorStack.push(op);
    }

    public static void performParantheses (String op) {
        operatorStack.push(op);
    }
    
    /** pushes the number on the number stack
     */
    public static void performNumber (String number) {
        numberStack.push(Double.valueOf(number));
    }

    /** get the number of the stack, if a number is available, else RIP
     */
    public static double  getNumber () {
        if ( numberStack.empty() ){
            System.out.println("not enough numbers ...");
            System.exit(2);
        }
        return numberStack.pop();
    }

    /** perform the required ovperation based on the operator in the stack
     */
    public static void evaluate () {
        String currentOp = operatorStack.pop();
        double right = getNumber();
        double left = getNumber();
        if ( currentOp.equals("+") )
            numberStack.push( left + right );
        else if ( currentOp.equals("-") )
            numberStack.push( left - right );
        else if ( currentOp.equals("*") )
            numberStack.push( left * right );
        else if ( currentOp.equals("%") )
            numberStack.push( left % right );
        else if ( currentOp.equals("/") )
            numberStack.push( left / right );
        else if ( currentOp.equals("^") )
            numberStack.push( Math.pow(left , right ) );
        else
            System.out.println("Unknown Operator");
    }
}
