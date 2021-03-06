package com.sun.tools.jdeps.resources;

public final class jdeps extends java.util.ListResourceBundle {
    protected final Object[][] getContents() {
        return new Object[][] {
            { "artifact.not.found", "not found" },
            { "err.invalid.arg.for.option", "invalid argument for option: {0}" },
            { "err.invalid.path", "invalid path: {0}" },
            { "err.missing.arg", "no value given for {0}" },
            { "err.option.after.class", "option must be specified before classes: {0}" },
            { "err.option.unsupported", "{0} not supported: {1}" },
            { "err.profiles.msg", "No profile information" },
            { "err.unknown.option", "unknown option: {0}" },
            { "error.prefix", "Error:" },
            { "jdeps.wiki.url", "https://wiki.openjdk.java.net/display/JDK8/Java+Dependency+Analysis+Tool" },
            { "main.opt.P", "  -P           -profile              Show profile or the file containing a package" },
            { "main.opt.R", "  -R           -recursive            Recursively traverse all dependencies.\n                                     The -R option implies -filter:none.  If -p, -e, -f\n                                     option is specified, only the matching dependences\n                                     are analyzed." },
            { "main.opt.apionly", "  -apionly                           Restrict analysis to APIs i.e. dependences\n                                     from the signature of public and protected\n                                     members of public classes including field\n                                     type, method parameter types, returned type,\n                                     checked exception types etc" },
            { "main.opt.cp", "  -cp <path>   -classpath <path>     Specify where to find class files" },
            { "main.opt.depth", "  -depth=<depth>                     Specify the depth of the transitive\n                                     dependency analysis" },
            { "main.opt.dotoutput", "  -dotoutput <dir>                   Destination directory for DOT file output" },
            { "main.opt.e", "  -e <regex>   -regex <regex>        Finds dependences matching the given pattern\n                                     (-p and -e are exclusive)" },
            { "main.opt.f", "  -f <regex>   -filter <regex>       Filter dependences matching the given pattern\n                                     If given multiple times, the last one will be used.\n  -filter:package                    Filter dependences within the same package (default)\n  -filter:archive                    Filter dependences within the same archive\n  -filter:none                       No -filter:package and -filter:archive filtering\n                                     Filtering specified via the -filter option still applies." },
            { "main.opt.h", "  -h -?        -help                 Print this usage message" },
            { "main.opt.include", "  -include <regex>                   Restrict analysis to classes matching pattern\n                                     This option filters the list of classes to\n                                     be analyzed.  It can be used together with\n                                     -p and -e which apply pattern to the dependences" },
            { "main.opt.jdkinternals", "  -jdkinternals                      Finds class-level dependences on JDK internal APIs.\n                                     By default, it analyzes all classes on -classpath\n                                     and input files unless -include option is specified.\n                                     This option cannot be used with -p, -e and -s options.\n                                     WARNING: JDK internal APIs may not be accessible in\n                                     the next release." },
            { "main.opt.p", "  -p <pkgname> -package <pkgname>    Finds dependences matching the given package name\n                                     (may be given multiple times)" },
            { "main.opt.s", "  -s           -summary              Print dependency summary only" },
            { "main.opt.v", "  -v           -verbose              Print all class level dependencies\n                                     Equivalent to -verbose:class -filter:none.\n  -verbose:package                   Print package-level dependencies excluding\n                                     dependencies within the same package by default\n  -verbose:class                     Print class-level dependencies excluding\n                                     dependencies within the same package by default" },
            { "main.opt.version", "  -version                           Version information" },
            { "main.usage", "Usage: {0} <options> <classes...>\nwhere <classes> can be a pathname to a .class file, a directory, a JAR file,\nor a fully-qualified class name.  Possible options include:" },
            { "main.usage.summary", "Usage: {0} <options> <classes...>\nuse -h, -? or -help for a list of possible options" },
            { "warn.invalid.arg", "Invalid classname or pathname not exist: {0}" },
            { "warn.mrjar.usejdk9", "{0} is a multi-release jar file.\nAll versioned entries are analyzed. To analyze the entries for a specific\nversion, use a newer version of jdeps (JDK 9 or later) \"--multi-release\" option." },
            { "warn.prefix", "Warning:" },
            { "warn.replace.useJDKInternals", "JDK internal APIs are unsupported and private to JDK implementation that are\nsubject to be removed or changed incompatibly and could break your application.\nPlease modify your code to eliminate dependency on any JDK internal APIs.\nFor the most recent update on JDK internal API replacements, please check:\n{0}" },
            { "warn.skipped.entry", "{0}" },
            { "warn.split.package", "package {0} defined in {1} {2}" },
        };
    }
}
