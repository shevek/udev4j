/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import org.anarres.udev.generated.UdevLibrary;

/**
 * An enumeration builder.
 *
 * The enumeration is not executed until {@link #toList()} or
 * {@link #iterator()} is called. After iteration, more conditions may be
 * added and either method may be called again.
 *
 * This object is NOT valid after the associated {@link Udev} object is closed.
 *
 * @author shevek
 */
@NotThreadSafe
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

    @Nonnull
    public UdevEnumeration withMatchProperty(final String name, final String value) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_property(enumerate, name, value);
            }
        });
    }

    /** A typographically safer version of {@link #withMatchProperty(String, String)}. */
    @Nonnull
    public UdevEnumeration withMatchProperty(final UdevProperty name, final String value) {
        return withMatchProperty(name.name(), value);
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

    /** A typographically safer version of {@link #withMatchSubsystem(String)}. */
    @Nonnull
    public UdevEnumeration withMatchSubsystem(final UdevSubsystem name) {
        return withMatchSubsystem(name.name());
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

    /** A typographically safer version of {@link #withNoMatchSubsystem(String)}. */
    @Nonnull
    public UdevEnumeration withNoMatchSubsystem(final UdevSubsystem name) {
        return withNoMatchSubsystem(name.name());
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
