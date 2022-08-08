import os
import shutil

CURRENT_DIR = os.path.dirname(os.path.realpath(__file__))

print("Current Dir: ", CURRENT_DIR)

os.system(f"cd {CURRENT_DIR} && git clone --branch=master --depth=1 https://github.com/ultralytics/yolov5.git")
yolov5_dir = os.path.join(CURRENT_DIR, "yolov5")

models_dir = os.path.join(yolov5_dir, "models")
models_target_dir = os.path.join(CURRENT_DIR, "models")

utils_dir = os.path.join(yolov5_dir, "utils")
utils_target_dir = os.path.join(CURRENT_DIR, "utils")


if os.path.isdir(yolov5_dir):
    shutil.copytree(models_dir, models_target_dir, ignore=shutil.ignore_patterns("hub"))
    shutil.copytree(utils_dir, utils_target_dir, ignore=shutil.ignore_patterns("aws", "docker", "flask_rest_api", "google_app_engine"))
else:
    print("Repo clone failed!")
