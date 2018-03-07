#include <iostream>
#include <thread>
#include <fstream>
#include <future>
#include <list>
#include <vector>
#include <unistd.h>

using namespace std;

void grep(wstring slowo, vector<string> nazwy_pliku, promise<int> ile_razy[], int numer_watku) {
    locale loc("pl_PL.UTF-8");

    int licznik_slow_lacznie = 0;
    int numer_pliku = 0;
    int licznik_slow_w_pliku;
    wfstream plik;

    while((numer_pliku * 4) + numer_watku < nazwy_pliku.size()) {
        string & nazwa = nazwy_pliku.at((numer_pliku * 4) + numer_watku);

        licznik_slow_w_pliku = 0;
        plik.open(nazwa);

        if(plik.is_open()) {
            plik.imbue(loc);
            wstring line;
            while (getline(plik, line)) {
                for (auto pos = line.find(slowo,0); pos != std::string::npos; pos = line.find(slowo, pos+1))
                    licznik_slow_w_pliku++;
            }
            plik.close();
        }
        licznik_slow_lacznie += licznik_slow_w_pliku;
        numer_pliku++;

        cout << "num watku : " << numer_watku << " - obliczanym pliku " << nazwa << " jest " << licznik_slow_w_pliku << " wystapien tego slowa" << endl;
    }
    ile_razy[numer_watku].set_value(licznik_slow_lacznie);
}

int main() {

    string slowo;
    cout << "Podaj szukane słowo : ";
    cin >> slowo;

    wstring wslowo(slowo.length(), L' ');
    copy(slowo.begin(), slowo.end(), wslowo.begin());

    int liczba_plikow;
    cout << "Podaj liczbę plikow : ";
    cin >> liczba_plikow;

    vector<string> vector_nazw_plikow;

    string nazwa;

    cout << "Podawaj nazwy plikow : " << endl;
    for(int i = 0; i < liczba_plikow; i++) {
        cin >> nazwa;
        vector_nazw_plikow.push_back(nazwa);
    }
    cout << "Nazwy plikow zatwierdzone" << endl;

    int ilosc_watkow = 4;

    thread watki[ilosc_watkow];
    promise<int> ile_razy[ilosc_watkow];
    future<int> pobrane_ile_razy[ilosc_watkow];
    for(int i = 0; i < ilosc_watkow; i++) pobrane_ile_razy[i] = ile_razy[i].get_future();

    for(int numer_watku = 0; numer_watku < ilosc_watkow; numer_watku++) {
        watki[numer_watku] = thread{
                [wslowo, vector_nazw_plikow, &ile_razy, numer_watku] {
                    grep(wslowo, vector_nazw_plikow, ile_razy, numer_watku);
                }
        };
    }


    volatile int suma = 0;
    for(int numer_watku = 0; numer_watku < ilosc_watkow; numer_watku++) {
        suma += pobrane_ile_razy[numer_watku].get();
    }
    for(int numer_watku = 0; numer_watku < ilosc_watkow; numer_watku++) {
        watki[numer_watku].join();
    }
    cout << endl;
    cout << "Tych slow jest " << suma << endl;
    return 0;
}