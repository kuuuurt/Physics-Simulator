package com.ps.physicssimulator.utils;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.measure.unit.Unit;

import static javax.measure.unit.NonSI.DAY;
import static javax.measure.unit.NonSI.DEGREE_ANGLE;
import static javax.measure.unit.NonSI.HOUR;
import static javax.measure.unit.NonSI.MINUTE;
import static javax.measure.unit.NonSI.MONTH;
import static javax.measure.unit.NonSI.WEEK;
import static javax.measure.unit.NonSI.YEAR;
import static javax.measure.unit.SI.ATTO;
import static javax.measure.unit.SI.CENTI;
import static javax.measure.unit.SI.DECI;
import static javax.measure.unit.SI.DEKA;
import static javax.measure.unit.SI.EXA;
import static javax.measure.unit.SI.FEMTO;
import static javax.measure.unit.SI.GIGA;
import static javax.measure.unit.SI.GRAM;
import static javax.measure.unit.SI.HECTO;
import static javax.measure.unit.SI.JOULE;
import static javax.measure.unit.SI.KILO;
import static javax.measure.unit.SI.MEGA;
import static javax.measure.unit.SI.METRE;
import static javax.measure.unit.SI.MICRO;
import static javax.measure.unit.SI.MILLI;
import static javax.measure.unit.SI.NANO;
import static javax.measure.unit.SI.NEWTON;
import static javax.measure.unit.SI.PETA;
import static javax.measure.unit.SI.PICO;
import static javax.measure.unit.SI.RADIAN;
import static javax.measure.unit.SI.SECOND;
import static javax.measure.unit.SI.TERA;
import static javax.measure.unit.SI.YOCTO;
import static javax.measure.unit.SI.YOTTA;
import static javax.measure.unit.SI.ZEPTO;
import static javax.measure.unit.SI.ZETTA;
/**
 * Created by qwerasdf on 8/24/16.
 */
public class ConversionUtils {
    public static String types[] = {"Mass", "Length", "Duration", "Speed", "Acceleration", "Force", "Work", "Energy", "Power", "Angle Degrees", "Angle Radians", "Momentum", "Moment of Inertia",
            "Torque",
            "Angular " +
            "Momentum"};
    public static Unit<?> baseUnits[] = {GRAM, METRE, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    public static String keywords[] = {"grams", "meters", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    public int defaultUnit;
    public List<Double> unitFactor = new ArrayList<>();
    public List<String> units = new ArrayList<>();
    public List<String> unitSymbol = new ArrayList<>();

    public List<String> populateLists(String type, Unit<?> unit, String stringUnit){
        unitSymbol.clear();
        unitFactor.clear();
        units.clear();
        if (type.equals("Mass") || type.equals("Length")) {
            unitSymbol.add("{" + YOCTO(unit).toString() + "}"); unitFactor.add(Math.pow(10, 24));
            unitSymbol.add("{" + ZEPTO(unit).toString() + "}"); unitFactor.add(Math.pow(10, 21));
            unitSymbol.add("{" + ATTO(unit).toString() + "}"); unitFactor.add(Math.pow(10, 18));
            unitSymbol.add("{" + FEMTO(unit).toString() + "}"); unitFactor.add(Math.pow(10, 15));
            unitSymbol.add("{" + PICO(unit).toString() + "}"); unitFactor.add(Math.pow(10, 12));
            unitSymbol.add("{" + NANO(unit).toString() + "}"); unitFactor.add(Math.pow(10, 9));
            unitSymbol.add("{" + MICRO(unit).toString() + "}"); unitFactor.add(Math.pow(10, 6));
            unitSymbol.add("{" + MILLI(unit).toString() + "}"); unitFactor.add(Math.pow(10, 3));
            unitSymbol.add("{" + CENTI(unit).toString() + "}"); unitFactor.add(Math.pow(10, 2));
            unitSymbol.add("{" + DECI(unit).toString() + "}"); unitFactor.add(Math.pow(10, 1));
            unitSymbol.add("{" + unit.toString() + "}"); unitFactor.add(Math.pow(10, 0));
            unitSymbol.add("{" + DEKA(unit).toString() + "}"); unitFactor.add(Math.pow(10, 1));
            unitSymbol.add("{" + HECTO(unit).toString() + "}"); unitFactor.add(Math.pow(10, 2));
            unitSymbol.add("{" + KILO(unit).toString() + "}"); unitFactor.add(Math.pow(10, 3));
            unitSymbol.add("{" + MEGA(unit).toString() + "}"); unitFactor.add(Math.pow(10, 6));
            unitSymbol.add("{" + GIGA(unit).toString() + "}"); unitFactor.add(Math.pow(10, 9));
            unitSymbol.add("{" + TERA(unit).toString() + "}"); unitFactor.add(Math.pow(10, 12));
            unitSymbol.add("{" + PETA(unit).toString() + "}"); unitFactor.add(Math.pow(10, 15));
            unitSymbol.add("{" + EXA(unit).toString() + "}"); unitFactor.add(Math.pow(10, 18));
            unitSymbol.add("{" + ZETTA(unit).toString() + "}"); unitFactor.add(Math.pow(10, 21));
            unitSymbol.add("{" + YOTTA(unit).toString() + "}"); unitFactor.add(Math.pow(10, 24));
            units.add("Yocto" + stringUnit + " (" + YOCTO(unit) + ")");
            units.add("Zepto" + stringUnit + " (" + ZEPTO(unit) + ")");
            units.add("Atto" + stringUnit + " (" + ATTO(unit) + ")");
            units.add("Femto" + stringUnit + " (" + FEMTO(unit) + ")");
            units.add("Pico" + stringUnit + " (" + PICO(unit) + ")");
            units.add("Nano" + stringUnit + " (" + NANO(unit) + ")");
            units.add("Micro" + stringUnit + " (" + MICRO(unit) + ")");
            units.add("Milli" + stringUnit + " (" + MILLI(unit) + ")");
            units.add("Centi" + stringUnit + " (" + CENTI(unit) + ")");
            units.add("Deci" + stringUnit + " (" + DECI(unit) + ")");
            units.add(stringUnit.substring(0, 1).toUpperCase() + stringUnit.substring(1) +  " (" + unit + ")");
            units.add("Deka" + stringUnit + " (" + DEKA(unit) + ")");
            units.add("Hecto" + stringUnit + " (" + HECTO(unit) + ")");
            units.add("Kilo" + stringUnit + " (" + KILO(unit) + ")");
            units.add("Giga" + stringUnit + " (" + GIGA(unit) + ")");
            units.add("Mega" + stringUnit + " (" + MEGA(unit) + ")");
            units.add("Tera" + stringUnit + " (" + TERA(unit) + ")");
            units.add("Peta" + stringUnit + " (" + PETA(unit) + ")");
            units.add("Exa" + stringUnit + " (" + EXA(unit) + ")");
            units.add("Zetta" + stringUnit + " (" + ZETTA(unit) + ")");
            units.add("Yotta" + stringUnit + " (" + YOTTA(unit) + ")");
            defaultUnit = 10;
        } else if (type.equals("Duration")) {
            unitSymbol.add("{" + NANO(SECOND).toString() + "}"); unitFactor.add(Math.pow(10, 9));
            unitSymbol.add("{" + MILLI(SECOND).toString() + "}"); unitFactor.add(Math.pow(10, 3));
            unitSymbol.add("{" + SECOND.toString() + "}"); unitFactor.add(1.0);
            unitSymbol.add("{" + MINUTE.toString() + "}"); unitFactor.add(60.0);
            unitSymbol.add("{" + HOUR.toString() + "}"); unitFactor.add(3600.0);
            unitSymbol.add("{" + DAY.toString() + "}"); unitFactor.add(DAY.getConverterTo(SECOND).convert(1));
            unitSymbol.add("{" + WEEK.toString() + "}"); unitFactor.add(WEEK.getConverterTo(SECOND).convert(1));
            unitSymbol.add("{" + MONTH.toString() + "}"); unitFactor.add(MONTH.getConverterTo(SECOND).convert(1));
            unitSymbol.add("{" + YEAR.toString() + "}"); unitFactor.add(YEAR.getConverterTo(SECOND).convert(1));

            units.add("Nanoseconds (" + NANO(SECOND) + ")");
            units.add("Milliseconds (" + MILLI(SECOND) + ")");
            units.add("Seconds (" + SECOND + ")");
            units.add("Minutes (" + MINUTE + ")");
            units.add("Hours (" + HOUR + ")");
            units.add("Days (" + DAY + ")");
            units.add("Weeks (" + WEEK + ")");
            units.add("Months (" + MONTH + ")");
            units.add("Years (" + YEAR + ")");
            defaultUnit = 2;
        } else if (type.equals("Speed")) {
            ConversionUtils lengthTemp = new ConversionUtils();
            lengthTemp.populateLists("Length", METRE, "meters");
            ConversionUtils timeTemp = new ConversionUtils();
            timeTemp.populateLists("Duration", null, null);
            for (int i = 0; i < lengthTemp.unitSymbol.size(); i++) {
                for (int k = 0; k < timeTemp.unitSymbol.size(); k++) {
                    String lengthSymbol = lengthTemp.unitSymbol.get(i);
                    String timeSymbol = timeTemp.unitSymbol.get(k);
                    unitSymbol.add(lengthSymbol + " \\over " + timeSymbol);

                    String lengthUnit = lengthTemp.units.get(i);
                    String timeUnit = timeTemp.units.get(k);
                    units.add(lengthUnit.substring(0, lengthUnit.indexOf("(") - 1)
                            + " per " + timeUnit.substring(0, timeUnit.indexOf("(") - 1) + " ("
                            + lengthSymbol.replace("{", "").replace("}", "") + " / "
                            + timeSymbol.replace("{", "").replace("}", "")
                            + ")");

                    double lengthFactor = 1, timeFactor = 1;

                    if (k < timeTemp.defaultUnit)
                        timeFactor = 1 / timeTemp.unitFactor.get(k).doubleValue();
                    else
                        timeFactor = timeTemp.unitFactor.get(k).doubleValue();

                    if (i < lengthTemp.defaultUnit)
                        lengthFactor = 1 / lengthTemp.unitFactor.get(i).doubleValue();
                    else
                        lengthFactor = lengthTemp.unitFactor.get(i).doubleValue();

                    unitFactor.add(lengthFactor / timeFactor);
                }
            }
            defaultUnit = 92;
        } else if (type.equals("Acceleration")) {
            ConversionUtils lengthTemp = new ConversionUtils();
            lengthTemp.populateLists("Length", METRE, "meters");
            ConversionUtils timeTemp = new ConversionUtils();
            timeTemp.populateLists("Duration", null, null);
            for (int i = 0; i < lengthTemp.unitSymbol.size(); i++) {
                for (int k = 0; k < timeTemp.unitSymbol.size(); k++) {
                    String lengthSymbol = lengthTemp.unitSymbol.get(i);
                    String timeSymbol = timeTemp.unitSymbol.get(k);
                    unitSymbol.add(lengthSymbol + " \\over " + timeSymbol + "^2");

                    String lengthUnit = lengthTemp.units.get(i);
                    String timeUnit = timeTemp.units.get(k);
                    units.add(lengthUnit.substring(0, lengthUnit.indexOf("(") - 1)
                            + " per " + timeUnit.substring(0, timeUnit.indexOf("(") - 1) + "Squared ("
                            + lengthSymbol.replace("{", "").replace("}", "") + " / "
                            + timeSymbol.replace("{", "").replace("}", "")
                            + ")");

                    double lengthFactor = 1, timeFactor = 1;

                    if (k < timeTemp.defaultUnit)
                        timeFactor = 1 / Math.pow(timeTemp.unitFactor.get(k).doubleValue(), 2);
                    else
                        timeFactor = Math.pow(timeTemp.unitFactor.get(k).doubleValue(), 2);

                    if (i < lengthTemp.defaultUnit)
                        lengthFactor = 1 / lengthTemp.unitFactor.get(i).doubleValue();
                    else
                        lengthFactor = lengthTemp.unitFactor.get(i).doubleValue();

                    unitFactor.add(lengthFactor / timeFactor);
                }
            }
            defaultUnit = 92;
        } else if (type.equals("Force")) {
            unitSymbol.add("{" + NEWTON.toString() + "}"); unitFactor.add(1.0);
            units.add("Newtons (" + NEWTON + ")");
            defaultUnit = 0;
        } else if (type.equals("Energy") || type.equals("Power") || type.equals("Work")) {
            unitSymbol.add("{" + JOULE.toString() + "}"); unitFactor.add(1.0);
            units.add("Joules (" + JOULE + ")");
            defaultUnit = 0;
        } else if (type.equals("Angle Degrees")) {
            unitSymbol.add("{\\circ}"); unitFactor.add(1.0);
            unitSymbol.add("{" + RADIAN.toString() + "}"); unitFactor.add(Math.PI / 180);
            units.add("Degrees (" + DEGREE_ANGLE + ")");
            units.add("Radians (" + RADIAN + ")");
            defaultUnit = 0;
        } else if (type.equals("Angle Radians")) {
            unitSymbol.add("{" + RADIAN.toString() + "}"); unitFactor.add(1.0);
            unitSymbol.add("{\\circ}"); unitFactor.add(180 / Math.PI);
            units.add("Radians (" + RADIAN + ")");
            units.add("Degrees (" + DEGREE_ANGLE + ")");
            defaultUnit = 0;
        } else if (type.equals("Momentum")) {
            unitSymbol.add("{{N}{s}}"); unitFactor.add(1.0);
            units.add("Newton Seconds ({{N}{s}})");
            defaultUnit = 0;
        } else if (type.equals("Moment of Inertia")) {
            unitSymbol.add("{{kg}{m^2}}"); unitFactor.add(1.0);
            units.add("Kilogram Meters Squared ({{kg}{m^2}})");
            defaultUnit = 0;
        } else if (type.equals("Torque")) {
            unitSymbol.add("{{N}}{m}}"); unitFactor.add(1.0);
            units.add("Newton Meters ({{N}}{m}})");
            defaultUnit = 0;
        } else if (type.equals("Angular Momentum")) {
            unitSymbol.add("{{kg}{m^3} \\over {s}}"); unitFactor.add(1.0);
            units.add("Kilogram Meters Cubed over Seconds ({{kg}{m^3} \\over {s}})");
            defaultUnit = 0;
        }
        return units;
    }


    public static int findTypeIndex(String type) {
        for(int i = 0; i < types.length; i++)
            if(types[i].equals(type))
                return i;
        return -1;
    }

    public static String convertExponentialNotation(double num){
        int power = (int)(Math.log(num)/Math.log(10));

        String strRes = new DecimalFormat("#.####").format(num);
        if((Math.abs(num) > 0) && (Math.abs(num) < Math.pow(10,-3)) || power > 6) {
            strRes = "(" + new DecimalFormat("#.####E0").format(num) + ")";
        }
        return strRes;
    }

    public static double convertValue(String value, ConversionUtils helper, int startUnit, int finalUnit){
        ExpressionBuilder expressionBuilder2 = new ExpressionBuilder("x * (y / z)")
                .variable("x")
                .variable("y")
                .variable("z");
        final Expression expression2 = expressionBuilder2.build();
        expression2.setVariable("x", Double.parseDouble(value.replace("(", "").replace(")", "")));

        double num = 1, den = 1;
        if(finalUnit < startUnit) {
            if(finalUnit < helper.defaultUnit && startUnit > helper.defaultUnit){
                num = helper.unitFactor.get(finalUnit) * helper.unitFactor.get(startUnit);
            } else {
                if(finalUnit > helper.defaultUnit-1)
                    num = helper.unitFactor.get(startUnit) / helper.unitFactor.get(finalUnit);
                else
                    num = helper.unitFactor.get(finalUnit) / helper.unitFactor.get(startUnit);
            }
        } else {
            if(finalUnit > helper.defaultUnit && startUnit < helper.defaultUnit){
                den = helper.unitFactor.get(finalUnit) * helper.unitFactor.get(startUnit);
            } else {
                if(finalUnit < helper.defaultUnit+1)
                    den = helper.unitFactor.get(startUnit) / helper.unitFactor.get(finalUnit);
                else
                    den = helper.unitFactor.get(finalUnit) / helper.unitFactor.get(startUnit);
            }
        }
        expression2.setVariable("y", num);
        expression2.setVariable("z", den);
        return expression2.evaluate();
    }

}
