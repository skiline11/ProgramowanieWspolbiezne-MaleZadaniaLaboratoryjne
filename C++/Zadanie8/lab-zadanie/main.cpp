#include <iostream>
#include <thread>
#include "Barrier.h"

Barrier bariera(4);

void fun(int id) {
    std::cout << "Jestem watkiem " << id << " i bede czekał na barierze" << std::endl;
    bariera.reach();
    std::cout << "Jestem watkiem " << id << " i przechodze przez bariere" << std::endl;
}

int main() {
    int id = 1;
    std::thread t1(fun, id);
    std::this_thread::sleep_for(std::chrono::seconds(1));
    id = 2;
    std::thread t2{fun, id};
    std::this_thread::sleep_for(std::chrono::seconds(1));
    id = 3;
    std::thread t3{fun, id};
    std::this_thread::sleep_for(std::chrono::seconds(1));
    id = 4;
    std::thread t4{fun, id};
    std::this_thread::sleep_for(std::chrono::seconds(1));
    id = 5;
    std::thread t5{fun, id};
    id = 6;
    std::thread t6{fun, id};
    id = 7;
    std::thread t7{fun, id};
//    std::this_thread::sleep_for(std::chrono::seconds(1));
    t1.join();
    t2.join();
    t3.join();
    t4.join();
    t5.join();
    t6.join();
    t7.join();
    return 0;
}