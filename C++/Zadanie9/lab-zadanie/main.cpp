//
// Created by skiline11 on 04.12.17.
//

#include<iostream>
#include <vector>
#include <future>
#include <cstdlib>

using namespace std;

vector<int> scal(vector<int> v1, vector<int> v2) {
//	cout << "Scalam v1(size = " << v1.size() << ") i v2(size = " << v2.size() << ")" << endl;
	vector<int> nowy;
	unsigned int beg1 = 0, beg2 = 0;
	while(beg1 < v1.size() && beg2 < v2.size()) {
		if(v1[beg1] < v2[beg2]) {
			nowy.push_back(v1[beg1]);
			beg1++;
		}
		else {
			nowy.push_back(v2[beg2]);
			beg2++;
		}
	}
	while(beg1 < v1.size()) {
		nowy.push_back(v1[beg1]);
		beg1++;
	}
	while(beg2 < v2.size()) {
		nowy.push_back(v2[beg2]);
		beg2++;
	}
	return nowy;
}

vector<int> posortuj(vector<int>& moj_wektor, int beg, int end) {
//	cout << "jestem w posortuj(" << beg << ", " << end << ")" << endl;
	future<vector<int>> handle_v1, handle_v2;
	vector<int> v1, v2;
	if(beg < (beg + end)/2) handle_v1 = async(posortuj, ref(moj_wektor), beg, (beg + end)/2);
	if((beg + end)/2 + 1 < end) handle_v2 = async(posortuj, ref(moj_wektor), (beg + end)/2 + 1, end);
	if(beg < (beg + end)/2) v1 = handle_v1.get();
	else v1.push_back(moj_wektor[beg]);
	if((beg + end)/2 + 1 < end) v2 = handle_v2.get();
	else if(beg < end) v2.push_back(moj_wektor[end]);
	return scal(v1, v2);
}

void wypisz_wektor(vector<int> moj_wektor) {
	cout << "Wektor wyglada tak : ";
	for(unsigned int i = 0; i < moj_wektor.size(); i++) {
		cout << moj_wektor[i] << " ";
	}
	cout << endl;
}

int main() {
	// UWAGA : Nie wiem jak zrobić to zadanie ze zwykłym wektorem przekazywanym przez referencję!!!
	// Bo wtedy może wiele wątków mięc dostęp do niego w tym samym czasie i wtedy dzieją się różne dziwne rzeczy


	// UWAGA : Wyrzucało taki błąd :
	/*
	 	In file included from /usr/include/c++/5/future:38:0,
                 from moj_test.cpp:7:
/usr/include/c++/5/functional: In instantiation of ‘struct std::_Bind_simple<std::vector<int> (*(std::vector<int>, int, int))(std::vector<int>&, int, int)>’:
/usr/include/c++/5/future:1709:67:   required from ‘std::future<typename std::result_of<_Functor(_ArgTypes ...)>::type> std::async(std::launch, _Fn&&, _Args&& ...) [with _Fn = std::vector<int> (&)(std::vector<int>&, int, int); _Args = {std::vector<int, std::allocator<int> >&, int&, int}; typename std::result_of<_Functor(_ArgTypes ...)>::type = std::vector<int>]’
/usr/include/c++/5/future:1725:19:   required from ‘std::future<typename std::result_of<_Functor(_ArgTypes ...)>::type> std::async(_Fn&&, _Args&& ...) [with _Fn = std::vector<int> (&)(std::vector<int>&, int, int); _Args = {std::vector<int, std::allocator<int> >&, int&, int}; typename std::result_of<_Functor(_ArgTypes ...)>::type = std::vector<int>]’
moj_test.cpp:41:84:   required from here
/usr/include/c++/5/functional:1505:61: error: no type named ‘type’ in ‘class std::result_of<std::vector<int> (*(std::vector<int>, int, int))(std::vector<int>&, int, int)>’
       typedef typename result_of<_Callable(_Args...)>::type result_type;
                                                             ^
/usr/include/c++/5/functional:1526:9: error: no type named ‘type’ in ‘class std::result_of<std::vector<int> (*(std::vector<int>, int, int))(std::vector<int>&, int, int)>’
         _M_invoke(_Index_tuple<_Indices...>)
	 */
	// rozwiązałem go otaczając wektor (w funkcji async) przez ref(...) ---> ref(wektor)
	// dzięki temu błąd zniknął
	vector<int> moj_wektor;

	srand(time(NULL));

	for(int i = 0; i < 10; i++) moj_wektor.push_back(rand() % 100);
	wypisz_wektor(moj_wektor);
	vector<int> posortowany_wektor = posortuj(moj_wektor, 0, moj_wektor.size() - 1);
	wypisz_wektor(posortowany_wektor);
	return 0;
}
