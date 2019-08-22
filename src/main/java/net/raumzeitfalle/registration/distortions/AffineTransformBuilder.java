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
package net.raumzeitfalle.registration.distortions;

public class AffineTransformBuilder {
	
	private final double cx;
	private final double cy;
	
	private final double tx;
	private final double ty;
	
	private double scaleX;
	private double scaleY;
	private double magnification;
	
	private double orthoX;
	private double orthoY;
	
	public AffineTransformBuilder(AffineTransform firstOrder) {
		 this.scaleX = firstOrder.getScaleX();
	     this.scaleY = firstOrder.getScaleY();
	     this.magnification = firstOrder.getMagnification();
	     
	     this.orthoX = firstOrder.getOrthoX();
	     this.orthoY = firstOrder.getOrthoY();
	     
	     this.tx = firstOrder.getTranslationX();
	     this.ty = firstOrder.getTranslationY();
	     
	     this.cx = firstOrder.getCenterX();
	     this.cy = firstOrder.getCenterY();
	}

	/**
	 * Creates the final {@link AffineTransform} function. In cases where ortho and
	 * scale parameters have been set to zero, a {@link SkipAffineTransform} is
	 * returned.
	 * 
	 * @return {@link AffineTransform} with desired settings
	 */
	public AffineTransform build() {
		if (scaleX == 0.0 && scaleY == 0.0 && orthoX == 0.0 && orthoY == 0.0) {
			return new SkipAffineTransform();
		}
		return new AffineTransform(tx, ty, scaleX, scaleY, orthoX, orthoY, cx, cy);
	}

	public AffineTransformBuilder disableOrthoXY() {
		this.orthoX = 0.0;
		this.orthoY = 0.0;
		return this;
	}

	public AffineTransformBuilder useMagnification() {
		this.scaleX = this.magnification;
		this.scaleY = this.magnification;
		return this;
	}

	public AffineTransformBuilder disableScaleXY() {
		this.scaleX = 0.0;
		this.scaleY = 0.0;
		return this;
	}

	public AffineTransformBuilder disableAll() {
		
		this.scaleX = 0.0;
		this.scaleY = 0.0;
		
		this.orthoX = 0.0;
		this.orthoY = 0.0;
		
		return this;
	}

}
