SUMMARY = "Tool Command Language"
HOMEPAGE = "http://tcl.sourceforge.net"
SECTION = "devel/tcltk"

# http://www.tcl.tk/software/tcltk/license.html
LICENSE = "tcl & BSD-3-Clause"
LIC_FILES_CHKSUM = "file://license.terms;md5=058f6229798281bbcac4239c788cfa38 \
    file://compat/license.terms;md5=058f6229798281bbcac4239c788cfa38 \
    file://library/license.terms;md5=058f6229798281bbcac4239c788cfa38 \
    file://macosx/license.terms;md5=058f6229798281bbcac4239c788cfa38 \
    file://tests/license.terms;md5=058f6229798281bbcac4239c788cfa38 \
    file://win/license.terms;md5=058f6229798281bbcac4239c788cfa38 \
"

DEPENDS = "tcl-native zlib"

inherit debian-package
require recipes-debian/sources/tcl8.6.inc
DEBIAN_UNPACK_DIR = "${WORKDIR}/tcl8.6.9"

FILESPATH_append = ":${COREBASE}/meta/recipes-devtools/tcltk/tcl"
SRC_URI += "file://tcl-add-soname.patch;striplevel=0"
SRC_URI_append_class_target += " \
           file://fix_non_native_build_issue.patch;striplevel=0 \
           file://fix_issue_with_old_distro_glibc.patch;striplevel=0 \
           file://no_packages.patch;striplevel=0 \
           file://tcl-remove-hardcoded-install-path.patch;striplevel=0 \
           file://alter-includedir.patch;striplevel=0 \
           file://run-ptest;striplevel=0 \
"

VER = "${PV}"

inherit autotools ptest binconfig

EXTRA_OECONF = "--enable-threads --disable-rpath --libdir=${libdir}"

# This is a dirty workaround for that the sources are located at ${S}/unix
# I hope that S = "${DEBIAN_UNPACK_DIR}/unix" works in that case, but it does not...
do_configure_prepend() {
        mv ${S}/* ${S}/../.
        mv ${S}/../unix/* ${S}/.
        rmdir ${S}/../unix
        ln -s ${S} ${S}/../unix
}

do_compile_prepend() {
	echo > ${S}/../compat/fixstrtod.c
}

do_install() {
	autotools_do_install
	oe_runmake 'DESTDIR=${D}' install-private-headers
	ln -sf ./tclsh${VER} ${D}${bindir}/tclsh
	ln -sf tclsh8.6 ${D}${bindir}/tclsh${VER}
	sed -i "s;-L${B};-L${STAGING_LIBDIR};g" tclConfig.sh
	sed -i "s;'${WORKDIR};'${STAGING_INCDIR};g" tclConfig.sh
	install -d ${D}${bindir_crossscripts}
	install -m 0755 tclConfig.sh ${D}${bindir_crossscripts}
	install -m 0755 tclConfig.sh ${D}${libdir}
	for dir in compat generic unix; do
		install -d ${D}${includedir}/${BPN}${VER}/$dir
		install -m 0644 ${S}/../$dir/*.h ${D}${includedir}/${BPN}${VER}/$dir/
	done
}

SYSROOT_DIRS += "${bindir_crossscripts}"

PACKAGES =+ "tcl-lib"
FILES_tcl-lib = "${libdir}/libtcl8.6.so.*"
FILES_${PN} += "${libdir}/tcl${VER} ${libdir}/tcl8.6 ${libdir}/tcl8"
FILES_${PN}-dev += "${libdir}/tclConfig.sh ${libdir}/tclooConfig.sh"

# isn't getting picked up by shlibs code
RDEPENDS_${PN} += "tcl-lib"
RDEPENDS_${PN}_class-native = ""
RDEPENDS_${PN}-ptest += "libgcc"

BBCLASSEXTEND = "native nativesdk"

do_compile_ptest() {
	oe_runmake tcltest
}

do_install_ptest() {
	cp ${B}/tcltest ${D}${PTEST_PATH}
	cp -r ${S}/../library ${D}${PTEST_PATH}
	cp -r ${S}/../tests ${D}${PTEST_PATH}
}

# Fix some paths that might be used by Tcl extensions
BINCONFIG_GLOB = "*Config.sh"

# Fix the path in sstate
SSTATE_SCAN_FILES += "*Config.sh"

# Cleanup host path from ${libdir}/tclConfig.sh and remove the
# ${bindir_crossscripts}/tclConfig.sh from target
PACKAGE_PREPROCESS_FUNCS += "tcl_package_preprocess"
tcl_package_preprocess() {
	sed -i -e "s;${DEBUG_PREFIX_MAP};;g" \
	       -e "s;-L${STAGING_LIBDIR};-L${libdir};g" \
	       -e "s;${STAGING_INCDIR};${includedir};g" \
	       -e "s;--sysroot=${RECIPE_SYSROOT};;g" \
	       ${PKGD}${libdir}/tclConfig.sh

	rm -f ${PKGD}${bindir_crossscripts}/tclConfig.sh
}
