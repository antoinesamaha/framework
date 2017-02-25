package com.foc.business.dateShifter;

public interface IDateShifterHolder {
	DateShifter getDateShifter(int shifter);//Because we might have several DateShifter we need to specify the suffix 
}
