## Suunnitelma

Ryhmän nimi: **Monitoimityökalu**

Jäsenet: **Axel "Kynsiviila" Talnopoika** ja **Juuso "Porakone" Viskari**

Efektin nimi: Psykedeelinen Lumihiutale


Kuvaus: Efekti koostuu "hiutaleista", jotka syntyy, kasvaa, himmenee ja poistuu.
Hiutaleessa piirretään jotain johonkin suuntaan, jonka jälkeen sama viiva piirretään 
myös n määrään muita suuntia.

Viivat koostuu useista pisteistä, joiden välille piirretään viivat.

Pisteiden rotaatiot keskipisteen suhteen saadaan esittämällä pisteen koordinatit 
muodossa (d*cos(a),d*sin(a)), jossa d on pisteen etäisyys origosta ja a on rotaatiokulma suhteessa 
x-akseliin. Uusia rotatoituja pisteitä saadaan muokkaamalla kulmaa a. Tämän jälkeen pisteet tulee sovittaa
swing koordinaatiostoon.

25 tick välein luodaan uusi hiutale, jolle valitaan väri, koko, piirtofunktio ja sektorien lkm.
Piirtofunktioita on sin(x), sqrt(x)*sin(x) ja semi-stokastinen viiva joka valitsee aina seuraavan pisteen
180° sektorilta viivan edestä.

Klikkaamalla saa luotua uusia stokastisia hiutaleita kursorin osoittamaan pisteeseen.
