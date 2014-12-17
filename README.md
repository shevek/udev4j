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
	for (String syspath : udev.newEnumeration().withMatchSubsystem("block")) {
		UdevDevice device = udev.getDeviceBySyspath(syspath);
		...
	}
} finally {
	udev.close();
}
```

Documentation
=============

The [JavaDoc API](http://shevek.github.io/udev4j/docs/javadoc/)
is available.

