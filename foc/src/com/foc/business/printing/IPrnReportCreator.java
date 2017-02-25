package com.foc.business.printing;


public interface IPrnReportCreator {
	public PrnReportLauncher getLauncher();
	public void initLauncher();
	public void resetLauncherContent(PrnLayout layout, PrnReportLauncher launcher);
	public void disposeLauncherContent();
}
