/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2016 Tobias Pietzsch, Stephan Preibisch, Stephan Saalfeld,
 * John Bogovic, Albert Cardona, Barry DeZonia, Christian Dietz, Jan Funke,
 * Aivar Grislis, Jonathan Hale, Grant Harris, Stefan Helfrich, Mark Hiner,
 * Martin Horn, Steffen Jaensch, Lee Kamentsky, Larry Lindsey, Melissa Linkert,
 * Mark Longair, Brian Northan, Nick Perry, Curtis Rueden, Johannes Schindelin,
 * Jean-Yves Tinevez and Michael Zinsmaier.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package net.imglib2.roi.geom.real;

import net.imglib2.AbstractEuclideanSpace;
import net.imglib2.RealLocalizable;

/**
 * Abstract base class for {@link Sphere} implementations.
 *
 * @author Alison Walter
 */
public abstract class AbstractSphere extends AbstractEuclideanSpace implements Sphere
{
	protected double[] center;

	protected double radius;

	/**
	 * Creates an n-d sphere.
	 *
	 * @param center
	 *            Point where the sphere is centered
	 * @param radius
	 *            Radius of the sphere
	 */
	public AbstractSphere( final double[] center, final double radius )
	{
		super( center.length );

		if ( radius <= 0 )
			throw new IllegalArgumentException( "Radius must be positive and non-zero." );

		this.center = center.clone();
		this.radius = radius;
	}

	@Override
	public abstract boolean contains( RealLocalizable l );

	@Override
	public double exponent()
	{
		return 2;
	}

	@Override
	public double semiAxisLength( final int d )
	{
		return radius;
	}

	/** Returns a copy of center. */
	@Override
	public double[] center()
	{
		return center.clone();
	}

	@Override
	public double radius()
	{
		return radius;
	}

	/**
	 * Sets the radius of this sphere. The dimension, d, is ignored since
	 * spheres have the same extension in all dimensions.
	 */
	@Override
	public void setSemiAxisLength( final int d, final double length )
	{
		setRadius( length );
	}

	/**
	 * If the array has length > n, it will be truncated, if < n an exception is
	 * thrown.
	 */
	@Override
	public void setCenter( final double[] center )
	{
		System.arraycopy( center, 0, this.center, 0, n );
	}

	@Override
	public void setRadius( final double radius )
	{
		if ( radius <= 0 )
			throw new IllegalArgumentException( "Radius must be positive and non-zero." );
		this.radius = radius;
	}
}