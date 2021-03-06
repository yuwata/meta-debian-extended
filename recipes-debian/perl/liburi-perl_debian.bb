# base recipe: poky/meta/recipes-devtools/perl/liburi-perl_1.74.bb
# base branch: warrior
# base commit: 720f7554a8cc3ca15321e010b9f1348cd7bfe23c
 
SUMMARY = "Perl module to manipulate and access URI strings"
DESCRIPTION = "This package contains the URI.pm module with friends. \
The module implements the URI class. URI objects can be used to access \
and manipulate the various components that make up these strings."

HOMEPAGE = "http://search.cpan.org/dist/URI/"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"

LIC_FILES_CHKSUM = "file://LICENSE;md5=c453e94fae672800f83bc1bd7a38b53f"

DEPENDS += "perl"

inherit debian-package
require recipes-debian/sources/liburi-perl.inc

DEBIAN_QUILT_PATCHES = ""
DEBIAN_UNPACK_DIR = "${WORKDIR}/URI-${PV}"
 
S = "${WORKDIR}/URI-${PV}"

EXTRA_CPANFLAGS = "EXPATLIBPATH=${STAGING_LIBDIR} EXPATINCPATH=${STAGING_INCDIR}"

inherit cpan ptest-perl

do_compile() {
	export LIBC="$(find ${STAGING_DIR_TARGET}/${base_libdir}/ -name 'libc-*.so')"
	cpan_do_compile
}

do_install_prepend() {
	# these tests require "-T" (taint) command line option
	rm -rf ${B}/t/cwd.t
	rm -rf ${B}/t/file.t
}

RDEPENDS_${PN}-ptest += "libtest-needs-perl perl-module-test-more"

BBCLASSEXTEND = "native"
