/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.udev;

import java.util.AbstractMap;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
/* pp */ class UdevListEntry extends AbstractMap.SimpleEntry<String, String> {

    /* pp */ UdevListEntry(@Nonnull String key, @Nonnull String value) {
        super(key, value);
    }
}