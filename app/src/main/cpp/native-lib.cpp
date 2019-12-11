#include <jni.h>
#include <string>



extern "C" JNIEXPORT jstring JNICALL
Java_com_example_hw_14r_ui_home_HomeFragment_changeTime(
        JNIEnv *env,
        jobject,
        jint intTime/* this */) {

    std::string time;
    std::string min;
    std::string sec;
    intTime = intTime/1000;
    if((intTime/60)<10){
        min = "0"+ std::to_string(intTime/60);
    }else{
        min = std::to_string(intTime/60);
    }
    if((intTime%60)<10){
        sec= "0"+std::to_string(intTime%60);
    }else{
        sec= std::to_string(intTime%60);
    }
    time.append(min);
    time.append(":");
    time.append(sec);;
    return env->NewStringUTF(time.c_str());
}