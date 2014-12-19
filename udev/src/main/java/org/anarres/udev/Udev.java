package org.anarres.udev;

import com.google.common.base.Preconditions;
import java.io.Closeable;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import org.anarres.udev.generated.UdevLibrary;

/**
 * A Udev handle - start here.
 *
 * This object MUST be closed after use or a leak in the native heap will result.
 *
 * See http://www.freedesktop.org/software/systemd/libudev/
 * @author shevek
 */
@NotThreadSafe
public class Udev implements Closeable {

    private final UdevLibrary library;
    private UdevLibrary.udev handle;

    public Udev(@Nonnull UdevLibrary library) {
        this.library = library;
        this.handle = library.udev_new();
    }

    /** You want this constructor. */
    public Udev() {
        this(UdevLibrary.INSTANCE);
    }

    /**
     * Returns the underlying native library dev handle.
     * You probably don't want this.
     */
    @Nonnull
    public UdevLibrary getLibrary() {
        return library;
    }

    /**
     * Returns the underlying native udev handle.
     * You probably don't want this.
     */
    @Nonnull
    public UdevLibrary.udev getHandle() {
        return Preconditions.checkNotNull(handle, "Udev object was closed.");
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

    /**
     * Closes this Udev object and releases all associated native resources.
     * Once this object is closed, all associated {@link UdevEnumeration}
     * objects become invalid, but {@link UdevDevice} objects may still be used.
     */
    @Override
    public void close() {
        UdevLibrary.udev h = handle;
        if (h != null)
            library.udev_unref(h);
        handle = null;
    }
}
