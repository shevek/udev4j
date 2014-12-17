/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
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
    public ImmutableMap<String, String> toMap() {
        ImmutableMap.Builder<String, String> out = ImmutableMap.builder();
        while (hasNext()) {
            UdevListEntry e = next();
            out.put(e.getKey(), e.getValue());
        }
        return out.build();
    }
}
