# Parallel Tasks

## What's this?

This is an analysis of OpenMP and Java 8 methods of executing loops in parallel. The included scripts will compile and execute benchmarks for the Groff document
to read.

## benchmark_blerp.sh

It compiles the Bilinear Interpolation algorithm and benchmarks it using different OpenMP scheduling. It scales an image by 10000% for each test. It outputs Groff code to doc/BLERP_C_BENCHMARK.mom.

## generate_doc.sh

Compiles the Groff document and outputs a PDF to output.pdf

## Assignment Prompt

### Part A:

You needed to submit a pdf document where you discuss optimization techniques to the following numerical methods provided below. One way to support your claim is to benchmark your approach and check the time it takes to perform the same task with different techniques. The reason why this can be helpful is that a lot of these High-Performance approaches are dependent on a particular system's architecture and this includes possible unforeseen dependencies that can influence the execution time.  You will not get a good score If you do not turn in a document (preferably in PDF) without arguing why your particular approach works. You are a college student who is about to graduate so, please submit work that is representative of you.

Ps- Keep in mind that sometimes, a serial approach may be faster than a parallel approach and some instances will require that you modify an algorithm to make it parallel. You are free to optimize your work however you see fit.

#### Bilinear interpolation

Code: https://rosettacode.org/wiki/Bilinear_interpolation
Wiki: https://en.wikipedia.org/wiki/Bilinear_interpolation

#### Gaussian elimination

Code: https://rosettacode.org/wiki/Gaussian_elimination
Wiki: https://en.wikipedia.org/wiki/Gaussian_elimination

### Part B:

Take those same algorithms and implement their java equivalent using only parallel streams. Benchmark your work and compare it to the serial section that is already provided in the links above.

Like Galileo's scientific method, you want to satisfy your assumption with empirical data, which are the benchmarking.

What you need to do for this project is try multiple approaches and see how they compare to one another. You then have to bench mark the said method on large parameters since other processes can influence the execution time greatly if the size relatively small.

Code analysis is very much encouraged.

Good luck.
