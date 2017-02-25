package com.foc.performance;

import java.io.PrintStream;

public class PerfThread {
	
	private PerfFunction rootFunction    = null;
	private PerfFunction currentFunction = null;
	
	public PerfThread(){
		rootFunction = new PerfFunction(null, "ROOT");
		rootFunction.start();
		currentFunction = rootFunction;
	}
	
	public void dispose(){
		if(rootFunction != null){
			rootFunction.dispose();
			rootFunction = null;
		}
		currentFunction = null;
	}
	
	public void start(String functionTitle){
		if(currentFunction != null){
			currentFunction = currentFunction.pushFunction(functionTitle);
			currentFunction.start();
		}
	}
	
	public void end(){
		if(currentFunction != null){
			currentFunction.end();
			currentFunction = currentFunction.getFather();
		}
	}
	
	public void startDBExec(){
		if(currentFunction != null){
			currentFunction.startDBExec();
		}
	}
	
	public void endDBExec(){
		if(currentFunction != null){
			currentFunction.endDBExec();
		}
	}

	public void endDBExecForRequest(String request){
		if(currentFunction != null){
			currentFunction.endDBExecForRequest(request);
		}
	}

	public void startDBRead(){
		if(currentFunction != null){
			currentFunction.startDBRead();
		}
	}
	
	public void endDBRead(){
		if(currentFunction != null){
			currentFunction.endDBRead();
		}
	}
	
	public void startProtection(){
		if(currentFunction != null){
			currentFunction.startProtection();
		}
	}
	
	public void endProtection(){
		if(currentFunction != null){
			currentFunction.endProtection();
		}
	}
	
	public void compute(){
		rootFunction.computeTotals();
		rootFunction.computePercentages();
	}
	
	public void printXML(PrintStream perfFile){
		if(rootFunction != null){
			rootFunction.printXML(perfFile);
		}
	}
	
	public void printCSV(PrintStream perfFile){
		if(rootFunction != null){
			perfFile.println("TITLE,TOTAL,DBExec,DBRead,NET");
			rootFunction.printCSV(perfFile);
		}
	}
}
