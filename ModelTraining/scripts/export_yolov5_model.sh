#!/bin/sh

python yolov5/export.py --weights yolov5s.pt --img-size 640 640 --include onnx --simplify --train
