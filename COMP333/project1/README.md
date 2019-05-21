# Parallel Tasks

## benchmark_blerp.sh

It compiles the Bilinear Interpolation algorithm and benchmarks it using different OpenMP scheduling. It scales an image by 10000% for each test. It outputs Groff code to doc/BLERP_C_BENCHMARK.mom.

## generate_doc.sh

Compiles the Groff document and outputs a PDF to output.pdf
