package com.foc.util.expression;

import java.util.ArrayList;

public interface IExpressionHandler {
  public String handleFieldOrParameter(String expression, char type, int startIndex, int endIndex, String fieldOfParameter, ArrayList<String> arguments);
}