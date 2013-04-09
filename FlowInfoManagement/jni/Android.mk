#
# by qinchenchong on 2012/3/18
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := flowinfo 
LOCAL_SRC_FILES := flowinfo.c

include $(BUILD_SHARED_LIBRARY)
