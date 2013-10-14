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

package com.eviware.soapui.impl.rest.panels.request;

import com.eviware.soapui.impl.rest.RestRequestInterface;
import com.eviware.soapui.impl.rest.RestResource;
import com.eviware.soapui.impl.rest.actions.request.AddRestRequestToTestCaseAction;
import com.eviware.soapui.impl.rest.support.RestUtils;
import com.eviware.soapui.impl.wsdl.support.HelpUrls;
import com.eviware.soapui.impl.wsdl.teststeps.RestTestRequestInterface;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.action.swing.SwingActionDelegate;
import com.eviware.soapui.support.components.JXToolBar;
import com.jgoodies.forms.factories.ButtonBarFactory;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestRequestDesktopPanel extends
		AbstractRestRequestDesktopPanel<RestRequestInterface, RestRequestInterface>
{
	private final ResourceChangeListener resourceChangeListener;
	protected TextPanelWithTopLabel resourcePanel;
	protected TextPanelWithTopLabel queryPanel;
	private JButton addToTestCaseButton;

	public RestRequestDesktopPanel( RestRequestInterface modelItem )
	{
		super( modelItem, modelItem );
		resourceChangeListener = new ResourceChangeListener();
		modelItem.getResource().addPropertyChangeListener( RestResource.PATH_PROPERTY, resourceChangeListener );
	}

	@Override
	protected void initializeFields()
	{
		String path = getRequest().getResource().getFullPath();
		resourcePanel = new TextPanelWithTopLabel( "Resource", path );
		resourcePanel.textField.addMouseListener( new MouseAdapter()
		{

			@Override
			public void mouseClicked( MouseEvent e )
			{
				showdialog( getRequest().getResource() );
			}
		} );

		String query = RestUtils.getQueryParamsString( getRequest().getParams(), getRequest() );
		queryPanel = new TextPanelWithTopLabel( "Query", query, false );

	}

	@Override
	protected void init( RestRequestInterface request )
	{
		addToTestCaseButton = createActionButton( SwingActionDelegate.createDelegate(
				AddRestRequestToTestCaseAction.SOAPUI_ACTION_ID, getRequest(), null, "/addToTestCase.gif" ), true );

		super.init( request );
	}

	protected String getHelpUrl()
	{
		return HelpUrls.RESTREQUESTEDITOR_HELP_URL;
	}

	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		addToTestCaseButton.setEnabled( enabled );
	}

	@Override
	protected void addTopToolbarComponents( JXToolBar toolBar )
	{
		addResourceAndQueryField( toolBar );
	}

	@Override
	protected void addBottomToolbar( JPanel panel )
	{
		//RestRequestDesktopPanel does not need a bottom tool bar
	}

	@Override
	protected void updateUiValues()
	{
		resourcePanel.setText( getRequest().getResource().getFullPath() );
		resetQueryPanelText();

	}

	@Override
	protected void insertButtons( JXToolBar toolbar )
	{
		toolbar.add( addToTestCaseButton );
	}

	private void resetQueryPanelText()
	{
		queryPanel.setText( RestUtils.getQueryParamsString( getRequest().getParams(), getRequest() ) );

	}

	protected class TextPanelWithTopLabel extends JPanel
	{
		private final Color MAC_DISABLED_BGCOLOR = new Color( 232, 232, 232 );

		JLabel textLabel;
		JTextField textField;


		TextPanelWithTopLabel( String label, String text )
		{
			textLabel = new JLabel( label );
			textField = new JTextField( text );
			setToolTipText( text );
			super.setLayout( new BorderLayout() );
			super.add( textLabel, BorderLayout.NORTH );
			super.add( textField, BorderLayout.SOUTH );
		}

		public TextPanelWithTopLabel( String label, String text, boolean isEditable )
		{
			this( label, text );
			textField.setEditable( isEditable );
			if( !isEditable && UISupport.isMac() )
			{
				textField.setBackground( MAC_DISABLED_BGCOLOR );
			}
		}

		public TextPanelWithTopLabel( String label, String text, DocumentListener documentListener )
		{
			this( label, text );
			textField.getDocument().addDocumentListener( documentListener );
		}


		public String getText()
		{
			return textField.getText();
		}

		public void setText( String text )
		{
			textField.setText( text );
			setToolTipText( text );
		}

		@Override
		public void setToolTipText( String text )
		{
			super.setToolTipText( text );
			textLabel.setToolTipText( text );
			textField.setToolTipText( text );
		}
	}

	private void addResourceAndQueryField( JXToolBar toolbar )
	{
		if( !( getRequest() instanceof RestTestRequestInterface ) )
		{

			toolbar.addWithOnlyMinimumHeight( resourcePanel );

			toolbar.add( Box.createHorizontalStrut( 4 ) );


			toolbar.addWithOnlyMinimumHeight( queryPanel );
		}
	}

	protected void rebuildResourcePanelText()
	{
		if( resourcePanel != null )
		{
			resourcePanel.setText( getRequest().getResource().getFullPath() );
		}
	}

	private class ResourceChangeListener implements PropertyChangeListener
	{

		@Override
		public void propertyChange( PropertyChangeEvent evt )
		{
			rebuildResourcePanelText();
		}
	}

	@Override
	protected boolean release()
	{
		getRequest().getResource().removePropertyChangeListener( resourceChangeListener );
		return super.release();
	}

	private class RestResourceTextField
	{
		private RestResource restResource;
		private JTextField textField;
		private Integer affectedRequestCount;

		private RestResourceTextField( RestResource restResource )
		{
			this.restResource = restResource;
			textField = new JTextField( restResource.getPath() );
			textField.setMaximumSize( new Dimension( 150, ( int )textField.getPreferredSize().getHeight() ) );
			textField.setPreferredSize( new Dimension( 150, ( int )textField.getPreferredSize().getHeight() ) );
		}

		public JTextField getTextField()
		{
			return textField;
		}

		public RestResource getRestResource()
		{
			return restResource;
		}

		public int getAffectedRequestCount()
		{
			if( affectedRequestCount == null )
			{
				affectedRequestCount = restResource.getAllChildResources().length + 1;
			}
			return affectedRequestCount;
		}
	}

	private void showdialog( final RestResource resource )
	{
		final JDialog dialog = new JDialog( UISupport.getMainFrame(), "Resource Path" );
		dialog.setResizable( false );
		final JPanel panel = new JPanel( new BorderLayout() );

		RestResource r = resource;
		final List<RestResource> resources = new ArrayList<RestResource>();
		while( r != null )
		{
			resources.add( r );
			r = r.getParentResource();
		}
		Collections.reverse( resources );

		Box contentBox = Box.createVerticalBox();

		int index = 0;

		ImageIcon icon = UISupport.createImageIcon( "/hake.png" );

		final JLabel changeWarningLabel = new JLabel( " " );
		changeWarningLabel.setBorder( BorderFactory.createCompoundBorder(
				contentBox.getBorder(),
				BorderFactory.createEmptyBorder( 10, 0, 10, 0 ) ) );
		final List<RestResourceTextField> restResourceTextFields = new ArrayList<RestResourceTextField>();
		DocumentListener pathChangedListener = new DocumentListener()
		{
			@Override
			public void insertUpdate( DocumentEvent e )
			{
				update(e);
			}

			@Override
			public void removeUpdate( DocumentEvent e )
			{
				update(e);
			}

			@Override
			public void changedUpdate( DocumentEvent e )
			{
				update(e);
			}

			public void update( DocumentEvent e )
			{
				int affectedRequestCount = 0;
				for( RestResourceTextField restResourceTextField : restResourceTextFields )
				{
					if(!restResourceTextField.getTextField().getText().equals( restResourceTextField.getRestResource().getPath() )){
						affectedRequestCount = restResourceTextField.getAffectedRequestCount();
						break;
					}
				}
				if(affectedRequestCount > 0){
					changeWarningLabel.setText( String.format( "<html>Changes will affect: <b>%d</b> request%s</html>",
							affectedRequestCount, affectedRequestCount>1 ? "s" : "" ) );
					changeWarningLabel.setVisible( true );
				} else {
					changeWarningLabel.setVisible( false );
				}
			}
		};
		for( RestResource restResource : resources )
		{
			Box row = Box.createHorizontalBox();
			row.setAlignmentX( 0 );

			if( index > 1 )
			{
				row.add( Box.createHorizontalStrut( ( index - 1 ) * icon.getIconWidth() ) );
			}
			if( index >= 1 )
			{
				row.add( new JLabel( icon ) );
			}

			RestResourceTextField restResourceTextField = new RestResourceTextField( restResource );
			restResourceTextField.getTextField().getDocument().addDocumentListener( pathChangedListener );
			restResourceTextFields.add( restResourceTextField );

			Box textFieldBox = Box.createVerticalBox();
			textFieldBox.add( Box.createVerticalGlue() );
			textFieldBox.add( restResourceTextField.getTextField() );
			row.add( textFieldBox );

			contentBox.add( row );

			index++;
		}

		panel.add( contentBox, BorderLayout.NORTH );

		panel.add( changeWarningLabel, BorderLayout.CENTER );

		JButton okButton = new JButton( "OK" );
		JButton cancelButton = new JButton( "Cancel" );

		JPanel buttonBar = ButtonBarFactory.buildRightAlignedBar( okButton, cancelButton );

		panel.add( buttonBar, BorderLayout.SOUTH );
		panel.setBorder( BorderFactory.createCompoundBorder(
				panel.getBorder(),
				BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) ) );

		okButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				for( RestResourceTextField restResourceTextField : restResourceTextFields )
				{
					restResourceTextField.getRestResource().setPath( restResourceTextField.getTextField().getText() );
				}
			}
		} );
		cancelButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				dialog.setVisible( false );
			}
		} );
		dialog.getRootPane().setContentPane( panel );
		dialog.pack();
		UISupport.showDialog( dialog );
	}

}
