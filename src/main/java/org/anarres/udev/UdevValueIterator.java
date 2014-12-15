/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import org.anarres.udev.generated.UdevLibrary;

/**
 *
 * @author shevek
 */
/* pp */ class UdevValueIterator extends UdevIterator<String> {

    /* pp */ UdevValueIterator(UdevLibrary library, UdevLibrary.udev_list_entry entry) {
        super(library, entry);
    }

    @Override
    protected String toEntry(UdevLibrary library, UdevLibrary.udev_list_entry entry) {
        return library.udev_list_entry_get_name(entry);
    }
}
