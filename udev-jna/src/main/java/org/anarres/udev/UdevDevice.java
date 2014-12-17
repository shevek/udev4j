/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.udev.generated.UdevLibrary;

/**
 * A device.
 *
 * This is a pure value-object, and remains valid after the associated
 * {@link Udev} object is closed.
 *
 * @author shevek
 */
public class UdevDevice {

    private final String action;
    private final String devnode;
    private final String devpath;
    private final String devtype;
    private final String driver;
    private final String subsystem;
    private final String sysname;
    private final String sysnum;
    private final String syspath;
    private final String parentSyspath;
    private final boolean initialized;
    private final long sequenceNumber;
    private final long usecSinceInitialized;
    private final ImmutableList<String> devlinks;
    private final ImmutableMap<String, String> properties;
    private final ImmutableList<String> sysattrs;
    private final ImmutableList<String> tags;

    /* pp */ UdevDevice(Udev udev, UdevLibrary.udev_device device) {
        UdevLibrary library = udev.getLibrary();
        this.action = library.udev_device_get_action(device);
        this.devnode = library.udev_device_get_devnode(device);
        this.devpath = library.udev_device_get_devpath(device);
        this.devtype = library.udev_device_get_devtype(device);
        this.driver = library.udev_device_get_driver(device);
        this.subsystem = library.udev_device_get_subsystem(device);
        this.sysname = library.udev_device_get_sysname(device);
        this.sysnum = library.udev_device_get_sysnum(device);
        this.syspath = library.udev_device_get_syspath(device);

        this.initialized = library.udev_device_get_is_initialized(device) != 0;

        this.sequenceNumber = library.udev_device_get_seqnum(device).longValue();
        this.usecSinceInitialized = library.udev_device_get_usec_since_initialized(device).longValue();

        this.devlinks = new UdevValueIterator(library, library.udev_device_get_devlinks_list_entry(device)).toList();
        this.properties = new UdevEntryIterator(library, library.udev_device_get_properties_list_entry(device)).toMap();
        this.sysattrs = new UdevValueIterator(library, library.udev_device_get_sysattr_list_entry(device)).toList();
        this.tags = new UdevValueIterator(library, library.udev_device_get_tags_list_entry(device)).toList();

        // We do not own the ref to this parent device.
        UdevLibrary.udev_device parent = library.udev_device_get_parent(device);
        if (parent != null)
            this.parentSyspath = library.udev_device_get_syspath(parent);
        else
            this.parentSyspath = null;
    }

    public String getAction() {
        return action;
    }

    public String getDevnode() {
        return devnode;
    }

    public String getDevpath() {
        return devpath;
    }

    @CheckForNull
    public String getDevtype() {
        return devtype;
    }

    public String getDriver() {
        return driver;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public String getSysname() {
        return sysname;
    }

    public String getSysnum() {
        return sysnum;
    }

    public String getSyspath() {
        return syspath;
    }

    @CheckForNull
    public String getParentSyspath() {
        return parentSyspath;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public long getUsecSinceInitialized() {
        return usecSinceInitialized;
    }

    /**
     * Returns a list of symlinks in /dev established for this device.
     *
     * http://www.freedesktop.org/software/systemd/libudev/libudev-udev-device.html#udev-device-get-devlinks-list-entry
     */
    @Nonnull
    public ImmutableList<String> getDevlinks() {
        return devlinks;
    }

    @Nonnull
    public ImmutableMap<? extends String, ? extends String> getProperties() {
        return properties;
    }

    @CheckForNull
    public String getProperty(@Nonnull String name) {
        return properties.get(name);
    }

    /** A typographically safer version of {@link #getProperty(String)}. */
    @CheckForNull
    public String getProperty(@Nonnull UdevProperty name) {
        return properties.get(name.name());
    }

    /**
     * Returns the list of sysattr names.
     * A sysattr is an exposed file in /sys/{syspath}/... which returns a
     * kernel-side value.
     */
    @Nonnull
    public ImmutableList<? extends String> getSysattrNames() {
        return sysattrs;
    }

    @CheckForNull
    public String getSysattr(@Nonnull String name)
            throws IOException {
        File file = new File(getSyspath(), name);
        return Files.toString(file, Charsets.ISO_8859_1);
    }

    @Nonnull
    public ImmutableList<? extends String> getTags() {
        return tags;
    }

    @CheckForNull
    public boolean hasTag(@Nonnull String name) {
        return tags.contains(name);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("action", getAction())
                .add("devnode", getDevnode())
                .add("devpath", getDevpath())
                .add("devtype", getDevtype())
                .add("driver", getDriver())
                .add("subsystem", getSubsystem())
                .add("sysname", getSysname())
                .add("sysnum", getSysnum())
                .add("syspath", getSyspath())
                .add("parentSyspath", getParentSyspath())
                .add("initialized", isInitialized())
                .add("sequenceNumber", getSequenceNumber())
                .add("usecSinceInitialized", getUsecSinceInitialized())
                .add("devlinks", getDevlinks())
                .add("properties", getProperties())
                .add("sysattrs", getSysattrNames())
                .add("tags", getTags())
                .toString();
    }
}
