DESCRIPTION = "An introspection library, written in C, focused on reading \
               and writing memory from virtual machines (VM's)."
HOMEPAGE = "https://github.com/libvmi/libvmi"
LICENSE = "LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING.LESSER;md5=e6a600fd5e1d9cbde2d983680233ad02"
SECTION = "console/tools"
PV = "0.12.0"

DEPENDS = "libvirt libcheck bison fuse byacc-native"

SRC_URI = "git://github.com/libvmi/libvmi.git;branch=master;protocol=https \
"

SRCREV = "e8c2061d11c42e5868cbf48229f59b5e7071395a"

S = "${WORKDIR}/git"

inherit autotools-brokensep pkgconfig

# Noramlly dynamic libs would be of the form libXX.so.1.0.0 in which case
# bitbake should be able to properly populate the -dev package and the main
# packages. Since libvmi uses the form libXX.1.0.0.so it breaks this automatic
# packaging so we need to be more explicit about what goes where.
FILES_${PN} += "${libdir}/libvmi-0.9.so"
FILES_${PN}-dev = "${includedir} ${libdir}/${BPN}.so ${libdir}/*.la \
                ${libdir}/*.o ${libdir}/pkgconfig ${datadir}/pkgconfig \
                ${datadir}/aclocal ${base_libdir}/*.o \
                ${libdir}/${BPN}/*.la ${base_libdir}/*.la"

PACKAGECONFIG ??= "json-c"
PACKAGECONFIG[xen] = "--enable-xen,--disable-xen,xen,"
PACKAGECONFIG[kvm] = "--disable-kvm-legacy,--enable-kvm-legacy,kvm,"
PACKAGECONFIG[json-c] = ",,json-c,"

# We include a sample conf file to which we have added
do_install_append () {
	mkdir ${D}${sysconfdir}
	cp etc/*.conf ${D}${sysconfdir}
}

# Construction of grammar.h is not parallel safe.
PARALLEL_MAKE = "-j1"
