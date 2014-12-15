LibUdev for Java
================

Usage
=====

```
Udev udev = new Udev();
try {
	for (String syspath : udev.newEnumeration().withMatchSubsystem("block")) {
		UdevDevice device = udev.getDeviceBySyspath(syspath);
		...
	}
} finally {
	udev.close();
}
```

