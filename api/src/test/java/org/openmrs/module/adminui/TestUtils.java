/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui;

import org.apache.commons.beanutils.PropertyUtils;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.mockito.ArgumentMatcher;
import org.openmrs.util.OpenmrsUtil;

import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Various utils to help with testing
 */
public class TestUtils {

    /**
     * To test things like: assertContainsElementWithProperty(listOfPatients, "patientId", 2)
     *
     * @param collection
     * @param property
     * @param value
     */
    public static void assertContainsElementWithProperty(Collection<?> collection, String property, Object value) {
        for (Object o : collection) {
            try {
                if (OpenmrsUtil.nullSafeEquals(value, PropertyUtils.getProperty(o, property))) {
                    return;
                }
            } catch (Exception ex) {
                // pass
            }
        }
        Assert.fail("Collection does not contain an element with " + property + " = " + value + ". Collection: "
                + collection);
    }

    public static <T> ArgumentMatcher<T> isCollectionOfExactlyElementsWithProperties(final String property,
                                                                                     final Object... expectedPropertyValues) {
        return new ArgumentMatcher<T>() {

            @Override
            public boolean matches(Object o) {
                assertTrue(o instanceof Collection);
                Collection actual = (Collection) o;
                assertThat(actual.size(), is(expectedPropertyValues.length));
                for (Object expectedPropertyValue : expectedPropertyValues) {
                    assertContainsElementWithProperty(actual, property, expectedPropertyValue);
                }
                return true;
            }
        };
    }

    /**
     * Creates an argument matcher that tests equality based on the equals method, the developer
     * doesn't have to type cast the returned argument when pass it to
     * {@link org.mockito.Mockito#argThat(org.hamcrest.Matcher)} as it would be the case if we used
     * {@link org.mockito.internal.matchers.Equals} matcher
     *
     * @param object
     * @return Matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> equalsMatcher(final T object) {
        return new ArgumentMatcher<T>() {

            /**
             * @see org.mockito.ArgumentMatcher#matches(Object)
             */
            @Override
            public boolean matches(Object arg) {
                return OpenmrsUtil.nullSafeEquals(object, (T) arg);
            }
        };
    }
}
