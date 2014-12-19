/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.CheckForNull;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class UdevTest {

    private static final Logger LOG = LoggerFactory.getLogger(UdevTest.class);

    private void testSubsystem(@CheckForNull String subsystem) {
        if (subsystem != null)
            UdevSubsystem.valueOf(subsystem);
    }

    private void testDevType(@CheckForNull String devtype) {
        if (devtype != null)
            UdevProperty.DevType.valueOf(devtype);
    }

    @Test
    public void testEnumeration() {
        Set<String> unknownProperties = new HashSet<String>();

        Udev udev = new Udev();
        try {
            assertNotNull(udev);
            for (String syspath : udev.newEnumeration().withMatchSubsystem("block")) {
                do {
                    UdevDevice device = udev.getDeviceBySyspath(syspath);
                    LOG.info("Device: " + syspath + " -> " + device);
                    for (Map.Entry<? extends String, ? extends String> e : device.getProperties().entrySet()) {
                        if (e.getKey().equals(e.getKey().toUpperCase())) {
                            try {
                                UdevProperty.valueOf(e.getKey());
                            } catch (IllegalArgumentException _) {
                                unknownProperties.add(e.getKey());
                            }
                        }
                    }

                    testSubsystem(device.getSubsystem());
                    testSubsystem(device.getProperty(UdevProperty.SUBSYSTEM));
                    testDevType(device.getDevtype());
                    testDevType(device.getProperty(UdevProperty.DEVTYPE));

                    syspath = device.getParentSyspath();
                } while (syspath != null);
            }
        } finally {
            udev.close();
        }

        if (!unknownProperties.isEmpty())
            throw new IllegalArgumentException("Unknown properties " + unknownProperties);
    }

    @Test
    public void testSubsystems() {
        Set<String> subsystems = new TreeSet<String>();
        Udev udev = new Udev();
        try {
            for (String syspath : udev.newEnumeration()) {
                UdevDevice device = udev.getDeviceBySyspath(syspath);
                subsystems.add(device.getSubsystem());
            }
            LOG.info("Subsystems are " + subsystems);
        } finally {
            udev.close();
        }

    }
}
