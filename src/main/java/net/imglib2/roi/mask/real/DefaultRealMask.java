package net.imglib2.roi.mask.real;

import java.util.function.Predicate;

import net.imglib2.AbstractEuclideanSpace;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.BoundaryType;
import net.imglib2.roi.RealMask;

/**
 * @author Tobias Pietzsch
 */
public class DefaultRealMask extends AbstractEuclideanSpace implements RealMask
{
	private final BoundaryType boundaryType;

	private final Predicate< ? super RealLocalizable > predicate;

	/**
	 * @param n
	 * 		number of dimensions.
	 */
	public DefaultRealMask(
			final int n,
			final BoundaryType boundaryType,
			final Predicate< ? super RealLocalizable > predicate )
	{
		super( n );
		this.boundaryType = boundaryType;
		this.predicate = predicate;
	}

	@Override
	public BoundaryType boundaryType()
	{
		return boundaryType;
	}

	@Override
	public boolean test( final RealLocalizable localizable )
	{
		return predicate.test( localizable );
	}
}