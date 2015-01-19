package com.adamheins.function.tree;

import java.util.Map;

import org.apfloat.Apfloat;

public class Divide extends Function {

    public Divide() {
        super("/", Precedence.MULTIPLICATION, Associativity.LEFT, false);
    }
    

    @Override
    public Function evaluate(Map<String, Function> varMap) {
        
        Function first = getFirstChild().evaluate(varMap);
        Function second = getSecondChild().evaluate(varMap);
        
        if (first instanceof Number && second instanceof Number) {
            Apfloat firstValue = new Apfloat(first.getValue(), PRECISION);
            Apfloat secondValue = new Apfloat(second.getValue(), PRECISION);
            return new Number(firstValue.divide(secondValue).toString(PRETTY));
        }
        
        Function me = new Divide();
        me.setFirstChild(first);
        me.setSecondChild(second);
        
        return me;
    }

    
    @Override
    public Function differentiateInternal(String var) {
        
        FunctionBuilder fb = new FunctionBuilder();
        fb.add(getFirstChild().differentiateInternal(var), 1);
        fb.add(new Multiply(), 1);
        fb.add(getSecondChild(), 1);
        fb.add(new Minus(), 1);
        fb.add(getFirstChild(), 1);
        fb.add(new Multiply(), 1);
        fb.add(getSecondChild().differentiateInternal(var), 1);
        fb.add(new Divide(), 0);
        fb.add(getSecondChild(), 0);
        fb.add(new Exponent(), 0);
        fb.add(new Number("2"), 0);
        
        Function derivative = fb.getFunction();

        return derivative;
    }

}
