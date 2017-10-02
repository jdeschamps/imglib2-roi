package net.imglib2.troi;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;

import org.scijava.ui.behaviour.DragBehaviour;
import org.scijava.ui.behaviour.io.InputTriggerConfig;
import org.scijava.ui.behaviour.io.InputTriggerDescription;
import org.scijava.ui.behaviour.util.Behaviours;

import bdv.util.AxisOrder;
import bdv.util.Bdv;
import bdv.util.BdvFunctions;
import bdv.util.BdvOverlay;
import bdv.util.BdvSource;
import bdv.viewer.ViewerPanel;
import bdv.viewer.animate.TextOverlayAnimator;
import bdv.viewer.animate.TextOverlayAnimator.TextPosition;
import net.imglib2.Interval;
import net.imglib2.RealInterval;
import net.imglib2.RealPoint;
import net.imglib2.RealRandomAccessibleRealInterval;
import net.imglib2.realtransform.AffineTransform2D;
import net.imglib2.troi.Bounds.AbstractIntervalOrEmpty;
import net.imglib2.troi.geom.real.ClosedSphere;
import net.imglib2.troi.geom.real.Sphere;
import net.imglib2.troi.util.TODO_Intervals;
import net.imglib2.type.logic.BoolType;
import net.imglib2.type.numeric.ARGBType;
import net.imglib2.util.LinAlgHelpers;

public class ModifyDemo
{
	public static void main( final String[] args )
	{
		// Create 3 spheres
		final Sphere s1 = new ClosedSphere( new double[] { 0, 0, 0 }, 3.5 );
		final Sphere s2 = new ClosedSphere( new double[] { 1, 2, 0 }, 1.5 );
		final Sphere s3 = new ClosedSphere( new double[] { 2, 2, 0 }, 1.5 );

		// Combine the spheres using operators
		final RealMaskRealInterval composite = s1.and( s2 ).and( s3 );

		final InputTriggerConfig config = new InputTriggerConfig( Arrays.asList( new InputTriggerDescription( new String[] {"not mapped"}, "2d drag rotate", "bdv" ) ) );

		// Wrap the intersection as a RealRandomAccessibleRealInterval
		final RealRandomAccessibleRealInterval< BoolType > s1rrai = Masks.toRRARI( s1 );
		final BdvSource bdv1 = BdvFunctions.show(
				s1rrai,
				new WrappedContainingInterval( s1rrai ),
				"Sphere 1",
				Bdv.options().is2D().axisOrder( AxisOrder.XYZ ).inputTriggerConfig( config ) );
		final RealRandomAccessibleRealInterval< BoolType > s2rrai = Masks.toRRARI( s2 );
		final BdvSource bdv2 = BdvFunctions.show(
				s2rrai,
				new WrappedContainingInterval( s2rrai ),
				"Sphere 2",
				Bdv.options().addTo( bdv1 ).axisOrder( AxisOrder.XYZ ) );
		final RealRandomAccessibleRealInterval< BoolType > s3rrai = Masks.toRRARI( s3 );
		final BdvSource bdv3 = BdvFunctions.show(
				s3rrai,
				new WrappedContainingInterval( s3rrai ),
				"Sphere 3",
				Bdv.options().addTo( bdv1 ).axisOrder( AxisOrder.XYZ ) );
		final RealRandomAccessibleRealInterval< BoolType > compositerrai = Masks.toRRARI( composite );
		final BdvSource bdv4 = BdvFunctions.show(
				compositerrai,
				new WrappedContainingInterval( compositerrai ),
				"composite",
				Bdv.options().addTo( bdv1 ).axisOrder( AxisOrder.XYZ ) );

		final ViewerPanel viewer = bdv1.getBdvHandle().getViewerPanel();
		BdvFunctions.showOverlay( new BdvOverlay()
		{
			@Override
			protected void draw( final Graphics2D g )
			{
				g.setColor( Color.RED );
				if ( TODO_Intervals.isEmpty( compositerrai ) )
					g.drawString( "composite interval is empty", 400, 300 );
				else
				{
					final AffineTransform2D t = new AffineTransform2D();
					this.getCurrentTransform2D( t );
					final double[] min = new double[ 2 ];
					final double[] max = new double[ 2 ];
					t.apply( new double[] { composite.realMin( 0 ), composite.realMin( 1 ) }, min );
					t.apply( new double[] { composite.realMax( 0 ), composite.realMax( 1 ) }, max );
					final int x = ( int ) min[ 0 ];
					final int y = ( int ) min[ 1 ];
					final int width = ( int ) max[ 0 ] - x;
					final int height = ( int ) max[ 1 ] - y;
					g.drawRect( x, y, width, height );
				}
			}
		}, "composite interval", Bdv.options().addTo( bdv1 ) );

		bdv1.setColor( new ARGBType( 0x440000 ) );
		bdv2.setColor( new ARGBType( 0x004400 ) );
		bdv3.setColor( new ARGBType( 0x000044 ) );
		bdv4.setColor( new ARGBType( 0xffff00 ) );

		final Behaviours behaviours = new Behaviours( new InputTriggerConfig() );
		behaviours.behaviour( new DragBehaviour()
		{
			Sphere s = null;

			final double[] offset = new double[ 3 ];

			@Override
			public void init( final int x, final int y )
			{
				final double[] pos = getPos( x, y );
				if ( s1.test( RealPoint.wrap( pos ) ) )
					s = s1;
				else if ( s2.test( RealPoint.wrap( pos ) ) )
					s = s2;
				else if ( s3.test( RealPoint.wrap( pos ) ) )
					s = s3;
				else
					s = null;

				if ( s != null )
					LinAlgHelpers.subtract( s.center(), pos, offset );
			}

			@Override
			public void end( final int x, final int y )
			{
				s = null;
			}

			@Override
			public void drag( final int x, final int y )
			{
				if ( s != null )
				{
					final double[] center = new double[ 3 ];
					LinAlgHelpers.add( getPos( x, y ), offset, center );
					s.setCenter( center );
					viewer.requestRepaint();
				}
			}

			double[] getPos( final int x, final int y )
			{
				final double[] pos = new double[ 3 ];
				viewer.displayToGlobalCoordinates( x, y, RealPoint.wrap( pos ) );
				return pos;
			}

		}, "move object", "button1" );
		behaviours.install( bdv1.getBdvHandle().getTriggerbindings(), "demo" );

		viewer.addOverlayAnimator( new TextOverlayAnimator( "left click + drag to move objects", 8000, TextPosition.CENTER ) );
	}

	static class WrappedContainingInterval extends AbstractIntervalOrEmpty implements Interval
	{
		private final RealInterval source;

		public WrappedContainingInterval( final RealInterval source )
		{
			super( source.numDimensions() );
			this.source = source;
		}

		@Override
		public long min( final int d )
		{
			return ( long ) Math.floor( source.realMin( d ) );
		}

		@Override
		public long max( final int d )
		{
			return ( long ) Math.ceil( source.realMax( d ) );
		}
	}
}