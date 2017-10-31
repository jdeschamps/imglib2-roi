package net.imglib2.troi.composite;

import java.util.function.Predicate;

import net.imglib2.AbstractEuclideanSpace;
import net.imglib2.RealLocalizable;
import net.imglib2.troi.BoundaryType;
import net.imglib2.troi.Operators.UnaryMaskOperator;
import net.imglib2.troi.RealMask;

/**
 * A {@link RealMask} which is the result of an operation on a
 * {@link Predicate}.
 *
 * @author Tobias Pietzsch
 */
public class DefaultUnaryCompositeRealMask
		extends AbstractEuclideanSpace
		implements UnaryCompositeMaskPredicate< RealLocalizable >, RealMask
{
	private final UnaryMaskOperator operator;

	private final Predicate< ? super RealLocalizable > arg0;

	private final BoundaryType boundaryType;

	private final Predicate< ? super RealLocalizable > predicate;

	private final Predicate< Predicate< ? > > emptyOp;

	private final boolean isAll;

	public DefaultUnaryCompositeRealMask(
			final UnaryMaskOperator operator,
			final Predicate< ? super RealLocalizable > arg0,
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
		if ( !( obj instanceof UnaryCompositeMaskPredicate ) || !( obj instanceof RealMask ) )
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
