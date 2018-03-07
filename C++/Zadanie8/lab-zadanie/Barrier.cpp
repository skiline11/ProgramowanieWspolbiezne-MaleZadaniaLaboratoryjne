//
// Created by skiline11 on 21.11.17.
//

#include <iostream>
#include <pthread.h>
#include "Barrier.h"

using namespace std;

Barrier::Barrier(int resistance) {
    cout << "Tworze bariere rozmiaru " << resistance << endl;
    this->resistance = resistance;
}

void Barrier::reach() {
    cout << "jestem w reach()" << endl;
    unique_lock<mutex> my_lock(this->my_mutex);
    while(this->resistance > 1) {
        cout << "resistance > 1 bo res = " << this->resistance << " więc CZEKAM *****" << endl;
        (this->resistance)--;
        my_condition.wait(my_lock, [&]{return (this->resistance) == 0;});
    }
    if(this->resistance <= 1){
        if((this->resistance) != 0) {
            cout << "Przebilem bariere !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" << endl;
            this->resistance = 0;
            my_lock.unlock();
            my_condition.notify_all();
        }
        cout << "resistance <= 1 bo res = " << this->resistance << " wiec PRZECHODZE ----->" << endl;
    }
    std::cout << "**** Watek przekroczyl bariere" << std::endl;
}
