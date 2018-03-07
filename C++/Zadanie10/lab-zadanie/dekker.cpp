/*
procedure processone;
	begin
		while true do
		begin
			{część programu przed sekcją krytyczną}
			p1wantstoenter:=true;
			while p2wantstoenter do
				if favoredprocess=2 then
				begin
					p1wantstoenter:=false;
					while favoredprocess=2 do; // czekam
					p1wantstoenter:=true;
				end
				{sekcja krytyczna}
				favoredprocess=2;
				p1wantstoenter:=false;
				{pozostała część programu}
			end;
	end;
*/

#include<iostream>
#include<cstdio>
#include<atomic>
#include<thread>

/* HINT ---> Rozważyć użycie atomic w drugim zadaniu zaliczeniowym */

using namespace std;

atomic<bool> chceWejsc[2];
atomic<int> przywilej;
atomic<int> x{0};

void local_section() {}

void entry_protocol(int nr) {
	if(przywilej == 1 - nr) {
		chceWejsc[nr] = false;
		while(przywilej == 1 - nr) {
			// aktywne oczekiwanie
		}
		chceWejsc[nr] = true;
	}
}

void critical_section() {
	int y = x;
	y++;
//	std::this_thread::sleep_for(std::chrono::milliseconds(10));
	x = y;
}

void exit_protocol(int nr) {
	przywilej = 1 - nr;
	chceWejsc[nr] = false;
}

void th(int nr) {
	for(int i = 0; i < 1000000; i++) {
		local_section();
		entry_protocol(nr);
		critical_section();
		exit_protocol(nr);
	}
}

void monitor() {
	long prev = 0;
	for (;;) {
		prev = x;
		std::this_thread::sleep_for(std::chrono::seconds(2));
		if (prev==x)
			std::cout << "Deadlock! wants = "<< chceWejsc[0] <<"/"<< chceWejsc[1] << endl;
		else
			std::cout << "monitor: "<< x << std::endl;
	}
}

int main() {
	chceWejsc[0] = false;
	chceWejsc[1] = false;
	przywilej = 0;
	cout << "main() starts" << endl;
	thread monitor_th{monitor};
	monitor_th.detach();
	thread t1{th, 0};
	thread t2{th, 1};
	t1.join(); // wait for t1 to complete
	t2.join(); // wait for t2 to complete
	cout << "main() completes: " << x << endl;
	return 0;
}
