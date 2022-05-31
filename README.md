# integracija-sistema
PMF UNS AC RS

Integracija Sistema - Zadatak
=============================

Implementirati distribuiranu chat aplikaciju pomocu Java RMI po uputstvima
u nastavku teksta.


RMI deo
-------

Server prilikom pokretanja registruje udaljeni objekat za slanje poruka na
lokalnom racunaru na standardnom portu pod imenom "chat". Ovaj objekat
implementira dati udaljeni interfejs:

interface ChatServer {
	public void sendMessage(String name, String message);
	public void addListener(ChatListener listener);
	public void removeListener(ChatListener listener);
}

interface ChatListener {
	public void receiveMessage(String name, String message);
}

Server podrzava vise klijentskih konekcija i svi povezani korisnici se nalaze u
istoj sobi za caskanje. Takodje, svi korisnici vide sve poslate poruke, nije
moguce slati privatne poruke.

Klijent prilikom pokretanja pita korisnika za nadimak i dobavlja udaljeni
objekat za slanje poruka. Host i port na kojem se nalazi serverski objekat se
zadaju preko jedinog argumenta komandne linije u obliku "host:port".

Posle uspesnog povezivanja, klijent registruje svoj objekat za primanje poruka
i u petlji nudi mogucnost korisniku da salje poruke. Klijent prekida svoj rad
kada korisnik posalje prazan string.

XML deo
-------

Server cuva listu neprikladnih reci u xml fajlu pod imenom reci.xml.

Ova lista se ucitava na pocetku rada servera, a snima pri svakoj izmeni.

Prilikom slanja poruka, server zamenjuje sve neprikladne reci zvezdicama. Svaka
rec se zamenjuje sa 5 zvezdica, bez obzira na duzinu.

Svaki kosrisnik moze da dodaje reci na ovu listu tako sto posalje specijalnu
poruku formata "/+/rec". Takodje, korisnik moze da obrise neku neprikladnu rec
koju je dodao slanjem "/-/rec". Ove specijalne poruke se ne salju ostalim
korisnicima vec se samo azurira lista neprikladnih reci i salje potvrda o tome
onom korisniku koji je inicirao operaciju.

XML dokument koji se koristi za skladistenje neprikladnih reci zadovoljava
sledeci DTD:
```
<!--ELEMENT lista (rec*) -->
<!--ELEMENT rec (#PCDATA) -->
<!--ATTLIST rec nadimak CDATA #REQUIRED-->
```
