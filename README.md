LibUdev for Java
================

libudev is the interface for enumerating and manipulating devices
under Linux. This Java library provides a Java-like API to libudev
which absolves the naive coder from caring about the underlying memory
management concerns and "Just Works."

See http://www.freedesktop.org/software/systemd/libudev/ for the C
API description.

Usage
=====

```
Udev udev = new Udev();
try {
	for (String syspath : udev.newEnumeration()
			.withMatchSubsystem(UdevSubsystem.block)) {
		UdevDevice device = udev.getDeviceBySyspath(syspath);
		...
		/* ... = */ device.getProperty(UdevProperty.ID_USB_DRIVER);
		/* ... = */ device.getProperty("CUSTOM_PROPERTY");
	}
} finally {
	udev.close();
}
```

In Java 7, do not forget to use:
```
	try (Udev udev = new Udev()) {
		...
	}
```

Documentation
=============

The [JavaDoc API](http://shevek.github.io/udev4j/docs/javadoc/)
is available.

Requirements
============

This package requirs Java 1.6 or greater, and may be installed from
Maven Central as `org.anarres.udev:udev-jna:<version>`.

This package requires libudev-dev to build. The runtime does not
require it.

