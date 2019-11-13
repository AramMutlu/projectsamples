# Technisch ontwerp

## Protocollen
**De protocollen zijn te vinden door op de volgende link te klikken:**
https://docs.google.com/document/d/1jX30jKENUi7XrDgCiH_gq2kV63tEhpTk3_SSau06G98/edit?usp=drivesdk

## In dit project  
In dit project zal er een netwerk maken van samenhangende systemen die een communicatienetwerk nabootsen van 2 landen in oorlogstijd. De te gebruiken zendtechnieken zullen bestaan uit: LoRaWAN, LoRaP2P, UDP en als communicatie tussen de LoRaWan modules BLE.
We gaan dit oplossen door onze complete projectgroep op te splitsen en onder te verdelen in kleinere groepen die zich gaan focussen op de individuele technieken. Zo kunnen mensen gefocust te werk.
* Er zal een Android applicatie geschreven worden die kan communiceren met de LoRaWAN module en kan broadcasten en ontvangen over WLAN. 
* Er zal een NodeJS applicatie geschreven worden die op een laptop of pc lokaal draait en een P2P module kan aansturen en tevens kan broadcasten en ontvangen over WLAN.

## Belangrijke componenten
Belangrijke componenten in dit project zijn (gesorteerd op applicatie):

### Android
De android applicatie zal in hoofdlijnen werken door een Netwerk Handler class die beide een BLE of een WLAN bericht kan ontvangen en doorpassen naar een controller voor de respectievelijke technieken. Het bericht zal gewrapt zitten in een Netwerk Component interface die om de klassificatie duidelijker te maken. Encryptie zal standaard bestaan uit AES 256.

### NodeJS
De NodeJS applicatie zal gebruik maken van de [serialport](https://www.npmjs.com/package/serialport) package om de BLE611 pro module te kunnen aansturen voor de P2P communicatie. De applicatie zal letten op signalen van de seriële poort (via USB) en bij binnenkomende berichten deze op het scherm vertonen. Ook is de UDP over WLAN techniek beschikbaar via de NodeJS applicatie.

### Globale Implementatie
Om te voorkomen dat we niet meer kunnen vinden wat er mis gaat realiseren we het globale protocol in een apart Java project. Alle situaties worden doormiddel van testen nagebootst. Op deze manier weet je zeker dat het niet aan de hardware ligt.

#### Testen  
Testen zal initieel bestaan uit development testing, gezien onze achterstand op het schema. Mocht het zo zijn dat er onverhoopt tijd is om automatiche testcases op te zetten zal dit voor Android gedaan worden met [Espresso](https://developer.android.com/training/testing/espresso/index.html), voor NodeJS met [Mocha](https://mochajs.org/) en voor de Globale implementatie met [JUnit](https://junit.org/)

## Einde sprint 1 / begin sprint 2
Aan het einde van sprint 1 hebben we ons vooral gefocust op het correct documenteren van onze code, zodat er bij de wissel met een ander team meteen doorgewerkt kan worden. In sprint 2 staan de volgende implementaties en verbeteringen nog op de agenda:  
#### Android:
- BLE (Zowel voor de app als de "firmware" van de RM1xx)
- Doorsturen van UDP berichten over LoRaWAN
- Test suites in Espresso
- Globale protocol implementeren

#### NodeJS:
- Doorsturen van P2P en UDP berichten over LoRaWAN
- Globale protocol implementeren
- De 2 programma's samenvoegen naar één geheel
- Test suites in Mocha

#### Globale protocol:
- Overkoepelende class voor AssignId, RequestId en de vectorclock
- Byte[] omzetten naar een model (En vice versa)
- VarInt's implementeren
- Encryptie (AES 256)
- JUnit tests

## Einde sprint 2
Aan het einde van sprint 2 is het ons niet gelukt om het project af te ronden. De oorzaak hiervan was dat we Laird moeilijk aan de praat kregen. Hier zijn een hoop uren mee verbrand. Hierdoor konden we bij op de android app voor Bluetooth low energy niet controleren of testen of er berichten ontvangen of verstuurd werden.
We hebben de volgende punten niet afgekregen:

#### Android:
- Doorsturen van UDP berichten over LoRaWAN
- Globale implementatie voor BLE
- Test suites in Espresso

#### NodeJS:
- Doorsturen van P2P en UDP berichten over LoRaWAN
- Globale implementatie
- De 2 programma's samenvoegen naar één geheel
- Test suites in Mocha

## Toelichting op Laird RM180 BLE

#### LORA Connectie
Om de LORA connectie tot stand te brengen zijn er meerdere functies en events nodig om dit af te handelen.
De codes die werden beschreven in de uitrolhandleiding zijn nodig om de verbinding "in te loggen" op
The Things Network (TTN). Als alles goed gaat zal er in de console van TTN staan dat er een apparaat
is verbonden. De laird zal elke X aantal seconden een bericht versturen om te controlen of er nieuwe berichten
zijn. Mocht er iets mis gaan, dan zal een error met een code in de terminal van UwTerminalX komen te staan.


#### BLE Connectie
De BLE connetie wordt d.m.v. de HndlrBLEMsg functie tot stand gebracht. Wanneer een toestel verbinding
probeert te maken met de laird dan zal deze functie worden aangeroepen. Deze zal vaker worden aangeroepen
met steeds een ander MsgId, dit betekent ook steeds ook iets anders. Zo betekent 0 dat er een nieuwe
connectie is, 1 betekent dat de connectie is verbroken en zo zijn er nog 18 andere codes.


#### Downlink berichten ontvangen
Wanneer een downlink bericht wordt ontvangen, dan start het event EVLORAMACRXDATA de methode HandlerRxData. 
In de methode worden de gegevens van het pakket, zoals de data, port number, framepending, packet type enz, 
gelezen en getoond. Deze gegevens worden getoond, zodat het in de UwTerminalX ook duidelijk wordt of en 
welke berichten binnenkomen.
Vervolgens wordt de data geschreven naar de characteristic1, d.mv. de methode BleCharValueNotify wordt het 
bericht doorgestuurd over Bluetooth. 


#### Uplink berichten verzenden
Op het moment dat de Android App een bericht verstuurd via Bluetooth, dan start het event EVCHARVAL de 
methode HandlerCharVal. In deze methode wordt gecontroleerd of de meegegeven parameter charHandle gelijk 
is aan de waarde van de characteristic2.
Als dit correct is, dan wordt de waarde uit charHandle gezet naar de variabele data. 
Deze variabele wordt gebruikt bij het verzenden van berichten over het LORA netwerk.


## Uitrolhandleiding

### Laird RM180 BLE
Indien de Laird al de correcte smartBasic configuratie bevat, dan hoeft alleen stap 16 en 17 te worden uitgevoerd in de UwTerminal. </br>
1.  Clone de repository.
2.  Maak een tekst bestand aan met de naam loraAutomation.txt.
3.  Plaats onderstaande code in het bestand:<br/>
`at+cfgex 1010 "70B3D57ED000A561"`<br/>
`at+cfgex 1011 "C0EE400001010DE6"`<br/>
`at+cfgex 1012 "1490E61945A17D33F98DC6D36E091D95"`<br/>
`atz`
4.  Open UwTerminalX.
5.  In het 'about' scherm klik op Accept.
6.  Ga naar het scherm 'config'
7.  Bij Port Setting Selecteer "RM 186/RM 191"
8.  Klik op OK.
9.  Ga naar het scherm 'terminal'
10. Voer het commando "at&f*" uit.
11. Rechter muisklik in de terminal en selecteer XCompile + Load
12. In de repository, ga naar de map src/BLE.
13. Selecteer firm.sb
14. Zodra het downloaden klaar is, Rechter muisklik in de terminal en selecteer Automation.
15. Klik op load en open loraAutomation.txt
16. Verstuur van boven naar beneden elk commando door op de "Send" knop te klikken.
17. Voer het commando "lora" in.

### NodeJS
1. Open het NodeJS project
2. Open een terminal
3. Type 'npm install'
4. Start de 'udp.js' en de 'p2p.js'
5. Open je browser en ga naar http://localhost:4000
