#!/bin/bash

NCNN_PARAM_FILE=$1
NCNN_BIN_FILE=$2
OUTPUT_NAME=$3

NCNN_PARAM_OPT_NAME="${OUTPUT_NAME}_fp16_opt.param"
NCNN_BIN_OPT_NAME="${OUTPUT_NAME}_fp16_opt.bin"
NCNN_CPP_ID_NAME="${OUTPUT_NAME}_fp16_opt.id.h"
NCNN_CPP_MEM_NAME="${OUTPUT_NAME}_fp16_opt.mem.h"

echo "Convert file: $NCNN_PARAM_FILE $NCNN_BIN_FILE with name $OUTPUT_NAME"

ncnnoptimize $NCNN_PARAM_FILE $NCNN_BIN_FILE $NCNN_PARAM_OPT_NAME $NCNN_BIN_OPT_NAME 1
ncnn2mem $NCNN_PARAM_OPT_NAME $NCNN_BIN_OPT_NAME $NCNN_CPP_ID_NAME $NCNN_CPP_MEM_NAME