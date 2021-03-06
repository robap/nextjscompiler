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

    java -jar dist/java-extjs-compiler.jar --app your/entry/file.js \
    --src src/dir --output path/to/app-all.min.js

Optional:

--prepend Optional path to a javascript file to prepend to the the ouput file
before the compiled output.


Build the helloword sample application
--------------------------------------

Run the helloworld app in dev mode by opening a browser to:

    file:///path/to/project/samples/helloworld/app_dev.html

Run the helloworld app in build mod by first building the app.min.js file:

    java -jar dist/java-extjs-compiler.jar \
    -a samples/helloworld/app/application.js \
    -o samples/helloworld/app.min.js \
    -s samples/helloworld/app/

Then open browser to:

    file:///path/to/project/samples/helloworld/app_prod.html