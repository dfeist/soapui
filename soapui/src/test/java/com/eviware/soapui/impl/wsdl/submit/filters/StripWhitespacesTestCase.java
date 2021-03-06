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

package com.eviware.soapui.impl.wsdl.submit.filters;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.eviware.soapui.support.xml.XmlUtils;

public class StripWhitespacesTestCase
{
	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter( StripWhitespacesTestCase.class );
	}

	@Test
	public void shouldStripWhitespaces() throws Exception
	{
		assertEquals( "<content/>", XmlUtils.stripWhitespaces( "<content>   </content>" ) );
		assertEquals( "<content><test>bil</test></content>",
				XmlUtils.stripWhitespaces( "<content>  <test>  bil   </test>   </content>" ) );
	}
}
