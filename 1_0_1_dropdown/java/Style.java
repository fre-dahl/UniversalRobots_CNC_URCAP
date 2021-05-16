package com.seal.student.thenewapp.impl;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;

public class Style {

	public static final Dimension PROGRAM_COMBOBOX_SIZE = new Dimension(300,30);
	public static final int PROGRAM_DEFAULT_VERTICAL_SPACE = 20;
	
	public static Component createVerticalSpace(int height) {
		return Box.createRigidArea(new Dimension(0,height));
	}
	
}
