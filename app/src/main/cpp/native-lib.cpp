#include <jni.h>
#include <string>



extern "C" JNIEXPORT jstring JNICALL
Java_com_example_hw_14r_ui_home_HomeFragment_changeTime(
        JNIEnv *env,
        jobject /* this */ int intTime, jint int_time) {


    std::string min;
    std::string sec;
    int_time = int_time/1000;
    if((int_time/60)<10){
        min = "0"+(int_time/60);
    }else{
        min = (int_time/60);
    }
    if((int_time%60)<10){
        sec= "0"+(int_time%60);
    }else{
        sec= (int_time%60);
    }
    return reinterpret_cast<jstring>(std::min);
}