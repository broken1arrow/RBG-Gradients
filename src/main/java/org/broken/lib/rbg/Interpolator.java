package org.broken.lib.rbg;

@FunctionalInterface
public interface Interpolator {

	double[] interpolate(double from, double to, int max);

}