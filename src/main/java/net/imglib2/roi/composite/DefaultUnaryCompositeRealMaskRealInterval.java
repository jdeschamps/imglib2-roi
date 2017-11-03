package net.imglib2.roi.composite;

import java.util.function.Predicate;

import net.imglib2.AbstractWrappedRealInterval;
import net.imglib2.RealInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.roi.BoundaryType;
import net.imglib2.roi.RealMaskRealInterval;
import net.imglib2.roi.Operators.UnaryMaskOperator;
import net.imglib2.util.Intervals;

/**
 * A {@link RealMaskRealInterval} which is the result of an operation on a
 * {@link Predicate}.
 *
 * @author Tobias Pietzsch
 */
public class DefaultUnaryCompositeRealMaskRealInterval
		extends AbstractWrappedRealInterval< RealInterval >
		implements UnaryCompositeMaskPredicate< RealLocalizable >, RealMaskRealInterval
{
	private final UnaryMaskOperator operator;

	private final Predicate< ? super RealLocalizable > arg0;

	private final BoundaryType boundaryType;

	private final Predicate< ? super RealLocalizable > predicate;

	private final Predicate< Predicate< ? > > emptyOp;

	private final boolean isAll;

	public DefaultUnaryCompositeRealMaskRealInterval(
			final UnaryMaskOperator operator,
			final Predicate< ? super RealLocalizable > arg0,
			final RealInterval interval,
			final BoundaryType boundaryType,
			final Predicate< Predicate< ? > > emptyOp,
			final boolean isAll )
	{
		super( interval );
		this.operator = operator;
		this.arg0 = arg0;
		this.boundaryType = boundaryType;
		this.predicate = operator.predicate( arg0 );
		this.emptyOp = emptyOp;
		this.isAll = isAll;
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

	@Override
	public UnaryMaskOperator operator()
	{
		return operator;
	}

	@Override
	public Predicate< ? super RealLocalizable > arg0()
	{
		return arg0;
	}

	@Override
	public boolean isEmpty()
	{
		return Intervals.isEmpty( sourceInterval ) || emptyOp.test( arg0 );
	}

	@Override
	public boolean isAll()
	{
		return isAll;
	}

	@Override
	public boolean equals( final Object obj )
	{
		if ( !( obj instanceof UnaryCompositeMaskPredicate ) || !( obj instanceof RealMaskRealInterval ) )
			return false;

		final UnaryCompositeMaskPredicate< ? > u = ( UnaryCompositeMaskPredicate< ? > ) obj;
		return u.operator() == operator && arg0.equals( u.arg0() );
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}
}