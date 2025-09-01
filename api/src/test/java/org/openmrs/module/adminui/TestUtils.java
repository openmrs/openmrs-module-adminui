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
import org.openmrs.util.OpenmrsUtil;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Various utils to help with testing
 */
public class TestUtils {

    /**
     * To test things like: assertContainsElementWithProperty(listOfPatients, "patientId", 2)
     *
     * @param collection the collection to search
     * @param property the name of the bean property to evaluate
     * @param value the expected value of the property
     * @throws AssertionError if no matching element is found
     */
    public static void assertContainsElementWithProperty(Collection<?> collection, String property, Object value) {
        for (Object o : collection) {
            try {
                if (OpenmrsUtil.nullSafeEquals(value, PropertyUtils.getProperty(o, property))) {
                    return;
                }
            } catch (Exception ex) {
                // Skip this element
            }
        }
        fail("Collection does not contain an element with " + property + " = " + value + ". Collection: " + collection);
    }

    /**
     * Asserts that the given collection contains exactly the expected property values, with no extra or missing values.
     * The assertion passes if the collection contains the same number of elements as the number of expected values,
     * and each expected property value is found in at least one element of the collection.
     * This does not consider order or duplicates; it treats both actual and expected values as sets.
     *
     * @param collection the collection to check
     * @param property the bean property to evaluate for each element
     * @param expectedPropertyValues the expected values of the property across the collection
     * @throws AssertionError if the size does not match or any expected value is missing
     */
    public static void assertCollectionHasExactlyElementsWithProperty(Collection<?> collection, String property, Object... expectedPropertyValues) {
        assertNotNull(collection, "Collection is null");
        assertEquals(expectedPropertyValues.length, collection.size(), "Collection size does not match expected");

        for (Object expectedValue : expectedPropertyValues) {
            assertContainsElementWithProperty(collection, property, expectedValue);
        }
    }

}
