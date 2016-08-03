package com.ps.physicssimulator.utils;

import net.objecthunter.exp4j.ValidationResult;
import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.function.Functions;
import net.objecthunter.exp4j.operator.Operator;
import net.objecthunter.exp4j.tokenizer.FunctionToken;
import net.objecthunter.exp4j.tokenizer.NumberToken;
import net.objecthunter.exp4j.tokenizer.OperatorToken;
import net.objecthunter.exp4j.tokenizer.Token;
import net.objecthunter.exp4j.tokenizer.VariableToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpressionModified {

    private final Token[] tokens;

    private final Map<String, Double> variables;
    private final Map<String, String> units;

    private final Set<String> userFunctionNames;

    private static Map<String, Double> createDefaultVariables() {
        final Map<String, Double> vars = new HashMap<String, Double>(4);
        vars.put("pi", Math.PI);
        vars.put("π", Math.PI);
        vars.put("φ", 1.61803398874d);
        vars.put("e", Math.E);
        return vars;
    }

    private static Map<String, String> createDefaultUnits() {
        final Map<String, String> vars = new HashMap<String, String>(4);
        vars.put("pi", "");
        vars.put("π", "");
        vars.put("φ", "");
        vars.put("e", "");
        return vars;
    }

    /**
     * Creates a new expression that is a copy of the existing one.
     *
     * @param existing the expression to copy
     */
    public ExpressionModified(final ExpressionModified existing) {
        this.tokens = Arrays.copyOf(existing.tokens, existing.tokens.length);
        this.variables = new HashMap<String,Double>();
        this.units = new HashMap<String,String>();
        this.variables.putAll(existing.variables);
        this.units.putAll(existing.units);
        this.userFunctionNames = new HashSet<String>(existing.userFunctionNames);
    }

    ExpressionModified(final Token[] tokens) {
        this.tokens = tokens;
        this.variables = createDefaultVariables();
        this.units = createDefaultUnits();
        this.userFunctionNames = Collections.<String>emptySet();
    }

    ExpressionModified(final Token[] tokens, Set<String> userFunctionNames) {
        this.tokens = tokens;
        this.units = createDefaultUnits();
        this.variables = createDefaultVariables();
        this.userFunctionNames = userFunctionNames;
    }


    public ExpressionModified setVariable(final String name, final double value, final String unit) {
        this.checkVariableName(name);
        this.variables.put(name, Double.valueOf(value));
        this.units.put(name, unit);
        return this;
    }

    private void checkVariableName(String name) {
        if (this.userFunctionNames.contains(name) || Functions.getBuiltinFunction(name) != null) {
            throw new IllegalArgumentException("The variable name '" + name + "' is invalid. Since there exists a function with the same name");
        }
    }


    public ValidationResult validate(boolean checkVariablesSet) {
        final List<String> errors = new ArrayList<String>(0);
        if (checkVariablesSet) {
            /* check that all vars have a value set */
            for (final Token t : this.tokens) {
                if (t.getType() == Token.TOKEN_VARIABLE) {
                    final String var = ((VariableToken) t).getName();
                    if (!variables.containsKey(var)) {
                        errors.add("The setVariable '" + var + "' has not been set");
                    }
                }
            }
        }

        /* Check if the number of operands, functions and operators match.
           The idea is to increment a counter for operands and decrease it for operators.
           When a function occurs the number of available arguments has to be greater
           than or equals to the function's expected number of arguments.
           The count has to be larger than 1 at all times and exactly 1 after all tokens
           have been processed */
        int count = 0;
        for (Token tok : this.tokens) {
            switch (tok.getType()) {
                case Token.TOKEN_NUMBER:
                case Token.TOKEN_VARIABLE:
                    count++;
                    break;
                case Token.TOKEN_FUNCTION:
                    final Function func = ((FunctionToken) tok).getFunction();
                    final int argsNum = func.getNumArguments();
                    if (argsNum > count) {
                        errors.add("Not enough arguments for '" + func.getName() + "'");
                    }
                    if (argsNum > 1) {
                        count -= argsNum - 1;
                    } else if (argsNum == 0) {
                        // see https://github.com/fasseg/exp4j/issues/59
                        count++;
                    }
                    break;
                case Token.TOKEN_OPERATOR:
                    Operator op = ((OperatorToken) tok).getOperator();
                    if (op.getNumOperands() == 2) {
                        count--;
                    }
                    break;
            }
            if (count < 1) {
                errors.add("Too many operators");
                return new ValidationResult(false, errors);
            }
        }
        if (count > 1) {
            errors.add("Too many operands");
        }
        return errors.size() == 0 ? ValidationResult.SUCCESS : new ValidationResult(false, errors);

    }

    public ValidationResult validate() {
        return validate(true);
    }



    public List<String> evaluate() {
        final ArrayStack output = new ArrayStack();
        List steps = new ArrayList<String>();

        for (int i = 0; i < tokens.length; i++) {
            Token t = tokens[i];
            if (t.getType() == Token.TOKEN_NUMBER) {
                output.push("");
                output.push(String.valueOf(((NumberToken)t).getValue()));
            } else if (t.getType() == Token.TOKEN_VARIABLE) {
                final String name = ((VariableToken) t).getName();
                final Double value = this.variables.get(name);
                final String unit = this.units.get(name);
                if (value == null) {
                    throw new IllegalArgumentException("No value has been set for the setVariable '" + name + "'.");
                }

                output.push(unit);
                output.push(String.valueOf(value));

            } else if (t.getType() == Token.TOKEN_OPERATOR) {
                OperatorToken op = (OperatorToken) t;
                if (output.size() < op.getOperator().getNumOperands()) {
                    throw new IllegalArgumentException("Invalid number of operands available for '" + op.getOperator().getSymbol() + "' operator");
                }
                if (op.getOperator().getNumOperands() == 2) {
                    /* pop the operands and push the result of the operation */
                    double rightArg = Double.parseDouble(output.pop());
                    String rightUnit = output.pop();
                    double leftArg = Double.parseDouble(output.pop());
                    String leftUnit = output.pop();
                    double res = op.getOperator().apply(leftArg, rightArg);
                    String strRightArg = String.valueOf(rightArg);
                    if(rightArg % 1 == 0){
                        strRightArg = strRightArg.replace(".0" , "");
                    }
                    String strLeftArg = String.valueOf(leftArg);
                    if(leftArg % 1 == 0){
                        strLeftArg = strLeftArg.replace(".0" , "");
                    }

                    String leftUnitTemp = convertUnit(leftUnit);
                    String rightUnitTemp = convertUnit(rightUnit);

                    if(!leftUnit.equals(leftUnitTemp)){

                        String formula = strLeftArg + leftUnit;
                        leftUnit = leftUnitTemp;
                        steps.add(new String[]{strLeftArg, leftUnit, formula});

                    }

                    if(!rightUnit.equals(rightUnitTemp)){

                        String formula = strRightArg + rightUnit;
                        rightUnit = rightUnitTemp;
                        steps.add(new String[]{strRightArg, rightUnit, formula});
                    }

                    String formula = strLeftArg + leftUnit + " " + getFormattedSymbol(op.getOperator().getSymbol()) + " " + strRightArg + rightUnit;
                    String unit = getUnit(leftUnit, op.getOperator().getSymbol(), rightUnit, String.valueOf(rightArg));
                    steps.add(new String[]{String.valueOf(res), unit, formula} );
                    if(!convertUnit(unit).equals(unit)) {
                        formula = String.valueOf(res) + unit;
                        unit = convertUnit(unit);
                        steps.add(new String[]{String.valueOf(res), unit, formula});
                    }



                    output.push(unit);
                    output.push(String.valueOf(res));
                } else if (op.getOperator().getNumOperands() == 1) {
                    /* pop the operand and push the result of the operation */
                    double arg = Double.parseDouble(output.pop());
                    double res = op.getOperator().apply(arg);
                    output.push(String.valueOf(res));
                    steps.add(new String[]{String.valueOf(res), "", ""});
                }
            } else if (t.getType() == Token.TOKEN_FUNCTION) {
                FunctionToken func = (FunctionToken) t;
                final int numArguments = func.getFunction().getNumArguments();
                if (output.size() < numArguments) {
                    throw new IllegalArgumentException("Invalid number of arguments available for '" + func.getFunction().getName() + "' function");
                }
                /* collect the arguments from the stack */
                double[] args = new double[numArguments];
                for (int j = numArguments - 1; j >= 0; j--) {
                    args[j] = Double.parseDouble(output.pop());
                }
                double res = func.getFunction().apply(args);
                //String formula[] = func.getFunction().getName() + "(" +
                //String rep = func.getFunction().getName() + "(" +
                output.push(String.valueOf(res));
            }
        }
        if (output.size() > 2) {
            throw new IllegalArgumentException("Invalid number of items on the output queue. Might be caused by an invalid number of arguments for a function.");
        }
        return steps;
    }

    private String convertUnit(String unit) {
        switch (unit) {
            case "{{J}}":
                return "{{kg}{m^2} \\over {s^2}}";
            case "{{kg}{m^2} \\over {s^2}}":
                return "{{J}}";
            default:
                return unit;

        }

    }

    public String getFormattedSymbol(String operator){
        switch (operator){
            case "^":
                return "^";
            case "/":
                return "\\over";
            case "*":
                return "\\cdot";
            case "+":
                return "+";
            case "-":
                return "-";
            default:
                return "Error";
        }
    }

    public String getUnit(String left, String op, String right, String rightArg){
        boolean leftIsFrac = left.contains("\\over"), rightIsFrac = right.contains("\\over");
        //boolean leftHasExp = left.contains("^"), rightHasExp = right.contains("^");
        String leftUnits[] = null, rightUnits[] = null;

        String num[] = {"1", "1"}, den[] = {"1", "1"};
        left = left.substring(1, left.length()-1);
        right = right.substring(1, right.length()-1);
        switch (op) {
            case "^":
                int l = Integer.parseInt(rightArg.replace(".0", ""));
                //left = left.replace("{", "").replace("}", "");
                for(int i = 0; i < l; i+=2)
                    left = getUnit(left, "*", left, "0");
                return left;
            case "/":
                if(right.equals("1")){
                    return "{" + left + "}";
                } else if(left.equals("1")){
                    return "{1 \\over " + right + "}";
                } else if (leftIsFrac || rightIsFrac) {
                    num[0] = left;
                    den[1] = right;

                    if (rightIsFrac) {

                        rightUnits = right.split(" ");
                        num[1] = rightUnits[2];
                        den[1] = rightUnits[0];
                    }
                    String rightSide = getUnit("{" + num[1] + "}","/", "{" + den[1] + "}", "");
                    return getUnit("{" + left + "}", "*", rightSide, "");
                } else if(left.contains(right) || right.contains(left) || left.contains("}{") || right.contains("}{")) {
                    String leftItems[] = left.split("\\}\\{");
                    for(int i = 0; i < leftItems.length; i++){
                        leftItems[i] = leftItems[i].replace("{", "").replace("}", "");
                    }

                    String rightItems[] = right.split("\\}\\{");
                    for(int i = 0; i < rightItems.length; i++){
                        rightItems[i] = rightItems[i].replace("{", "").replace("}", "");
                    }

                    for(int s = 0; s < leftItems.length; s++){
                        leftUnits = leftItems[s].split("\\^");
                        for(int r = 0; r < rightItems.length; r++) {
                            rightUnits = rightItems[r].split("\\^");
                            boolean leftHasExp = leftUnits.length > 1;
                            boolean rightHasExp = rightUnits.length > 1;
                            if (leftUnits[0].equals(rightUnits[0])) {
                                int exponentLeft = 1;
                                int exponentRight = 1;
                                if(leftHasExp && rightHasExp) {
                                    exponentLeft = Integer.parseInt(leftUnits[1]);
                                    exponentRight = Integer.parseInt(rightUnits[1]);
                                } else if (leftHasExp) {
                                    exponentLeft = Integer.parseInt(leftUnits[1]);
                                } else if (rightHasExp) {
                                    exponentRight =  Integer.parseInt(rightUnits[1]);
                                }

                                int exp = Math.abs(exponentLeft - exponentRight);

                                if (exp > 1) {
                                    if(exponentLeft > exponentRight) {
                                        leftItems[s] = leftUnits[0] + "^" + exp;
                                        rightItems[r] = "";
                                    } else {
                                        leftItems[s] = "";
                                        rightItems[r] = rightUnits[0] + "^" + exp;
                                    }
                                } else if (exp == 1){
                                    if(exponentLeft > exponentRight){
                                        leftItems[s] = leftUnits[0];
                                        rightItems[r] = "";
                                    } else {
                                        leftItems[s] = "";
                                        rightItems[r] = rightUnits[0];
                                    }
                                } else {
                                    leftItems[s] = "";
                                    rightItems[r] = "";
                                }
                            }
                        }
                    }
                    left = "";
                    right = "";

                    for(String s : leftItems)
                        if(!s.equals(""))
                            left += "{" + s + "}";
                    for(String s : rightItems)
                        if(!s.equals(""))
                            right += "{" + s + "}";

                    if(left == ""){
                        left = "1";
                    }
                    if( right == ""){
                        right = "1";
                    }
                    //return left + " \\over " + right;
                }
                return "{" + left + " \\over " + right + "}";
            case "*":
                if(left.equals("1")) {
                    return "{" + right + "}";
                } else if(right.equals("1")) {
                    return "{" + left + "}";
                } else if (leftIsFrac || rightIsFrac) {
//                    left = left.substring(1, left.length()-1);
//                    right = right.substring(1, right.length()-1);

                    num[0] = left;
                    num[1] = right;

                    if(leftIsFrac) {

                        leftUnits = left.split(" ");
                        num[0] = leftUnits[0];
                        den[0] = leftUnits[2];
                    }
                    if(rightIsFrac) {

                        rightUnits = right.split(" ");
                        num[1] = rightUnits[0];
                        den[1] = rightUnits[2];
                    }

                    String temp = "";
                    for(int i = 0; i < num.length; i++){
                        for(int k = 0; k < den.length; k++) {
                            if(!num[i].equals("1") && !den[k].equals("1")) {
                                temp = getUnit("{" + num[i] + "}", "/", "{" + den[k] + "}", "");
                                if (temp.contains("\\over")) {
                                    temp = temp.substring(1, temp.length()-1);
                                    String tempArr[] = temp.split(" ");
                                    num[i] = tempArr[0];
                                    den[k] = tempArr[2];
                                } else {
                                    num[i] = temp.substring(1,temp.length()-1);
                                    den[k] = temp.substring(1,temp.length()-1);
                                }
                            }
                        }
                    }
                    String leftSide = "{1}", rightSide = "{1}";
                    if(!(num[0].equals("1") && num[1].equals("1")))
                        leftSide = getUnit("{" + num[0] + "}", "*", "{" + num[1] + "}", "");
                    if(!(den[0].equals("1") && den[1].equals("1")))
                        rightSide = getUnit("{" + den[0] + "}", "*", "{" + den[1] + "}", "");
                    return getUnit(leftSide, "/", rightSide, "");

                } else if(left.contains(right) || right.contains(left)) {
                    String leftItems[] = left.split("\\}\\{");
                    for(int i = 0; i < leftItems.length; i++){
                        leftItems[i] = leftItems[i].replace("{", "").replace("}", "");
                    }

                    String rightItems[] = right.split("\\}\\{");
                    for(int i = 0; i < rightItems.length; i++){
                        rightItems[i] = rightItems[i].replace("{", "").replace("}", "");
                    }

                    for(int s = 0; s < leftItems.length; s++){
                        leftUnits = leftItems[s].split("\\^");
                        for(int r = 0; r < rightItems.length; r++) {
                            rightUnits = rightItems[r].split("\\^");
                            boolean leftHasExp = leftUnits.length > 1;
                            boolean rightHasExp = rightUnits.length > 1;
                            if (leftUnits[0].equals(rightUnits[0])) {
                                int exponentLeft = 1;
                                int exponentRight = 1;
                                if(leftHasExp && rightHasExp) {
                                    exponentLeft = Integer.parseInt(leftUnits[1]);
                                    exponentRight = Integer.parseInt(rightUnits[1]);
                                } else if (leftHasExp) {
                                    exponentLeft = Integer.parseInt(leftUnits[1]);
                                } else if (rightHasExp) {
                                    exponentRight =  Integer.parseInt(rightUnits[1]);
                                }

                                int exp = exponentLeft + exponentRight;

                                leftItems[s] = leftUnits[0] + "^" + exp;
                                rightItems[r] = "";

                            }
                        }
                    }
                    String result = "";

                    for(String s : leftItems)
                        if(!s.equals(""))
                            result += "{" + s + "}";
                    for(String s : rightItems)
                        if(!s.equals(""))
                            result += "{" + s + "}";

                    return "{" + result + "}";

                }
                return "{" + left + right + "}";
            case "+":
                if(left.equals(right)){
                    return "{" + left + "}";
                } else if(left.equals("1")) {
                    return "{" + right + "}";
                } else if(right.equals("1")) {
                    return "{" + left + "}";
                } return "{" + left + " + " + right + "}";

            case "-":
                if(left.equals(right)){
                    return "{" + left + "}";
                } else if(left.equals("1")) {
                    return "{" + right + "}";
                } else if(right.equals("1")) {
                    return "{" + left + "}";
                }
                return "{" + left + " - " + right + "}";
            default:
                return "Error";
        }
    }

}
