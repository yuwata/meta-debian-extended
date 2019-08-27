# base recipe: meta-openembedded/meta-oe/recipes-support/gnulib/gnulib_2018-03-07.03.bb
# base branch: warrior
# base commit: af94fa02ca443e2dc9338f5fd2c4d62ee99031b3

SUMMARY = "The GNU portability library"
DESCRIPTION = "A collection of software subroutines which are designed to \
be usable on many operating systems. The goal of the project \
is to make it easy for free software authors to make their \
software run on many operating systems. Since source is designed \
to be copied from gnulib, it is not a library per-se, as much \
as a collection of portable idioms to be used in other projects."

HOMEPAGE = "http://www.gnu.org/software/gnulib/"
SECTION = "devel"
LICENSE = "LGPLv2+"

LIC_FILES_CHKSUM = "file://COPYING;md5=e4cf3810f33a067ea7ccd2cd889fed21"

inherit debian-package
require recipes-debian/sources/gnulib.inc
DEBIAN_UNPACK_DIR = "${WORKDIR}/gnulib-20140202-stable"

inherit utils

do_install () {
    cd ${S}

    mkdir -p ${D}/usr/bin
    cp --no-preserve=ownership check-module ${D}/usr/bin/.

    mkdir -p ${D}/usr/share/gnulib
    cp -r --no-preserve=ownership \
       build-aux posix-modules config doc lib m4 modules top tests \
       MODULES.html.sh Makefile gnulib-tool cfg.mk check-copyright \
       ${D}/usr/share/gnulib/.
    ln -s ../share/gnulib/gnulib-tool ${D}/usr/bin

    rm -f ${D}/usr/share/gnulib/doc/gnulib.info
    rm -f ${D}/usr/share/gnulib/doc/gnulib.html
    rm -f ${D}/usr/share/gnulib/modules/COPYING
    rm -f ${D}/usr/share/gnulib/*/.cvsignore ${D}/usr/share/gnulib/.cvsignore
    rm -f ${D}/usr/share/gnulib/*/.gitignore ${D}/usr/share/gnulib/.gitignore
    rm -f ${D}/usr/share/gnulib/*/.gitattributes ${D}/usr/share/gnulib/.gitattributes
}

do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_package[noexec] = "1"
do_packagedata[noexec] = "1"
deltask package_write_ipk
deltask package_write_deb
deltask package_write_rpm
deltask do_deploy_archives 

BBCLASSEXTEND = "native"
