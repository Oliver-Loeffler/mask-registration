/*-
 * #%L
 * Image-Registration
 * %%
 * Copyright (C) 2019 Oliver Loeffler, Raumzeitfalle.net
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package net.raumzeitfalle.registration.displacement;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/**
 * Binds all details together, which are needed to calculate image registration for a single location. {@link Displacement} class is a data class. 
 * <p>
 * Each displacement is bound to a specific location on a mask (x,y).<br>
 * The measured position of the structure is specified by (x<sub>d</sub>, y<sub>d</sub>).<br>
 * The differences between (x,y)<sub>d</sub> and (x,y) are calculated when a {@link Displacement} object is created.<br>
 * <p>
 * Depending of the location on a mask, a geometrical feature has a specific task. To distinguish between different features, the {@link DisplacementClass} enum category type is used. 
 * 
 * @author Oliver Loeffler
 *
 */
public class Displacement {

	/**
	 * Creates a new {@link Displacement} object based on a given source object.
	 * The actual displacement values (x<sub>d</sub>, y<sub>d</sub>) will be updated accordingly.
	 * 
	 * @param source {@link Displacement}
	 * @param xd - new displacement value for X-axis (design plus deviation)
	 * @param yd - new displacement value for Y-axis (design plus deviation)
	 * 
	 * @return {@link Displacement}
	 */
	public static Displacement from(Displacement source, double xd, double yd) {
		return new Displacement(source.index,source.id, source.x, source.y, xd, yd, source.siteClass);
	}
	
	/**
	 * Creates a new {@link Displacement} object from scratch, belonging to DisplacementClass REG.
	 * 
	 * @param index Usually a consecutive number (e.g. from a table) reflecting the order of {@link Displacement} object creation.
	 * @param id An arbitrary ID value which can be assigned.
	 * @param x Given design (reference) location (X-direction) for a displacement result.
	 * @param y Given design (reference) location (Y-direction) for a displacement result.
	 * @param xd displaced (measured) X-coordinate
	 * @param yd displaced (measured) Y-coordinate
	 * 
	 * @return {@link Displacement}
	 */
	public static Displacement at(int index, int id, double x, double y, double xd, double yd) {
		return at(index,id, x, y, xd, yd, DisplacementClass.REG);
	}
	
	/**
	 * Creates a new {@link Displacement} object from scratch.
	 * 
	 * @param index Usually a consecutive number (e.g. from a table) reflecting the order of {@link Displacement} object creation.
	 * @param id An arbitrary ID value which can be assigned.
	 * @param x Actual design location (X-direction) for a displacement result.
	 * @param y Actual design location (Y-direction) for a displacement result.
	 * @param xd displaced (measured) X-coordinate
	 * @param yd displaced (measured) Y-coordinate
	 * @param type Category type to distinguish different {@link Displacement} groups.
	 * 
	 * @return {@link Displacement}
	 */
	public static Displacement at(int index, int id, double x, double y, double xd, double yd, DisplacementClass type) {
		return new Displacement(index,id, x, y, xd, yd, type);
	}
	
	/**
	 * Creates a statistical summary for the given collection of {@link Displacement} instances.
	 * For both axes (X,Y) descriptive statistics such as average (mean), min, max, 3Sigma etc. are provided.
	 * 
	 * @param t Given {@link Collection}ollection of {@link Displacement} items.
	 * @param calculationSelection {@link Predicate} for {@link Displacement} which determines, which Displacements will be considered for summary creation.
	 * @return {@link DisplacementSummary} Table with descriptive statistics of positionals, alignment details (translation, rotation) and and first order systematics (scaling and shear aka. non-orthogonality).
	 */
	public static DisplacementSummary summarize(Collection<Displacement> t, Predicate<Displacement> calculationSelection) {
		return DisplacementSummary.over(t, calculationSelection);
	}
	
	/**
	 * Creates the average value for a given double property of a {@link Displacement} while considering only Displacements which match the filter {@link Predicate}.
	 *
	 * @param t Given {@link Collection}ollection of {@link Displacement} items.
	 * @param filter {@link Predicate} to {@link Displacement} which determines which Displacements will be considered for averaging.
	 * @param mapper {@link ToDoubleFunction} which extracts a double value from a {@link Displacement} 
	 * @return double 
	 */
	public static double average(Collection<Displacement> t, Predicate<Displacement> filter, ToDoubleFunction<Displacement> mapper) {
		return t.stream()
				.filter(filter)
				.mapToDouble(mapper)
				.filter(Double::isFinite)
				.average()
				.orElse(Double.NaN);
	}
	
	private final int index;
	
	private final int id;
	
	private final double x;
	private final double y;
	private final double xd;
	private final double yd;
	
	private final double dx;
	private final double dy;
	
	private final DisplacementClass siteClass;
	
	private Displacement(int index, int id, double x, double y, double xd, double yd, DisplacementClass type) {
		this.index = index;
		this.id = id;
		
		this.x = x;
		this.y = y;
		this.xd = xd;
		this.yd = yd;
		
		this.dx = xd -x;
		this.dy = yd -y;
		
		this.siteClass = Objects.requireNonNull(type, "type must not be null");
	}
	
	/**
	 * Moves a displacement to a new location by adding an offset<sub>X</sub> and offset<sub>Y</sub> to given design coordinates (x,y) and to displaced coordinates (xd,yd).
	 * 
	 * @param offsetX Offset for X direction, will be applied to x, xd.
	 * @param offsetY Offset for Y direction, will be applied to y, yd.
	 * 
     * @return Displacement where: 
     * <ul>
     * 	<li>x<sub>moved</sub> = x<sub>old</sub> + offsetX 
     *  <li>y<sub>moved</sub> = y<sub>old</sub> + offsetY
     *  <li>xd<sub>moved</sub> = xd<sub>old</sub> + offsetX 
     *  <li>yd<sub>moved</sub> = yd<sub>old</sub> + offsetY
     * </ul>
	 */
	public Displacement moveBy(double offsetX, double offsetY) {
		return new Displacement(index, id, offsetX + x, offsetY + y, offsetX + xd, offsetY + yd, siteClass);
	}
	
	/**
	 * Corrects (xd,yd) by subtracting the corresponding (dx,dy) whereas the design coordinates (x,y) remain unmodified.
	 * 
	 * @param dx Difference in X direction to be subtracted from xd.
	 * @param dy Difference in Y direction to be subtracted from yd.
	 * @return Displacement where: 
	 * <ul>
	 * 	<li>xd<sub>corrected</sub> = xd<sub>old</sub> - dx
	 *  <li>yd<sub>corrected</sub> = yd<sub>old</sub> - dy
	 * </ul>
	 */
	public Displacement correctBy(double dx, double dy) {
		return new Displacement(index, id, x, y, xd - dx, yd - dy, siteClass);
	}

	public int getIndex() {
		return index;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * Provides the X component of design coordinate.
	 * @return double x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Provides the Y component of design coordinate.
	 * @return double y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Provides the X component of displaced (measured) coordinate.
	 * @return double xd
	 */
	public double getXd() {
		return xd;
	}

	/**
	 * Provides the Y component of displaced (measured) coordinate.
	 * @return double yd
	 */
	public double getYd() {
		return yd;
	}
	
	/**
	 * Difference dx = xd - x 	
	 * @return double dx
	 */
	public double dX() {
		return dx;
	}

	/**
	 * Difference dy = yd - y 	
	 * @return double dy
	 */
	public double dY() {
		return dy;
	}

	/**
	 * Provides a category description for each {@link Displacement}. 
	 * @return {@link DisplacementClass}
	 */
	public DisplacementClass getType() {
		return siteClass;
	}

	@Override
	public String toString() {
		return "Displacement [type="+siteClass.name()+ " id=" + id + " x=" + x + ", y=" + y + ", xd=" + xd + ", yd=" + yd + ", "
				+ System.lineSeparator() + "\t\tdx=" + dx + ", dy=" + dy + "]";
	}

	/**
	 * Verifies if this {@link Displacement} belongs to a specific {@link DisplacementClass}.
	 * @param other {@link DisplacementClass} to be tested for
	 * @return true, when this {@link Displacement} belongs to the provided {@link DisplacementClass}.
	 */
	public boolean isOfType(DisplacementClass other) {
		return this.siteClass.equals(other);
	}
}
