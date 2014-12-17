/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
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

        public void apply(@Nonnull UdevLibrary library, @Nonnull UdevLibrary.udev_enumerate enumerate);
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
    private UdevEnumeration withMatch(@Nonnull Match match) {
        matches.add(match);
        return this;
    }

    @Nonnull
    public UdevEnumeration withMatchProperty(@Nonnull final String name, @Nonnull final String value) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_property(enumerate, name, value);
            }
        });
    }

    /** A typographically safer version of {@link #withMatchProperty(String, String)}. */
    @Nonnull
    public UdevEnumeration withMatchProperty(@Nonnull final UdevProperty name, @Nonnull final String value) {
        return withMatchProperty(name.name(), value);
    }

    @Nonnull
    public UdevEnumeration withMatchSubsystem(@Nonnull final String name) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_subsystem(enumerate, name);
            }
        });
    }

    /** A typographically safer version of {@link #withMatchSubsystem(String)}. */
    @Nonnull
    public UdevEnumeration withMatchSubsystem(@Nonnull final UdevSubsystem name) {
        return withMatchSubsystem(name.name());
    }

    @Nonnull
    public UdevEnumeration withMatchSysattr(@Nonnull final String name, @Nonnull final String value) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_sysattr(enumerate, name, value);
            }
        });
    }

    @Nonnull
    public UdevEnumeration withMatchSysname(@Nonnull final String name) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_sysname(enumerate, name);
            }
        });
    }

    @Nonnull
    public UdevEnumeration withMatchTag(@Nonnull final String value) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_match_tag(enumerate, value);
            }
        });
    }

    @Nonnull
    public UdevEnumeration withNoMatchSubsystem(@Nonnull final String name) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_nomatch_subsystem(enumerate, name);
            }
        });
    }

    /** A typographically safer version of {@link #withNoMatchSubsystem(String)}. */
    @Nonnull
    public UdevEnumeration withNoMatchSubsystem(@Nonnull final UdevSubsystem name) {
        return withNoMatchSubsystem(name.name());
    }

    @Nonnull
    public UdevEnumeration withNoMatchSysattr(@Nonnull final String name, @Nonnull final String value) {
        return withMatch(new Match() {
            @Override
            public void apply(UdevLibrary library, UdevLibrary.udev_enumerate enumerate) {
                library.udev_enumerate_add_nomatch_sysattr(enumerate, name, value);
            }
        });
    }

    /**
     * Returns a list of syspaths matching the criteria of this enumeration.
     * 
     * This method encapsulates the entire native call-set for enumeration.
     * The returned list of names remain valid after the {@link Udev} object is closed.
     */
    @Nonnull
    public ImmutableList<String> toList() {
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
    public UnmodifiableIterator<String> iterator() {
        return toList().iterator();
    }
}
