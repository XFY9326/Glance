#!/bin/bash

PT_FILE=$1

OUTPUT_NAME="${PT_FILE%%.*}"
echo "Convert file: $PT_FILE with name $OUTPUT_NAME"

NCNN_PARAM_FILE=${OUTPUT_NAME}.ncnn.param
NCNN_BIN_FILE=${OUTPUT_NAME}.ncnn.bin

NCNN_PARAM_FP16_OPT_NAME="${OUTPUT_NAME}_fp16_opt.param"
NCNN_BIN_FP16_OPT_NAME="${OUTPUT_NAME}_fp16_opt.bin"
NCNN_CPP_FP16_ID_NAME="${OUTPUT_NAME}_fp16_opt.id.h"
NCNN_CPP_FP16_MEM_NAME="${OUTPUT_NAME}_fp16_opt.mem.h"

# NCNN_PARAM_FP32_OPT_NAME="${OUTPUT_NAME}_fp32_opt.param"
# NCNN_BIN_FP32_OPT_NAME="${OUTPUT_NAME}_fp32_opt.bin"
# NCNN_CPP_FP32_ID_NAME="${OUTPUT_NAME}_fp32_opt.id.h"
# NCNN_CPP_FP32_MEM_NAME="${OUTPUT_NAME}_fp32_opt.mem.h"

./pnnx/pnnx $PT_FILE

ncnnoptimize $NCNN_PARAM_FILE $NCNN_BIN_FILE $NCNN_PARAM_FP16_OPT_NAME $NCNN_BIN_FP16_OPT_NAME 1
ncnn2mem $NCNN_PARAM_FP16_OPT_NAME $NCNN_BIN_FP16_OPT_NAME $NCNN_CPP_FP16_ID_NAME $NCNN_CPP_FP16_MEM_NAME

# ncnnoptimize $NCNN_PARAM_FILE $NCNN_BIN_FILE $NCNN_PARAM_FP32_OPT_NAME $NCNN_BIN_FP32_OPT_NAME 0
# ncnn2mem $NCNN_PARAM_FP32_OPT_NAME $NCNN_BIN_FP32_OPT_NAME $NCNN_CPP_FP32_ID_NAME $NCNN_CPP_FP32_MEM_NAME
