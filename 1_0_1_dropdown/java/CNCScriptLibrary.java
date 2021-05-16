package com.seal.student.thenewapp.impl;

import com.ur.urcap.api.domain.script.ScriptWriter;

public class CNCScriptLibrary {
	
	private static final String RSC_FLAG = "RSC_flag";
	private static final String BUSY = "busy";
	private static final String IS_OPERATOR_WORKING = "isOperatorWorking";
	
	private static final String RSC1 = "rsc1";
	private static final String RSC2 = "rsc2";
	private static final String RSC3 = "rsc3";

	// Library of functions and threads (master.script)
	
	public static void writeGlobalVariables(ScriptWriter writer) {
		writer.appendLine("# Global for å sjekke om UR har mottatt noen RSC.");
		writer.appendLine("global " + RSC_FLAG + " = 0");
		writer.appendLine("global " + BUSY + " = False");
		writer.appendLine("global " + IS_OPERATOR_WORKING + " = False");
		writer.appendLine("");
	}
	
	public static void writeOpenClose(ScriptWriter writer) {
		writer.appendRaw("  # Funksjon for å åpne eller lukke noe\n" + 
				"  def open_close(out, in):\n" + 
				"    millis = 0\n" + 
				"    set_standard_digital_out(out, True)\n" + 
				"  \n" + 
				"    while get_standard_digital_in(in) != True:\n" + 
				"      if (millis < 1000):\n" + 
				"        millis = millis + 1\n" + 
				"        sleep(0.01)\n" + 
				"      else:\n" + 
				"        set_standard_digital_out(out, False)\n" + 
				"        return False\n" + 
				"      end\n" + 
				"    end\n" + 
				"  \n" + 
				"    set_standard_digital_out(out, False)\n" + 
				"    return True\n" + 
				"  end\n");
		writer.appendLine("");
		/*
		writer.appendLine("# Funksjon for å åpne eller lukke noe");
		writer.appendLine("def " + "open_close" + "(in, out):");
		writer.assign("millis", "0");
		//writer.appendLine("millis = 0");
		writer.appendLine("set_standard_digital_out(out, True)");
		writer.appendLine("");
		writer.whileCondition("get_standard_digital_in(in) != True");
		writer.ifCondition("millis < 1000");
		writer.appendLine("millis = millis + 1");
		writer.sleep(0.01);
		writer.elseCondition();
		writer.appendLine("set_standard_digital_out(out, False)");
		writer.appendLine("return False");
		writer.end();
		writer.end();
		writer.appendLine("");
		writer.appendLine("set_standard_digital_out(out, False)");
		writer.appendLine("return True");
		writer.appendLine("end");
		*/
	}
	
	public static void writeRobotCallFinish(ScriptWriter writer) {
		writer.appendLine("# Function for sending the Robot Call Finish signal to the CNC-machine");
		writer.defineFunction("robot_call_finish");
		writer.appendLine("set_standard_digital_out(4, True)");
		writer.sleep(0.5);
		writer.appendLine("set_standard_digital_out(4, False)");
		writer.end();
		writer.appendLine("");
	}
	
	public static void writeExecuteCloseDoor(ScriptWriter writer) {
		writer.defineFunction("executeCloseDoor");
		writer.appendLine("door = run closeDoor()");
		writer.appendLine("join door");
		writer.appendLine("kill door");
		writer.end();
		writer.appendLine("");
	}
	
	public static void writeExecuteOpenDoor(ScriptWriter writer) {
		writer.defineFunction("executeOpenDoor");
		writer.appendLine("door = run openDoor()");
		writer.appendLine("join door");
		writer.appendLine("kill door");
		writer.end();
		writer.appendLine("");
	}
	
	public static void writeExecuteClamp(ScriptWriter writer) {
		writer.defineFunction("executeClamp");
		writer.appendLine("clamp = run clampChuck()");
		writer.appendLine("join clamp");
		writer.appendLine("kill clamp");
		writer.end();
		writer.appendLine("");
	}
	
	public static void writeExecuteUnclamp(ScriptWriter writer) {
		writer.defineFunction("executeUnclamp");
		writer.appendLine("clamp = run unclampChuck()");
		writer.appendLine("join clamp");
		writer.appendLine("kill clamp");
		writer.end();
		writer.appendLine("");
	}
	
	public static void writeRSCPollingThread(ScriptWriter writer) {
		writer.appendLine("# Polls the RSC inputs to check for RSC signals.");
		writer.defineThread("RSC_polling");
		writer.whileTrue();
		writer.assign(RSC1, "get_standard_digital_in(4)");
		writer.assign(RSC2, "get_standard_digital_in(5)");
		writer.assign(RSC3, "get_standard_digital_in(6)");
		writer.appendLine("");
		
		writer.ifCondition(RSC3);
		writer.ifCondition(IS_OPERATOR_WORKING);
		writer.appendLine("isOperatorWorking = False");
		writer.elseCondition();
		writer.appendLine("isOperatorWorking = True");
		writer.end();
		writer.appendLine("robot_call_finish()");
		writer.sleep(0.1);
		writer.end();
		
		writer.ifCondition(RSC1);
		writer.ifNotCondition(IS_OPERATOR_WORKING);
		writer.assign(RSC_FLAG, "1");
		writer.end();
		writer.appendLine("robot_call_finish()");
		writer.sleep(0.1);
		writer.end();
		
		writer.ifCondition(RSC2);
		writer.ifNotCondition(IS_OPERATOR_WORKING);
		writer.assign(RSC_FLAG, "2");
		writer.end();
		writer.appendLine("robot_call_finish()");
		writer.sleep(0.1);
		writer.end();
		
		writer.sleep(0.1);
		writer.end();
		writer.end();
		writer.appendLine("");
	}
	
	public static void writeOpenDoorThread(ScriptWriter writer) {
		writer.defineThread("openDoor");
		writer.ifNotCondition("open_close(0, 0)");
		writer.appendLine("textmsg(\"Something went wrong while opening the door. Inspection required.\")");
		writer.end();
		writer.end();
		writer.appendLine("");
	}
	
	public static void writeCloseDoorThread(ScriptWriter writer) {
		writer.defineThread("closeDoor");
		writer.ifNotCondition("open_close(1, 1)");
		writer.appendLine("textmsg(\"Something went wrong while closing the door. Inspection required.\")");
		writer.end();
		writer.end();
		writer.appendLine("");
	}

	public static void writeClampChuckThread(ScriptWriter writer) {
		writer.defineThread("clampChuck");
		writer.ifNotCondition("open_close(2, 2)");
		writer.appendLine("textmsg(\"Something went wrong while clamping the chuck. Inspection required.\")");
		writer.end();
		writer.end();
		writer.appendLine("");
	}
	
	public static void writeUnclampChuckThread(ScriptWriter writer) {
		writer.defineThread("unclampChuck");
		writer.ifNotCondition("open_close(3, 3)");
		writer.appendLine("textmsg(\"Something went wrong while unclamping the chuck. Inspection required.\")");
		writer.end();
		writer.end();
		writer.appendLine("");
	}
	
	public static void WriteRunCNCPolling(ScriptWriter writer) {
		writer.appendLine("run RSC_polling()");
		writer.appendLine("");
	}
}
