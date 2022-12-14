#!/bin/bash

ONNX_FILE=$1
OUTPUT_NAME=$2

NCNN_PARAM_NAME="${OUTPUT_NAME}.param"
NCNN_BIN_NAME="${OUTPUT_NAME}.bin"
NCNN_PARAM_OPT_NAME="${OUTPUT_NAME}_fp16_opt.param"
NCNN_BIN_OPT_NAME="${OUTPUT_NAME}_fp16_opt.bin"
NCNN_CPP_ID_NAME="${OUTPUT_NAME}_fp16_opt.id.h"
NCNN_CPP_MEM_NAME="${OUTPUT_NAME}_fp16_opt.mem.h"

echo "Convert file: $ONNX_FILE with name $OUTPUT_NAME"

onnx2ncnn $ONNX_FILE $NCNN_PARAM_NAME $NCNN_BIN_NAME
ncnnoptimize $NCNN_PARAM_NAME $NCNN_BIN_NAME $NCNN_PARAM_OPT_NAME $NCNN_BIN_OPT_NAME 1
ncnn2mem $NCNN_PARAM_OPT_NAME $NCNN_BIN_OPT_NAME $NCNN_CPP_ID_NAME $NCNN_CPP_MEM_NAME