/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import java.util.Set;
import java.util.TreeSet;
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

    @Test
    public void testEnumeration() {
        Udev udev = new Udev();
        try {
            assertNotNull(udev);
            for (String syspath : udev.newEnumeration().withMatchSubsystem("block")) {
                do {
                    UdevDevice device = udev.getDeviceBySyspath(syspath);
                    LOG.info("Device: " + syspath + " -> " + device);
                    syspath = device.getParentSyspath();
                } while (syspath != null);
            }
        } finally {
            udev.close();
        }
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
