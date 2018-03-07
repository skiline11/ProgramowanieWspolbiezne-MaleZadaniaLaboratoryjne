cmake_minimum_required(VERSION 3.8)
project(lab_zadanie)

set(CMAKE_CXX_STANDARD 17)

set(SOURCE_FILES main.cpp Barrier.cpp Barrier.h)
add_executable(lab_zadanie ${SOURCE_FILES})