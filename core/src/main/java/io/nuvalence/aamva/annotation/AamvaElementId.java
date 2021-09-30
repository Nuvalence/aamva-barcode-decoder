package io.nuvalence.aamva.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AAMVA Driver License / Non-Driver ID Card Design Standard (CDS) element id(s).
 *
 * @see <a href="https://www.aamva.org/DL-ID-Card-Design-Standard/">AAMVA DL/ID Standards</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AamvaElementId {
    String[] value();
}
