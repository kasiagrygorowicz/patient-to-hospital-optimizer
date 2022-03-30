# patient-to-hospital-optimizer
Group project in the form of desktop application. Its main goal is to transfer each patient to hospital taking into account number of free beds in every hospital.
If the closest hospital doesn't have free beds application searches for second closest hospital and does it until it finds place for a patient.
Application creates map of teritory (hosptals and objects determine teritory) on which ground ambulance picks up patients. If patient is outside of the border, they will not be picked up.
Algorithms used: Dijkstra algorithms, convex hull

## Input data
```
# hospitals (id | name |  x |  y | beds | free beds)
1 | Szpital Wojewódzki nr 997 | -10 | 700 | 9999 | 100
2 | Krakowski Szpital Kliniczny | 100 | -120 | 999 | 99
3 | Pierwszy Szpital im. Prezesa RP | 120 | 130 | 99 | 0
4 | Drugi Szpital im. Naczelnika RP | 10 | 140 | 70 | 1
5 | Trzeci Szpital im. Króla RP | 140 | 10 | 996 | 0
# objects (id | name | x | y)
1 | Pomnik Wikipedii | -1 | 50
2 | Pomnik Fryderyka Chopina | 110 | 55
3 | Pomnik Anonimowego Przechodnia | 40 | 70
# Drogi (id | id_szpitala | id_szpitala | odległość)
1 | 1 | 2 | 700
2 | 1 | 4 | 550
3 | 1 | 5 | 800
4 | 2 | 3 | 300
5 | 2 | 4 | 550
6 | 3 | 5 | 600
7 | 4 | 5 | 750
```
