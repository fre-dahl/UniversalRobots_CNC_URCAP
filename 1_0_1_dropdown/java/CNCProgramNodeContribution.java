package com.seal.student.thenewapp.impl;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;

public class CNCProgramNodeContribution implements ProgramNodeContribution{

	private final ProgramAPI programAPI;
	private final UndoRedoManager undoRedoManager;
	private final ProgramAPIProvider apiProvider;
	private final CNCProgramNodeView view;
	private final DataModel model;
	
	
	private static final String FUNCTION_KEY = "function";
	
	public CNCProgramNodeContribution(ProgramAPIProvider apiProvider, CNCProgramNodeView view, DataModel model) {
		this.apiProvider = apiProvider;
		this.view = view;
		this.model = model;
		this.undoRedoManager = this.apiProvider.getProgramAPI().getUndoRedoManager();
		this.programAPI = this.apiProvider.getProgramAPI();
	}
	
	enum Function {
		PLACEHOLDER("-",""),
		OPEN_DOOR("Open Door","executeOpenDoor()"),
		OPEN_CHUCK("Open Chuck","executeUnclamp()"),
		CLOSE_DOOR("Close Door","executeCloseDoor()"),
		CLOSE_CHUCK("Close Chuck","executeClamp()");
		
		final String name;
		final String definition;
		
		Function(String name, String definition){
			this.name = name;
			this.definition = definition;
		}
	}
	
	public void onFunctionSelection(final String functionName) {
		undoRedoManager.recordChanges(new UndoableChanges() {
			
			@Override
			public void executeChanges() {
				model.set(FUNCTION_KEY, functionName);
			}
		});
	}
	
	private String getFunction() {
		return model.get(FUNCTION_KEY, Function.PLACEHOLDER.name);
	}
	
	private String getInstallationStatus() {
		if(getInstallation().isCheckBoxEnabled()) return "enabled";
		return "disabled";
	}
	
	private String[] getFunctions() {
		String[] functionList = new String[Function.values().length];
		functionList[0] = Function.PLACEHOLDER.name;
		functionList[1] = Function.OPEN_DOOR.name;
		functionList[2] = Function.OPEN_CHUCK.name;
		functionList[3] = Function.CLOSE_DOOR.name;
		functionList[4] = Function.CLOSE_CHUCK.name;
		return functionList;
	}
	
	private void assignDefinition(ScriptWriter writer) {
		String name = getFunction();
		String definition = Function.PLACEHOLDER.definition;
		for(Function f: Function.values()) {
			if(f.name.equals(name)) {
				definition = f.definition;
			}
		}
		writer.appendLine(definition);
	}
	
	private CNCInstallationNodeContribution getInstallation() {
		return programAPI.getInstallationNode(CNCInstallationNodeContribution.class);
	}
	
	@Override
	public void openView() {
		view.setProgramComboBoxItems(getFunctions());
		view.setProgramComboBoxSelection(getFunction());
		view.setCheckBoxEnabled(getInstallationStatus());
	}

	@Override
	public void closeView() {}

	@Override
	public String getTitle() {
		return "CNC: " + getFunction();
	}

	@Override
	public boolean isDefined() {
		// Også viktig at polling er enablet
		return !getFunction().equals(Function.PLACEHOLDER.name) && getInstallation().isCheckBoxEnabled();
	}

	@Override
	public void generateScript(ScriptWriter writer) {
		assignDefinition(writer);
	}

}
