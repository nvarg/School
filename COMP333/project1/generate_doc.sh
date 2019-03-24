#!/usr/bin/env bash

font_dir=./doc/site-font
input=./doc/document.mom
output=./output.pdf

for command in "groff";
do
    command -v $command >/dev/null 2>&1 || {
        echo >&2 "This script requires $command,\
		but it is not installed. Aborting.."
        exit 1;
    }
done

printf -- "Converting $input to $output\n"
pdfmom -U -F "$font_dir" -f LM -tep 2>/dev/null > "$output" < "$input"
printf -- "DONE\n"
exit 0

