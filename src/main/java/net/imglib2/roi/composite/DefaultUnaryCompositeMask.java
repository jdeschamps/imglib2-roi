package net.imglib2.roi.composite;

import java.util.function.Predicate;

import net.imglib2.AbstractEuclideanSpace;
import net.imglib2.Localizable;
import net.imglib2.roi.BoundaryType;
import net.imglib2.roi.Mask;
import net.imglib2.roi.Operators.UnaryMaskOperator;

/**
 * A {@link Mask} which is the result of an operation on a {@link Predicate}.
 *
 * @author Tobias Pietzsch
 */
public class DefaultUnaryCompositeMask
		extends AbstractEuclideanSpace
		implements UnaryCompositeMaskPredicate< Localizable >, Mask
{
	private final UnaryMaskOperator operator;

	private final Predicate< ? super Localizable > arg0;

	private final BoundaryType boundaryType;

	private final Predicate< ? super Localizable > predicate;

	private final Predicate< Predicate< ? > > emptyOp;

	private final boolean isAll;

	public DefaultUnaryCompositeMask(
			final UnaryMaskOperator operator,
			final Predicate< ? super Localizable > arg0,
			final int numDimensions,
			final BoundaryType boundaryType,
			final Predicate< Predicate< ? > > emptyOp,
			final boolean isAll )
	{
		super( numDimensions );
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
	public boolean test( final Localizable localizable )
	{
		return predicate.test( localizable );
	}

	@Override
	public UnaryMaskOperator operator()
	{
		return operator;
	}

	@Override
	public Predicate< ? super Localizable > arg0()
	{
		return arg0;
	}

	@Override
	public boolean isEmpty()
	{
		return emptyOp.test( arg0 );
	}

	@Override
	public boolean isAll()
	{
		return isAll;
	}

	@Override
	public boolean equals( final Object obj )
	{
		if ( !( obj instanceof UnaryCompositeMaskPredicate ) || !( obj instanceof Mask ) )
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