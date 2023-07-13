#include <jni.h>
#include <cmath>


extern "C"
JNIEXPORT jboolean JNICALL
Java_com_dem_blazeball_fragments_GameFragment_isBallScored(
        JNIEnv* env,
        jobject /* this */,
        jobject ballPosition,
        jobject hoopPosition,
        jfloat hoopRadius) {
    jclass vector2Class = env->GetObjectClass(ballPosition);

    jmethodID getXMethod = env->GetMethodID(vector2Class, "getX", "()F");
    jmethodID getYMethod = env->GetMethodID(vector2Class, "getY", "()F");

    jfloat ballX = env->CallFloatMethod(ballPosition, getXMethod);
    jfloat ballY = env->CallFloatMethod(ballPosition, getYMethod);

    jfloat hoopX = env->CallFloatMethod(hoopPosition, getXMethod);
    jfloat hoopY = env->CallFloatMethod(hoopPosition, getYMethod);

    jfloat dx = hoopX - ballX;
    jfloat dy = hoopY - ballY;

    jfloat distance = sqrt(dx * dx + dy * dy);

    return distance <= hoopRadius;
}
