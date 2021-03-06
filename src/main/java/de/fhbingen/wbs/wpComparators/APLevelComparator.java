/*
 * The WBS-Tool is a project management tool combining the Work Breakdown
 * Structure and Earned Value Analysis Copyright (C) 2013 FH-Bingen This
 * program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your
 * option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY;; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package de.fhbingen.wbs.wpComparators;

import de.fhbingen.wbs.globals.Workpackage;

import java.util.Comparator;

/**
 * This comparator sorts the work packages after the level id.
 */
public class APLevelComparator implements Comparator<Workpackage> {
    /**
     * Compares to work packages after the level id.
     * @param ap1
     *            The first work package.
     * @param ap2
     *            The second work package which has to compare with the
     *            first one.
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    @Override
    public final int compare(final Workpackage ap1, final Workpackage ap2) {

        Integer[] ap1IDs = ap1.getLvlIDs();
        Integer[] ap2IDs = ap2.getLvlIDs();

        int position = 0;
        int levels = ap1IDs.length;

        while (ap1IDs[position].equals(ap2IDs[position])) {
            position++;
        }

        if (position + 1 < levels && ap1IDs[position + 1] == 0
            && ap2IDs[position + 1] == 0) {
            return ap1IDs[position].compareTo(ap2IDs[position]);
        } else {
            return ap1IDs[position].compareTo(ap2IDs[position]);
        }

    }

}
