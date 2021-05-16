package com.seal.student.thenewapp.impl;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;

public class CNCInstallationNodeContribution implements InstallationNodeContribution{

	private DataModel model;
	private final CNCInstallationNodeView view;
	
	private final static boolean DEBUG = true; // Show script in terminal on program start (Ursim)
	private final static boolean CNC_ENABLE_DEFAULT = false;
	private final static String CNC_ENABLE_KEY = "CNC enabled";
	private final static String CNC_INIT_MESSAGE = "CNC Pre-Program Script Initialized";
	private final static String CNC_ENABLE_MESSAGE = "CNC functionality enabled";
	private final static String CNC_DISABLE_MESSAGE = "CNC functionality disabled";
	
	public CNCInstallationNodeContribution(InstallationAPIProvider apiProvider, DataModel model, CNCInstallationNodeView view) {
		this.model = model;
		this.view = view;
	}
	
	public ItemListener getListenerForCheckBox() {
		return new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent itemEvent) {
				if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
					setCheckBoxEnabled(true);
				} else {
					setCheckBoxEnabled(false);
				}
			}
		};
	}

	public boolean isCheckBoxEnabled() {
		return model.get(CNC_ENABLE_KEY, CNC_ENABLE_DEFAULT);
	}

	private void setCheckBoxEnabled(boolean enable) {
		if(enable)System.out.println(CNC_ENABLE_MESSAGE);
		else System.out.println(CNC_DISABLE_MESSAGE);
		model.set(CNC_ENABLE_KEY, enable);
	}
	
	
	private void writeScript(ScriptWriter writer) {
		CNCScriptLibrary.writeGlobalVariables(writer);
		CNCScriptLibrary.writeOpenClose(writer);
		CNCScriptLibrary.writeRobotCallFinish(writer);
		CNCScriptLibrary.writeExecuteCloseDoor(writer);
		CNCScriptLibrary.writeExecuteOpenDoor(writer);
		CNCScriptLibrary.writeExecuteClamp(writer);
		CNCScriptLibrary.writeExecuteUnclamp(writer);
		CNCScriptLibrary.writeRSCPollingThread(writer);
		CNCScriptLibrary.writeOpenDoorThread(writer);
		CNCScriptLibrary.writeCloseDoorThread(writer);
		CNCScriptLibrary.writeClampChuckThread(writer);
		CNCScriptLibrary.writeUnclampChuckThread(writer);
		CNCScriptLibrary.WriteRunCNCPolling(writer);
		if(DEBUG)System.out.println(writer.generateScript());
	}
	
	@Override
	public void openView() {
		view.setEnableCheckBox(isCheckBoxEnabled());
	}

	@Override
	public void closeView() {}

	@Override
	public void generateScript(ScriptWriter writer) {
		if(isCheckBoxEnabled()) {
			writeScript(writer);
			System.out.println(CNC_INIT_MESSAGE);
		}
		
	}

}
