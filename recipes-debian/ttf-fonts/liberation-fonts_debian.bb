# base recipe: meta/recipes-graphics/ttf-fonts/liberation-fonts_2.00.1.bb
# base branch: warrior
# base commit: 997b526d8b732112edae252eb615b124c5a95760

SUMMARY = "Liberation(tm) Fonts"
DESCRIPTION = "The Liberation(tm) Fonts is a font family originally \
created by Ascender(c) which aims at metric compatibility with \
Arial, Times New Roman, Courier New."
HOMEPAGE = "https://releases.pagure.org/liberation-fonts/"
BUGTRACKER = "https://bugzilla.redhat.com/"

SECTION = "x11/fonts"
LICENSE = "OFL-1.1"
LIC_FILES_CHKSUM = "file://License.txt;md5=9e3cffc28e2ebe189f5639d09babf6bb"
PE = "1"

inherit debian-package
require recipes-debian/sources/fonts-liberation.inc
FILESPATH_append = ":${COREBASE}/meta/recipes-graphics/ttf-fonts/liberation-fonts"
DEBIAN_UNPACK_DIR = "${WORKDIR}/liberation-fonts-${PV}"
DEPEND = "fontforge"

inherit allarch fontcache

FONT_PACKAGES = "${PN}"

SRC_URI += " \
           file://30-liberation-aliases.conf"

do_install () {
	install -d ${D}${datadir}/fonts/ttf/
	for i in *.ttf; do
		install -m 0644 $i ${D}${prefix}/share/fonts/ttf/${i}
	done

	install -d ${D}${sysconfdir}/fonts/conf.d/
	install -m 0644 ${WORKDIR}/30-liberation-aliases.conf ${D}${sysconfdir}/fonts/conf.d/

	install -d ${D}${prefix}/share/doc/${BPN}/
	install -m 0644 LICENSE ${D}${datadir}/doc/${BPN}/
}

PACKAGES = "${PN}"
FILES_${PN} += "${sysconfdir} ${datadir}"

BBCLASSEXTEND = "native nativesdk"
