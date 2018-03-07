//
// Created by skiline11 on 21.11.17.
//

#ifndef LAB_ZADANIE_BARRIER_H
#define LAB_ZADANIE_BARRIER_H

#include <mutex>
#include <condition_variable>

class Barrier {
private:
    int resistance;
    std::mutex my_mutex;
    std::condition_variable my_condition;
public:
    Barrier(int resistance);
    Barrier(const Barrier&);
    void reach();
};


#endif //LAB_ZADANIE_BARRIER_H
