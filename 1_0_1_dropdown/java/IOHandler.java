package com.seal.student.thenewapp.impl;

import java.util.Collection;
import java.util.Iterator;

import com.ur.urcap.api.domain.io.DigitalIO;
import com.ur.urcap.api.domain.io.IOModel;

public class IOHandler {

	private final IOModel ioModel;
	
	public IOHandler(IOModel ioModel) {
		this.ioModel = ioModel;
	}
	
	enum RobotIO {
        
        DI0("digital_in[0]","Door Open",1),
        DI1("digital_in[1]","Door Closed",2),
        DI2("digital_in[2]","Chuck clamp end",3),
        DI3("digital_in[3]","Chuck unclamp end",4),
        DI4("digital_in[4]","Robot Service Call 1",5),
        DI5("digital_in[5]","Robot Service Call 2",6),
        DI6("digital_in[6]","Robot Service Call 3",7),
        DI7("digital_in[7]","Emergency stop",8),

        DO0("digital_out[0]","Open door",17),
        DO1("digital_out[1]","Close door",18),
        DO2("digital_out[2]","Chuck clamp",19),
        DO3("digital_out[3]","Chuck unclamp",20),
        DO4("digital_out[4]","Robot Call Finish",21),
        DO5("digital_out[5]","Emergency stop",22),
        DO6("digital_out[6]","Feed hold",23);

        public final String defaultName;
        public final String description;
        public final int pin;

        RobotIO(String defaultName, String description, int pin) {
            this.defaultName = defaultName;
            this.description = description;
            this.pin = pin;
        }
    }
	 
	public DigitalIO getDigitalIO(String defaultName){
		Collection<DigitalIO> IOcollection = ioModel.getIOs(DigitalIO.class);
		int IO_count = IOcollection.size();
		if(IO_count > 0){
			Iterator<DigitalIO> IO_itr = IOcollection.iterator();
			while(IO_itr.hasNext()){
				DigitalIO thisIO = IO_itr.next();
				String thisDefaultName = thisIO.getDefaultName();
				//System.out.println("Found an IO named "+thisDefaultName);
				if(thisDefaultName.equals(defaultName)){
					return thisIO;
				}
			}
		}
		return null;
	}
	
	public DigitalIO getDigitalIO(RobotIO io){
		Collection<DigitalIO> IOcollection = ioModel.getIOs(DigitalIO.class);
		int IO_count = IOcollection.size();
		if(IO_count > 0 && io != null){
			Iterator<DigitalIO> IO_itr = IOcollection.iterator();
			while(IO_itr.hasNext()){
				DigitalIO thisIO = IO_itr.next();
				String thisDefaultName = thisIO.getDefaultName();
				//System.out.println("Found an IO named "+thisDefaultName);
				if(thisDefaultName.equals(io.defaultName)){
					return thisIO;
				}
			}
		}
		return null;
	}
}
