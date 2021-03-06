# base recipe: meta/recipes-extended/libidn/libidn2_2.0.5.bb
# base branch: warrior
# base commit: e87b501659eecc22f48b3e3ca8cde6b4de9f663a

SUMMARY = "Internationalized Domain Name support library"
DESCRIPTION = "Implementation of the Stringprep, Punycode and IDNA specifications defined by the IETF Internationalized Domain Names (IDN) working group."
HOMEPAGE = "http://www.gnu.org/software/libidn/"
SECTION = "libs"
LICENSE = "(GPLv2+ | LGPLv3) & GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=ab90e75ef97cc6318ce4f2fbda62fe4d \
                    file://COPYING.LESSERv3;md5=e6a600fd5e1d9cbde2d983680233ad02 \
                    file://COPYINGv2;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://src/idn2.c;endline=16;md5=0283aec28e049f5bcaaeee52aa865874 \
                    file://lib/idn2.h.in;endline=27;md5=c2cd28d3f87260f157f022eabb83714f"

inherit debian-package
require recipes-debian/sources/libidn2.inc
DEBIAN_QUILT_PATCHES = ""
FILESPATH_append = ":${COREBASE}/meta/recipes-extended/libidn/libidn2"


SRC_URI += " \
           file://Unset-need_charset_alias-when-building-for-musl.patch \
          "
DEPENDS = "virtual/libiconv libunistring"

inherit pkgconfig autotools gettext texinfo gtk-doc lib_package

EXTRA_OECONF += "--disable-rpath \
                 --with-libunistring-prefix=${STAGING_EXECPREFIXDIR} \
                 "

LICENSE_${PN} = "(GPLv2+ | LGPLv3)"
LICENSE_${PN}-bin = "GPLv3+"

BBCLASSEXTEND = "native nativesdk"
