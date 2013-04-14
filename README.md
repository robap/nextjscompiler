Extjs Compiler
==============

This is an experiment in building a "compiled" javascript source file for an
extjs application. 

Why?
---
The official tool seems lacking in at least these areas:

  - Exit status always returns 0 making it useless for deployment scripts
  - It forces the use of a static html file as the input file. Front controller
style web applications do not use a file like this so, the file must be created
and maintained separately.
  - Files are often not included in the build file.

So this tool aims to address these issues.

Usage
-----

    java -jar dist/java-extjs-compiler.jar --app YourNamespace.Application \
    --src src/dir --output path/to/app-all.min.js
