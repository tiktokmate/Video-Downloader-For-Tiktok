#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_leetlab_videodownloaderfortiktok_utils_Keys_ivKey(JNIEnv *env, jobject instance) {

 return (*env)->  NewStringUTF(env, "p2u7ilxhWGYzpm2o");
}

JNIEXPORT jstring JNICALL
Java_com_leetlab_videodownloaderfortiktok_utils_Keys_secretKey(JNIEnv *env, jobject instance) {

 return (*env)->NewStringUTF(env, "LSGPDo2cb5lPrVht");
}

JNIEXPORT jstring JNICALL
Java_com_leetlab_videodownloaderfortiktok_utils_Keys_secretUrl(JNIEnv *env, jobject instance) {

 return (*env)->NewStringUTF(env, "");
}



