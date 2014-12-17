/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import org.anarres.udev.generated.UdevLibrary;

/**
 *
 * @author shevek
 */
public class UdevEnumeration implements Iterable<String> {

    private static interface Match {

        public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate);
    }
    private final Udev udev;
    private final List<Match> matches = new ArrayList<Match>();

    /* pp */ UdevEnumeration(@Nonnull Udev udev) {
        this.udev = udev;
    }

    @Nonnull
    /* pp */ UdevLibrary getLibrary() {
        return getUdev().getLibrary();
    }

    @Nonnull
    /* pp */ Udev getUdev() {
        return udev;
    }

    @Nonnull
    private UdevEnumeration withMatch(Match match) {
        matches.add(match);
        return this;
    }

    // property subsystem sysattr sysname tag
    @Nonnull
    public UdevEnumeration withMatchProperty(final String name, final String value) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_property(enumerate, name, value);
            }
        });
    }

    @Nonnull
    public UdevEnumeration withMatchSubsystem(final String name) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_subsystem(enumerate, name);
            }
        });
    }

    @Nonnull
    public UdevEnumeration withMatchSysattr(final String name, final String value) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_sysattr(enumerate, name, value);
            }
        });
    }

    @Nonnull
    public UdevEnumeration withMatchSysname(final String name) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_sysname(enumerate, name);
            }
        });
    }

    @Nonnull
    public UdevEnumeration withMatchTag(final String value) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_tag(enumerate, value);
            }
        });
    }

    @Nonnull
    public UdevEnumeration withNoMatchSubsystem(final String name) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_nomatch_subsystem(enumerate, name);
            }
        });
    }

    @Nonnull
    public UdevEnumeration withNoMatchSysattr(final String name, final String value) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_nomatch_sysattr(enumerate, name, value);
            }
        });
    }

    @Nonnull
    public List<String> toList() {
        UdevLibrary library = udev.getLibrary();
        UdevLibrary.udev_enumerate enumerate = library.udev_enumerate_new(udev.getHandle());
        try {
            for (Match match : matches)
                match.apply(library, enumerate);
            library.udev_enumerate_scan_devices(enumerate);
            UdevLibrary.udev_list_entry entry = library.udev_enumerate_get_list_entry(enumerate);
            return new UdevValueIterator(library, entry).toList();
        } finally {
            library.udev_enumerate_unref(enumerate);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return toList().iterator();
    }
}