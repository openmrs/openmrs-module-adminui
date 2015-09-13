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

import java.util.Comparator;

import org.openmrs.Retireable;

public class ByRetiredComparator implements Comparator<Retireable> {
	
	@Override
	public int compare(Retireable r1, Retireable r2) {
		if (r2.isRetired() != null && r2.isRetired()) {
			return -1;
		} else if (r1.isRetired() != null && r1.isRetired()) {
			return 1;
		}
		return 0;
	}
}
