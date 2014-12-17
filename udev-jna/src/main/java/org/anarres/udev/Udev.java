package org.anarres.udev;

import java.io.Closeable;
import javax.annotation.Nonnull;
import org.anarres.udev.generated.UdevLibrary;

/**
 * A Udev handle.
 *
 * This object MUST be closed after use or a leak in the native heap will result.
 *
 * See http://www.freedesktop.org/software/systemd/libudev/
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

    /**
     * Enumerates udev devices.
     *
     * The returned object is a builder pattern and is reusable.
     */
    @Nonnull
    public UdevEnumeration newEnumeration() {
        return new UdevEnumeration(this);
    }

    @Nonnull
    public UdevDevice getDeviceBySyspath(@Nonnull String syspath) {
        UdevLibrary.udev_device device = library.udev_device_new_from_syspath(getHandle(), syspath);
        try {
            return new UdevDevice(this, device);
        } finally {
            library.udev_device_unref(device);
        }
    }

    @Nonnull
    public UdevDevice getDeviceBySubsystemSysname(@Nonnull String subsystem, @Nonnull String sysname) {
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
