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
package net.raumzeitfalle.registration.alignment;

import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.raumzeitfalle.registration.displacement.Displacement;

public final class TranslateToCenter implements BiFunction<Collection<Displacement>,Predicate<Displacement>, Collection<Displacement>> {

	private OptionalDouble meanx = OptionalDouble.empty();
	
	private OptionalDouble meany = OptionalDouble.empty();
	
	public Translate reverse() {
		return new Translate(meanx.orElse(0.0), meany.orElse(0.0));
	}
	
	@Override
	public Collection<Displacement> apply(Collection<Displacement> t, Predicate<Displacement> u) {
		
		double mx = Displacement.average(t, u, Displacement::getX);
    	double my = Displacement.average(t, u, Displacement::getY);
    
		List<Displacement> centered = t.stream()
									   .map(displacement -> displacement.moveBy(-mx, -my))
									   .collect(Collectors.toList());
		
		this.meanx = OptionalDouble.of(mx);
		this.meany = OptionalDouble.of(my);
		
		return centered;
	}

	public OptionalDouble getMeanx() {
		return meanx;
	}

	public OptionalDouble getMeany() {
		return meany;
	}

}
