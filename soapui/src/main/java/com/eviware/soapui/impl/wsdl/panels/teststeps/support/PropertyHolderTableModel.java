/*
 *  soapUI, copyright (C) 2004-2012 smartbear.com 
 *
 *  soapUI is free software; you can redistribute it and/or modify it under the 
 *  terms of version 2.1 of the GNU Lesser General Public License as published by 
 *  the Free Software Foundation.
 *
 *  soapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */

package com.eviware.soapui.impl.wsdl.panels.teststeps.support;

import javax.swing.table.TableModel;

import com.eviware.soapui.model.testsuite.TestProperty;

public interface PropertyHolderTableModel extends TableModel
{
	public abstract void fireTableDataChanged();

	public abstract TestProperty getPropertyAtRow( int rowIndex );

	public abstract void fireTableRowsDeleted( int row, int row2 );

	public abstract void fireTableRowsInserted( int row, int row2 );
}
