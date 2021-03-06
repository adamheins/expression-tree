/*
 * Copyright (c) 2015 Adam Heins
 *
 * This file is part of the Dervish project, which is distributed under the MIT
 * license. For the full terms, see the included LICENSE file.
 */

package com.adamheins.dervish.function;

import java.util.Map;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

/**
 * Trigonometric tangent operator.
 *
 * @author Adam
 */
public class Tan extends Function {

    public Tan() {
        super("tan", Precedence.TRIG, Associativity.RIGHT, false);
    }

    @Override
    public Function evaluate(Map<String, Function> varMap) {

        Function child = getFirstChild().evaluate(varMap);

        if(child instanceof Number) {
            Apfloat result = ApfloatMath.tan((Apfloat)child.getValue());
            return new Number(precisionRound(result));
        }

        Function me = new Tan();
        me.setFirstChild(child);

        return me;
    }

    /*
     * Possibility for adding polymorphism to evaluation. Pass a reference to the current class
     * to the child, and then the child can use the calculate method if it is Number.
     */
    double calculate(double value) {
        return Math.tan(value);
    }


    @Override
    public Function differentiateInternal(String var) {

        Function div = new Divide();
        div.setFirstChild(getFirstChild().differentiateInternal(var));

        Function pow = new Exponent();
        Function child = getFirstChild().evaluate();
        Function cos = new Cos();
        cos.setFirstChild(child);

        pow.setFirstChild(cos);
        pow.setSecondChild(new Number("2"));
        div.setSecondChild(pow);

        return div;
    }
}
