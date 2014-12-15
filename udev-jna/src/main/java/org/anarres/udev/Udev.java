package org.anarres.udev;

import java.io.Closeable;
import javax.annotation.Nonnull;
import org.anarres.udev.generated.UdevLibrary;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author shevek
 */
public class Udev implements Closeable {

    private final UdevLibrary library;
    private final UdevLibrary.udev udev;

    public Udev(UdevLibrary library) {
        this.library = library;
        this.udev = library.udev_new();
    }

    public Udev() {
        this(UdevLibrary.INSTANCE);
    }

    @Nonnull
    public UdevLibrary getLibrary() {
        return library;
    }

    @Nonnull
    public UdevLibrary.udev getHandle() {
        return udev;
    }

    @Nonnull
    public UdevEnumeration newEnumeration() {
        return new UdevEnumeration(this);
    }

    @Nonnull
    public UdevDevice getDeviceBySyspath(String syspath) {
        UdevLibrary.udev_device device = library.udev_device_new_from_syspath(getHandle(), syspath);
        try {
            return new UdevDevice(this, device);
        } finally {
            library.udev_device_unref(device);
        }
    }

    @Nonnull
    public UdevDevice getDeviceBySubsystemSysname(String subsystem, String sysname) {
        UdevLibrary.udev_device device = library.udev_device_new_from_subsystem_sysname(getHandle(), subsystem, sysname);
        try {
            return new UdevDevice(this, device);
        } finally {
            library.udev_device_unref(device);
        }
    }

    @Override
    public void close() {
        library.udev_unref(udev);
    }
}
