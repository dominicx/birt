/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.util.ColorUtil;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Line border for Label, Text and Data element.
 */

public class LineBorder extends BaseBorder
{

	private static final Insets DEFAULT_CROP = new Insets( 0, 0, 1, 1 );

	private Insets paddingInsets = new Insets( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets( IFigure figure )
	{
		return getTrueBorderInsets( ).add( paddingInsets );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder#getBorderInsets()
	 */
	public Insets getBorderInsets( )
	{
		return getTrueBorderInsets( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder#getTrueBorderInsets()
	 */
	protected Insets getTrueBorderInsets( )
	{
		int t = 1, b = 1, l = 1, r = 1;

		int style = 0;

		style = getBorderStyle( bottomStyle );
		if ( style != 0 )
		{
			b = getBorderWidth( bottomWidth );
		}

		style = getBorderStyle( topStyle );
		if ( style != 0 )
		{
			t = getBorderWidth( topWidth );
		}

		style = getBorderStyle( leftStyle );
		if ( style != 0 )
		{
			l = getBorderWidth( leftWidth );
		}

		style = getBorderStyle( rightStyle );
		if ( style != 0 )
		{
			r = getBorderWidth( rightWidth );
		}

		return new Insets( t, l, b, r );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder#setPaddingInsets(org.eclipse.draw2d.geometry.Insets)
	 */
	public void setPaddingInsets( Insets padding )
	{
		if ( padding != null )
		{
			if ( padding.top >= 0 )
			{
				paddingInsets.top = padding.top;
			}
			if ( padding.bottom >= 0 )
			{
				paddingInsets.bottom = padding.bottom;
			}
			if ( padding.left >= 0 )
			{
				paddingInsets.left = padding.left;
			}
			if ( padding.right >= 0 )
			{
				paddingInsets.right = padding.right;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure,
	 *      org.eclipse.draw2d.Graphics, org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint( IFigure figure, Graphics g, Insets insets )
	{
		i_bottom_style = getBorderStyle( bottomStyle );
		i_bottom_width = getBorderWidth( bottomWidth );

		i_top_style = getBorderStyle( topStyle );
		i_top_width = getBorderWidth( topWidth );

		i_left_style = getBorderStyle( leftStyle );
		i_left_width = getBorderWidth( leftWidth );

		i_right_style = getBorderStyle( rightStyle );
		i_right_width = getBorderWidth( rightWidth );

		g.restoreState( );

		//draw bottom line
		drawBorder( figure, g, BOTTOM, i_bottom_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, bottomColor, insets );

		//draw top line
		drawBorder( figure, g, TOP, i_top_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, topColor, insets );

		//draw left line
		drawBorder( figure, g, LEFT, i_left_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, leftColor, insets );

		//draw right line
		drawBorder( figure, g, RIGHT, i_right_style, new int[]{
				i_top_width, i_bottom_width, i_left_width, i_right_width
		}, rightColor, insets );
	}

	/**
	 * @param figure
	 * @param g
	 * @param side
	 * @param style
	 * @param width
	 *            the border width array, arranged by {top, bottom, left,
	 *            right};
	 * @param color
	 * @param insets
	 */
	protected void drawBorder( IFigure figure, Graphics g, int side, int style,
			int[] width, String color, Insets insets )
	{
		Rectangle r = figure.getBounds( )
				.getCopy( )
				.crop( DEFAULT_CROP )
				.crop( insets );

		if ( style != 0 )
		{
			//set ForegroundColor with the given color
			g.setForegroundColor( ColorManager.getColor( ColorUtil.parseColor( color ) ) );

			//if the border style is set to "double",
			//draw a double line with the given width and style of "solid"
			if ( style == -2 )
			{
				drawDoubleLine( figure, g, side, width, r );
			}
			// if the border style is set to "solid", "dotted" or "dashed",
			//draw a single line according to the give style and width
			else
			{
				drawSingleLine( figure, g, side, style, width, r );
			}
		}
		else
		{
			g.setForegroundColor( ReportColorConstants.ShadowLineColor );
			//if the border style is set to none, draw a default dot line in
			// black as default
			drawDefaultLine( figure, g, side, r );
		}

		g.restoreState( );
	}

}