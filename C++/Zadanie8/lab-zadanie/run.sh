g++ -c -std=c++17 Barrier.cpp -o Barrier.o -Wall -Wextra
g++ -c -std=c++17 main.cpp -o main.o -Wall -Wextra
g++ -pthread  main.o Barrier.o -o program