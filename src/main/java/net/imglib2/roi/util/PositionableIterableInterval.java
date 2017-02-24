package net.imglib2.roi.util;

import java.util.Iterator;

import net.imglib2.Cursor;
import net.imglib2.IterableInterval;
import net.imglib2.Sampler;

public class PositionableIterableInterval extends AbstractPositionableInterval implements IterableInterval< Void >
{
	private final IterableInterval< Void > source;

	public PositionableIterableInterval( final IterableInterval< Void > source )
	{
		super( source );
		this.source = source;
	}

	protected PositionableIterableInterval( final PositionableIterableInterval other )
	{
		super( other );
		this.source = other.source;
	}

	@Override
	public long size()
	{
		return source.size();
	}

	@Override
	public Void firstElement()
	{
		return source.firstElement();
	}

	@Override
	public Object iterationOrder()
	{
		return this;
	}

	@Override
	public Iterator< Void > iterator()
	{
		return cursor();
	}

	@Override
	public Cursor< Void > cursor()
	{
		return new PositionableIterableIntervalCursor( source.cursor() );
	}

	@Override
	public Cursor< Void > localizingCursor()
	{
		return new PositionableIterableIntervalCursor( source.localizingCursor() );
	}

	class PositionableIterableIntervalCursor implements Cursor< Void >
	{
		private final Cursor< Void > cursor;

		public PositionableIterableIntervalCursor( final Cursor< Void > cursor )
		{
			this.cursor = cursor;
		}

		@Override
		public Void get()
		{
			return null;
		}

		@Override
		public Sampler< Void > copy()
		{
			return new PositionableIterableIntervalCursor( cursor.copyCursor() );
		}

		@Override
		public void jumpFwd( final long steps )
		{
			cursor.jumpFwd( steps );
		}

		@Override
		public void fwd()
		{
			cursor.fwd();
		}

		@Override
		public void reset()
		{
			cursor.reset();
		}

		@Override
		public boolean hasNext()
		{
			return cursor.hasNext();
		}

		@Override
		public Void next()
		{
			return cursor.next();
		}

		@Override
		public Cursor< Void > copyCursor()
		{
			return cursor.copyCursor();
		}

		@Override
		public void localize( final float[] position )
		{
			for ( int i = 0; i < position.length; i++ )
			{
				position[ i ] = cursor.getFloatPosition( i ) + currentOffset[ i ];
			}
		}

		@Override
		public void localize( final double[] position )
		{
			for ( int i = 0; i < position.length; i++ )
			{
				position[ i ] = cursor.getDoublePosition( i ) + currentOffset[ i ];
			}
		}

		@Override
		public float getFloatPosition( final int d )
		{
			return cursor.getFloatPosition( d ) + currentOffset[ d ];
		}

		@Override
		public double getDoublePosition( final int d )
		{
			return cursor.getDoublePosition( d ) + currentOffset[ d ];
		}

		@Override
		public int numDimensions()
		{
			return cursor.numDimensions();
		}

		@Override
		public void localize( final int[] position )
		{
			for ( int i = 0; i < position.length; i++ )
			{
				position[ i ] = ( int ) ( cursor.getIntPosition( i ) + currentOffset[ i ] );
			}
		}

		@Override
		public void localize( final long[] position )
		{
			for ( int i = 0; i < position.length; i++ )
			{
				position[ i ] = cursor.getLongPosition( i ) + currentOffset[ i ];
			}
		}

		@Override
		public int getIntPosition( final int d )
		{
			return ( int ) ( cursor.getIntPosition( d ) + currentOffset[ d ] );
		}

		@Override
		public long getLongPosition( final int d )
		{
			return cursor.getLongPosition( d ) + currentOffset[ d ];
		}
	}

	public PositionableIterableInterval copy()
	{
		return new PositionableIterableInterval( this );
	}
}