/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.device.parted;

import java.util.ArrayList;
import java.util.List;
import org.anarres.device.parted.generated.PartedLibrary;
import org.anarres.device.parted.generated.PedDevice;

/**
 *
 * @author shevek
 */
public class Parted {

    public List<? extends PartedDevice> probe_all() {
        List<PartedDevice> out = new ArrayList<PartedDevice>();
        PartedLibrary.INSTANCE.ped_device_probe_all();
        PedDevice device = null;
        for (;;) {
            device = PartedLibrary.INSTANCE.ped_device_get_next(device);
            if (device == null)
                break;
            out.add(new PartedDevice(device.path.getString(0)));
        }
        return out;
    }
}