/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import com.google.common.base.Objects;
import java.util.Map;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.udev.generated.UdevLibrary;

/**
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
    private final Map<String, String> properties;
    private final Map<String, String> sysattrs;
    private final Map<String, String> tags;

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

        this.properties = new UdevEntryIterator(library, library.udev_device_get_properties_list_entry(device)).toMap();
        this.sysattrs = new UdevEntryIterator(library, library.udev_device_get_sysattr_list_entry(device)).toMap();
        this.tags = new UdevEntryIterator(library, library.udev_device_get_tags_list_entry(device)).toMap();

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

    @Nonnull
    public Map<? extends String, ? extends String> getProperties() {
        return properties;
    }

    @Nonnull
    public Map<? extends String, ? extends String> getSysattrs() {
        return sysattrs;
    }

    @Nonnull
    public Map<? extends String, ? extends String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("action", action)
                .add("devnode", devnode)
                .add("devpath", devpath)
                .add("devtype", devtype)
                .add("driver", driver)
                .add("subsystem", subsystem)
                .add("sysname", sysname)
                .add("sysnum", sysnum)
                .add("syspath", syspath)
                .add("parentSyspath", parentSyspath)
                .add("initialized", initialized)
                .add("sequenceNumber", sequenceNumber)
                .add("usecSinceInitialized", usecSinceInitialized)
                .add("properties", properties)
                .add("sysattrs", sysattrs)
                .add("tags", tags)
                .toString();
    }
}
