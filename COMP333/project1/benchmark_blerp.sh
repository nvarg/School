#!/usr/bin/env bash

src="./src/BilinearInterpolation/blerp.c"
output="./bin"

img_scale=10000 # (in percent)
img_src=./res/IMPORTANT.bmp
img_dst=./res/IMPORTANT_SCALED.bmp

declare -A tests

tests[serial]=""
tests[auto]=""
tests[static]="1 2 4 8"
tests[dynamic]="2 4 8 32"
tests[guided]="2 4 8 32"

for command in "tbl" "gcc" "/usr/bin/time" "javac" "java";
do
    command -v $command >/dev/null 2>&1 || {
        echo >&2 "This script requires $command,\
		but it is not installed. Aborting.."
        exit 1;
    }
done

mkdir -p $output

printf -- "Compiling source...\n"
for schedule in "${!tests[@]}"; do
	c_flags="-O2"
	if [[ "$schedule" == "serial" ]]; then
		c_flags="-D OMP -fopenmp -O2"
	fi

	# compile using the default chunk size first
	OMP_SCHEDULE=$schedule \
		gcc "$src"  "$c_flags" -o $output/blerp_$schedule
	for chunk_size in ${tests[$schedule]}; do
		OMP_SCHEDULE="$schedule,$chunk_size" \
			gcc "$src"  "$c_flags" -o $output/blerp_$schedule_$chunk_size
	done
done

printf -- "Running benchmarks...\n"
results="$(
for schedule in "${!tests[@]}"; do
	# Run using the default chunk size first
	executable="$output/blerp_$schedule -i $img_src -o $img_dst -s $img_scale"
	elapsed="$( (/usr/bin/time -f %e $executable) 2>&1)"
	printf -- "\n$schedule\tdefault\t$elapsed"
	for chunk_size in ${tests[$schedule]}; do
		executable="$output/blerp_$schedule_$chunk_size -i $img_src -o $img_dst -s $img_scale"
		elapsed="$( (/usr/bin/time -f %e $executable) 2>&1)"
		printf -- "\n$schedule\t$chunk_size\t$elapsed"
	done
done | sort -n -k3 | sed '/^$/d')"
printf -- "\nresults:\nschedule chunk\truntime (seconds)\n$results"

cpu_name=""
if command -v lscpu &>/dev/null; then
    cpu_name="$(lscpu | grep "Model name:" | sed -r 's/Model name:\s{1,}//g')"
fi

results=$(sed -e 's/\t/:/g' <<< "$results") # -e 'a_' <<< "$results")
doc=".TS H CENTER BOXED
tab(:) center allbox;
c s s
c c c.
\\f(SCBenchmark Results\\fP
schedule:chunk-size:runtime (seconds)
_
.TH
$results
.TE SOURCE \"\\s-4 $cpu_name \\s0\""

tbl <<< "$doc" > doc/BLERP_C_BENCHMARK.mom
printf -- "DONE"
exit 0

