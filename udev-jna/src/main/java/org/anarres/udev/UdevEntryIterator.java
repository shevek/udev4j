/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import org.anarres.udev.generated.UdevLibrary;

/**
 *
 * @author shevek
 */
/* pp */ class UdevEntryIterator extends UdevIterator<UdevListEntry> {

    /* pp */ UdevEntryIterator(UdevLibrary library, UdevLibrary.udev_list_entry entry) {
        super(library, entry);
    }

    @Override
    protected UdevListEntry toEntry(UdevLibrary library, UdevLibrary.udev_list_entry entry) {
        String key = library.udev_list_entry_get_name(entry);
        String value = library.udev_list_entry_get_value(entry);
        return new UdevListEntry(key, value);
    }

    @Nonnull
    public Map<String, String> toMap() {
        Map<String, String> out = new LinkedHashMap<String, String>();
        while (hasNext()) {
            UdevListEntry e = next();
            out.put(e.getKey(), e.getValue());
        }
        return out;
    }
}
